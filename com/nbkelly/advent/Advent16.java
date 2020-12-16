package com.nbkelly.advent;
import com.nbkelly.helper.DebugLogger;
import com.nbkelly.helper.ConceptHelper;
import java.util.Scanner;

import java.util.*;
import com.nbkelly.helper.Util;

public class Advent16 extends ConceptHelper {
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

    public ArrayList<String> readInput() {
	var input = new ArrayList<String>();
	
	while(hasNextLine())
	    input.add(nextLine());

	return input;
    }

    private class Params {
	public Params() {
	    ArrayList<String> input = readInput();

	    int i = 0;
	    for(; i < input.size() && !input.get(i).equals("empty"); i++) {
		//println(input.get(i));
		categories.add(new Category(input.get(i)));
	    }
	    
	    i += 2;

	    myTicket = new Ticket(input.get(i));

	    i += 3;

	
	    for(; i < input.size(); i++)
		nearbyTickets.add(new Ticket(input.get(i)));	    
	}

	
	ArrayList<Category> categories = new ArrayList<>();
	Ticket myTicket;
	ArrayList<Ticket> nearbyTickets = new ArrayList<>();;

	public int ticket_length() {
	    return myTicket.numFields();
	}
    }
    
    private class Category {

	public String toString() {
	    return name;
	}
	
	String name;
	int[] i1;
	int[] i2;

	public Category(String line) {
	    String[] s = line.split(":");
	    String[] s1 = s[1].substring(1).split(" ")[0].split("-");;
	    String[] s2 = s[1].substring(1).split(" ")[2].split("-");;
	    
	    i1 = new int[] {Integer.parseInt(s1[0]), Integer.parseInt(s1[1])};
	    i2 = new int[] {Integer.parseInt(s2[0]), Integer.parseInt(s2[1])};
	    name = s[0];
	}

	public boolean valid(int val) {
	    return (i1[0] <= val && val <= i1[1])
		|| (i2[0] <= val && val <= i2[1]);
	}
    }


    public class Ticket {
	int[] vals;
	public Ticket(String line) {
	    String[] s = line.split(",");
	    vals = new int[s.length];
	    for(int i = 0; i < s.length; i++) {
		vals[i] = Integer.parseInt(s[i]);
	    }
	}

	public int numFields() {
	    return vals.length;
	}
	
	public int get(int field) {
	    return vals[field];
	}	   
    }
    
    //this is basically your main method
    public void solveProblem() throws Exception {
	Timer t = new Timer().start();

	Params params = new Params();

	ArrayList<Ticket> validTickets = new ArrayList<Ticket>();
	int invalid_ct = 0;
	
	for(Ticket ticket : params.nearbyTickets) {
	    boolean valid = true;
	    for(int val : ticket.vals) {
		if(!validForAnyField(val, params.categories)) {
		    invalid_ct += val;
		    valid = false;
		}
	    }

	    if(valid)
		validTickets.add(ticket);
	}

	DEBUGF("PART ONE: ");
	println(invalid_ct);

	ArrayList<ArrayList<Category>> valid_cats = new ArrayList<>();
	for(int i = 0; i < params.ticket_length(); i++)
	    valid_cats.add(new ArrayList<>());
	
	for(int i = 0; i < params.categories.size(); i++) {
	    Category cat = params.categories.get(i);
	    //for every category

	    params:
	    for(int j = 0; j < params.ticket_length(); j++) {
		//check that every ticket is valid for this param
		boolean param_valid = true;
		for(Ticket ticket : validTickets)
		    if(!cat.valid(ticket.get(j)))
			continue params;
		
		valid_cats.get(j).add(cat);		    
	    }
	}

	var ordered = Util.singleElim(valid_cats);

	long mult = 1;
	for(int i = 0; i < ordered.size(); i++) {
	    var ct = ordered.get(i);
	    if(ct.name.contains("departure"))
		mult *= params.myTicket.get(i);
	}

	DEBUGF("PART TWO: ");
	println(mult);
		
	t.total("Finished processing of file. ");
    }    
    
    boolean validForAnyField(int val, ArrayList<Category> categories) {
	for(var c : categories) {
	    if(c.i1[0] <= val && val <= c.i1[1])
		return true;
	    if(c.i2[0] <= val && val <= c.i2[1])
		return true;	    
	}

	return false;
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
    
    public Advent16() {
	super();
    }
    
    public static void main(String[] argv) {
	new Advent16().process(argv);
    }

    
}
