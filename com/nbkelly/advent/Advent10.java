package com.nbkelly.advent;
import com.nbkelly.helper.DebugLogger;
import com.nbkelly.helper.ConceptHelper;
import java.util.Scanner;
import java.util.*;
import java.math.*;

public class Advent10 extends ConceptHelper {
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
    
    //this is basically your main method
    public void solveProblem() throws Exception {
	Timer t = new Timer().start();
	
        ArrayList<Integer> inputs = new ArrayList<Integer>();
	while(hasNextInt()){
	    inputs.add(nextInt());
	}
	Collections.sort(inputs);

	

	TreeMap<Integer, Integer> jsize = new TreeMap<Integer, Integer>();

	final int maxdiff = 3; //the maximum jump size allowed
	int c_joltage = 0;
	
	for(int i = 0; i < inputs.size(); i++) {
	    int new_jolt = inputs.get(i);
	    int diff = new_jolt - c_joltage;

	    if(jsize.containsKey(diff))
		jsize.put(diff, jsize.get(diff) + 1);
	    else {
		if(diff > maxdiff) {
		    DEBUGF("NO MATCH: %d %d%n", new_jolt, c_joltage);
		}
		jsize.put(diff, 1);		
	    }
	    c_joltage = new_jolt;
	}

	var p1 = BigInteger.valueOf(jsize.get(1)).multiply(BigInteger.valueOf(jsize.get(3) + 1));
	DEBUGF("PART ONE: ");
	println(p1);

	ArrayList<BigInteger> valid = new ArrayList<BigInteger>();
	for(int i = 0; i < inputs.size(); i++) {
	    //first, the empty adapter is valid
	    long input = inputs.get(i);
	    BigInteger tally = BigInteger.ZERO;
	    //if(i == 0)
	    //	valid.add(1);
	    for(int j = i-1; j >= 0; j--) {
		long pre = inputs.get(j);
		if(input - pre <= maxdiff) {
		    //this is valid
		    tally = tally.add(valid.get(j));
		}
		else
		    break;
	    }

	    if(input <= maxdiff)
		tally = tally.add(BigInteger.ONE);
	    
	    valid.add(tally);
	}
	    
	c_joltage += 3;

	DEBUGF("PART TWO: ");
	println(valid.get(valid.size()-1));
	
	
	t.total("Finished processing of file. ");
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
    
    public Advent10() {
	super();
    }
    
    public static void main(String[] argv) {
	new Advent10().process(argv);
    }

    
}
