package de.carlos.hackerrank.syncshop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;


//https://www.hackerrank.com/challenges/synchronous-shopping/problem

//Failing: 14,16,17,20,22-29
public class Solution {
	
	static final boolean DEBUG = false;


    static public Scanner scan = new Scanner(System.in);

    
    public static void main(String[] args) {
        
    	Problem problem = readProblem();
    	
    	int solution = solve(problem);
    	System.out.println(solution);
    	
    }

	public static Problem readProblem() {
		int n_centers = scan.nextInt();
    	int m_roads = scan.nextInt();
    	int k_fishes = scan.nextInt();
    	scan.nextLine();
    	List<Center> centers = new ArrayList<>();
    	for(int i = 0;i<n_centers;i++){
    		Center center = new Center();
    		center.id = i+1;
    		Set<Integer> fishSet =  Arrays.stream(scan.nextLine().split(" "))
	    		.skip(1)
	    		.map(Integer::parseInt)
	    		.collect(Collectors.toSet());
    		for (int f:fishSet){
    			center.fishes = center.fishes | i<<(f-1);
    		}
    		centers.add(center);
    	}
    	assert centers.size() == n_centers;
    	for(int i = 0;i<m_roads;i++){
    		Center from = centers.get(scan.nextInt()-1);
    		Center to = centers.get(scan.nextInt()-1);
    		int cost = scan.nextInt();
    		from.roads.put(to, cost);
    		to.roads.put(from, cost);
    	}
    	Problem problem = new Problem(centers, k_fishes);
		return problem;
	}
    
    static int solve(Problem problem) {
		
    	List<Path> pathsToEnd = new ArrayList<>();
    	
    	int allFishes = (int)Math.pow(2,problem.fishes)-1;
    	
    	Walker walker = new Walker(problem.centers.get(0));
    	
    	while (true){
    		Path next = walker.findNextToFinish(problem.endPoint());
    		pathsToEnd.add(next);
    	
    		int matchingPathCost = findMatchingPath(pathsToEnd, next, allFishes);
    		if (matchingPathCost >= 0){
    			return matchingPathCost;
    		}
    	}
	}
    
    static int findMatchingPath(List<Path> pathsToEnd, Path next, int allFishes) {
		int fishesMissing = allFishes & ~next.fishesCollected;
		for (Path other: pathsToEnd){
			if (fishContainsAll(other.fishesCollected,fishesMissing)){
				if (DEBUG) System.out.println("Found working combination of "+next+" with "+other);
				return Math.max(other.totalCost, next.totalCost);
			}
		}
		return -1;
	}

	static class Problem{
    	List<Center> centers;
    	int fishes;
		public Problem(List<Center> centers, int fishes) {
			super();
			this.centers = centers;
			this.fishes = fishes;
		}
		
		Center endPoint() {
			return centers.get(centers.size()-1);
		}
    	
    }

	static class Center{
    	int id;
    	int fishes = 0;
    	Map<Center,Integer> roads = new HashMap<>();
    	@Override
    	public String toString() {
    		return "C"+id;
    	}
    }
	
	static class Path{
		Path prev = null;
		Center to;
		int totalCost;
		int fishesCollected = 0;
		
		Path(Center root){
			to = root;
			totalCost=0;
			fishesCollected = root.fishes;
		}
		
		Path(Path prev, Center to){
			this.prev = prev;
			this.to = to;
			this.totalCost = prev.totalCost + prev.to.roads.get(to);
			this.fishesCollected = prev.fishesCollected | to.fishes;
		}
		
		boolean strictlyBetterOrEqual(Path other){
			return  this.to == other.to
					&& this.totalCost <= other.totalCost 
					&& fishContainsAll(this.fishesCollected, other.fishesCollected);
		}
		
		@Override
		public String toString() {
			return "Path"+getCenterList();
		}

		private List<Center> getCenterList() {
			
			List<Center> l;
			if (this.prev == null){
				l = new ArrayList<>();
			}else{
				l = this.prev.getCenterList();
			}
			l.add(to);
			
			return l;
		}
	}
	
	static boolean fishContainsAll(int a , int b){
		return ~((~a) & b)== -1;
	}
	
	
	static class Walker{
		SortedSet<Path> stubs = new TreeSet<>(Comparator
				.<Path,Integer>comparing(p->p.totalCost)
				.thenComparing(Object::hashCode));//otherwhise TreeSet will treat the paths as equal.
		Map<Center, Set<Path>> processed = new HashMap<>();
		Walker(Center root){
			stubs.add(new Path(root));
		}
		
		public Path findNextToFinish(Center end){
			
			Path path = processNextStub();
			while (path.to != end){
				path = processNextStub();
			}
			return path;			
		}

		public Path processNextStub() {
			Path path = stubs.first();
			stubs.remove(path);
			while (isNoImprovement(path)){
				if (DEBUG) System.out.println("Skipping "+path);
				path = stubs.first();
				stubs.remove(path);				
			}
			if (DEBUG) System.out.println("Processing "+path);
			processed.putIfAbsent(path.to, new HashSet<>());
			processed.get(path.to).add(path);
			for(Center roadTo:path.to.roads.keySet()){
				Path stub = new Path(path, roadTo);
				this.stubs.add(stub);
			}
			return path;
		}
			
		boolean isNoImprovement(Path path) {
			return processed.containsKey(path.to)
					&& processed.get(path.to).stream().anyMatch(other->other.strictlyBetterOrEqual(path));
		}
	}	
}
