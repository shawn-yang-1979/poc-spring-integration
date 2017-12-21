package idv.shawnyang.poc.spring.integration.mqtt;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.AbstractMqttMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.AbstractMqttMessageHandler;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;

@Configuration
public class BeanConfiguration {

	@Autowired
	private MqttProperties mqttProperties;

	@Bean
	public DefaultMqttPahoClientFactory mqttClientFactory() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setServerURIs(mqttProperties.getServerUris());
		factory.setConnectionTimeout(mqttProperties.getTimeout());
		return factory;
	}

	@Bean
	@ServiceActivator(inputChannel = ChannelDefinition.MqttAdapter.Outbound.CHANNEL_UID_LOCAL)
	public AbstractMqttMessageHandler mqttMessageHandler() {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(UUID.randomUUID().toString(),
				mqttClientFactory());
		// CompletionTimeout have to longer than ConnectionTimeout a little bit.
		// Otherwise, client side may see the connecting as fail, but it
		// actually success at MQTT server side.
		// When client side tries to reconnect again, the client Id will be
		// dulplicate.
		// That's why we multiple 1500
		int timeout = mqttProperties.getTimeout() * 1500;
		messageHandler.setCompletionTimeout(timeout);
		messageHandler.setAsync(true);
		return messageHandler;
	}

	@Bean
	public AbstractMqttMessageDrivenChannelAdapter mqttMessageDrivenChannelAdapter() {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
				UUID.randomUUID().toString(), mqttClientFactory());
		// CompletionTimeout have to longer than ConnectionTimeout a little bit.
		// Otherwise, client side may see the connecting as fail, but it
		// actually success at MQTT server side.
		// When client side tries to reconnect again, the client Id will be
		// dulplicate.
		// That's why we multiple 1500
		int timeout = mqttProperties.getTimeout() * 1500;
		adapter.setCompletionTimeout(timeout);
		adapter.setSendTimeout(timeout);
		adapter.setOutputChannelName(ChannelDefinition.MqttAdapter.Inbound.CHANNEL_UID_LOCAL);
		return adapter;
	}
}
