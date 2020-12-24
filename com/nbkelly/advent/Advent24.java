package com.nbkelly.advent;
import com.nbkelly.helper.DebugLogger;
import com.nbkelly.helper.ConceptHelper;
import java.util.Scanner;

import java.util.*;

public class Advent24 extends ConceptHelper {
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
	ArrayList<String> res = new ArrayList<String>();

	while(hasNextLine())
	    res.add(nextLine());

	return res;
    }

    final int[] x_token = new int[]   {2, 1, -1, -2, -1, 1};
    final int[] y_token = new int[]   {0, -1, -1,  0, 1, 1};
    
    //this is basically your main method
    public void solveProblem() throws Exception {
	Timer t = new Timer().start();
	
	var input = readInput();

	String[] tokens = new String[] {"e", "se", "sw", "w", "nw", "ne"};
	
	//default facing: white side up
	//determine how many have black side up
	TreeSet<Pair> active = new TreeSet<Pair>();
	for(String s : input) {
	    int x = 0;
	    int y = 0;	    

	    int index = 0;
	    while(index < s.length()) {
		for(int i = 0; i < tokens.length; i++) {
		    if(s.substring(index).startsWith(tokens[i])) {
			x += x_token[i];
			y += y_token[i];
			index += tokens[i].length();
			break;
		    }
		}
	    }

	    Pair p = new Pair(x, y);
	    if(active.contains(p))
		active.remove(p);
	    else
		active.add(p);
	    //println("Active.size(): " + active.size());
	}

	DEBUGF("Part One: ");
	println(active.size());        

	for(int i = 0; i < 100; i++) {
	    //first determine all of the white adjacents
	    var whites = adjacentWhites(active);

	    TreeSet<Pair> flips = new TreeSet<Pair>();
	    //any white tile with exactly two blacks adjacent becomes black
	    for(var white : whites)
		if(adjacentBlacks(active, white).size() == 2)
		    flips.add(white);

	    //any black with zero or >2 blacks becomes white
	    for(var black : active) {
		var adj = adjacentBlacks(active, black);

		if(adj.size() == 0 || adj.size() > 2)
		    flips.add(black);
	    }

	    for(Pair p : flips) {
		if(active.contains(p))
		active.remove(p);
	    else
		active.add(p);
	    }

	    //println("NA: " + active.size());
	}

	DEBUGF("Part Two: ");
	printf(active.size());
	t.total("Finished processing of file. ");
    }

    private TreeSet<Pair> adjacentBlacks(TreeSet<Pair> blacks, Pair p) {
	TreeSet<Pair> res = new TreeSet<Pair>();

	for(int i = 0; i < 6; i++) {
	    //for all 6 neighbors
	    Pair newPair = new Pair(p.X + x_token[i], p.Y + y_token[i]);
	    //if not black
	    if(blacks.contains(newPair)) {
		res.add(newPair);
	    }
	}

	return res;
    }
    
    private TreeSet<Pair> adjacentWhites(TreeSet<Pair> blacks) {
	TreeSet<Pair> res = new TreeSet<Pair>();
	for(Pair p : blacks) {
	    //for each pair
	    for(int i = 0; i < 6; i++) {
		//for all 6 neighbors
		Pair newPair = new Pair(p.X + x_token[i], p.Y + y_token[i]);
		//if not black
		if(!blacks.contains(newPair)) {
		    res.add(newPair);
		}
	    }
	}

	return res;
    }
    
    private class Pair implements Comparable<Pair> {
	int X;
	int Y;
	public Pair(int x, int y) {
	    this.X = x;
	    this.Y = y;
	}

	public int compareTo(Pair p) {
	    int res = X - p.X;
	    if(res != 0)
		return res;

	    return Y - p.Y;	    
	}

	public boolean equals(Pair p) {
	    return compareTo(p) == 0;
	}
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
    
    public Advent24() {
	super();
    }
    
    public static void main(String[] argv) {
	new Advent24().process(argv);
    }

    
}
