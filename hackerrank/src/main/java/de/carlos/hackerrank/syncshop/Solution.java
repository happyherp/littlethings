package de.carlos.hackerrank.syncshop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;


//https://www.hackerrank.com/challenges/synchronous-shopping/problem

//Failing: 14,16,22-29
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
    		Set<Fish> fishes = Arrays.stream(scan.nextLine().split(" "))
				.skip(1)
				.map(Integer::parseInt)
				.map(fishIndex->Fish.values()[fishIndex-1])
				.collect(Collectors.toSet());
    		if (!fishes.isEmpty()){    			
    			center.fishes = EnumSet.copyOf(fishes);
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
    	
    	Set<Fish> allFishes = EnumSet.noneOf(Fish.class);
    	for (int i = 0;i<problem.fishes;i++){
    		allFishes.add(Fish.values()[i]);
    	}
    	
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
    
    static int findMatchingPath(List<Path> pathsToEnd, Path next, Set<Fish> allFishes) {
		Set<Fish> fishesMissing = EnumSet.copyOf(allFishes);
		fishesMissing.removeAll(next.fishesCollected);
		for (Path other: pathsToEnd){
			if (other.fishesCollected.containsAll(fishesMissing)){
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
    	Set<Fish> fishes =  EnumSet.noneOf(Fish.class);
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
		Set<Fish> fishesCollected;
		int hashCode;
		
		Path(Center root){
			to = root;
			totalCost=0;
			fishesCollected = root.fishes;
			this.hashCode = getCenterList().hashCode();
		}
		
		Path(Path prev, Center to){
			this.prev = prev;
			this.to = to;
			this.totalCost = prev.totalCost + prev.to.roads.get(to);
			this.fishesCollected = EnumSet.copyOf(prev.fishesCollected);
			this.fishesCollected.addAll(to.fishes);
			this.hashCode = getCenterList().hashCode();
		}
		
		boolean strictlyBetterOrEqual(Path other){
			return  this.to == other.to
					&& this.totalCost <= other.totalCost 
					&& this.fishesCollected.containsAll(other.fishesCollected);
		}
		
		boolean strictlyBetter(Path other){
			return this.strictlyBetterOrEqual(other) && 
					(this.totalCost < other.totalCost || this.fishesCollected.size() > other.fishesCollected.size());
		}
		@Override
		public String toString() {
			return "Path"+getCenterList();
		}

		List<Center> getCenterList() {
			
			List<Center> l;
			if (this.prev == null){
				l = new ArrayList<>();
			}else{
				l = this.prev.getCenterList();
			}
			l.add(to);
			
			return l;
		}

		Path returnTrip(){
			List<Center> centers = getCenterList();
			Collections.reverse(centers);
			Path reversed = new Path(centers.remove(0));
			while(!centers.isEmpty()){
				reversed = new Path(reversed, centers.remove(0));
			}
			return reversed;
		}
		
		@Override
		public int hashCode() {
			return hashCode;
		}
	}

	static Map<Center, Path> findFastestWayToEnd(Problem problem){
		Map<Center, Path> fastestPaths = new HashMap<>();

		SortedMap<Integer,List<Path>> stubs = new TreeMap<>();
		stubs.put(0, new LinkedList<>());
		stubs.get(0).add(new Path(problem.endPoint()));

		while(!stubs.isEmpty()){
			int cost = stubs.firstKey();
			List<Path> cheapestPaths = stubs.get(cost);
			Path stub = cheapestPaths.remove(0);
			if (cheapestPaths.isEmpty()){
				stubs.remove(cost);
			}
			if (!fastestPaths.containsKey(stub.to)){
				fastestPaths.put(stub.to, stub.returnTrip());

				//Find new ways to go.
				for (Center roadTo:stub.to.roads.keySet()){
					Path newStub = new Path(stub, roadTo);
					stubs.putIfAbsent(newStub.totalCost, new LinkedList<>());
					stubs.get(newStub.totalCost).add(newStub);
				}
			}

		}

		return fastestPaths;
	}
	
	
	static class Walker{
		SortedMap<Integer, List<Path>> stubs = new TreeMap<>();
		Map<Center, List<Path>> processed = new HashMap<>();
		Walker(Center root){
			stubs.put(0,new LinkedList<>(Collections.singleton(new Path(root))));
		}
		
		public Path findNextToFinish(Center end){
			
			Path path = processNextStub();
			while (path.to != end){
				path = processNextStub();
			}
			return path;			
		}

		public Path processNextStub() {
			Path path = findNextRelevantPath();
			if (DEBUG) System.out.println("Processing "+path);
			if (!processed.containsKey(path.to)) processed.put(path.to, new LinkedList<>());
			
			//Remove paths that are worse than the new one.
			Iterator<Path> otherPaths = processed.get(path.to).iterator();
			while (otherPaths.hasNext()){
				Path other = otherPaths.next();
				if (path.fishesCollected.containsAll(other.fishesCollected)){
					otherPaths.remove();
				}
			}
			processed.get(path.to).add(path);
			for(Center roadTo:path.to.roads.keySet()){
				Path stub = new Path(path, roadTo);
				if (!stubs.containsKey(stub.totalCost))stubs.put(stub.totalCost, new LinkedList<>());
				this.stubs.get(stub.totalCost).add(stub);
			}
			return path;
		}

		public Path findNextRelevantPath() {
			Path path = removeNextStub();
			while (isNoImprovement(path)){
				if (DEBUG) System.out.println("Skipping "+path);
				path = removeNextStub();			
			}
			return path;
		}

		public Path removeNextStub() {
			List<Path> shortestStubs = stubs.get(stubs.firstKey());
			Path path = shortestStubs.get(0);
			shortestStubs.remove(path);
			if (shortestStubs.isEmpty()){
				stubs.remove(stubs.firstKey());
			}
			return path;
		}
			
		boolean isNoImprovement(Path path) {
			
			List<Path> pathsToSameCenter = processed.get(path.to);
			if (pathsToSameCenter != null){
				if (DEBUG) System.out.println("Checking "+pathsToSameCenter.size()+" paths ");
				for (Path other:pathsToSameCenter){
					if (other.strictlyBetterOrEqual(path)){
						return true;
					}
				}
			}
			return false;
		}
	}


	enum Fish{
		F1,F2,F3,F4,F5,F6,F7,F8,F9,F10;
	}
		
    
}
