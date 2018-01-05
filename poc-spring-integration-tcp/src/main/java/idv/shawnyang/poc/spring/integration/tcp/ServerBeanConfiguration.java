package idv.shawnyang.poc.spring.integration.tcp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.ip.tcp.TcpInboundGateway;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.messaging.Message;

@Configuration
public class ServerBeanConfiguration {

	private static final String PREFIX = "idv.shawnyang.poc.spring.integration.tcp.ServerBeanConfiguration";
	private static final String TRANSFORMER = PREFIX + ".transformer";

	@Bean
	public AbstractServerConnectionFactory serverConnectionFactory() {
		return new TcpNetServerConnectionFactory(1234);
	}

	@Bean
	public TcpInboundGateway tcpInGate(AbstractServerConnectionFactory serverConnectionFactory) {
		TcpInboundGateway inGate = new TcpInboundGateway();
		inGate.setConnectionFactory(serverConnectionFactory);
		inGate.setRequestChannelName(TRANSFORMER);
		return inGate;
	}

	@Transformer(inputChannel = TRANSFORMER, outputChannel = ChannelDefinition.TcpServer.Inbound.CHANNEL_UID_LOCAL)
	public String byteArrayToStringTransformer(Message<byte[]> msg) {
		byte[] bytes = msg.getPayload();
		return new String(bytes);
	}

}
