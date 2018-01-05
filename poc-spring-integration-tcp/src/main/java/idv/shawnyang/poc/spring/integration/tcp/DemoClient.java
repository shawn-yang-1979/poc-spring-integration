package idv.shawnyang.poc.spring.integration.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DemoClient {

	private static final Logger log = LoggerFactory.getLogger(DemoClient.class);
	@Autowired
	private DemoGateway demoGateway;

	@Scheduled(initialDelay = 1000, fixedRate = 1000)
	public void scheduledOutbound() {
		String result = demoGateway.outboundRequest("Hello");
		log.info(result);
	}

	@MessagingGateway
	public interface DemoGateway {

		@Gateway(requestChannel = ChannelDefinition.TcpClient.Outbound.CHANNEL_UID_LOCAL)
		String outboundRequest(String in);

	}

}
