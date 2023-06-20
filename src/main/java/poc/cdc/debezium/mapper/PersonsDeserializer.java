package poc.cdc.debezium.mapper;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import poc.cdc.debezium.entity.Persons;

import java.io.IOException;
import java.util.HashMap;
@Slf4j
public class PersonsDeserializer  extends JsonDeserializer<Persons> {

    @Override
    public Persons deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        //HashMap<String, Object> pers = p.readValueAsTree();

        Persons persons = new Persons();
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);

        JsonNode personIdNode = node.get("PERSON_ID");
        Object personId = personIdNode.asText();
        persons.setPersonId(personId);

        JsonNode firstNameNode = node.get("FIRST_NAME");
        String firstName = firstNameNode.asText();
        persons.setFirstName(firstName);

        JsonNode lastNameNode = node.get("LAST_NAME");
        String lastName = lastNameNode.asText();
        persons.setLastName(lastName);


        /*JsonNode ageNode = node.get("AGE");
        Object age = ageNode.asText();
        persons.setAge(age);*/

        return persons;
    }
    /*private static class Persons{
        @JsonProperty("PERSON_ID")
        private Long personId;
        @JsonProperty("FIRST_NAME")
        private String firstName;
        @JsonProperty("LAST_NAME")
        private String lastName;
    }*/
}
