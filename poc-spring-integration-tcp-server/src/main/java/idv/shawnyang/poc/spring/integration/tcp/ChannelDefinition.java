package idv.shawnyang.poc.spring.integration.tcp;

public class ChannelDefinition {

	private ChannelDefinition() {
	}

	public static final String SEPARATOR = "/";
	private static final String MODULE_ID = "poc-spring-integration-tcp";

	public static class TcpServer {
		private TcpServer() {
		}

		private static final String THING_TYPE_ID = "TcpClTcpServerient";

		public static class InboundGateway {
			private InboundGateway() {
			}

			private static final String CHANNEL_ID = "InboundGateway";
			public static final String CHANNEL_UID_LOCAL = SEPARATOR + MODULE_ID + SEPARATOR + THING_TYPE_ID + SEPARATOR
					+ CHANNEL_ID + SEPARATOR;
		}

		public static class InboundAdapter {
			private InboundAdapter() {
			}

			private static final String CHANNEL_ID = "InboundAdapter";
			public static final String CHANNEL_UID_LOCAL = SEPARATOR + MODULE_ID + SEPARATOR + THING_TYPE_ID + SEPARATOR
					+ CHANNEL_ID + SEPARATOR;
		}

		public static class OutboundAdapter {
			private OutboundAdapter() {
			}

			private static final String CHANNEL_ID = "OutboundAdapter";
			public static final String CHANNEL_UID_LOCAL = SEPARATOR + MODULE_ID + SEPARATOR + THING_TYPE_ID + SEPARATOR
					+ CHANNEL_ID + SEPARATOR;
		}

	}

}
