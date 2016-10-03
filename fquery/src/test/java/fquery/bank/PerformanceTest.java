package fquery.bank;

import java.util.Random;

import org.junit.Test;

import fquery.Flatmapping;
import fquery.Halde;
import fquery.Index;
import fquery.Join;
import fquery.RawData;

public class PerformanceTest {

	
	
	@Test
	public void testPerf(){  
		
		Halde halde = new Halde();
		
		Flatmapping<RawData, Account> accounts = halde.get(Account.class);
		Flatmapping<RawData, Transfer> transfers = halde.get(Transfer.class);
		
		
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
			halde.read(new Account(i));						
			for (int j = 0;j<TRANSFERS;j++){
				halde.read(new Transfer(i, random.nextInt(ACCOUNTS), random.nextInt(1000)));
			}						
		}
		
		
	}
}
