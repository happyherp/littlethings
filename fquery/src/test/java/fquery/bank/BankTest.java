package fquery.bank;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fquery.CachedReduction2;
import fquery.ChangingView;
import fquery.Halde;
import fquery.Join;
import fquery.Mapping;
import fquery.MultiJoin;
import fquery.NestedView;
import fquery.Reduction2;

public class BankTest {
	
	
	
	
	
	@Test
	public void testBankAccount(){
		
		Halde halde = new Halde();
		
		
		ChangingView<Account> accounts = halde.get(Account.class);
		ChangingView<Transfer> transfers = halde.get(Transfer.class);
		ChangingView<Deposit> deposits = halde.get(Deposit.class);
		ChangingView<Withdrawal> withdrawals = halde.get(Withdrawal.class);
		
		
		Join<Long, AccountBalance, Transfer, Transfer> balance1 = new Join<Long, AccountBalance, Transfer, Transfer>(
				transfers.index(Transfer::getFromAccountId), 
				transfers.index(Transfer::getToAccountId), 
				(key, outgoing, incoming) -> new AccountBalance(key, 
						- sumTransfers(outgoing) 
						+ sumTransfers(incoming)
						)); 
		
		
		Join<Long, AccountBalance, AccountBalance, Deposit> balance2 = new Join<>(
				balance1.index(AccountBalance::getAccountId),
				deposits.index(Deposit::getAccountId),
				(key, balance, deposits2) -> new AccountBalance(key, 
						+ balance.stream().mapToInt(AccountBalance::getBalance).sum()
						+ deposits2.stream().mapToInt(Deposit::getAmount).sum()
						));
		
		Join<Long, AccountBalance, AccountBalance, Withdrawal> balance3 = new Join<>(
				balance2.index(AccountBalance::getAccountId),
				withdrawals.index(Withdrawal::getAccountId),
				(key, balance, withdrawals2) -> new AccountBalance(key, 
						+ balance.stream().mapToInt(AccountBalance::getBalance).sum()
						- withdrawals2.stream().mapToInt(Withdrawal::getAmount).sum()
						));		
		
		
		halde.write(new Account(1));
		halde.write(new Account(2));
		halde.write(new Transfer(1,2,200));
		
		Assert.assertEquals(-200, balance1.getKey(1L).getBalance());
		Assert.assertEquals(200, balance1.getKey(2L).getBalance());
		Assert.assertEquals(-200, balance2.getKey(1L).getBalance());
		Assert.assertEquals(200, balance2.getKey(2L).getBalance());
		Assert.assertEquals(-200, balance3.getKey(1L).getBalance());
		Assert.assertEquals(200, balance3.getKey(2L).getBalance());
		
		halde.write(new Deposit("Carlos", 1, 500));
		
		Assert.assertEquals(-200, balance1.getKey(1L).getBalance());//Unchanged
		Assert.assertEquals(200, balance1.getKey(2L).getBalance());//Unchanged		
		Assert.assertEquals(300, balance2.getKey(1L).getBalance());
		Assert.assertEquals(300, balance3.getKey(1L).getBalance());
		
		halde.write(new Withdrawal("Carlos", 1, 200));
		
		Assert.assertEquals(-200, balance1.getKey(1L).getBalance());//Unchanged
		Assert.assertEquals(200, balance1.getKey(2L).getBalance());//Unchanged		
		Assert.assertEquals(300, balance2.getKey(1L).getBalance());//Unchanged		
		Assert.assertEquals(100, balance3.getKey(1L).getBalance());
		
	}

	public static int sumTransfers(Collection<Transfer> outgoing) {
		return outgoing.stream().mapToInt(Transfer::getAmount).sum();
	}

	@Test
	public void testWithMultiJoin(){
		Halde halde = new Halde();
		
		
		ChangingView<Account> accounts = halde.get(Account.class);
		ChangingView<Transfer> transfers = halde.get(Transfer.class);
		ChangingView<Deposit> deposits = halde.get(Deposit.class);
		ChangingView<Withdrawal> withdrawals = halde.get(Withdrawal.class);
		
		MultiJoin balanceJoin = new MultiJoin(
				Arrays.asList(
						transfers.index(Transfer::getFromAccountId), 
						transfers.index(Transfer::getToAccountId), 
						deposits.index(Deposit::getAccountId),
						withdrawals.index(Withdrawal::getAccountId)), 
				(key, list)-> {
					List l = (List)list;
					Collection<Transfer> outgoing = (Collection<Transfer>) l.get(0);
					Collection<Transfer> incoming = (Collection<Transfer>) l.get(1);
					Collection<Deposit> deposits_ = (Collection<Deposit>) l.get(2);
					Collection<Withdrawal> withdrawals_ = (Collection<Withdrawal>) l.get(3);
					int sum = 
							- outgoing.stream().mapToInt(Transfer::getAmount).sum()
							+ incoming.stream().mapToInt(Transfer::getAmount).sum()
							+ deposits_.stream().mapToInt(Deposit::getAmount).sum()
							- withdrawals_.stream().mapToInt(Withdrawal::getAmount).sum();
					return new AccountBalance((Long) key, sum);	
				});
		
		halde.write(new Account(1));
		halde.write(new Account(2));
	   
		
		halde.write(new Transfer(1,2,200));
		Assert.assertEquals(-200, ((AccountBalance)balanceJoin.getKey(1L)).getBalance());
		Assert.assertEquals(200, ((AccountBalance)balanceJoin.getKey(2L)).getBalance());		

		halde.write(new Deposit("Carlos", 1, 500));
		Assert.assertEquals(300, ((AccountBalance)balanceJoin.getKey(1L)).getBalance());
		Assert.assertEquals(200, ((AccountBalance)balanceJoin.getKey(2L)).getBalance());
		
		halde.write(new Withdrawal("Carlos", 1, 200));
		Assert.assertEquals(100, ((AccountBalance)balanceJoin.getKey(1L)).getBalance());
		Assert.assertEquals(200, ((AccountBalance)balanceJoin.getKey(2L)).getBalance());
		
		
	}
	
