package com.nbkelly.advent;
import com.nbkelly.helper.DebugLogger;
import com.nbkelly.helper.ConceptHelper;
import java.util.Scanner;

import java.util.*;
//import com.nbkelly.helper.Util;

public class Advent15 extends ConceptHelper {
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

    /*private ArrayList<Integer> readInput() {
	var list = new ArrayList<Integer> ();
	while(hasNextLine())
	    list.add(nextLine());
	return list;	
	}*/


    private int[] readInput() {
	String[] l = nextLine().split(",");
	int[] res = new int[l.length];

	for(int i = 0; i < l.length; i++)
	    res[i] = Integer.parseInt(l[i]);

	return res;
    }
    //this is basically your main method
    public void solveProblem() throws Exception {
	Timer t = new Timer().start();

	var input = readInput();
	//input = new int[] {0, 3, 6};
		
	var p1 = solve(input, 2020);
	DEBUGF("PART ONE: "); println(p1);
	var p2 = solve(input, 30000000);
	DEBUGF("PART TWO: "); println(p2);
	
	t.total("Finished processing of file. ");
    }

    private int solve(int[] input, int lim ) {
	HashMap<Integer, Integer> memory = new HashMap<Integer, Integer>();
	//{0, 3, 6} -> {0, 3, 6, 0, 3, 3, 1, 0, 4, 0}
	//insert the input into memory
	for(int i = 1; i <= input.length; i++)
	    memory.put(input[i-1], i);
	
	//after the sequence, the next number is always 0
	int next = 0;//int last = input[input.length-1];
	
	for(int i = input.length+1; i < lim; i++) {	    
	    //if it's the first time next appears, then next_next is zero
	    int next_next = 0;
	    if(memory.containsKey(next))
		//next_next is the difference between i and the last time the number was spoken
		next_next = i - memory.get(next);	
	    
	    memory.put(next, i);
	    next = next_next;
	}

	return next;
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
    
    public Advent15() {
	super();
    }
    
    public static void main(String[] argv) {
	new Advent15().process(argv);
    }

    
}
