package idv.shawnyang.poc.spring.integration.tcp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.ip.tcp.TcpInboundGateway;
import org.springframework.integration.ip.tcp.TcpReceivingChannelAdapter;
import org.springframework.integration.ip.tcp.TcpSendingMessageHandler;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

@Configuration
public class ServerBeanConfiguration {

	private static final String PREFIX = "idv.shawnyang.poc.spring.integration.tcp.ServerBeanConfiguration";
	private static final String TRANSFORMER_GATEWAY = PREFIX + ".transformerGateway";
	private static final String TRANSFORMER_ADAPTER = PREFIX + ".transformerAdapter";

	@Bean
	public CustomizedByteArrayStxEtxSerializer customizedByteArrayStxEtxSerializer() {
		CustomizedByteArrayStxEtxSerializer serializer = new CustomizedByteArrayStxEtxSerializer();
		serializer.setPoolSize(10);
		serializer.setPoolWaitTimeout(1000);
		serializer.setMaxMessageSize(1024); // 1kb
		return serializer;
	}

	@Bean
	public AbstractServerConnectionFactory serverConnectionFactoryGateway(
			CustomizedByteArrayStxEtxSerializer customizedByteArrayStxEtxSerializer) {
		TcpNetServerConnectionFactory scf = new TcpNetServerConnectionFactory(1234);
		scf.setSerializer(customizedByteArrayStxEtxSerializer);
		scf.setDeserializer(customizedByteArrayStxEtxSerializer);
		scf.setLookupHost(false);// if use IP only
		return scf;
	}

	@Bean
	public TcpInboundGateway inboundGateway(AbstractServerConnectionFactory serverConnectionFactoryGateway) {
		TcpInboundGateway inGate = new TcpInboundGateway();
		inGate.setConnectionFactory(serverConnectionFactoryGateway);
		inGate.setRequestChannelName(TRANSFORMER_GATEWAY);
		return inGate;
	}

	@Transformer(inputChannel = TRANSFORMER_GATEWAY, outputChannel = ChannelDefinition.TcpServer.InboundGateway.CHANNEL_UID_LOCAL)
	public String transformerGateway(Message<byte[]> msg) {
		byte[] bytes = msg.getPayload();
		return new String(bytes);
	}

	@Bean
	public AbstractServerConnectionFactory serverConnectionFactoryAdapter(
			CustomizedByteArrayStxEtxSerializer customizedByteArrayStxEtxSerializer) {
		TcpNetServerConnectionFactory scf = new TcpNetServerConnectionFactory(2234);
		scf.setSerializer(customizedByteArrayStxEtxSerializer);
		scf.setDeserializer(customizedByteArrayStxEtxSerializer);
		scf.setLookupHost(false);// if use IP only
		return scf;
	}

	/**
	 * the server side, message correlation is automatically handled by the
	 * adapters because the inbound adapter adds a header allowing the outbound
	 * adapter to determine which connection to use to send the reply message.
	 * 
	 * @param serverConnectionFactory
	 * @return
	 */
	@Bean
	public TcpReceivingChannelAdapter inboundAdapter(AbstractServerConnectionFactory serverConnectionFactoryAdapter) {
		TcpReceivingChannelAdapter adapter = new TcpReceivingChannelAdapter();
		adapter.setConnectionFactory(serverConnectionFactoryAdapter);
		adapter.setOutputChannelName(TRANSFORMER_ADAPTER);
		return adapter;
	}

	@Transformer(inputChannel = TRANSFORMER_ADAPTER, outputChannel = ChannelDefinition.TcpServer.InboundAdapter.CHANNEL_UID_LOCAL)
	public String transformerAdapter(Message<byte[]> msg) {
		byte[] bytes = msg.getPayload();
		return new String(bytes);
	}

	@Bean
	@ServiceActivator(inputChannel = ChannelDefinition.TcpServer.OutboundAdapter.CHANNEL_UID_LOCAL)
	public MessageHandler outboundAdapter(AbstractServerConnectionFactory serverConnectionFactoryAdapter) {
		TcpSendingMessageHandler adapter = new TcpSendingMessageHandler();
		adapter.setConnectionFactory(serverConnectionFactoryAdapter);
		return adapter;
	}

}
