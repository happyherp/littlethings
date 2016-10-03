package fquery.bank;

import java.io.Serializable;

public class Transfer implements Serializable{
	
	long fromAccountId;
	long toAccountId;
	int amount;
	public Transfer(long fromAccountId, long toAccountId, int amount) {
		super();
		this.fromAccountId = fromAccountId;
		this.toAccountId = toAccountId;
		this.amount = amount;
	}
	public long getFromAccountId() {
		return fromAccountId;
	}
	public long getToAccountId() {
		return toAccountId;
	}
	public int getAmount() {
		return amount;
	}

	
	
}
