
package idv.shawnyang.poc.spring.integration.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.integration.ip.tcp.serializer.AbstractPooledBufferByteArraySerializer;
import org.springframework.integration.ip.tcp.serializer.SoftEndOfStreamException;
import org.springframework.integration.mapping.MessageMappingException;

public class CustomizedByteArrayStxEtxSerializer extends AbstractPooledBufferByteArraySerializer {

	public static final int STX1 = 0xFA;
	public static final int STX2 = 0x5A;

	public static final int ETX1 = 0x7E;
	public static final int ETX2 = 0xA5;

	@Override
	public byte[] doDeserialize(InputStream inputStream, byte[] buffer) throws IOException {
		int stx1 = inputStream.read();
		if (stx1 < 0) {
			throw new SoftEndOfStreamException("Stream closed between payloads");
		}
		int stx2 = inputStream.read();
		if (stx2 < 0) {
			throw new SoftEndOfStreamException("Stream closed between payloads");
		}
		int n = 0;
		try {
			if (stx1 != STX1 || stx2 != STX2) {
				throw new MessageMappingException("Expected 0xFA0x5A to begin message");
			}
			int bite;
			while ((bite = inputStream.read()) != ETX1) {
				checkClosure(bite);
				buffer[n++] = (byte) bite;
				if (n >= this.maxMessageSize) {
					throw new IOException("ETX not found before max message length: " + this.maxMessageSize);
				}
			}
			int etx1 = bite;
			checkClosure(etx1);
			int etx2 = inputStream.read();
			checkClosure(etx2);
			// etx1 != ETX1 || etx2 != ETX2 is complain by code analyzer.
			if (etx2 != ETX2) {
				throw new MessageMappingException("Expected 0xFA0x5A to begin message");
			}

			byte[] data = copyToSizedArray(buffer, n - 1); // don't copy crc
			byte expectedCrc = crcFunction(data);
			byte crc = buffer[n - 1];
			if (crc != expectedCrc) {
				throw new MessageMappingException("CRC check fail.");
			}
			return data;

		} catch (IOException | RuntimeException e) {
			publishEvent(e, buffer, n);
			throw e;
		}
	}

	@Override
	public void serialize(byte[] bytes, OutputStream outputStream) throws IOException {
		outputStream.write(STX1);
		outputStream.write(STX2);
		outputStream.write(bytes);
		byte crc = crcFunction(bytes);
		outputStream.write(crc);
		outputStream.write(ETX1);
		outputStream.write(ETX2);
	}

	private byte crcFunction(byte[] cmd) {
		byte crc = cmd[0];
		for (int n = 1; n < cmd.length; n++) {
			crc ^= cmd[n];
		}
		return crc;
	}

}
