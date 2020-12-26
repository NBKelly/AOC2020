package com.nbkelly.advent;
import com.nbkelly.helper.DebugLogger;
import com.nbkelly.helper.ConceptHelper;
import java.util.Scanner;

import java.util.*;

public class Advent17 extends ConceptHelper {
    //BE AWARE OF:
    //  DEBUG, IGNORE UNCLEAN, TIMER, DEBUG_TIME_MAGNITUDE
    //  the timer and debug functions make use of these

    //BE AWARE OF:
    //  PAGE_ENABLED
    //  PAGE_ENABLED disables real time input, but allows for arbitrary navigation of input stream (by line)
    //  making use of the Reset and Page functions, ie
    //  Page()          -> int page
    //  Reset(int page) -> ()
    // to pass a logger to another class:
    //   getDebugLogger()  :  seeDebugLogger.java

    // Input:
    //   * NOTE: checking must be explicit (existence is assumed)
    
    //   hasNextInt()        -> boolean, input contains another int
    //   hasNextBigInteger() -> boolean, same as above but bigint
    //   hasNextLine()       -> boolean, same as above but String (full line)
    //   hasNext()           -> boolean, same as above but String (token)
    //   hasNextDouble()     -> boolean, same as above but double
    //   hasNextBigDecimal() -> boolean, same as above but bigdecimal
    //   nextInt()           -> integer, gets next int from stream
    //   nextBigInteger()    -> bigint,  gets next bigint from stream
    //   nextLine()          -> String,  gets all text from stream up to next line break
    //   next()              -> string, gets next token from the stream
    //   nextBigInteger()    -> bigint,  gets next bigint from stream
    //   nextBigDecimal()    -> bigdec,  gets next bigdecimal from stream
    
    // To print:
    //   Errors: ERR(x)       -> uses stderr, only enabled with -se
    //   Line:   println(x)
    //   Block:  print(x)     -> inserts space after integer argument
    //   Format: printf(x,[]) -> standard printf
    //   Debug:  DEBUG(x)     -> uses stderr, only enabled with -d
    //   Debug:  DEBUGF(x,[]) -> uses stderr, only enabled with -d
    //   Timer:  TEBUG(x)     -> uses stderr, enabled with -d/-t
    //   Timer:  TEBUGF(x,[]) -> uses stderr, enabled with -d/-t
    //
    // These take input of arbitrary object, or integer
    // V(object) = object.toString();

    // Timer:
    // * start()             -> gets time, returns timer too
    //   split()             -> split(null)
    // * split(Str)          -> prints split time, and also reason
    // * total(Str, bool)    -> prints total time, reason, bool = reset
    //   total()             -> total(null, false)
    //   total(Str)          -> total(Str, false)
    //   total(bool)         -> total(null, bool)
    //   reset()             -> resets timer
    
    // GCD, LCM:
    //   Single pairs, or lists of numbers are supported <>, []

    private ArrayList<String> readInput() {
	var list = new ArrayList<String> ();
	
	while(hasNextLine())
	    list.add(nextLine());
	return list;	
    }

        
    private class ActiveCube implements Comparable<ActiveCube> {
	int dimension;
	int[] vars;
	
	public ActiveCube(int[] vars) {
	    this.vars = vars;
	    this.dimension = vars.length;
	}

	public boolean equals(ActiveCube c) {
	    if(c.dimension != dimension)
		return false;
	    for(int i = 0; i < dimension; i++)
		if(vars[i] != c.vars[i])
		    return false;

	    return true;
	}

	public int compareTo(ActiveCube c) {
	    int diff = c.dimension - dimension;
	    if(diff != 0)
		return diff;

	    for(int i = 0; i < dimension; i++) {
		if((diff = c.vars[i] - vars[i]) != 0)
		    return diff;
	    }

	    return diff;
	}
    }
    
    //this is basically your main method
    public void solveProblem() throws Exception {
	Timer t = new Timer().start();

	var input = readInput();

	
	for(int dim = 3; dim <= 4; dim++) {
	    //	int dim = 4;
	    TreeSet<ActiveCube> activeCubes = new TreeSet<ActiveCube>();
	    ArrayList<int[]> diffs = genDiff(dim);
	    
	    for(int y = 0; y < input.size(); y++) {
		String line = input.get(y);	    
		for(int x = 0; x < line.length(); x++) {
		    if(line.charAt(x) == '#') { //active
			int[] v = new int[dim];
			v[0] = x;
			v[1] = y;
			activeCubes.add(new ActiveCube(v));
		    }
		}
	    }
	    
	    for(int cycle = 0; cycle < 6; cycle++) {
		TreeSet<ActiveCube> nextCycle = new TreeSet<ActiveCube>();
		//get a set of all cubes next to an active cube
		TreeSet<ActiveCube> inactiveNeighbors = new TreeSet<ActiveCube>();
		for(ActiveCube active : activeCubes) {
		    inactiveNeighbors.addAll(neighbors(active, diffs));		
		}
		
		for(ActiveCube active : activeCubes) {
		    var setActive = getActiveNeighbors(active, activeCubes, diffs);
		    if(2 <= setActive.size() && setActive.size() <= 3)
			nextCycle.add(active);
		}
		
		//exactly 3 neighbors of blank => active
		for(ActiveCube blank : inactiveNeighbors) {
		    var setActive = getActiveNeighbors(blank, activeCubes, diffs);
		    if(setActive.size() == 3)
			nextCycle.add(blank);
		}
		
		activeCubes = nextCycle;
	    }

	    if(dim == 3)
		DEBUGF("PART ONE: ");
	    if(dim == 4)
		DEBUGF("PART ONE: ");
	    println(activeCubes.size());//t.split("DIM=" + dim + " ANS = " + activeCubes.size());
	    //println(activeCubes.size());
	}
	
	t.total("Finished processing of file. ");	
    }

