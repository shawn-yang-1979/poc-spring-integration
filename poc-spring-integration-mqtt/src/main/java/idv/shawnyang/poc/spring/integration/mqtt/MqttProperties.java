package idv.shawnyang.poc.spring.integration.mqtt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * This class maps to application-module-name.properties file. <br>
 * It's a object-properties mapping mechanism.
 * 
 * @author SHAWN.SH.YANG
 *
 */
@Component
@ConfigurationProperties(prefix = "mqtt")
public class MqttProperties {

	private String serverUris = "tcp://localhost:1883";
	private int timeout = 3; // sec

	public String getServerUris() {
		return serverUris;
	}

	public void setServerUris(String serverUris) {
		this.serverUris = serverUris;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}
