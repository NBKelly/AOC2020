package com.nbkelly.advent;
import com.nbkelly.helper.DebugLogger;
import com.nbkelly.helper.ConceptHelper;
import java.util.Scanner;
import java.util.ArrayList;
import java.math.BigInteger;

public class Advent03 extends ConceptHelper {
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

	ArrayList<String> lines = new ArrayList<String>();
        String first = nextLine();
	int width = first.length();
	
	while(hasNextLine())
	    lines.add(nextLine());

	int[][] slopes = new int[][] {
	    new int[] {1, 1},
	    new int[] {3, 1},
	    new int[] {5, 1},
	    new int[] {7, 1},
	    new int[] {1, 2}
	};

	//part one: the slope is always 3/1
	DEBUGF("PART ONE: ");
	println(checkslope(3, 1, lines, width));
	t.split();
	

	//the answer could be arbitrarily large: it's the multiplicative product of all of the intermediate answers
	BigInteger  p2_res = BigInteger.ONE;
	final int x = 0;
	final int y = 1;
	for(int[] slope : slopes)
	    p2_res = p2_res.multiply(BigInteger.valueOf(checkslope(slope[x], slope[y], lines, width)));

	DEBUGF("PART TWO: ");
	println(p2_res);

	t.total("Finished processing of file. ");
    }

    private int checkslope(int dx, int dy, ArrayList<String> lines, int width) {
	int loc = 0;
	int trees = 0;

	int y = 0;
	for(String line : lines) {
	    y++;
	    if(y%dy == 0) {
		loc = (loc + dx) % width;
		char token = line.charAt(loc);
		if(token == '#')
		    trees++;
	    }
	}

	return trees;//println(trees);
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
    
    public Advent03() {
	super();
    }
    
    public static void main(String[] argv) {
	new Advent03().process(argv);
    }

    
}
