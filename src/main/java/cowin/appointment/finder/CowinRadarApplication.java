package cowin.appointment.finder;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@PropertySource("file:/opt/conf/cowin-radar/outsource/application.properties")
@Log4j
public class CowinRadarApplication {

	public static void main(String[] args) {
		//PropertyConfigurator.configure("/opt/conf/cowin-radar/outsource/log4j2.properties");
		log.info("Cowin radar started");
		SpringApplication.run(CowinRadarApplication.class, args);
	}

}
