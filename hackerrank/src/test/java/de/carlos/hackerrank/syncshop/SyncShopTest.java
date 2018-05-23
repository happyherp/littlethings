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
import de.carlos.hackerrank.syncshop.Solution.Fish;
import de.carlos.hackerrank.syncshop.Solution.Path;
import de.carlos.hackerrank.syncshop.Solution.Problem;
import de.carlos.hackerrank.syncshop.Solution.Walker;

public class SyncShopTest {
	
	@Test
	public void test0(){
		
		Solution.scan = new Scanner(SyncShopTest.class.getResourceAsStream("input0.txt"));
    	Problem problem = Solution.readProblem();
    	
    	int solution = Solution.solve(problem);
    	System.out.println(solution);
    	Assert.assertEquals(30, solution);
		
	}
	
	
	@Test
	public void test1(){
		
		Solution.scan = new Scanner(SyncShopTest.class.getResourceAsStream("input1.txt"));
    	Problem problem = Solution.readProblem();
    	
    	int solution = Solution.solve(problem);
    	System.out.println(solution);
    	Assert.assertEquals(792, solution);
		
	}
	
	/**
	 * 
	 * Takes 5.4s
	 */
	@Test
	public void test14(){
		
		Solution.scan = new Scanner(SyncShopTest.class.getResourceAsStream("input14.txt"));
    	Problem problem = Solution.readProblem();
    	//waitForMe();
    	int solution = Solution.solve(problem);
    	System.out.println(solution);
    	Assert.assertEquals(7461, solution);
		
	}
	
	@Test
	public void testWalker(){
		
		Solution.scan = new Scanner(SyncShopTest.class.getResourceAsStream("input0.txt"));
		Problem problem = Solution.readProblem();
		
		
		Solution.Walker walker = new Walker(problem.centers.get(0));
		Path nextStub = walker.processNextStub();
		Assert.assertEquals(problem.centers.get(0), nextStub.to);
		
		Path pathToEnd = walker.findNextToFinish(problem.endPoint());
		Assert.assertEquals(problem.endPoint(), pathToEnd.to);
	}
	
	@Test
	public void testBig(){
		Problem problem = buildProblem();
//		waitForMe();
		int result = Solution.solve(problem);
		
		System.out.println("Solution: "+result);
		Assert.assertEquals(102837, result);
		
		//Bisher ca 6s

	}


	public void waitForMe() {
		System.out.println("Waiting for enter");
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Starting");
	}

	public Problem buildProblem() {
		Random rnd = new Random(123);
		
		List<Center> centers = new ArrayList<>();
		
		
		int n = 1000;
		for (int i = 0;i<n;i++){
			Center center = new Center();
			center.id=i+1;
			centers.add(center);
		}
		
		int k = 10;
		for (int i = 0;i<k;i++){
			centers.get(rnd.nextInt(n)).fishes.add(Fish.values()[i]);
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
