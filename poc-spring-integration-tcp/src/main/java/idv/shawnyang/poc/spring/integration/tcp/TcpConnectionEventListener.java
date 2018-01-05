package idv.shawnyang.poc.spring.integration.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.integration.ip.tcp.connection.TcpConnectionEvent;
import org.springframework.stereotype.Component;

@Component
public class TcpConnectionEventListener {

	private static final Logger log = LoggerFactory.getLogger(TcpConnectionEventListener.class);

	@EventListener(TcpConnectionEvent.class)
	public void receive(TcpConnectionEvent event) {
		String eventPrint = event.toString();
		log.info(eventPrint);
	}

}
