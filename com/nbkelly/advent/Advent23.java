package com.nbkelly.advent;
import com.nbkelly.helper.DebugLogger;
import com.nbkelly.helper.ConceptHelper;
import java.util.Scanner;

import java.util.*;

public class Advent23 extends ConceptHelper {
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

	var input = nextLine();
	//input = "389125467";
	
	int maxvalue = 1000000;
	CircularLinkedMap p1_map = new CircularLinkedMap(9);
	CircularLinkedMap p2_map = new CircularLinkedMap(maxvalue);
	
	for(int i = 0; i < input.length(); i++) {
	    p1_map.add(Integer.parseInt(input.charAt(i)+""));
	    p2_map.add(Integer.parseInt(input.charAt(i)+""));
	}	 
	
	for(int i = 10; i <= maxvalue; i++)
	    p2_map.add(i);
	
	for(int i = 0; i < 100; i++)
	    cycleCups(p1_map, 9);
	
	for(int i = 0; i < 10000000; i++)
	    cycleCups(p2_map, maxvalue);
	
	DEBUGF("PART ONE: "); println(p1_map.partOne());	
	DEBUGF("PART TWO: "); println(p2_map.partTwo());

	t.total("Finished processing of file. ");
    }    
    
    private class CircularLinkedMap {
	public String toString() {
	    String res = "" + head.value;
	    Node ct = head;
	    while((ct = ct.next) != head)
		res += ct.value;

	    return res;
	}

	public String partOne() { //no shame
	    while(head.value != 1)
		rotate();
	    return toString().substring(1);
	}
	
	Node[] location;
	Node head = null;
	Node tail = null;
	
	public CircularLinkedMap(int size) { location = new Node[size+1]; }
	
	private class Node {
	    public Node(int value) { this.value = value; location[value] = this; }
	    public Node next = head;
	    final public int value;	    
	}

	private Node transpose(Node first, Node next) {
	    next.next = first.next;
	    first.next = next;
	    return next;
	}
	
	public void add(int i) { //add a new element to the map
	    Node n = new Node(i);

	    if(head == null)
		head = tail = n;
	    
	    tail = transpose(tail, n);
	    tail.next = head;
	}
	
	public int getFirst() { return head.value; }
	
	public int removeSecond() {
	    Node n = head.next;
	    head.next = n.next;
	    return n.value;
	}

	public long partTwo() { //easy enough
	    Node n = location[1].next;
	    long sum = n.value;
	    sum *= n.next.value;
	    return sum;
	}
	
	public void insert(List<Integer> vals, int after) {
	    Node start = location[after];
	    
	    for(int val : vals)
		start = transpose(start, new Node(val));		
	}
	
	public void rotate() { head = head.next; }
    }
    
    private void cycleCups(CircularLinkedMap map, int maxValue) {
	//first, we get the current value
	int currentCup = map.getFirst();

	//then we need to get the three values to the right of that, and remove them
	ArrayList<Integer> picked = new ArrayList<Integer>();
	for(int i = 0; i < 3; i++)
	    picked.add(map.removeSecond());
	
	//then we set the destination - shouldn't take more than 4 checks
	int dest = currentCup - 1;
	while(picked.contains(dest) || dest == 0)
	    if(dest == 0) dest = maxValue;
	    else          dest -= 1;	

	//we now have the destination cup - we just need to insert picked after that
	map.insert(picked, dest);

	//rotate 1
	map.rotate();	
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
    
    public Advent23() {
	super();
    }
    
    public static void main(String[] argv) {
	new Advent23().process(argv);
    }

    
}
