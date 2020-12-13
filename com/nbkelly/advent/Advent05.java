package com.nbkelly.advent;
import com.nbkelly.helper.DebugLogger;
import com.nbkelly.helper.ConceptHelper;
import java.util.Scanner;
import java.util.*;

public class Advent05 extends ConceptHelper {
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

	ArrayList<Integer> ids = new ArrayList<Integer>();
	
	while(hasNextLine()) {
	    String line = nextLine();
	    
	    int[] y = {0, 127};
	    int[] x = {0, 7};
	    
	    
	    for(int i = 0; i < 7; i++) {
		y = binary(y[0], y[1], 'F', 'B', line.charAt(i));
	    }
	    
	    for(int i = 7; i < 10; i++) {
		//printarr(x);
		x = binary(x[0], x[1], 'L', 'R', line.charAt(i));
	    }
	    
	    int row = y[0];
	    int col = x[0];
	    int id = row*8 + col;
	    ids.add(id);
	}
	//printarr(x);
	Collections.sort(ids);
	//what's the highest ID?
	DEBUGF("PART ONE: ");
	println(ids.get(ids.size()-1));

	

	int validId = -1;
	//print out missing ids
	int lastId = ids.get(0);
	for(int i = 1; i < ids.size(); i++) {
	    int id = ids.get(i);
	    if(id - lastId > 1)
		validId = id-1;
	    lastId = id;
	}
	DEBUGF("PART TWO: ");
	println(validId);
	
	t.total("Finished processing of file. ");
    }


    //performs a binary chop on two integers, based on a given equality token
    public int[] binary(int lower, int upper, char l_half, char u_half, char token) {
	if(upper - lower == 1) {
	    return token == u_half ? new int[] {upper, upper} : new int[]{lower, lower};
	}
	else {
	    if(token == u_half)
		return new int[]{lowHalf(lower, upper)+1, upper};
	    else
		return new int[]{lower, lowHalf(lower, upper)};
	}
    }
    
    private int lowHalf(int low, int high) {
	int diff = high - low;
	diff = diff/2;
	return low + diff;
    }
    
    private void printarr(int[] arr) {
	for(int i : arr) {
	    print(i);	    
	}
	println();
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
    
    public Advent05() {
	super();
    }
    
    public static void main(String[] argv) {
	new Advent05().process(argv);
    }

    
}
