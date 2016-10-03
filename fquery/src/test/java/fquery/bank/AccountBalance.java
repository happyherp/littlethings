package fquery.bank;

public class AccountBalance {

	
	private Long accountId;

	private int balance;

	public AccountBalance(Long accountId, int balance) {
		super();
		this.accountId = accountId;
		this.balance = balance;
	}

	public Long getAccountId() {
		return accountId;
	}

	public int getBalance() {
		return balance;
	}
	
	

	
}
