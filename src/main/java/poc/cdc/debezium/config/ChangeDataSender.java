package poc.cdc.debezium.config;

import io.debezium.config.Configuration;
import io.debezium.connector.oracle.OracleConnectorConfig;
import io.debezium.embedded.EmbeddedEngine;
import io.debezium.engine.DebeziumEngine;
import io.debezium.util.Clock;
import org.apache.kafka.connect.json.JsonConverter;
import org.apache.kafka.connect.source.SourceRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class ChangeDataSender implements Runnable{

    private static final Logger LOGGER = LoggerFactory.getLogger(ChangeDataSender.class);

    private static final String APP_NAME = "cdcdbzapp";

    private Configuration config;
    private EmbeddedEngine engine;
    private final JsonConverter valueConverter;
    private final String offsetStorageFileName = "D:\\spring\\offset-file.dat";
    private final String schemaHistoryInternalFilename = "D:\\spring\\schema-history-file.dat";

    public ChangeDataSender() {
        config = Configuration.empty().withSystemProperties(Function.identity()).edit()
                .with(EmbeddedEngine.CONNECTOR_CLASS, "io.debezium.connector.oracle.OracleConnector")
                .with(EmbeddedEngine.ENGINE_NAME, APP_NAME)
                .with(OracleConnectorConfig.TOPIC_PREFIX,APP_NAME)
                // for demo purposes let's store offsets and history only in memory
                .with(EmbeddedEngine.OFFSET_STORAGE, "org.apache.kafka.connect.storage.FileOffsetBackingStore"/*"org.apache.kafka.connect.storage.MemoryOffsetBackingStore"*/)
                .with(EmbeddedEngine.OFFSET_STORAGE_FILE_FILENAME, offsetStorageFileName)
                .with(OracleConnectorConfig.SCHEMA_HISTORY, "io.debezium.storage.file.history.FileSchemaHistory")
                .with("schema.history.internal.file.filename", schemaHistoryInternalFilename)
                .with(EmbeddedEngine.OFFSET_FLUSH_INTERVAL_MS, "60000")
                .with(OracleConnectorConfig.HOSTNAME, "localhost")
                .with(OracleConnectorConfig.PORT, 1521)
                .with(OracleConnectorConfig.USER, "c##leondbzuser")
                .with(OracleConnectorConfig.PASSWORD, "cdcdbz")
                .with(OracleConnectorConfig.PDB_NAME , "ORCLPDB1")
                .with(OracleConnectorConfig.DATABASE_NAME, "ORCLCDB")
                .with(OracleConnectorConfig.DATABASE_INCLUDE_LIST, "ORCLCDB")
                .with(OracleConnectorConfig.INCLUDE_SCHEMA_CHANGES, "false")
                .build();

        valueConverter = new JsonConverter();
        valueConverter.configure(config.asMap(), false);

    }
    @Override
    public void run() {
        engine = EmbeddedEngine.create()
                .using(config)
                .using(this.getClass().getClassLoader())
                .using(Clock.SYSTEM)
                .notifying(this::sendRecord)
                .build();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(engine);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Requesting embedded engine to shut down");
            engine.stop();
        }));

        // the submitted task keeps running, only no more new ones can be added
        executor.shutdown();

        awaitTermination(executor);


        LOGGER.info("Engine terminated");
    }

    private void sendRecord(SourceRecord record) {
        // We are interested only in data events not schema change events
        if (record.topic().equals(APP_NAME)) {
            return;
        }
    }

    private void awaitTermination(ExecutorService executor) {
        try {
            while (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                LOGGER.info("Waiting another 10 seconds for the embedded engine to complete");
            }
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
