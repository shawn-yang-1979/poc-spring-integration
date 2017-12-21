package idv.shawnyang.poc.spring.integration.mqtt;

public class ChannelDefinition {

	private ChannelDefinition() {
	}

	public static final String SEPARATOR = "/";
	private static final String MODULE_ID = "poc-spring-integration-mqtt";

	public static class MqttAdapter {
		private MqttAdapter() {
		}

		private static final String THING_TYPE_ID = "MqttAdapter";

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
