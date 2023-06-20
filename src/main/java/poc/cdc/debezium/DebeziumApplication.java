package poc.cdc.debezium;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DebeziumApplication {

	public static void main(String[] args) {
		SpringApplication.run(DebeziumApplication.class, args);
	}

	/*@Component
	class DebeziumRoute extends RouteBuilder {

		private final String offsetStorageFileName = "D:\\spring\\offset-file.dat";
		private final String host = "localhost";
		private final int port = 1521;
		private final String username = "";
		private final String password = "";
		private final String db = "ORCLCDB";
		private final String pdb = "ORCLPDB1";

		@Override
		public void configure() throws Exception {
			from("localhost:1521/ORCLCDB?offsetStorageFileName="+offsetStorageFileName
			+"");
		}
	}*/
}


