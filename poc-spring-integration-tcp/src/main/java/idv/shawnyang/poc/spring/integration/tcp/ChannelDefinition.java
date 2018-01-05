package idv.shawnyang.poc.spring.integration.tcp;

public class ChannelDefinition {

	private ChannelDefinition() {
	}

	public static final String SEPARATOR = "/";
	private static final String MODULE_ID = "poc-spring-integration-tcp";

	public static class TcpClient {
		private TcpClient() {
		}

		private static final String THING_TYPE_ID = "TcpClient";

		public static class Inbound {
			private Inbound() {
			}

			private static final String CHANNEL_ID = "Inbound";
			public static final String CHANNEL_UID_LOCAL = SEPARATOR + MODULE_ID + SEPARATOR + THING_TYPE_ID + SEPARATOR
					+ CHANNEL_ID + SEPARATOR;
		}

		public static class Outbound {
			private Outbound() {
			}

			private static final String CHANNEL_ID = "Outbound";
			public static final String CHANNEL_UID_LOCAL = SEPARATOR + MODULE_ID + SEPARATOR + THING_TYPE_ID + SEPARATOR
					+ CHANNEL_ID + SEPARATOR;
		}

	}
	
	public static class TcpServer {
		private TcpServer() {
		}

		private static final String THING_TYPE_ID = "TcpClTcpServerient";

		public static class Inbound {
			private Inbound() {
			}

			private static final String CHANNEL_ID = "Inbound";
			public static final String CHANNEL_UID_LOCAL = SEPARATOR + MODULE_ID + SEPARATOR + THING_TYPE_ID + SEPARATOR
					+ CHANNEL_ID + SEPARATOR;
		}

		public static class Outbound {
			private Outbound() {
			}

			private static final String CHANNEL_ID = "Outbound";
			public static final String CHANNEL_UID_LOCAL = SEPARATOR + MODULE_ID + SEPARATOR + THING_TYPE_ID + SEPARATOR
					+ CHANNEL_ID + SEPARATOR;
		}

	}

}
