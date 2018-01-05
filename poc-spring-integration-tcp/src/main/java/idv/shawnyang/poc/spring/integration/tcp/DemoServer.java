package idv.shawnyang.poc.spring.integration.tcp;

import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class DemoServer {

	@ServiceActivator(inputChannel = ChannelDefinition.TcpServer.Inbound.CHANNEL_UID_LOCAL)
	public String inbound(Message<String> message) {
		String in = message.getPayload();
		return in + " World!";
	}

}