	//TODO: Fix those damn types.

//	@Test
//	public void testWithMultiJoinTyped(){
//		Halde halde = new Halde();
//		
//		
//		ChangingView<Account> accounts = halde.get(Account.class);
//		ChangingView<Transfer> transfers = halde.get(Transfer.class);
//		ChangingView<Deposit> deposits = halde.get(Deposit.class);
//		ChangingView<Withdrawal> withdrawals = halde.get(Withdrawal.class);
//		
//		MultiJoin<Long, AccountBalance, Object> balanceJoin = new MultiJoin<Long, AccountBalance, Object>(
//				Arrays.asList(
//						transfers.index(Transfer::getFromAccountId), 
//						transfers.index(Transfer::getToAccountId), 
//						deposits.index(Deposit::getAccountId),
//						withdrawals.index(Withdrawal::getAccountId)), 
//				(key, list)-> {
//					List l = (List)list;
//					Collection<Transfer> outgoing = (Collection<Transfer>) l.get(0);
//					Collection<Transfer> incoming = (Collection<Transfer>) l.get(1);
//					Collection<Deposit> deposits_ = (Collection<Deposit>) l.get(2);
//					Collection<Withdrawal> withdrawals_ = (Collection<Withdrawal>) l.get(3);
//					int sum = 
//							- outgoing.stream().mapToInt(Transfer::getAmount).sum()
//							+ incoming.stream().mapToInt(Transfer::getAmount).sum()
//							+ deposits_.stream().mapToInt(Deposit::getAmount).sum()
//							- withdrawals_.stream().mapToInt(Withdrawal::getAmount).sum();
//					return new AccountBalance((Long) key, sum);	
//				});
//		
//		halde.read(new Account(1));
//		halde.read(new Account(2));
//	   
//		
//		halde.read(new Transfer(1,2,200));
//		Assert.assertEquals(-200, ((AccountBalance)balanceJoin.getKey(1L)).getBalance());
//		Assert.assertEquals(200, ((AccountBalance)balanceJoin.getKey(2L)).getBalance());		
//
//		halde.read(new Deposit("Carlos", 1, 500));
//		Assert.assertEquals(300, ((AccountBalance)balanceJoin.getKey(1L)).getBalance());
//		Assert.assertEquals(200, ((AccountBalance)balanceJoin.getKey(2L)).getBalance());
//		
//		halde.read(new Withdrawal("Carlos", 1, 200));
//		Assert.assertEquals(100, ((AccountBalance)balanceJoin.getKey(1L)).getBalance());
//		Assert.assertEquals(200, ((AccountBalance)balanceJoin.getKey(2L)).getBalance());
//		
//		
//	}
	
	
	@Test
	public void innerChange(){
		Halde halde = new Halde();
		ChangingView<Transfer> transfers = halde.get(Transfer.class);
		halde.write(new Transfer(1,2,200));
		halde.write(new Transfer(2,1,500));
		halde.write(new Transfer(1,3,100));
		
		NestedView<Transfer,Long> nestedView = new NestedView<>(transfers, Transfer::getFromAccountId);
		Assert.assertEquals(2, nestedView.get(1L).stream().count());
		Assert.assertEquals(500, nestedView.get(2L).iterator().next().getAmount());
		
		Mapping<NestedView<Transfer, Long>.SubView, CachedReduction2<Transfer, Integer>> mapToCount = 
				 new Mapping<>(nestedView, subview -> new CachedReduction2<>(Reduction2.counter(), subview));
		 
		Assert.assertTrue(mapToCount.stream().map(CachedReduction2::getResult).anyMatch(c-> c == 1));
		Assert.assertTrue(mapToCount.stream().map(CachedReduction2::getResult).anyMatch(c-> c == 2));
		 
		 
	}
	
}
