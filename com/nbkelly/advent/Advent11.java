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
    final int FLOOR = 0;
    final int EMPTY = 1;
    final int OCCUPIED = 2;
    final boolean generategif = true;
    
    private int[][] translate(ArrayList<String> input) {
	int[][] state = new int[input.size()][];
	for(int i = 0; i < input.size(); i++) {
	    String s = input.get(i);
	    state[i] = new int[s.length()];
	    for(int j = 0; j < s.length(); j++) {
		int res = 0;
		switch(s.charAt(j)) {
		case 'L':
		    state[i][j] = EMPTY;
		    break;
		case '.':
		    state[i][j] = FLOOR;
		    break;
		case '#':
		    state[i][j] = OCCUPIED;
		    break;
		}
	    }
	}

	return state;
    }
    
    public void solveProblem() throws Exception {
	Timer t = new Timer().start();

	ArrayList<String> input = new ArrayList<String>();
        while(hasNextLine())
	    input.add(nextLine());
	
	int[][] state = translate(input);
	
	//get the height and width of the state
	int height = input.size();
	int width = input.get(0).length();

	t.split("Start 1");
	println(solve_simple_fast(state, height, width));
	t.split("Start 2");
	println(solve_simple(state, height, width));
	t.split("Start 3");
	println(solve_complex(state, height, width));

	t.total("Finished processing of file. ");
    }

    public int[][] dup(int[][] target, int height, int width) {
	int[][] res = new int[height][];
	    ;
	for(int y = 0; y < height; y++) {
	    res[y] = new int[width];
	    for(int x = 0; x < width; x++)
		res[y][x] = target[y][x];
	}
	return res;
    }
    //state is double buffered, we don't need to worry about cloning it
    private int solve_simple_fast(int[][] _state, int height, int width) throws Exception {	
	int iteration = 0;

	final int[][] state = dup(_state, height, width);
	
	//only seats can change
	ConcurrentLinkedQueue<Pair> changed = new ConcurrentLinkedQueue<>();
	for(int y = 0; y < height; y++)
	    for(int x = 0; x < width; x++)
		if(isSeat(y, x, state, height, width))
		    changed.add(new Pair(x, y));

	while(changed.size() > 0) {
	    //if(DEBUG) print_matrix(state, height, width);

	    ConcurrentLinkedQueue<Pair> changed2 = new ConcurrentLinkedQueue<>();	    

	    changed.parallelStream().
		forEach(pair -> {
			int y = pair.Y;
			int x = pair.X;
			if(isEmpty(y, x, state, height, width)) {
			    if(occupied_surrounding(y, x, state, height, width) == 0) {
				changed2.add(new Pair(x, y));
			    }
			}
			else if (isOccupied(y, x, state, height, width)) {
			    if(occupied_surrounding(y, x, state, height, width) >= 4) {
				changed2.add(new Pair(x, y));
			    }
			}			
		    });

	    changed = changed2;
	    
	    changed.parallelStream().forEach(pair -> {
		    int y = pair.Y;
		    int x = pair.X;
		    state[y][x] ^= 3;
		});

	    
	    //DEBUG(iteration++);
	    
	}

	return ct_token(state, height, width, OCCUPIED);
    }

    private class Pair {
	int X;
	int Y;
	public Pair(int X, int Y) {
	    this.X = X;
	    this.Y = Y;
	}

	public boolean equals(Pair p) {
	    return p.X == X && p.Y == Y;
	}

	public int compareTo(Pair p) {
	    return (X - p.X) + 3*(Y - p.Y);
	}
    }
    
    //state is double buffered, we don't need to worry about cloning it
    private int solve_simple(int[][] _state, int height, int width) throws Exception {	
	int iteration = 0;

	final Res res = new Res();

	res.hasChanged = true;
	while(res.hasChanged) {
	    //if(DEBUG) print_matrix(_state, height, width);
	    res.hasChanged = false;
	    final int[][] state = _state;
	    final int[][] newState = new int[height][];
	    IntStream lines = IntStream.range(0, height);
	    lines.parallel().forEach(y -> {
		newState[y] = new int[width];
		for(int x = 0; x < width; x++) {
		    newState[y][x] = state[y][x];
		    if(isEmpty(y, x, state, height, width)) {
			if(occupied_surrounding(y, x, state, height, width) == 0) {
			    newState[y][x] = OCCUPIED;
			    res.hasChanged = true;
			}
		    }
		    else if (isOccupied(y, x, state, height, width)) {
			if(occupied_surrounding(y, x, state, height, width) >= 4) {
			    newState[y][x] = EMPTY;
			    res.hasChanged = true;
			}			
		    }		    
		}
		});
	    
	    _state = newState;
	    //DEBUG(iteration++);

	}

	return ct_token(_state, height, width, OCCUPIED);
    }

    //state is double buffered, we don't need to worry about cloning it
    private int solve_complex(int[][] _state, int height, int width) {	
	int iteration = 0;
	
	final Res res = new Res();
	res.hasChanged = true;
	while(res.hasChanged) {
	    //if(DEBUG) print_matrix(_state, height, width);
	    res.hasChanged = false;
	    final int[][] state = _state;
	    final int[][] newState = new int[height][];
	    IntStream lines = IntStream.range(0, height);
	    lines.parallel().forEach(y -> {
		newState[y] = new int[width];
		for(int x = 0; x < width; x++) {
		    newState[y][x] = state[y][x];
		    if(isEmpty(y, x, state, height, width)) {
			if(occupied_surrounding_line(y, x, state, height, width) == 0) {
			    newState[y][x] = OCCUPIED;
			    res.hasChanged = true;
			}
		    }
		    else if (isOccupied(y, x, state, height, width)) {
			if(occupied_surrounding_line(y, x, state, height, width) >= 5) {
			    newState[y][x] = EMPTY;
			    res.hasChanged = true;
			}			
		    }		    
		}
		});
	    
	    _state = newState;
	    //DEBUG(iteration++);

	}

	return ct_token(_state, height, width, OCCUPIED);
    }

    private class Res {	
	public boolean hasChanged = false;
    }
    
    private int print_matrix(int[][] state, int height, int width) {
	if(DEBUG) {
	    for(int y = 0; y < height; y++) {
		for(int x = 0; x < width; x++) {
		    switch(state[y][x]) {
		    case 2: DEBUGF("%c",'#'); break;
		    case 1: DEBUGF("%c",'L'); break;
		    case 0: DEBUGF("%c",'.'); break;
		    }
		}
		DEBUG();
	    }
	}

	return 0;
    }
    
    int ct_token(int[][] state, int height, int width, int token) {
	int ct = 0;
	for(int y = 0; y < height; y++)
	    for(int x = 0; x< width; x++) {
		if(state[y][x] == token)
		    ct++;
	    }

	return ct;
    }

    boolean isEmpty(int y, int x, int[][] state, int height, int width) {
	return isSeat(y, x, state, height, width) && state[y][x] == EMPTY;
    }

    boolean isOccupied(int y, int x, int[][] state, int height, int width) {
	return isSeat(y, x, state, height, width) && state[y][x] == OCCUPIED;
    }
    
    boolean isSeat(int y, int x, int[][] state, int height, int width) {
	return (y >= 0 && y < height &&
		x >= 0 && x < width &&
		state[y][x] != FLOOR);
    }

    boolean firstSeatOccupied(int y, int x, int[][] state, int height, int width, int dy, int dx) {
	int[] res = firstSeat(y, x, state, height, width, dy, dx);
	return (isOccupied(res[0], res[1], state, height, width));
    }
    
    int[] firstSeat(int y, int x, int[][] state, int height, int width, int dy, int dx) {
	y += dy;
	x += dx;
	
	while(y >= 0 && y < height && x >= 0 && x < width) {
	    if((isSeat(y, x, state, height, width)))
		return new int[]{y, x}; //location of seat
	    y += dy;
	    x += dx;
	}

	return new int[]{-1, -1};
    }

    int occupied_surrounding_line(int y, int x, int[][] state, int height, int width) {
	int ct = 0;
	for(int dy = -1; dy <= 1; dy++) {
	    for(int dx = -1; dx <= 1; dx++) {
		if(dx == 0 && dy == 0)
		    continue;
		if(firstSeatOccupied(y, x, state, height, width, dy, dx))
		    ct++;
	    }
	}

	return ct;
    }
    
    int occupied_surrounding(int y, int x, int[][] state, int height, int width) {
	int ct = 0;
	for(int dy = -1; dy <= 1; dy++) {
	    for(int dx = -1; dx <= 1; dx++) {
		if(dx == 0 && dy == 0)
		    continue;
		if(isOccupied(y + dy, x + dx, state, height, width))
		    ct++;
	    }
	}
	
	
	return ct;
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
