package com.nbkelly.advent;
import com.nbkelly.helper.DebugLogger;
import com.nbkelly.helper.ConceptHelper;
import java.util.Scanner;
import java.util.*;
import java.math.BigInteger;
import java.util.Collections;
public class Advent09 extends ConceptHelper {
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
	
	ArrayList<BigInteger> sequence = new ArrayList<>();
	while(hasNextBigInteger()) {
	    sequence.add(nextBigInteger());
	}
	
	//if the size of the preamble needs to be changed, it's this
	int winsize = 25;
	
	t.split("File read");
	var part1 = findInvalid(sequence, winsize);
	DEBUGF("PART ONE: ");
	println(part1);
	

	t.split("Part 1 Done");
	
	var part2 = findSequence(sequence, part1);
	t.split("We're sorting for no reason");
	var weakness = min(part2).add(max(part2));//part2.getFirst().add(part2.getLast());
	DEBUGF("PART TWO: ");
	println(weakness);
	t.total("Finished processing of file. ");
    }

    private BigInteger min(LinkedList<BigInteger> li) {
	var min = li.get(0);
	for(var val : li)
	    min = min.min(val);

	return min;
    }

    private BigInteger max(LinkedList<BigInteger> li) {
	var max = li.get(0);
	for(var val : li)
	    max = max.max(val);

	return max;
    }

    private LinkedList<BigInteger> findSequence(ArrayList<BigInteger> sequence, BigInteger target) {
	LinkedList<BigInteger> res = new LinkedList<BigInteger>();

	BigInteger current = BigInteger.ZERO;

	int start = 0;
	int end = 0;

	while(true) {
	    if(current.compareTo(target) < 0) {
		//get the value at the current address
		var tmp = sequence.get(end);
		res.add(tmp);
		current = current.add(tmp);
		end++;
	    }
	    else if (current.compareTo(target) > 0) {
		var tmp = res.pop();
		current = current.subtract(tmp);
		start++;
	    }
	    else { //equals
		if(end - start == 1) {
		    var tmp = sequence.get(end);
		    res.add(tmp);
		    current = current.add(tmp);
		    end++;
		}
		return res;
	    }	    
	}
    }

    private BigInteger findInvalid(ArrayList<BigInteger> sequence, int win_size) {
	//sliding window setup
	LinkedList<BigInteger> window = new LinkedList<BigInteger>();
	LinkedList<LinkedList<BigInteger>> sums = new LinkedList<>();
	
	for(int i = 0; i < win_size; i++) {
	    window.add(sequence.get(i));
	}

	//get all the sums within the window
	for(int i = 0; i < window.size(); i++) {
	    LinkedList<BigInteger> il =  new LinkedList<BigInteger>();
	    for(int j = i+1; j < window.size(); j++) {
		il.add(window.get(i).add(window.get(j)));
	    }
	    sums.add(il);
	}

	for(int i = window.size(); i < sequence.size(); i++) {
	    //check it's valid
	    var val = sequence.get(i);

	    boolean valid = false;
	    //check if any of the sums contain it
	    for(LinkedList<BigInteger> li : sums) {
		if(li.contains(val)) {
		    valid = true;
		    break;
		}
	    }

	    if(valid) {
		//remove the leftmost sum
		sums.pop();
		window.pop();
		
		for(int j = 0; j < window.size(); j++) {
		    var res = window.get(j);
		    sums.get(j).add(val.add(res));
		}

		window.add(val);
		
		LinkedList<BigInteger> newList = new LinkedList<>();
		sums.add(newList);
	    }
	    else {
		return val;
	    }
	}

	//null indicates no valid result
	return null;

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
    
    public Advent09() {
	super();
    }
    
    public static void main(String[] argv) {
	new Advent09().process(argv);
    }

    
}
