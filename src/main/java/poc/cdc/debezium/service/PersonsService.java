package poc.cdc.debezium.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.debezium.data.Envelope;
import io.debezium.data.VariableScaleDecimal;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.data.Struct;
import org.springframework.stereotype.Service;
import poc.cdc.debezium.entity.Persons;
import poc.cdc.debezium.mapper.PersonsDeserializer;

import java.util.Map;

@Slf4j
@Service
public class PersonsService {



    public void replicateData(Map<String, Object> personsData, Envelope.Operation operation) {
        final ObjectMapper mapper = new ObjectMapper();
        //mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        Struct o = (Struct)personsData.get("PERSON_ID");
        VariableScaleDecimal.toLogical(o).getDecimalValue().get().longValue();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(Persons.class, new PersonsDeserializer());
        mapper.registerModule(simpleModule);
    mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        final Persons persons = mapper.convertValue(personsData, Persons.class);

        if (Envelope.Operation.DELETE == operation) {
            log.info("ID delete : {}", persons.getPersonId());
        } else {
            log.info("ID save : {}", persons.getPersonId());
        }
    }
}
