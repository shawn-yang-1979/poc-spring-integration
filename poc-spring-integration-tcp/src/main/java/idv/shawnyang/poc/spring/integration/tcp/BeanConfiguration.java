package idv.shawnyang.poc.spring.integration.tcp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

	@Bean
	public CustomizedByteArrayStxEtxSerializer customizedByteArrayStxEtxSerializer() {
		CustomizedByteArrayStxEtxSerializer serializer = new CustomizedByteArrayStxEtxSerializer();
		serializer.setPoolSize(10);
		serializer.setPoolWaitTimeout(1000);
		serializer.setMaxMessageSize(1024); // 1kb
		return serializer;
	}

}
