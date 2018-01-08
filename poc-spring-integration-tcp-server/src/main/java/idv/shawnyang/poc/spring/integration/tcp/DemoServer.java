package idv.shawnyang.poc.spring.integration.tcp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.ip.IpHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class DemoServer {

	@Autowired
	private DemoServerGateway demoServerGateway;

	@ServiceActivator(inputChannel = ChannelDefinition.TcpServer.InboundGateway.CHANNEL_UID_LOCAL)
	public String fromInboundGateway(Message<String> message) {
		String in = message.getPayload();
		return in + " World!";
	}

	/**
	 * On the server side, care must be taken to populate the ip_connectionId
	 * header because it is used to correlate the message to a connection.
	 * Messages that originate at the inbound adapter will automatically have
	 * the header set. If you wish to construct other messages to send, you will
	 * need to set the header. The header value can be captured from an incoming
	 * message.
	 * 
	 * @param message
	 * @throws InterruptedException
	 */
	@ServiceActivator(inputChannel = ChannelDefinition.TcpServer.InboundAdapter.CHANNEL_UID_LOCAL)
	public void fromInboundAdapter(Message<String> message) throws InterruptedException {

		Object connectionId = message.getHeaders().get(IpHeaders.CONNECTION_ID);

		String in = message.getPayload();
		String out = in + " Kitty!";

		MessageBuilder<String> msgBuilder = MessageBuilder.withPayload(out);
		msgBuilder.setHeader(IpHeaders.CONNECTION_ID, connectionId);

		int j = 0;
		while (true) { // reachable end condition added
			demoServerGateway.toOutboundAdapter(msgBuilder.build());
			Thread.sleep(1000);
			j++;
			if (j == Integer.MIN_VALUE) { // true at Integer.MAX_VALUE +1
				break;
			}
		}
	}

	@MessagingGateway
	public interface DemoServerGateway {

		@Gateway(requestChannel = ChannelDefinition.TcpServer.OutboundAdapter.CHANNEL_UID_LOCAL)
		public void toOutboundAdapter(Message<String> out);

	}

}
