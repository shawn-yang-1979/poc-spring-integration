package idv.shawnyang.poc.spring.integration.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DemoClient {

	private static final Logger log = LoggerFactory.getLogger(DemoClient.class);
	@Autowired
	private DemoGateway demoGateway;

	@Scheduled(initialDelay = 1000, fixedRate = 1000)
	public void scheduledOutboundGateway() {
		try {
			String result = demoGateway.outboundGateway("Hello");
			log.info(result);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@EventListener(ContextRefreshedEvent.class)
	void init() {
		try {
			demoGateway.outboundAdapter("Hi");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

	}

	@MessagingGateway
	public interface DemoGateway {

		@Gateway(requestChannel = ChannelDefinition.TcpClient.OutboundGateway.CHANNEL_UID_LOCAL)
		String outboundGateway(String in);

		@Gateway(requestChannel = ChannelDefinition.TcpClient.OutboundAdapter.CHANNEL_UID_LOCAL)
		void outboundAdapter(String in);

	}

	@ServiceActivator(inputChannel = ChannelDefinition.TcpClient.InboundAdapter.CHANNEL_UID_LOCAL)
	public void fromInboundAdapter(Message<String> message) {
		String result = message.getPayload();
		log.info(result);
	}

}
