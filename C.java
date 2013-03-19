/**
 *  CS378 Algorithm && Complexity 
 *  An implementation for Programming & Problem Solving: 
 *  	Algorithm C
 *  November 14, 2012
 *  
 *	Algorithm C implements a polynomial-time algorithm for the 
 *	Mooov Around problem with general preferences.
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class C {
	
	/**
	 * Global variables
	 */
    private static final int WHITE = 1;
    private static final int GREY = 2;
    private static final int BLACK = 3;
    private static int[] colored;
    private List<List<Integer>> adj;
    private List<List<Integer>> adjRev;
    private List<List<int[]>> preferences;
    private int[] TH;
    private final int V;
    private int E;
    public final static boolean DEBUGGING = false;
    final int INFINITY = Integer.MAX_VALUE;
    
   /**
	 * Main: processes file with block of students, and
	 * their strict preferences. Algorithm A is applied 
	 * to their preferences to know allocations.
	 */
    public static void main(String[] args) throws FileNotFoundException {
    	try{
    		/*
		    long start = System.currentTimeMillis();
		    /*
	            if(DEBUGGING){
	           	 input = "large_samples/large.in";
		  	 	outputFile = "out.txt";
		    }
		    */
		    String input = args[0];
		    String outputFile = args[1];
	            
		    StringBuilder output = new StringBuilder();
		    
		    Scanner scan = new Scanner(new File(input));
		    
		    int numberOfInputs = scan.nextInt();
		    
		    //Looping through set of inputs 
		    for(int inputs = 0; inputs < numberOfInputs; inputs++){
		    	
		    	//Getting input
		        int numberOfStudents = scan.nextInt();
		        List<List<int[]>> studentPreferences = getInput(scan, numberOfStudents);
		        
		        //Creating the graph
		        C G = new C(studentPreferences);
		        
		        boolean actionEnabled = true;
		        
		        //Will perform a revelation actions, if none
		        //exist, it will try a trade action, else
		        //we are done
		        while(actionEnabled){
		        	
		        	actionEnabled = false;
		        	List<Integer> exh = G.exhausted();
		        	if(exh.size() > 0){		//revelation actions
		        		for(int j = 0; j < exh.size();j++)
		        				G.reveal(exh.get(j));
		        		actionEnabled = true;
		        	}
		        	else {
		        			actionEnabled = G.trade();
		            	}
		        	}
	        		output.append(G.output());
	        		writeOutput(output.toString(), outputFile);
		        }
		    
		    /*
		    if(DEBUGGING){

					System.out.print ln(output.toString());
		    	cmp(outputFile, input.replace(".in", ".out");
		    }
		    
		    long end = System.currentTimeMillis();

		    System.out.println("TIME: " + (end - start)/1000.0);
		    */
		    
    	}catch(ArrayIndexOutOfBoundsException e){
    		System.out.println("A: missing File Input/Output \nUsage: A [File Input] [File Output]");
    		e.printStackTrace();
    	}
    	catch(IOException e){
    		e.printStackTrace();
    	}

    }

    /**
     * 
     * @return graph G
     */
    public List<List<Integer>> G(){
    	return adj;
    }
    
    /**
     * 
     * @return Graph Reversal
     */
    public List<List<Integer>> Gr(){
    	return adjRev;
    }
    
    /**
     * A constructor: creates a graph with
     * n vertices for students, and n spaces
     */
    public C(List<List<int[]>> preferences) {
        adj = new ArrayList<List<Integer>>();
        adjRev = new ArrayList<List<Integer>>();
    	this.V = 2 * preferences.size();
        this.E = 0;
        this.TH = new int[V/2];
    	this.preferences = preferences;
        int E = V;
        for (int i = 0; i < V/2; i++) {
            adj.add(new ArrayList<Integer>());	//out(G, i) is empty for all student i in U
            //Adding to the reversal graph
            adjRev.add(new ArrayList<Integer>());
            adjRev.get(i).add(i + (V/2));
            TH[i] = -1;
        }
        for (int j = V/2; j < V; j++) {
            int v = j, w = j - (V/2);
            adj.add(new ArrayList<Integer>());
            adj.get(v).add(w); // j -> i
            //reversal graph
            adjRev.add(new ArrayList<Integer>());
        }
    } 
    
    /**
     * 
     * @return The output allocation j to i
     */
    public String output(){
    	StringBuilder out = new StringBuilder();
    	String[] map = new String[V/2];
    	for(int v = V/2; v < V; v++)
    		map[adj.get(v).get(0)] = v - V/2 + "";
    	for(String str: map){
    		out.append(str + " ");
    	}
    	return out.append("\n").toString();
    }
    
    /**
     * 
     * @param G
     * @param s
     * @return void
     */
	private static void print(List<List<Integer>> G, String s){
    	System.out.println("Printing " + s);
    	int k = 0;
        for(List<Integer> i : G){
        	System.out.print(k + ": ");
        	for(Integer j : i){
        		System.out.print(j + " ");
        	}
        	System.out.println();
        	k++;
        }
    }
	
    /**
	 * Number of vertices
	 */
    public int V() {
        return V;
    }
    
    
    /**
	 * Number of edges
	 */
    public int E() {
        return E;
    }
    
    /**
     * 
     * @param j
     * @return The unique student i such that edge (j, i) belongs in E
     */
    private Integer student(int j){
    	assert j >= V/2 && j < V;
    	return adj.get(j).get(0);
    }
    
    
    /**
     * 
     * @param i
     * @return The unique space j such that student(G, j) = i
     */
    private Integer space(int i){
    	assert i >= 0 && i < V/2;
    	return adjRev.get(i).get(0);
    }
    
    
    /**
     * 
     * @param i
     * @return All spaces {j | (i, j) in E}
     */
    private List<Integer> out(int i){
    	assert i >= 0 && i < V/2;
    	return adj.get(i);
    }
    /**
     * 
     * @return Students that belong in a cycle are satisfied
     */
    
    private List<Integer> satisfied(){
    	List<Integer> sat = new ArrayList<Integer>();
    	for(int i = 0; i < V/2; i++){
    		List<Integer> out = out(i);
    		Integer jj = space(i);
    		for(int j : out){
    			if(jj == j){
    				sat.add(i);
    				break;
    			}
    		}
    	}
    	return sat;
    }
    
    /**
     * 
     * @return U\satisfied()
     */
    private List<Integer> unsatisfied(){
    	List<Integer> sat = satisfied();
    	List<Integer> unsat = new ArrayList<Integer>();
    	int start = 0;
    	for(int l = 0; l < sat.size(); l++){
    		int end = sat.get(l); 
	    	for(int i = start;i < end; i++){
	    		unsat.add(i);
	    	}
	    	start = end + 1 ;
    	}
    	for(int i = start; i < V/2; i++){
    		unsat.add(i);
    	}
    	return unsat;  
    }

    /**
     * 
     * @param j
     * @return The length of a shortest path form j to a student in unsatisfied(). 
     * If there is no such path, we define distance(j) as INFINITY
     * 
     * Implemented by using a minor modification to dijkstra's algorithm
     */
    private int distance(int j){
    	assert j >= V/2 && j < V;
    	List<Integer> unsat = unsatisfied();
    	int min = INFINITY;

    	for(int i = 0; i < unsat.size(); i++){	//Iterate over all unsatisfied students
    		Queue<Integer> q=new LinkedList<Integer>();
    		int[] d = new int[V];
    		for(int k = 0; k < d.length; k++){
    			d[k] = INFINITY;
    		}
    		q.add(unsat.get(i));
    		d[unsat.get(i)] = 0;

				//Look at adjacent nodes
    		while(!q.isEmpty()){
    			int jj = q.poll();
    			if(j == jj){
    				break;
    			}
    			for(int k = 0; k < adjRev.get(jj).size(); k++){
    				int v = adjRev.get(jj).get(k);
    				int dd = d[jj] + 1;
    				if(d[v] > dd){
    					d[v] = dd;
    					q.add(v);
    				}
    			}
    		}
    		if(d[j] < min){
				min = d[j];
			}
    	}
    	return min;
    }
    
    
    /**
     * 
     * @param i
     * @return The minimum space in V', null if not such path(INFINITY on all j in out(i))
     */
    private Integer next(int i){
    	int min = INFINITY;
    	int next = INFINITY;
    	List<Integer> out = out(i);
    	List<Integer> temp = new ArrayList<Integer>();

    	for(int ii = 0; ii < out.size(); ii++){
    		temp.add(out.get(ii));
    	}

    	Collections.sort(temp);
    	int j = 0;

    	while(j < temp.size()){
    		int dist = distance(temp.get(j));
    		if(dist < min){
    			min = dist;
    			next = temp.get(j);
    		}
    		j++;
    	}

    	if(next==INFINITY)
    		return null;
    	return next;
    }
    
    /**
     * 
     * @return G' = (U, V, E \ E') where E' = {(i,j) | i in U ^ j != next(i)}
     */
    private List<List<Integer>> pruned(){
    	List<List<Integer>> GPrime = new ArrayList<List<Integer>>();
    	for(int i = 0; i < V/2; i++){
    		List<Integer> temp = new ArrayList<Integer>();
    		temp.add(next(i));
    		GPrime.add(temp);
    	}
    	////////////////////////////////////////////////
    	boolean areNull = true;
    	for(int i = 0; i < GPrime.size(); i++){
    		if(GPrime.get(i).get(0) != null){
    			areNull = false;
    			break;
    		}
    	}
    	if(areNull){
    		return null;
    	}
    	///////////////////////////////////////////////
    	for(int j = V/2; j < V; j++){
    		List<Integer> temp = new ArrayList<Integer>();
    		for(int i = 0; i < adj.get(j).size(); i++){
    			temp.add(adj.get(j).get(i));
    		}
    		GPrime.add(temp);
    	}
    	return GPrime;
    }
    
    /**
     * 
     * @param pruned
     * @return The set of all directed cycles in pruned.
     */
    public List<List<int[]>> cycles(List<List<Integer>> pruned){
    	colored = new int[V];
        ArrayList<int[]> cycleEdges = new ArrayList<int[]>();

        //color all vertices as white
        for(int v = 0; v < V; v++){
           colored[v] = WHITE;
        }
        //visit each vertex
        for(int v = 0; v < V; v++){
           visit(v, cycleEdges, pruned);
        }
        //Getting a cycle with length greater than two
        List<List<int[]>> cycles = new ArrayList<List<int[]>>();
        for(int[] d: cycleEdges){
     	   List<int[]> temp = new ArrayList<int[]>();
     	   
     	   int v = d[0];
     	   int u = d[1];
     	   
     	   while( u != d[0]){
     		   temp.add(new int[]{v,u});
     		   v = pruned.get(v).get(0);//space(v);
     		   u = pruned.get(u).get(0);//space(u);
     	   }
     	   
     	   temp.add(new int[]{v,u});
     	   
     	   if(temp.size() > 2){
     		   cycles.add(temp);
     		   break;
     	   }
        }	  
        return cycles;
     }
    
    /**
	 * Used by findCycles()
	 */
    private void visit(int v, ArrayList<int[]> cycleEdges, List<List<Integer>> pruned){
      colored[v] = GREY;
      Integer u = pruned.get(v).get(0);
      if(u != null)
      if( colored[u] == GREY ){
    	 cycleEdges.add(new int[]{v,u});
      }
      else if( colored[u] == WHITE ){
         visit(u, cycleEdges, pruned);
      }
      colored[v] = BLACK;
    }
    
    /**
     * 
     * @return G = (U, V, (E \ E') U E'') 
     * 	where E'  = {(j,i) in V X U | j in C ^ student(i) = i}
     *  where E'' = {(j,i) in V X U | i in C ^ next(i) = j}
     */
    private boolean trade(){
    	List<List<Integer>> pruned = pruned();
    	boolean isNull = pruned == null;
    	if(!isNull){
	    	List<List<int[]>> cycles = cycles(pruned);
	    	for(int C = 0; C < cycles.size(); C++){
	    		
	    		//E prime
	    		Integer jj = 0;
				if(cycles.get(C).get(0)[0] < V/2)
					jj = 1;
				for(int j = jj; j < cycles.get(C).size(); j += 2){
					//j
					adj.get(cycles.get(C).get(j)[0]).clear();
					//i
					adjRev.get(cycles.get(C).get(j)[1]).clear();
				}//E double prime
	    		int ii = 0;
				if(cycles.get(C).get(0)[0] >= V/2)
					ii = 1;
				for(int j = ii; j < cycles.get(C).size(); j += 2){
					jj = cycles.get(C).get(j)[1];
					
					int i = cycles.get(C).get(j)[0];
					adj.get(jj).add(i);
					adjRev.get(i).add(jj);
				}
	    	}
    	}
    	return !isNull;
    }
    
    /**
     * 
     * @return The set of all students i in unsatisfied()
     */
    private List<Integer> exhausted(){
    	List<Integer> unsat = unsatisfied();
    	List<Integer> ex = new ArrayList<Integer>();
    	for(int i = 0; i < unsat.size(); i++){
    		if(next(unsat.get(i)) == null){
    			ex.add(unsat.get(i));
    		}
    	}
    	return ex;
    }
    
    /**
     * 
     * @param i
     * @return It reveal the next tier for student i
     */
    private List<Integer> top(int i){
    	List<Integer> tier = new ArrayList<Integer>();
    	++TH[i];
    	assert TH[i] < preferences.get(i).size();
    	for(int j = 0; j < preferences.get(i).get(TH[i]).length; j++){
    		tier.add(preferences.get(i).get(TH[i])[j]);
    	}
    	return tier;
    }
    
    /**
     * 
     * @param i
     * G = (U, V, E U E') where E' = {(i,j) | j in top(P, i, V \ out(i)}
     */
    private void reveal(int i){
    	List<Integer> newTier = top(i);
    	for(int j = 0; j < newTier.size(); j++){
    		adj.get(i).add(newTier.get(j) + (V/2));
    		adjRev.get(newTier.get(j) + (V/2)).add(i);
    	}
    }
    
    
    /**
	 * Prints the graph creates by algorithm A
	 * For testing purposes
	 */
    public String toString() {
        StringBuilder s = new StringBuilder();
        String NEWLINE = System.getProperty("line.separator");
        for(int v = 0; v < V/2; v++) {
        	String temp = "";
        	for(int i = 0; i < adj.get(v).size(); i++)
        		temp += (adj.get(v).get(i) - V/2) + " " ;
          s.append(v + ": " + temp + NEWLINE);
        }
        for(int v = V/2; v < V; v++) {
            s.append((v - V/2) + ": " + adj.get(v).get(0) + " " + NEWLINE);
        }
        return s.toString();
    }
    
    
    
    /**
  	 * Gets the input, student preferences.
  	 */
      public static List<List<int[]>> getInput(Scanner scan, int numberOfStudents){
      	//Getting all preferences
          List<List<int[]>> studentPreferences = new ArrayList<List<int[]>>();
          scan.nextLine();
          for(int j = 0; scan.hasNextLine() && j < numberOfStudents; j++){
              String line = scan.nextLine();
              List<int[]> temp = new ArrayList<int[]>(); 
              for(String st: line.split("-1 ")){
              	temp.add(strArrayToIntArray(st.split("\\s")));
              }
              studentPreferences.add(temp);
          }
          return studentPreferences;
      }
    
     /**
  	  * Converts an array of string compose of integer characters,
  	  * to an array of integers
  	  */
      private static int[] strArrayToIntArray(String[] str){
      	int[] intArray = new int[str.length];
      	for(int i = 0; i < intArray.length; i++){
      		intArray[i] = Integer.parseInt(str[i]);
      	}
      	return intArray;
      }
    
    /**
     * Writes results to output to outputFile
     */
    public static void writeOutput(String output, String outputFile){
    	try{
		  BufferedWriter out = new BufferedWriter(new FileWriter(outputFile));
		  out.write(output);
		  out.close();
    	}catch (Exception e){	//Catch exception if any
    	  System.err.println("Error: " + e.getMessage());
    	}
    }
    
    /**
	 * Compares two files, line by line.
	 * Not use in main implementation,
	 * only for testing purposes.
	 */
    public static void cmp(String myOP, String OP) throws IOException{
    		BufferedReader myOutput = 
    			new BufferedReader(new InputStreamReader(new FileInputStream(myOP)));
    	
        BufferedReader Output = 
        		new BufferedReader(new InputStreamReader(new FileInputStream(OP)));
        
        String thisLine = "";
        String thatLine = "";
        int inputs = 0;
		
        while ((thisLine != null) || (thatLine != null)) {  
			if(!thisLine.equals(thatLine)){
				break;
			}
			thisLine = myOutput.readLine();
			thatLine = Output.readLine();

			System.out.println(thisLine != null ? (inputs++ + 1) + "\t:\t" + thisLine : "");
		}

		if(thisLine == null && thatLine == null){
			System.out.println("Files Matched!");
		}
		else
			System.out.println("Files Differed!");
    	}
}
