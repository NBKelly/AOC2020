package com.nbkelly.advent;
import com.nbkelly.helper.DebugLogger;
import com.nbkelly.helper.ConceptHelper;
import java.util.Scanner;

import java.util.*;
    
public class Advent12 extends ConceptHelper {
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

    final int WAYPOINT_X_START = 10;
    final int WAYPOINT_Y_START = 1;
    final int FACING_START = 90;
    final int X_START = 0;
    final int Y_START = 0;
    
    //this is basically your main method
    public void solveProblem() throws Exception {
	Timer t = new Timer().start();

	//read our input
	var input = readInput();

	DEBUGF("PART ONE: ");
	println(follow_instructions(input));
	
	DEBUGF("PART TWO: ");
	println(follow_waypoint(input));
	
	t.total("Finished processing of file. ");
    }

    public long follow_instructions(ArrayList<String> input) {
	int y = Y_START;
	int x = X_START;

	int facing = FACING_START;

	for(String s : input) {
	    char instr = s.charAt(0);
	    int component = Integer.parseInt(s.substring(1));

	    switch(instr) {
	    case 'N':
		y += component;
		break;
	    case 'S':
		y -= component;
		break;
	    case 'E':
		x += component;
		break;
	    case 'W':
		x -= component;
		break;
	    case 'L':
		facing -= component;
		break;
	    case 'R':
		facing += component;
		break;
	    case 'F':
		var tmp = rotate(component, 0, facing/90);
		y += tmp[0];
		x += tmp[1];
		break;
	    }
	}

	return abs(x) + abs(y);
    }
    
    public long follow_waypoint(ArrayList<String> input) {
	int waypoint_y = WAYPOINT_Y_START;
	int waypoint_x = WAYPOINT_X_START;

	int y = Y_START;
	int x = X_START;	

	for(String s : input) {
	    char instr = s.charAt(0);
	    int component = Integer.parseInt(s.substring(1));

	    switch(instr) {
	    case 'N':
		waypoint_y += component;
		break;
	    case 'S':
		waypoint_y -= component;
		break;
	    case 'E':
		waypoint_x += component;
		break;
	    case 'W':
		waypoint_x -= component;
		break;
	    case 'L':
		var tmp1 = rotate(waypoint_y, waypoint_x, -component / 90);
		waypoint_y = tmp1[0];
		waypoint_x = tmp1[1];
		break;
		
	    case 'R':
		var tmp2 = rotate(waypoint_y, waypoint_x, (component) / 90);
		waypoint_y = tmp2[0];
		waypoint_x = tmp2[1];
		break;
	    case 'F':
		x += waypoint_x * component;
		y += waypoint_y * component;
		
	    }
	}

	return abs(x) + abs(y);
    }

    int[] rotate(int y, int x, int count) {
	//to rotate x and y, set x = y and y = -x
	count = count%4;

	if(count == 0)
	    return new int[]{y, x};
	if(count == 1)
	    return new int[]{-1*x, y};

	//perform one rotation, then apply to the rest
	return rotate(-1*x, y, count-1);
    }

    int abs(int a) {
	if(a < 1)
	    return -a;
	return a;
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
    
    public Advent12() {
	super();
    }
    
    public static void main(String[] argv) {
	new Advent12().process(argv);
    }

    
}
