package poc.cdc.debezium.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/*@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, visible = true)
@JsonInclude(JsonInclude.Include.NON_NULL)*/
public class Persons {
        //@JsonProperty("PERSON_ID")
        private Object personId;
        //@JsonProperty("FIRST_NAME")
        private String firstName;
        //@JsonProperty("LAST_NAME")
        private String lastName;

        private Object age;
}
