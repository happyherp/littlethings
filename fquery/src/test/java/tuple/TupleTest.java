package tuple;

import org.junit.Test;

import org.junit.Assert;

public class TupleTest {

	
	@Test
	public void test(){
		Single<String> s = new Single<String>("A");
		
		More<Integer, Single<String>> is = s.add(1);
		
		More<Long, More<Integer, Single<String>>> lis = is.add(1L);
		
		
		String str = lis.next().next().get();
		Assert.assertEquals("A", str);
	}
}
