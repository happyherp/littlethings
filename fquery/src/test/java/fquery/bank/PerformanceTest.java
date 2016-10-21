package fquery.bank;

import java.util.Random;

import org.junit.Test;

import fquery.ChangingView;
import fquery.Halde;
import fquery.Index;
import fquery.Join;

public class PerformanceTest {

	
	
	@Test
	public void testPerf(){  
		
		Halde halde = new Halde();
		
		ChangingView<Account> accounts = halde.get(Account.class);
		ChangingView<Transfer> transfers = halde.get(Transfer.class);
		
		
		Join<Long, AccountBalance, Transfer, Transfer> balance = new Join<Long, AccountBalance, Transfer, Transfer>(
				transfers.index(Transfer::getFromAccountId), 
				transfers.index(Transfer::getToAccountId), 
				(key, outgoing, incoming) -> new AccountBalance(key, 
						- BankTest.sumTransfers(outgoing) 
						+ BankTest.sumTransfers(incoming)
						)); 
		
		Index<AccountBalance, Integer> balanceIndex = balance.index(AccountBalance::getBalance);
		
		
		Random random = new Random(5);		
		int ACCOUNTS = (int) 10e2;
		int TRANSFERS = (int) 10e2;
		for (int i = 0;i<ACCOUNTS;i++){
			halde.write(new Account(i));						
			for (int j = 0;j<TRANSFERS;j++){
				halde.write(new Transfer(i, random.nextInt(ACCOUNTS), random.nextInt(1000)));
			}						
		}
		
		
	}
}
