/* ShortestPath.java
   Author: AJ Po-Deziel
   
   Template originally created by Bill Bird, 08/02/2014.
   
   This template includes some testing code to help verify the implementation.
   To interactively provide test inputs, run the program with
	java ShortestPath
	
   To conveniently test the algorithm with a large input, create a text file
   containing one or more test graphs (in the format described below) and run
   the program with
	java ShortestPath file.txt
   where file.txt is replaced by the name of the text file.
   
   The input consists of a series of graphs in the following format:
   
    <number of vertices>
	<adjacency matrix row 1>
	...
	<adjacency matrix row n>
	
   Entry A[i][j] of the adjacency matrix gives the weight of the edge from 
   vertex i to vertex j (if A[i][j] is 0, then the edge does not exist).
   Note that since the graph is undirected, it is assumed that A[i][j]
   is always equal to A[j][i].
	
   An input file can contain an unlimited number of graphs; each will be 
   processed separately.
*/

import java.lang.Double;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Vector;
import java.io.File;


//Vertex class implementation (review for example on pg. 642)
class Vertex implements Comparable<Vertex>{
	public final int currVertex;
	public final double distance;

	public Vertex(int currVertex, double distance) {
		this.currVertex = currVertex;
		this.distance = distance;
	}//end Edge class constructor

	public double distance() {
		return distance;
	}//end weight

	public int from() {
		return currVertex;
	}//end from
	
	public int compareTo(Vertex that) {
		if (this.distance() < that.distance()) {
			return -1;
		} else if (this.distance() > that.distance()) {
			return 1;
		} else {
			return 0;
		}
	}
}//end class Vertex

//Do not change the name of the ShortestPath class
public class ShortestPath{
	
	private static PriorityQueue<Vertex> shortPath;

	/* ShortestPath(G)
		Given an adjacency matrix for graph G, return the total weight
		of a minimum weight path from vertex 0 to vertex 1.
		
		If G[i][j] == 0, there is no edge between vertex i and vertex j
		If G[i][j] > 0, there is an edge between vertices i and j, and the
		value of G[i][j] gives the weight of the edge.
		No entries of G will be negative.
	*/
	static int ShortestPath(int[][] G){
		int numVerts = G.length;
		int totalWeight = 0;
		
		double[] dist = new double[numVerts];
		shortPath = new PriorityQueue<Vertex>();
		
		dist[0] = 0;
		shortPath.add(new Vertex(0, 0));

		for (int i = 0; i < numVerts; i++) {
			dist[i] = Double.POSITIVE_INFINITY;
			shortPath.add(new Vertex(i, Double.POSITIVE_INFINITY));			
		}//end for
		
		boolean[] visited = new boolean[numVerts];
		
		for (int i = 0; i < numVerts; i++) {
			visited[i] = false;
		}//end for
		
		while(shortPath.size() != 0) {
			Vertex w = shortPath.poll();
			visited[w.currVertex] = true;
			
			for (int i = 0; i < numVerts; i++) {				
				if (G[i][w.currVertex] != 0) {
					if (!visited[i]) {
						if (Double.isInfinite(dist[i]) || dist[i] > dist[w.currVertex] + G[i][w.currVertex]) {
							Vertex v = new Vertex(i, dist[i]);
							dist[i] = w.distance() + G[i][w.currVertex];
							shortPath.remove(v);
							shortPath.add(new Vertex(i, dist[i]));
						} else {
							continue;
						}
					}//end if statement
				}//end if statement
			}//end for
		}//end while
		shortPath.clear();
		totalWeight = (int)dist[1];
		return totalWeight;
		
	}
	
		
	/* main()
	   Contains code to test the ShortestPath function. You may modify the
	   testing code if needed, but nothing in this function will be considered
	   during marking, and the testing process used for marking will not
	   execute any of the code below.
	*/
	public static void main(String[] args){
		Scanner s;
		if (args.length > 0){
			try{
				s = new Scanner(new File(args[0]));
			} catch(java.io.FileNotFoundException e){
				System.out.printf("Unable to open %s\n",args[0]);
				return;
			}
			System.out.printf("Reading input values from %s.\n",args[0]);
		}else{
			s = new Scanner(System.in);
			System.out.printf("Reading input values from stdin.\n");
		}
		
		int graphNum = 0;
		double totalTimeSeconds = 0;
		
		//Read graphs until EOF is encountered (or an error occurs)
		while(true){
			graphNum++;
			if(graphNum != 1 && !s.hasNextInt())
				break;
			System.out.printf("Reading graph %d\n",graphNum);
			int n = s.nextInt();
			int[][] G = new int[n][n];
			int valuesRead = 0;
			for (int i = 0; i < n && s.hasNextInt(); i++){
				for (int j = 0; j < n && s.hasNextInt(); j++){
					G[i][j] = s.nextInt();
					valuesRead++;
				}
			}
			if (valuesRead < n*n){
				System.out.printf("Adjacency matrix for graph %d contains too few values.\n",graphNum);
				break;
			}
			long startTime = System.currentTimeMillis();
			
			int totalWeight = ShortestPath(G);
			long endTime = System.currentTimeMillis();
			totalTimeSeconds += (endTime-startTime)/1000.0;
			
			System.out.printf("Graph %d: Minimum weight of a 0-1 path is %d\n",graphNum,totalWeight);
		}
		graphNum--;
		System.out.printf("Processed %d graph%s.\nAverage Time (seconds): %.2f\n",graphNum,(graphNum != 1)?"s":"",(graphNum>0)?totalTimeSeconds/graphNum:0);
	}
}
