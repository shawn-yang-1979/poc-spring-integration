package idv.shawnyang.poc.spring.integration.mqtt;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.inbound.AbstractMqttMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import idv.shawnyang.poc.spring.integration.mqtt.transferobject.v1.DocCustomer;
import idv.shawnyang.poc.spring.integration.mqtt.transferobject.v1.DocVendor;

@Component
public class Demo {

	private static final Logger log = LoggerFactory.getLogger(Demo.class);

	private static final String MQTT_INBOUND_TOPIC_PREFIX = "/inbound/";
	private static final String MQTT_OUTBOUND_TOPIC_PREFIX = "/outbound/";

	@Autowired
	private DemoGateway demoGateway;

	@Autowired
	private AbstractMqttMessageDrivenChannelAdapter mqttMessageDrivenChannelAdapter;

	@Autowired
	private Gson gson;

	@PostConstruct
	void init() {
		mqttMessageDrivenChannelAdapter.addTopic(MQTT_INBOUND_TOPIC_PREFIX + DocCustomer.TYPE_NAME, 2);
		mqttMessageDrivenChannelAdapter.addTopic(MQTT_INBOUND_TOPIC_PREFIX + DocVendor.TYPE_NAME, 2);
	}

	@ServiceActivator(inputChannel = ChannelDefinition.MqttAdapter.Inbound.CHANNEL_UID_LOCAL)
	public void receive(Message<String> mqttInboundMessage) {
		try {
			Object topic = mqttInboundMessage.getHeaders().get(MqttHeaders.TOPIC);
			if (topic == null) {
				return;
			}
			if (topic.equals(MQTT_INBOUND_TOPIC_PREFIX + DocVendor.TYPE_NAME)) {
				DocVendor vendor = gson.fromJson(mqttInboundMessage.getPayload(), DocVendor.class);
				String print = vendor.toString();
				log.info(print);
			} else if (topic.equals(MQTT_INBOUND_TOPIC_PREFIX + DocCustomer.TYPE_NAME)) {
				DocCustomer customer = gson.fromJson(mqttInboundMessage.getPayload(), DocCustomer.class);
				String print = customer.toString();
				log.info(print);
			}

		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}

	@Scheduled(initialDelay = 10000, fixedRate = 10000)
	public void scheduledOutbound() {
		try {
			DocVendor vendor = new DocVendor();
			vendor.setFirstName("Sean");
			vendor.setLastName("Yang");
			MessageBuilder<String> msgBuilder = MessageBuilder//
					.withPayload(gson.toJson(vendor))//
					.setHeader(MqttHeaders.TOPIC, MQTT_OUTBOUND_TOPIC_PREFIX + DocVendor.TYPE_NAME)//
					.setHeader(MqttHeaders.QOS, 2);
			demoGateway.send(msgBuilder.build());

		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}

		try {
			DocCustomer cust = new DocCustomer();
			cust.setFirstName("Shawn");
			cust.setLastName("Yang");
			MessageBuilder<String> msgBuilder = MessageBuilder//
					.withPayload(gson.toJson(cust))//
					.setHeader(MqttHeaders.TOPIC, MQTT_OUTBOUND_TOPIC_PREFIX + DocCustomer.TYPE_NAME)//
					.setHeader(MqttHeaders.QOS, 2);
			demoGateway.send(msgBuilder.build());
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}

	@MessagingGateway
	public static interface DemoGateway {

		@Gateway(requestChannel = ChannelDefinition.MqttAdapter.Outbound.CHANNEL_UID_LOCAL)
		void send(Message<String> mqttOutboundMessage);
	}

}
