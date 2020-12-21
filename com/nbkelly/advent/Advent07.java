package com.nbkelly.advent;
import com.nbkelly.helper.DebugLogger;
import com.nbkelly.helper.ConceptHelper;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayDeque;

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

	HashMap<String, ArrayList<Capacity>> bags = new HashMap<>();
	HashMap<String, ArrayList<String>> ancestors = new HashMap<>();

	for(String s : lines) {
	    String[] segments = s.split(" bags contain ");
	    String key = segments[0];
	    //println(key);

	    if(segments[1].equals("no other bags."))
		bags.put(key, new ArrayList<Capacity>());
	    else {
		String[] components = segments[1].split(", ");
		ArrayList<Capacity> caps = new ArrayList<>();
		for(int i = 0; i < components.length; i++) {
		    var tmp = extractNumeral(components[i]);
		    int count = Integer.parseInt(tmp[0]);
		    String col = trim(tmp[1], trim_str);
		    Capacity c = new Capacity(col, count);
		    caps.add(c);
		    //println(c.bagColor);

		    //add the key to our set of ancestors
		    if(ancestors.containsKey(col))
			ancestors.get(col).add(key);
		    else {
			ArrayList<String> a = new ArrayList<String>();
			a.add(key);
			ancestors.put(col, a);
		    }
		}
		bags.put(key, caps);
	    }
	}

	DEBUGF("PART ONE: ");
	println(number_of_distinct_ancestors("shiny gold", ancestors));

	DEBUGF("PART TWO: ");
	println(number_of_children("shiny gold", bags, new HashMap<String, Long>()));
    }

    //memo-ize the results to stop looking at things too many times    
    private long number_of_children(String target, HashMap<String, ArrayList<Capacity>> children,
				    HashMap<String, Long> memo) {
	//we need to add the children of this bag, and their children

	long sum = 0;
	var local_children = children.get(target);

	for(Capacity c : local_children) {
	    //get the sum
	    String key = c.bagColor;
	    long quantity = c.quantity;
	    var memo_result = memo.get(key);
	    if(memo_result != null) {
		sum += quantity * (memo_result + 1);
	    }
	    else {
		long single = number_of_children(key, children, memo);
		memo.put(key, single);

		sum += quantity * (single+1);
	    }
	}

	return sum;
    }
    
    private int number_of_distinct_ancestors(String target, HashMap<String, ArrayList<String>> ancestors) {
	HashSet<String> visited = new HashSet<String>();

	ArrayDeque<String> stack = new ArrayDeque<String>();

	stack.add(target);

	int num_passes = 0;
	while(stack.size() > 0) {
	    num_passes++;
	    String current = stack.pop();
	    if(!visited.contains(current)) {
		visited.add(current);
		var direct_ancestors = ancestors.get(current);
		if(direct_ancestors != null) {
		    for(String ancestor : direct_ancestors) {
			stack.push(ancestor);
		    }
		}
	    }
	}

	//roughly height * average_ancestors
	//println(num_passes);

	//subtract 1 because we count the target itself, and the target is not it's own ancestor
	return visited.size() - 1;
    }

    private class Capacity {
	public String bagColor;
	public int quantity;

	public Capacity(String key, int quantity) {
	    this.bagColor = key;
	    this.quantity = quantity;
	}
    }

    //extract a number from a string, and then return the rest of the string
    private String[] extractNumeral(String s) {
	int len = 0;
	while(s.charAt(len) >= '0' && s.charAt(len) <= '9')
	    len++;

	return new String[]{s.substring(0, len), s.substring(len+1)};
    }

    //if a string ends with any of the values in targets, remove that value
    //otherwise, return that string
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
