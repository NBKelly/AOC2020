package com.nbkelly.advent;
import com.nbkelly.helper.DebugLogger;
import com.nbkelly.helper.ConceptHelper;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.LinkedList;

public class Advent07 extends ConceptHelper {
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

	var trim_str = new String[]{" bags", " bag", " bags.", " bag."};
	ArrayList<String> lines = new ArrayList<String>();
	while(hasNextLine()) {
	    lines.add(nextLine());
	}

	TreeMap<String, TreeMap<String, Integer>> bagmap = new TreeMap<>();
	TreeMap<String, Integer> index = new TreeMap<>();
	ArrayList<String> colors = new ArrayList<String>();
	TreeMap<Integer, TreeSet<String>> ancestors = new TreeMap<>();
	int[] canContainGold = new int[index.size()];
	
	for(int i = 0; i < lines.size(); i++) {
	    String[] sides = lines.get(i).split(" bags contain ");
	    
	    String key = sides[0];
	    index.put(key, i);
	    colors.add(key);
	    
	    TreeMap<String, Integer> ct = new TreeMap<>();
	    bagmap.put(key, ct);
	    if(sides[1].equals("no other bags."))
		continue;
	    
	    String[] contents = sides[1].split(", ");
	    	    	    
	    for(String s : contents) {
		//extract the number
		var numero = extractNumeral(s);
		var targetcolor = trim(numero[1], trim_str);
		ct.put(targetcolor, Integer.parseInt(numero[0]));
	    }
	}

	TreeSet<String> containsGold = new TreeSet<String>();
	TreeSet<String> remainder = new TreeSet<String>();
	remainder.addAll(colors);
	//once we're here, we know of every bag and what bags it can (directly) contain
	//how do we work this: let's try a crawl
	boolean foundResult = true;
	String target = "shiny gold";
	while(foundResult) {
	    foundResult = false;
	    TreeSet<String> newRemainder = new TreeSet<String>();
	    for(String s : remainder) {
		//see if it directly contains gold
		TreeMap<String, Integer> contents = bagmap.get(s);
		if(contents.size() > 0) {
		    boolean foundOne = false;
		    //for each of the things in it, see if it's either shiny gold or contains shiny gold
		    for(String col : contents.keySet()) {
			if(col.equals(target) || containsGold.contains(col)) {
			    containsGold.add(s);
			    foundResult = foundOne = true;
			    break;
			}
		    }
		    //if we found nothing here, then just add it to our remainder list
		    if(!foundOne)
			newRemainder.add(s);
		}
		else {
		    //contains nothing -  we do not add it to the remainder
		}
	    }
	    remainder = newRemainder;
	}

	DEBUGF("PART ONE: ");
	println(containsGold.size());

	DEBUGF("PART TWO: ");
	println(count_total("shiny gold", bagmap));

	t.total("Finished processing of file. ");
    }

    private int count_total(String _target, TreeMap<String, TreeMap<String, Integer>> state) {
	LinkedList<String> stack_target = new LinkedList<String>();
	LinkedList<Integer> stack_count = new LinkedList<Integer>();

	stack_target.add(_target);
	stack_count.add(1);
	int total = 0;

	while(stack_target.size() > 0) {
	    String target = stack_target.pop();
	    int mult = stack_count.pop();

	    TreeMap<String, Integer> microstate = state.get(target);
	    for(String s : microstate.keySet()) {
		//get the thing and the number
		//put them in our stack
		//add to our total
		int count = microstate.get(s);
		total += count*mult;
		stack_target.add(s);
		stack_count.add(count*mult);
	    }
	}

	return total;
    }
    
    private String[] extractNumeral(String s) {
	int len = 0;
	while(s.charAt(len) >= '0' && s.charAt(len) <= '9')
	    len++;

	return new String[]{s.substring(0, len), s.substring(len+1)};
    }
    
    private String trim(String input, String[] targets) {
	for(String s : targets) {
	    if(input.endsWith(s)) {
		return input.substring(0, input.length() - s.length());
	    }
	}

	return input;
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
    
    public Advent07() {
	super();
    }
    
    public static void main(String[] argv) {
	new Advent07().process(argv);
    }

    
}
