package de.carlos.hackerrank.syncshop;

import java.util.Scanner;

import org.junit.Assert;
import org.junit.Test;

import de.carlos.hackerrank.syncshop.Solution.Path;
import de.carlos.hackerrank.syncshop.Solution.Problem;
import de.carlos.hackerrank.syncshop.Solution.Walker;

public class SyncShopTest {
	
	@Test
	public void testFull(){
		
		Solution.scan = new Scanner(SyncShopTest.class.getResourceAsStream("input"));
		Solution.main(null);
		
	}
	
	@Test
	public void testWalker(){
		
		Solution.scan = new Scanner(SyncShopTest.class.getResourceAsStream("input"));
		Problem problem = Solution.readProblem();
		
		
		Solution.Walker walker = new Walker(problem.centers.get(0));
		Path nextStub = walker.processNextStub();
		Assert.assertEquals(problem.centers.get(0), nextStub.to);
		
		Path pathToEnd = walker.findNextToFinish(problem.endPoint());
		Assert.assertEquals(problem.endPoint(), pathToEnd.to);
		
		
	}

}
