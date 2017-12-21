package idv.shawnyang.poc.spring.integration.mqtt;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@IntegrationComponentScan
@EnableScheduling
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(Application.class, args);

		log.info("Beans provided by Spring Boot:");
		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			log.info(beanName);
		}
	}

}