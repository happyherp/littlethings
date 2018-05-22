package mypack;

import org.junit.Assert;
import org.junit.Test;
import static java.lang.Double.NaN;
import static java.lang.Math.sqrt;

public class MathTest {
	
	@Test
	public void testSquare(){
		Assert.assertEquals(4.0,    sqrt(16.0), 0.1e-10);
		Assert.assertEquals(2.0,    sqrt(4.0), 0.1e-10);
		Assert.assertEquals(1.4142, sqrt(2.0), 0.1e-3);
		Assert.assertEquals(0.0,    sqrt(0.0), 0.1e-10);
		Assert.assertEquals(NaN,    sqrt(-2.0), 0.1e-10);
	}

}
