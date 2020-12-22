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

	//determine the maximum value in the collection - this could be done inline faster during map insertion
	int max_value = max(inputs);

	HashMap<Integer, BigInteger> mappings = new HashMap<Integer, BigInteger>();

	for(int i : inputs)
	    mappings.put(i, BigInteger.ZERO);

	mappings.put(0, BigInteger.ONE);
	
	int _1_jolt_diffs = 0;
	int _3_jolt_diffs = 0;

	int last = 0;
	for(int i = 1; i <= max_value; i++) {
	    if(mappings.containsKey(i)) {
		if(i - last == 1)
		    _1_jolt_diffs++;
		else if (i - last == 3)
		    _3_jolt_diffs++;

		last = i;

		//THIS IS FOR PART TWO: DETERMINE SUM OF PREVIOUS MAPPINGS
		BigInteger ct_score = BigInteger.ZERO;
		for(int j = -3; j < 0; j++) {
		    if(mappings.containsKey(i+j))
			ct_score = ct_score.add(mappings.get(i+j));
		}

		mappings.put(i, ct_score);
	    }
	}

	DEBUGF("PART ONE: ");
	println(_1_jolt_diffs * (1 + _3_jolt_diffs));
	DEBUGF("PART TWO: ");
	println(mappings.get(max_value));
	
	t.total("Finished processing of file. ");
    }

    private int max(ArrayList<Integer> inputs) {
	int max = inputs.get(0);
	for(int i = 1; i < inputs.size(); i++)
	    max = Math.max(inputs.get(i), max);
	return max;
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
