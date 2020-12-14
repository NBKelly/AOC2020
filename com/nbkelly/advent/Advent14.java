package com.nbkelly.advent;
import com.nbkelly.helper.DebugLogger;
import com.nbkelly.helper.ConceptHelper;
import java.util.Scanner;

import java.util.*;
import java.math.BigInteger;
import com.nbkelly.helper.Util;

public class Advent14 extends ConceptHelper {
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
	println(partOne(input));
	DEBUGF("PART TWO: ");
	println(partTwo(input));
	
	t.total("Finished processing of file. ");
    }
    
    public long partTwo(ArrayList<String> input) {
	TreeMap<Long, Long> memory = new TreeMap<>();
	
	String mask = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
	for(String s : input) {
	    assert mask.length() == 36 : "MASK SIZE WRONG";
	    if(s.startsWith("mem[")) {
		long addr = Integer.parseInt(s.split("\\[")[1].split("\\]")[0]);
		var addrs = mask_addr(addr, mask);
		//long[] addr2 = mask2(add, mask);
		long value = Long.parseLong(s.split("= ")[1]);
		for(long a : addrs)
		    memory.put(a, value);//value = mask(value, mask);
	    }
	    else if( s.startsWith("mask =")) {
		mask = s.split("= ")[1];
	    }
	    else
		DEBUG("UNRECOGNIZED LINE: '" + s + "'");
	}

	long sum = 0;
	for(long key : memory.keySet()) {
	    sum += memory.get(key);
	}

	return sum;
    }
        
    public long partOne(ArrayList<String> input) {
	TreeMap<Integer, Long> memory = new TreeMap<>();
	
	String mask = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
	for(String s : input) {
	    assert mask.length() == 36 : "MASK SIZE WRONG";
	    if(s.startsWith("mem[")) {
		int addr = Integer.parseInt(s.split("\\[")[1].split("\\]")[0]);
		long value = Long.parseLong(s.split("= ")[1]);
		value = mask_val(value, mask);
		memory.put(addr, value);
	    }
	    else if( s.startsWith("mask =")) {
		mask = s.split("= ")[1];
	    }
	    else
		DEBUG("UNRECOGNIZED LINE: '" + s + "'");
	}

	long sum = 0;
	for(int key : memory.keySet()) {
	    sum += memory.get(key);
	}

	return(sum);
    }

    public long mask_val(long val, String mask) {
	for(int i = 0; i < mask.length(); i++) {
	    int index = mask.length() - 1 - i;
	    if(mask.charAt(i) == 'X')
		continue;
	    else {
		int ch = 0;
		if(mask.charAt(i) == '1')
		    ch = 1;

		long position = (long)Math.pow(2, index);
		if(ch == 1)
		    val |= position;
		else if(ch == 0)
		    val &= ~position;
	    }
	}

	return val;
    }

    public TreeSet<Long> mask_addr(long val, String mask) {
	ArrayList<Long> floating = new ArrayList<Long>();
	
	for(int i = 0; i < mask.length(); i++) {
	    int index = mask.length() - 1 - i;
	    long position = (long)Math.pow(2, index);
	    
	    if(mask.charAt(i) == 'X') {
		floating.add(position);
		//set the position to zero too
		val &= ~position;
	    }
	    else {
		if(mask.charAt(i) == '1')
		    val |= position;		
	    }
	}

	//components, combinator, seed
	TreeSet<Long> res = Util.combinations(val, floating, combinator);
					     
	return res;
    }

    //this is the combinations class implemented as a combinator
    private final Util.Combinator<Long> combinator = new Util.Combinator<Long>() {
	    public TreeSet<Long> combinations(Long val, Long component) {
		TreeSet<Long> res = new TreeSet<Long>();
		res.add(val &= ~component);
		res.add(val |= component);
		return res;
	    }
	};

    public TreeSet<Long> combinations(ArrayList<Long> floats, long val) {
	TreeSet<Long> vals = new TreeSet<Long>();
	vals.add(val);

	for(long pos : floats) {
	    TreeSet<Long> res = new TreeSet<Long>();
	    for(long v : vals) {
		res.add(v &= ~pos);
		res.add(v |= pos);
	    }
	    vals.addAll(res);
	}

	return vals;
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
    
    public Advent14() {
	super();
    }
    
    public static void main(String[] argv) {
	new Advent14().process(argv);
    }

    
}
