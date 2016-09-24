package fquery;

import java.time.Instant;

public class RawData {
	
	public byte[] payload;
	public Instant timeReceived;
	public long id;
	public RawData(byte[] payload, Instant timeReceived, long counter) {
		super();
		this.payload = payload;
		this.timeReceived = timeReceived;
		this.id = counter;
	}

	
}
