package com.nbkelly.advent;
import com.nbkelly.helper.DebugLogger;
import com.nbkelly.helper.ConceptHelper;
import java.util.Scanner;

import java.util.*;
import java.math.BigInteger;

public class Advent18 extends ConceptHelper {
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
    
    //this is basically your main method
    public void solveProblem() throws Exception {
	Timer t = new Timer().start();

	var input = readInput();	

	DEBUGF("PART ONE: ");
	println(equalPrecedence(input));

	DEBUGF("PART TWO: ");
	println(additivePrecedence(input));
	
	t.total("Finished processing of file. ");
    }

    private ArrayDeque<String> preprocess(String input) {
	//first step: strip the input of spaces
	input = input.replace(" ", "");
	ArrayDeque<String> res = new ArrayDeque<String>();

	for(int i = 0; i < input.length(); i++) {
	    res.push(""+input.charAt(i));
	}

	return res;
    }    

    private BigInteger equalPrecedence(ArrayList<String> input) {
	BigInteger sum = BigInteger.ZERO;
	for(String s : input) {
	    //eval(preprocess(s).split(" +"), 0);
	    var tmp = preprocess(s);
	    long tsum = eval(tmp);
	    sum = sum.add(BigInteger.valueOf(tsum));// += tsum;
	}

	return sum;
    }

    private BigInteger additivePrecedence(ArrayList<String> input) {
	BigInteger sum = BigInteger.ZERO;
	for(String s : input) {
	    var tmp = preprocess(s);
	    long tsum = eval2(tmp, 0);
	    sum = sum.add(BigInteger.valueOf(tsum));// += tsum;
	}

	return sum;
    }
    
    public long eval(ArrayDeque<String> s) {
	String token = s.pop();
	if(token.equals(")")) {
	    long val = eval(s);
	    s.push(String.valueOf(val));
	    return eval(s);
	}
	else {
	    //if it's the final item, return the number itself
	    long val = Long.parseLong(token);
	    if(s.size() == 0)
		return val;
	    
	    token = s.pop();

	    switch (token) {
	    case "(":
		return val;
	    case "+":
		return val + eval(s);
	    case "*":
		return val * eval(s);
	    default:
		println("UNRECOGNIZED TOKEN '" + token + "' IN INPUT");
	    }
	}	

	return 0;
    }

    public long eval2(ArrayDeque<String> s, long component) {
	String token = s.pop();
	if(token.equals(")")) {	    
	    long val = eval2(s, 0);
	    s.push(String.valueOf(val));
	    return eval2(s, component);
	}
	else {// (!s.get(index).equals(")")) {
	    long val = Long.parseLong(token) + component;
	    if(s.size() == 0)
		return (val);

	    token = s.pop();

	    switch(token) {
	    case "(":
		return val;
	    case "+":
		return eval2(s, val);
	    case "*":
		return val * eval2(s, 0);
	    }
	}	

	return 0;
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
    
    public Advent18() {
	super();
    }
    
    public static void main(String[] argv) {
	new Advent18().process(argv);
    }
}
