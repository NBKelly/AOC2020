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
	int x;
	int y;
	int z;

	public ActiveCube(int x, int y, int z) {
	    this.x = x;
	    this.y = y;
	    this.z = z;
	}

	public boolean equals(ActiveCube c) {
	    return c.x == x &&
		c.y == y &&
		c.z == z;
	}

	public int compareTo(ActiveCube c) {
	    if(c.x - x == 0)
		if(c.y - y == 0)
		    if (c.z - z == 0)
			return 0;
		    else return c.z - z;
		else return c.y - y;
	    else return c.x - x;
	}
    }
    
    //this is basically your main method
    public void solveProblem() throws Exception {
	Timer t = new Timer().start();

	var input = readInput();

	TreeSet<ActiveCube> active = new TreeSet<ActiveCube>();

	for(int y = 0; y < input.size(); y++) {
	    String line = input.get(y);
	    for(int x = 0; x < line.length(); x++) {
		if(line.charAt(x) == '#') //active
		    active.add(new ActiveCube(x, y, 0));
	    }
	}

	println(active.size());


	for(int cycle = 0; cycle < 6; cycle++) {
	    TreeSet<ActiveCube> nextCycle = new TreeSet<ActiveCube>();
	    //get a set of all cubes next to an active cube
	    TreeSet<ActiveCube> inactiveNeighbors = new TreeSet<ActiveCube>();
	    for(ActiveCube a : active) {
		inactiveNeighbors.addAll(neighbors(a.x, a.y, a.z));		
	    }

	    println("Inactive: " + inactiveNeighbors.size());
	    for(ActiveCube a : active) {
		var setActive = getActiveNeighbors(a, active);
		if(2 <= setActive.size() && setActive.size() <= 3)
		    nextCycle.add(a);
	    }

	    //exactly 3 neighbors of blank => active
	    for(ActiveCube a : inactiveNeighbors) {
		var setActive = getActiveNeighbors(a, active);
		if(setActive.size() == 3)
		    nextCycle.add(a);
	    }

	    active = nextCycle;

	    println("Active: " + active.size());
	}

	
	
	t.total("Finished processing of file. ");
    }

    public TreeSet<ActiveCube> getActiveNeighbors(ActiveCube cube, TreeSet<ActiveCube> active) {
	var tmp = neighbors(cube.x, cube.y, cube.z);

	TreeSet<ActiveCube> res = new TreeSet<ActiveCube>();
	for(var v : tmp)
	    if(active.contains(v))
		res.add(v);

	return res;
    }
    
    public ArrayList<ActiveCube> neighbors(int x, int y, int z) {
	ArrayList<ActiveCube> res = new ArrayList<>();
	for(int dx = -1; dx < 2; dx++)
	    for(int dy = -1; dy < 2; dy++)
		for(int dz = -1; dz < 2; dz++)
		    if(dx != 0 || dy != 0 || dz != 0)
			res.add(new ActiveCube(x+dx, y+dy, z+dz));

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