    public TreeSet<ActiveCube> getActiveNeighbors(ActiveCube cube, TreeSet<ActiveCube> active,
						  ArrayList<int[]> diffs) {
	var tmp = neighbors(cube, diffs);

	TreeSet<ActiveCube> res = new TreeSet<ActiveCube>();
	for(var v : tmp)
	    if(res.size() >= 4)
		break;
	    else if(active.contains(v))
		res.add(v);

	return res;
    }

    public ArrayList<int[]> genDiff(int dim) {
	ArrayList<int[]> diffs = new ArrayList<>();

	int[] diff_a = new int[dim];
	int[] diff_b = new int[dim];
	int[] diff_c = new int[dim];

	diff_b[0] = 1;
	diff_c[0] = -1;
	
	diffs.add(diff_a);
	diffs.add(diff_b);
	diffs.add(diff_c);

	diffs.addAll(genDiff(1, diff_a));
	diffs.addAll(genDiff(1, diff_b));
	diffs.addAll(genDiff(1, diff_c));

	//println("Diffs: " + diffs.size());
	
	//find the one with total value = 0
	for(int i = 0; i < diffs.size(); i++) {
	    long ct = 0;
	    for(int val : diffs.get(i))
		if(val != 0)
		    ct++;
	    if(ct == 0)
		diffs.remove(i);
	}

	//println("Diffs: " + diffs.size());
	
	return diffs;
    }

    public ArrayList<int[]> genDiff(int index, int[] template) {
	int[] a = template.clone();
	int[] b = template.clone();
	int[] c = template.clone();

	ArrayList<int[]> res = new ArrayList<>();
	if(index >= template.length)
	    return res;

	b[index] = 1;
	c[index] = -1;

	res.add(a);
	res.add(b);
	res.add(c);
	
	res.addAll(genDiff(index+1, a));
	res.addAll(genDiff(index+1, b));
	res.addAll(genDiff(index+1, c));

	return res;
    }
    
    public ArrayList<ActiveCube> neighbors(ActiveCube c, ArrayList<int[]> diffs) {	
	int dim = c.dimension;

	ArrayList<ActiveCube> res = new ArrayList<>();
	
	for(int[] diff : diffs) {
	    int[] adj = new int[dim];
	    for(int i = 0; i < dim; i++)
		adj[i] = diff[i] + c.vars[i];

	    res.add(new ActiveCube(adj));
	}
		/*	
	for(int dx = -1; dx < 2; dx++)
	    for(int dy = -1; dy < 2; dy++)
		for(int dz = -1; dz < 2; dz++)
		    for(int dw = -1; dw < 2; dw++)
		    if(dx != 0 || dy != 0 || dz != 0 || dw != 0)
		    res.add(new ActiveCube(x+dx, y+dy, z+dz, w + dw));*/

	return res;
    }

    
    //do any argument processing here
    public boolean processArgs(String[] argv) {
	for(int i = 0; i < argv.length; i++) {
	    switch(argv[i]) {
	    case "-se" : IGNORE_UNCLEAN = false; break;
	    case "-d"  : DEBUG = true; IGNORE_UNCLEAN = false; break;
	    case "-t"  : TIMER = true; break;
	    case "-dt" :
		Scanner tst = null;
		if(i + 1 < argv.length &&
		   (tst = new Scanner(argv[i+1])).hasNextInt()) {
		    DEBUG_TIME_MAGNITUDE = tst.nextInt();
		    i++;
		    break;
		}
		return fail();
		
		
	    default :
		return fail();
	    }
	}


	//set page enabled - if paging is enabled, the entire input stream will be read in advance
	//                   and it will be possible to arbitrarily skip between places in the
	//                   input stream
	PAGE_ENABLED = true;
	
	return true; //everything is fine
    }

    //customize your usage text here
    private boolean fail() {
	System.err.println("Usage: -se       = (show exceptions),\n" +
			   "       -d        = debug mode,\n" +
			   "       -t        = timer mode (debug lite),\n" +
			   "       -dt <int> = set timer digits");
	return false; //false - exit program
    }
    
    public Advent17() {
	super();
    }
    
    public static void main(String[] argv) {
	new Advent17().process(argv);
    }

    
}
