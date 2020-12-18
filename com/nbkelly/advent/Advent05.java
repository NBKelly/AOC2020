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

    private ArrayList<String> getInput() {
	ArrayList<String> res = new ArrayList<String>();
	while(hasNextLine())
	    res.add(nextLine());	
	return res;
    }
    
    //this is basically your main method
    public void solveProblem() throws Exception {
	Timer t = new Timer().start();

	var input = getInput();
	var ids	= getIDs(input);
	int highest = Collections.max(ids); //O(n)

	DEBUGF("PART ONE: ");
	println(highest);
	
	DEBUGF("PART TWO: ");               //O(n)
	println(missingEntry(ids));         //O(n)
	t.total("Finished processing of file. ");
    }

    private ArrayList<Integer> getIDs(ArrayList<String> input) {
	ArrayList<Integer> ids = new ArrayList<Integer>();

	//this entire thing is O(n)
	for(String line : input) {
	    int[] y = {0, 127};
	    int[] x = {0, 7};
	    
	    for(int i = 0; i < 7; i++)
		y = binary(y[0], y[1], line.charAt(i) == 'B');
	    	    
	    for(int i = 7; i < 10; i++)
		x = binary(x[0], x[1], line.charAt(i) == 'R');	    
	    
	    int row = y[0];
	    int col = x[0];
	    int id = row*8 + col;
	    ids.add(id);
	}

	return ids;
    }
    
    private int missingEntry(ArrayList<Integer> ids) {
	int highest = Collections.max(ids); //O(n)
	int lowest = Collections.min(ids);  //O(n)	

	//this is all done in O(n)
	int[] found = new int[highest-lowest + 1];
	
	int validId = -1;
	for(int id : ids)
	    found[id - lowest] = 1;
	int myID = 0;
	while(found[myID] == 1)
	    myID++;	

	return  (myID + lowest);
    }
    

    public int[] binary(int lower, int upper, boolean matched) {
	if(matched)
	    return new int[] {lowHalf(lower, upper)+1, upper};
	else
	    return new int[]{lower, lowHalf(lower, upper)};
    }
    
    private int lowHalf(int low, int high) {
	return (high + low)/2;
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
