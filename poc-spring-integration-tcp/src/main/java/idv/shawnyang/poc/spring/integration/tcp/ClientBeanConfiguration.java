package idv.shawnyang.poc.spring.integration.tcp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.ip.tcp.TcpOutboundGateway;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetClientConnectionFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

@Configuration
public class ClientBeanConfiguration {

	private static final String PREFIX = "idv.shawnyang.poc.spring.integration.tcp.ClientBeanConfiguration";
	private static final String TRANSFORMER = PREFIX + ".transformer";

	@Bean
	public AbstractClientConnectionFactory clientConnectionFactory() {
		return new TcpNetClientConnectionFactory("localhost", 1234);
	}

	@Bean
	@ServiceActivator(inputChannel = ChannelDefinition.TcpClient.Outbound.CHANNEL_UID_LOCAL)
	public MessageHandler tcpOutbound(AbstractClientConnectionFactory clientConnectionFactory) {
		TcpOutboundGateway gate = new TcpOutboundGateway();
		gate.setConnectionFactory(clientConnectionFactory);
		gate.setOutputChannelName(TRANSFORMER);
		return gate;
	}

	@Transformer(inputChannel = TRANSFORMER)
	public String transformer(Message<byte[]> message) {
		byte[] bytes = message.getPayload();
		return new String(bytes);
	}
}
