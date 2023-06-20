package poc.cdc.debezium.config;

import io.debezium.connector.oracle.OracleConnectorConfig;
import io.debezium.embedded.EmbeddedEngine;
import org.apache.kafka.connect.json.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

@Configuration
public class DebeziumConnectorConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChangeDataSender.class);

    private static final String APP_NAME = "cdcdbzapp";
    private final String offsetStorageFileName = "D:\\spring\\offset-file.dat";
    private final String schemaHistoryInternalFilename = "D:\\spring\\schema-history-file.dat";

    @Bean
    public io.debezium.config.Configuration personsConnector() throws IOException {
        return io.debezium.config.Configuration.create()
                .with(EmbeddedEngine.CONNECTOR_CLASS, "io.debezium.connector.oracle.OracleConnector")
                .with(EmbeddedEngine.ENGINE_NAME, APP_NAME)
                .with("plugin.path","/plugins")
                //.with("oracle.jdbc.timezoneAsRegion",false)
                .with(OracleConnectorConfig.TOPIC_PREFIX,APP_NAME)
                .with("schema.history.internal.file.filename", schemaHistoryInternalFilename)
                .with(EmbeddedEngine.OFFSET_FLUSH_INTERVAL_MS, "60000")
                // for demo purposes let's store offsets and history only in memory
                .with(EmbeddedEngine.OFFSET_STORAGE, "org.apache.kafka.connect.storage.FileOffsetBackingStore"/*"org.apache.kafka.connect.storage.MemoryOffsetBackingStore"*/)
                .with(EmbeddedEngine.OFFSET_STORAGE_FILE_FILENAME, offsetStorageFileName)
                .with(OracleConnectorConfig.SCHEMA_HISTORY, "io.debezium.storage.file.history.FileSchemaHistory")
                .with(OracleConnectorConfig.HOSTNAME, "localhost")
                .with(OracleConnectorConfig.PORT, 1521)
                .with(OracleConnectorConfig.USER, "c##leondbzuser")
                .with(OracleConnectorConfig.PASSWORD, "cdcdbz")
                .with(OracleConnectorConfig.PDB_NAME , "ORCLPDB1")
                .with(OracleConnectorConfig.DATABASE_NAME, "ORCLCDB")
                //.with(OracleConnectorConfig.DATABASE_INCLUDE_LIST, "ORCLCDB")
                .with(OracleConnectorConfig.TABLE_INCLUDE_LIST, "DEBEZIUM.PERSONS")
                .with(OracleConnectorConfig.INCLUDE_SCHEMA_CHANGES, "false")
                .build();
    }
}
