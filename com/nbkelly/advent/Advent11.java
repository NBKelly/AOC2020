package com.nbkelly.advent;
import com.nbkelly.helper.DebugLogger;
import com.nbkelly.helper.ConceptHelper;
import java.util.Scanner;
import java.util.*;
import java.util.stream.IntStream;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Advent11 extends ConceptHelper {
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

    private final char EMPTY = 'L';
    private final char FLOOR = '.';
    private final char TAKEN = '#';
    private final char CONVERT = EMPTY ^ TAKEN;
    
    private char[][] translate(ArrayList<String> input) {
	char[][] res = new char[input.size()][];
	int lc = 0;
	for(String s : input) {
	    res[lc++] = s.toCharArray();
	}

	return res;
    }
    
    public void solveProblem() throws Exception {
	Timer t = new Timer().start();

	ArrayList<String> input = new ArrayList<String>();
        while(hasNextLine())
	    input.add(nextLine());
	
	char[][] state = translate(input);
	int height = state.length;
	int width = state[0].length;
	var p1_state = dup(state, height, width);

	var precomp_change = computeDynamic(state, height, width);
	
	t.split("Starting part 1");
	println(solve_simple_fast(p1_state, height, width, precomp_change));

	t.split("Starting part 2");
	println(solve_complex_fast(state, height, width, precomp_change));
	
	t.total("Finished processing of file. ");
    }

    private ConcurrentLinkedQueue<Pair> computeDynamic(char[][] state, int height, int width) {
	final ConcurrentLinkedQueue<Pair> _changed = new ConcurrentLinkedQueue<Pair>();

	IntStream.range(0, state.length).parallel().forEach(y -> {
		char[] line = state[y];
		for(int x = 0; x < line.length; x++) {
		    if(is_seat(line[x]))
			_changed.add(new Pair(x, y));
		}
	    });

	return _changed;
    }
    
    public int solve_simple_fast(char[][] state, int height, int width, ConcurrentLinkedQueue<Pair> changed) {
	
	while(changed.size() > 0) {	    
	    ConcurrentLinkedQueue<Pair> changed2 = new ConcurrentLinkedQueue<>();

	    changed.parallelStream().forEach(pair -> {
		    switch(state[pair.Y][pair.X]) {
			//if a seat is empty (L) and there are no occupied seats adjacent to it,
			//the seat becomes occupied
		    case EMPTY:
			if(occupied_adjacent(state, height, width, pair.X, pair.Y, 1) == 0)
			    changed2.add(pair);
			break;
			//if a seat is occupied and there are four or more occupied adjacent seats,
			//then we consider that seat is abandoned
		    case TAKEN:
			if(occupied_adjacent(state, height, width, pair.X, pair.Y, 4) >= 4)
			    changed2.add(pair);
			break;
		    }		    
		});
	    
	    changed = changed2;

	    //turns out clear/addall has a 40% performance hit haha
	    //changed.clear();
	    //changed.addAll(changed2);

	    changed.parallelStream().forEach(pair -> {
		    state[pair.Y][pair.X] ^= CONVERT;
		});
	}

	return count_occupied(state, height, width);
    }

    //COUNTS THE NUMBER OF OCCUPIED SEATS DIRECTLY ADJACENT TO A SEAT
    private int occupied_adjacent(char[][] state, int height, int width, int x, int y, int max) {
	int ct = 0;
	for(int dx = -1; dx < 2 && ct < max; dx++)
	    for(int dy = -1; dy < 2 && ct < max; dy++) {
		//check not identity
		if(dx != 0 || dy != 0) {
		    //check inbounds
		    if(dx + x >= 0 && dx + x < width
		       && dy + y >= 0 && dy + y < height) {
			if(state[y+dy][x+dx] == TAKEN)
			    ct++;
		    }		       
		}
	    }

	return ct;
    }
    
    public int solve_complex_fast(char[][] state, int height, int width, ConcurrentLinkedQueue<Pair> changed) {
	//precompute all the neighbors
	Pair[][][] neighbors = new Pair[height][width][];
	changed.parallelStream().forEach(pair -> {
		neighbors[pair.Y][pair.X] = findNeighbors(state, height, width, pair.X, pair.Y);	    
	    });

	while(changed.size() > 0) {
	    ConcurrentLinkedQueue<Pair> changed2 = new ConcurrentLinkedQueue<Pair>();
	    
	    changed.parallelStream().forEach(pair -> {
		    switch(state[pair.Y][pair.X]) {
		    case EMPTY:
			//if we can see no occupied seats, fill
			if(occupied_adjacent(state, neighbors[pair.Y][pair.X], 1) == 0)
			    changed2.add(pair);
			break;
		    case TAKEN:
			//if we can see 5 or more occupied seats, vacate
			if(occupied_adjacent(state, neighbors[pair.Y][pair.X], 5) == 5)
			    changed2.add(pair);
		    }
		});

	    changed = changed2;

	    //convert all the changes
	    changed.parallelStream().forEach(pair -> {
		    state[pair.Y][pair.X] ^= CONVERT;
		});
	}
	
	return count_occupied(state, height, width);
	
    }

    //CHECKS IF A GIVEN TILE IS A STEA
    private boolean is_seat(char c) {
	return (c != FLOOR);
    }

    private int occupied_adjacent(char[][] state, Pair[] targets, int max) {
	int rem = max;
	int ct = 0;

	for(int i = 0; i < targets.length && rem <= targets.length - i && ct < max; i++) {
	    var target = targets[i];
	    if(state[target.Y][target.X] == TAKEN) {
		ct++;
		rem--;
	    }		
	}

	return ct;
    }
    
    //COUNTS THE NUMBER OF OCCUPIED SEATS IN A MAP
    private int count_occupied(char[][] state, int height, int width) {
	int ct = 0;

	for(int y = 0; y < height; y++)
	    for(int x = 0; x < width; x++) {
		if(state[y][x] == TAKEN)
		    ct++;
	    }
		    
	return ct;
    }

    //finds all neighbors in octal trajectories
    private Pair[] findNeighbors(char[][] state, int height, int width, int x, int y) {
	int ct = 0;
	Pair[] neighbors = new Pair[8];

	for(int dx = -1; dx < 2; dx++)
	    for(int dy = -1; dy < 2; dy++) {
		if(dx != 0 || dy != 0) {
		    var pair = findNeighbor(state, height, width, x, y, dx, dy);
		    if(pair != null)
			neighbors[ct++] = pair;
		}
	    }

	return IntStream.range(0, ct).mapToObj(i -> neighbors[i]).toArray(Pair[]::new);
    }

    //finds the nearest neighbor along a given trajectory
    private Pair findNeighbor(char[][] state, int height, int width, int x, int y, int dx, int dy) {

	int dist = 1;
	int _x = 0;
	int _y = 0;
	while((_x = dist*dx + x) >= 0 && _x < width &&
	      (_y = dist*dy + y) >= 0 && _y < height) {
	    if(is_seat(state[_y][_x]))
		return new Pair(_x, _y);
	    dist++;
	}

	return null;
    }
    	
    public char[][] dup(char[][] target, int height, int width) {
	char[][] res = new char[height][];
	    ;
	for(int y = 0; y < height; y++) {
	    res[y] = new char[width];
	    for(int x = 0; x < width; x++)
		res[y][x] = target[y][x];
	}
	return res;
    }
    
    private class Pair {
	final int X;
	final int Y;
	final int hashcode = -1;
	
	@Override public int hashCode() {
	    //compute only once to optimize
	    return hashcode; //return 7 * ((X*Y) + X + Y);
	}
	
	public Pair(int X, int Y) {
	    this.X = X;
	    this.Y = Y;
	    //this.hashcode = 7 * (X*Y + X + Y);
	}

	public boolean equals(Pair p) {
	    return p.X == X && p.Y == Y;
	}

	public int compareTo(Pair p) {
	    return (X - p.X) + 3*(Y - p.Y);
	}
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
    
    public Advent11() {
	super();
    }
    
    public static void main(String[] argv) {
	new Advent11().process(argv);
    }

    
}
