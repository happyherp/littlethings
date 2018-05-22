package de.carlos.hackerrank.syncshop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import de.carlos.hackerrank.syncshop.Solution.Center;
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
	
	@Test
	public void testBig() throws IOException{
		Problem problem = buildProblem();
		System.out.println("Waiting for keystroke");
		System.in.read();
		System.out.println("Starting");
		int result = Solution.solve(problem);
		
		System.out.println("Solution: "+result);
		Assert.assertEquals(102837, result);

	}

	public Problem buildProblem() {
		Random rnd = new Random(123);
		
		List<Center> centers = new ArrayList<>();
		
		
		int n = 1000;
		for (int i = 0;i<n;i++){
			Center center = new Center();
			centers.add(center);
		}
		
		int k = 10;
		for (int i = 0;i<k;i++){
			centers.get(rnd.nextInt(n)).fishes |= 1<<i;
		}
		for (int i = 0;i<n*2;i++){
			Center from = centers.get(rnd.nextInt(n));
			Center to = centers.get(rnd.nextInt(n));
			while (to == from || from.roads.containsKey(to)){
				to = centers.get(rnd.nextInt(n));
			}
			int cost = rnd.nextInt(10000)+1;
			from.roads.put(to, cost);
			to.roads.put(from, cost);			
		}
		
		return new Problem(centers, k);
	}

}
