package fquery.bank;

import java.io.Serializable;

public class Withdrawal implements Serializable{
	
	String toPerson;
	long accountId;
	int amount;
	public Withdrawal(String toPerson, long accountId, int amount) {
		super();
		this.toPerson = toPerson;
		this.accountId = accountId;
		this.amount = amount;
	}
	public String getToPerson() {
		return toPerson;
	}
	public long getAccountId() {
		return accountId;
	}
	public int getAmount() {
		return amount;
	}
	
	

}
