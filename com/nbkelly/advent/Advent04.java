package com.nbkelly.advent;
import com.nbkelly.helper.DebugLogger;
import com.nbkelly.helper.ConceptHelper;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.*;

public class Advent04 extends ConceptHelper {
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

	//num_valid: part 2 ans
	int num_valid = 0;
	//min_valid: part 1 ans
	int min_valid = 0;
	while(hasNextLine()) {
	    //per passport
	    String line = "";
	    TreeMap<String,String> found_fields = new TreeMap<String, String>();

	    //these are mandatory
	    String[] req_fields = new String[]{"byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid"};
	    //these are acceptable
	    String[] opt_fields = new String[]{"cid"};
	    
	    String brick = "empty";
	    while(hasNextLine() && !(line = nextLine()).equals(brick)) {
		//printf("LINE SIZE: %d%n", line.length());
		//split line on spaces
		String[] fields = line.split(" ");
		//printf("Fields: %d%n", fields.length);
		for(String s : fields) {
		    String[] mp = s.split(":");
		    found_fields.put(mp[0], mp[1]);
		}
		//field names: byr, iyr, eyr, hgt,
		//             hcl, ecl, pid, cid
	    }
	    
	    if(found_fields.size() > 0) {
		boolean valid = true;
		for(String s : req_fields) {
		    if(found_fields.containsKey(s)) {
			continue;
		    }
		    valid = false;
		    break;
		}
		if(valid) {
		    //number of minimally valid fields
		    min_valid++;
		    
		    //check the birth year, issue year and expiry year
		    if(!valid_year("byr", found_fields, 1920, 2002))
			continue;
		    if(!valid_year("iyr", found_fields, 2010, 2020))
			continue;
		    if(!valid_year("eyr", found_fields, 2020, 2030))
			continue;
		    
		    if(!valid_regex("hgt", found_fields, "^1([5-8][0-9]|9[0-3])cm"))
			if(!valid_regex("hgt", found_fields, "^(59|6[0-9]|7[0-6])in$"))
			    continue;
		    
		    if(!valid_regex("hcl", found_fields, "^#[0-9a-f]{6}$"))
			continue;
		    if(!valid_regex("ecl", found_fields, "^amb|blu|brn|gry|grn|hzl|oth$"))
			continue;
		    if(!valid_regex("pid", found_fields, "^[0-9]{9}$"))
			continue;
		    
		    num_valid++;
		}
	    }	    	    	    
	}

	DEBUGF("PART ONE: ");
	println(min_valid);
	DEBUGF("PART TWO: ");
	println(num_valid);

	t.total("Finished processing of file. ");
    }

    private boolean valid_year(String token, TreeMap<String, String> map, int min, int max) {
	try {
	    int year = Integer.parseInt(map.get(token));
	    if(year < min || year > max)
		return false;
	} catch(Exception e) {
	    return false;
	}
	
	return true;		    
    }

    private boolean valid_regex(String token, TreeMap<String, String> map, String regex) {
	return map.get(token).matches(regex);
    }
        
    //do any argument processing here
    public boolean processArgs(String[] argv) {
	for(int i = 0; i < argv.length; i++) {
	    switch(argv[i]) {
	    case "-se" : IGNORE_UNCLEAN = false; break;
	    case "-d"  : DEBUG = true; IGNORE_UNCLEAN = false; TIMER = true; break;
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
    
    public Advent04() {
	super();
    }
    
    public static void main(String[] argv) {
	var v = new Advent04();
	v.ALLOW_EMPTY = true;
	v.process(argv);
    }

    
}
