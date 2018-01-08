package idv.shawnyang.poc.spring.integration.tcp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.ip.tcp.TcpOutboundGateway;
import org.springframework.integration.ip.tcp.TcpReceivingChannelAdapter;
import org.springframework.integration.ip.tcp.TcpSendingMessageHandler;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.CachingClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetClientConnectionFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;

@Configuration
public class ClientBeanConfiguration {

	private static final String PREFIX = "idv.shawnyang.poc.spring.integration.tcp.ClientBeanConfiguration";
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
	public AbstractClientConnectionFactory realClientConnectionFactoryGateway(
			CustomizedByteArrayStxEtxSerializer customizedByteArrayStxEtxSerializer) {
		TcpNetClientConnectionFactory ccf = new TcpNetClientConnectionFactory("localhost", 1234);
		ccf.setDeserializer(customizedByteArrayStxEtxSerializer);
		ccf.setSerializer(customizedByteArrayStxEtxSerializer);
		ccf.setLookupHost(false); // if you use IP only
		return ccf;
	}

	@Bean
	public AbstractClientConnectionFactory clientConnectionFactoryGateway(
			AbstractClientConnectionFactory realClientConnectionFactoryGateway) {
		return new CachingClientConnectionFactory(realClientConnectionFactoryGateway, 10);
	}

	@Bean
	@ServiceActivator(inputChannel = ChannelDefinition.TcpClient.OutboundGateway.CHANNEL_UID_LOCAL)
	public MessageHandler outboundGateway(AbstractClientConnectionFactory clientConnectionFactoryGateway) {
		TcpOutboundGateway gate = new TcpOutboundGateway();
		gate.setConnectionFactory(clientConnectionFactoryGateway);
		gate.setOutputChannelName(TRANSFORMER_GATEWAY);
		return gate;
	}

	@Transformer(inputChannel = TRANSFORMER_GATEWAY)
	public String transformerGateway(Message<byte[]> message) {
		byte[] bytes = message.getPayload();
		return new String(bytes);
	}

	@Bean
	public AbstractClientConnectionFactory clientConnectionFactoryAdapter(
			CustomizedByteArrayStxEtxSerializer customizedByteArrayStxEtxSerializer) {
		TcpNetClientConnectionFactory ccf = new TcpNetClientConnectionFactory("localhost", 2234);
		ccf.setDeserializer(customizedByteArrayStxEtxSerializer);
		ccf.setSerializer(customizedByteArrayStxEtxSerializer);
		ccf.setLookupHost(false); // if you use IP only
		return ccf;
	}

	@Bean
	@ServiceActivator(inputChannel = ChannelDefinition.TcpClient.OutboundAdapter.CHANNEL_UID_LOCAL)
	public MessageHandler outboundAdapter(AbstractClientConnectionFactory clientConnectionFactoryAdapter) {
		TcpSendingMessageHandler adapter = new TcpSendingMessageHandler();
		adapter.setConnectionFactory(clientConnectionFactoryAdapter);
		return adapter;
	}

	@Bean
	public TcpReceivingChannelAdapter inboundAdapter(AbstractClientConnectionFactory clientConnectionFactoryAdapter) {
		TcpReceivingChannelAdapter adapter = new TcpReceivingChannelAdapter();
		adapter.setConnectionFactory(clientConnectionFactoryAdapter);
		adapter.setOutputChannelName(TRANSFORMER_ADAPTER);
		return adapter;
	}

	@Transformer(inputChannel = TRANSFORMER_ADAPTER, outputChannel = ChannelDefinition.TcpClient.InboundAdapter.CHANNEL_UID_LOCAL)
	public String transformerAdapter(Message<byte[]> msg) {
		byte[] bytes = msg.getPayload();
		return new String(bytes);
	}

}
