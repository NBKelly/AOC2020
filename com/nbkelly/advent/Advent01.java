package com.nbkelly.advent;
import com.nbkelly.helper.DebugLogger;
import com.nbkelly.helper.ConceptHelper;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collections;
import java.math.BigInteger;
import java.util.stream.IntStream;

public class Advent01 extends ConceptHelper {
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
    private int target = 2020;
    private int num_ops = 0;
    private boolean parallel_done = false;
    //this is basically your main method
    public void solveProblem() throws Exception {
	Timer t = new Timer().start();

	ArrayList<Integer> list = new ArrayList<Integer>();

	while(hasNextInt()) {
	    list.add(nextInt());
	}
	t.split("File Processed");
	DEBUGF("LIST SIZE: %d ENTRIES%n", list.size());
	Collections.sort(list);
	t.split("List sorted");
	DEBUG();
	
	listmul(get_sum(0, target, list));
	DEBUG();
	t.split("Part one finished");
	//DEBUGF("NUM OPS: %d%n", num_ops);
	num_ops = 0;
	DEBUG();

	//create a list with n entries
	
	var stream = IntStream.range(0, list.size()-1);
	var res = stream.
	    filter(i -> process(i, target, list).size() > 0)
	    .findFirst();
	//listmul(res);
	/*stream.parallel().forEach(i -> {
		if(parallel_done)
		    return;
		
		int val = list.get(i);
		int new_target = target - val;
		var part_two = get_sum(i+1, new_target, list);

		num_ops++;
		if(part_two.size() > 0) {
		    parallel_done = true;
		    //t.split("BigInts are slow");
		    part_two.push((long)val);
		    listmul(part_two);
		    return;
		}
		});*/
	/*for(int i = 0; i < list.size(); i++) {
	    var part_two = process(i, target, list);
	    
	    num_ops++;
	    if(part_two.size() > 0) {
		//t.split("BigInts are slow");
		//part_two.push((long)val);
		listmul(part_two);
		break;
	    }
	    }*/

	DEBUG();
	t.split("Part two finished");
	//DEBUGF("NUM OPS: %d%n", num_ops);
	num_ops = 0;
	
	t.total("Finished processing of file. ");

	
    }

    public LinkedList<Long> process(int start, int target, ArrayList<Integer> values) {
	int val = values.get(start);
	int new_target = target - val;
	var res = get_sum(start+1, new_target, values);

	if(res.size() > 0) {
	    res.push((long)val);
	    listmul(res);
	}

	return res;
    }
    
    public LinkedList<Long> get_sum(int start, int target, ArrayList<Integer> values) {
	LinkedList<Long> res = new LinkedList<>();

	int end = values.size() - 1;
	
	long left = values.get(start);
	long right = values.get(end);
	while(true) {	    
	    //printf("LEFT: %d, RIGHT: %d, SUM: %d%n", left, right, left + right);
	    num_ops++;
	    if(end <= start) {
		start++;
		if(start >= values.size() - 1) //we reached the end without finding what we want
		    break;

		left = values.get(start);
		
		while(left + right < target && end < values.size() - 1) {
		    num_ops++;
		    end++;
		    right = values.get(end);
		}				       
	    }
	    else if(left + right == target) {
		res.add(left);
		res.add(right);
		return res;
	    }
	    else if (left + right > target) {
		//decrement end
		end--;
		right = values.get(end);
		//we need to make sure start and end do not collide
	    }
	    else {
		start++;
		
		if(start >= values.size() - 1) //we reached the end without finding what we want
		    break;
		
		left = values.get(start);
		
		while(left + right < target && end < values.size() - 1) {
		    num_ops++;
		    end++;
		    right = values.get(end);
		}		
	    }
	}

	return res;
    }

    public void listmul(LinkedList<Long> target) {
	BigInteger res = BigInteger.ONE;
	//long res = 1;
	for (long i : target) {
	    println(i);
	    res = res.multiply(BigInteger.valueOf(i));
	}

	println(res);
    }

    //do any argument processing here
    public boolean processArgs(String[] argv) {
	for(int i = 0; i < argv.length; i++) {
	    try {
		switch(argv[i]) {
		case "-se" : IGNORE_UNCLEAN = false; break;
		case "-d"  : DEBUG = true; IGNORE_UNCLEAN = false; TIMER = true; break;
		case "-t"  : TIMER = true; break;
		case "--target" :target = Integer.parseInt(argv[i+1]); i++; break;
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
	    } catch (Exception e) {
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
			   "       --target  = set target number (default 2020), \n" +
			   "       -dt <int> = set timer digits");
	
	return false; //false - exit program
    }
    
    public Advent01() {
	super();
    }
    
    public static void main(String[] argv) {
	new Advent01().process(argv);
    }

    
}
