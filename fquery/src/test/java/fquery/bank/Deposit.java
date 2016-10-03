package fquery.bank;

import java.io.Serializable;

public class Deposit implements Serializable{
	
	String fromPerson;
	long accountId;
	int amount;
	public Deposit(String toPerson, long accountId, int amount) {
		super();
		this.fromPerson = toPerson;
		this.accountId = accountId;
		this.amount = amount;
	}
	public String getToPerson() {
		return fromPerson;
	}
	public long getAccountId() {
		return accountId;
	}
	public int getAmount() {
		return amount;
	}
	
	

}
