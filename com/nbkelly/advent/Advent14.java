package com.nbkelly.advent;
import com.nbkelly.helper.DebugLogger;
import com.nbkelly.helper.ConceptHelper;
import java.util.Scanner;

import java.util.*;
import java.math.BigInteger;
import java.math.BigDecimal;

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
	
	var mappings = chunkify(input);
	Collections.reverse(mappings);

	final int addr_size = 36;

	BTree b = new BTree();
	for(Mapping m : mappings) {
	    //printf("MASK: %s%n", m.mask);
	    //println("Size: " + m.size());

	    for(int i = 0; i < m.size(); i++) {		
		String addr = transform_to_binary(m.addresses.get(m.size() - 1 - i), addr_size);
		//printf("ADDR: %s%n", addr);
		String masked = mask_address(m.mask, addr, addr_size);
		//printf("MSKD: %s%n", masked);
		var value = m.values.get(m.size() - 1 - i);
		//printf("VSET: %d%n", value);
		
		b.add(masked, value);
		
		//return;
	    }
	}
	DEBUGF("PART ONE: "); println(partOne(input));
	DEBUGF("PART TWO:  "); println(b.enumerate());
	//DEBUGF("MEMBERS:   %s%n", b.members().toString());
	//DEBUGF("ADDRESSES: %s%n", b.addresses().toString());

	t.total("Finished processing of file. ");
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
		boolean ch = mask.charAt(i) == '1';
		val = Util.setBit(val, index, ch);//val.set
	    }
	}

	return val;
    }
    
    public class BTree {
	Node head = new Node(0);
	
	public BigInteger members() {
	    return head.members();
	}

	public BigInteger addresses() {
	    return head.addresses();
	}
	
	public BigInteger enumerate() {
	    return head.enumerate();
	}
	
	private class Node {
	    long value;
	    
	    Node node0;
	    Node node1;
	    Node nodeX;

	    public BigInteger members() {
		BigInteger res = BigInteger.ONE;
		
		if(node0 != null)
		    res = res.add(node0.members());
		if(node1 != null)
		    res = res.add(node1.members());
		if(nodeX != null)
		    res = res.add(nodeX.members());
		
		return res;
	    }

	    public BigInteger addresses() {
		BigInteger res = BigInteger.ZERO;

		if(nodeX == null && node0 == null && node1 == null)
		    return BigInteger.ONE;
		
		if(nodeX != null)
		    return nodeX.addresses().multiply(BigInteger.valueOf(2));
		if(node0 != null)
		    res = res.add(node0.addresses());
		if(node1 != null)
		    res = res.add(node1.addresses());
		
		return res;
	    }
	    
	    public BigInteger enumerate() {
		//if no children, then return our value
		//otherwise return the sum of children
		if(node0 == null &&
		   node1 == null &&
		   nodeX == null) {
		    return BigInteger.valueOf(value);
		}

		//if x is not null
		if(nodeX != null) {
		    return BigInteger.valueOf(2).multiply(nodeX.enumerate());
		}

		//if 1 and 2 are non-null
		if(node1 != null && node0 != null) {
		    return node1.enumerate().add(node0.enumerate());
		}

		//if 1 is valid
		if(node1 != null)
		    return node1.enumerate();

		return node0.enumerate();
	    }
	    
	    public Node(String s, long value, int depth, String original, int order) {
		this.value = value;
		add(s, value, depth+1, original, order);
	    }

	    private Node(long value) { this.value = value; }
	    
	    public void add(String s, long value, int depth, String original, int order) {
		if(s.length() > 0) {
		    char start = s.charAt(0);
		    String next = s.substring(1);
		    
		    if(start == '1') {
			//if we have an x, we need to split that into a 0 and a 1
			if(nodeX != null) {
			    node1 = nodeX;
			    node0 = dup(nodeX);
			    nodeX = null;
			}

			//if node1 is not null, we add to it
			if(node1 != null) {
			    //then we add the next part of the sum to node 1
			    node1.add(next, value, depth+1, original, order);
			}
			else {
			    //we create the node node1
			    node1 = new Node(next, value, depth+1, original, order);
			}
		    }
		    else if (start == '0') {
			//if we have an X, we need to split that into a 0 and a 1
			if(nodeX != null) {
			    node1 = nodeX;
			    node0 = dup(nodeX);
			    nodeX = null;
			}

			//if node0 is not null, we add to it
			if(node0 != null) {
			    //then we add the next part of the sum to node 1
			    node0.add(next, value, depth+1, original, order);
			}
			else {
			    //we create the node node1
			    node0 = new Node(next, value, depth+1, original, order);
			}
		    }
		    else if (start == 'X') {
			//if either 1 or 0 exists, then we must crawl both branches
			if(node0 != null || node1 != null) {
			    //if node0 is null, we create it
			    if(node0 == null)
				node0 = new Node(next, value, depth+1, original, order);
			    //otherwise we add to it
			    else
				node0.add(next, value, depth+1, original, order);

			    //if node1 is null, we create it
			    if(node1 == null)
				node1 = new Node(next, value, depth+1, original, order);
			    //otherwise we add to it			    
			    else
				node1.add(next, value, depth+1, original, order);
			}
			else {
			    //if nodeX is null, we create it
			    if(nodeX == null)
				nodeX = new Node(next, value, depth+1, original, order);
			    else
				nodeX.add(next, value, depth+1, original, order);
			}
		    }
		}
	    }

	    private Node dup(Node n) {
		if(n == null)
		    return null;

		//duplicate the value itself
		Node res = new Node(n.value);
		
		//duplicate all children
		res.node0 = dup(n.node0);
		res.node1 = dup(n.node1);
		res.nodeX = dup(n.nodeX);
		
		return res;
	    }
	}

	private int _order = 0;
	public void add(String s, long value) {
	    head.add(s, value, 0, s, ++_order);
	}
    }
    
    public String transform_to_binary(long addr, int len) {
	//turn the address into a binary string
	return String.format("%36s", Long.toBinaryString(addr)).replace(' ', '0');
    }
    
    public String mask_address(String mask, String addr, int len) {
	StringBuilder res = new StringBuilder();
	for(int i =0; i < len; i++) {
	    //if mask is x
	    if(mask.charAt(i) == 'X')
		res.append('X');
	    else if (mask.charAt(i) == '1' || addr.charAt(i) == '1')
		res.append('1');
	    else
		res.append('0');
	}

	return res.toString();
    }
    
    public ArrayList<Mapping> chunkify( ArrayList<String> input) {
	//split an input of mask...mask...
	//into an input of <mask...>

	//ArrayList<String> ct = new ArrayList<String>();
	Mapping current = new Mapping();
	ArrayList<Mapping> res = new ArrayList<>();

	int order = 0;
	for(String s : input) {
	    if(s.startsWith("mem[")) {
		long addr = Long.parseLong(s.split("\\[")[1].split("\\]")[0]);
		long val = Long.parseLong(s.split("= ")[1]);

		current.addresses.add(addr);
		current.values.add(val);
	    }
	    else if( s.startsWith("mask =")) {
		if(!current.isEmpty()) {
		    res.add(current);
		    current = new Mapping();
		}		    
		current.mask = s.split("= ")[1];		
	    }
	}

	res.add(current);

	return res;
    }

    
    private class Mapping {
	public ArrayList<Long> addresses = new ArrayList<Long>();
	public ArrayList<Long> values = new ArrayList<Long>();

	public String mask = null;
	
	public int size() {
	    return addresses.size();
	}

	public boolean isEmpty() {
	    return size() == 0;
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
    
    public Advent14() {
	super();
    }
    
    public static void main(String[] argv) {
	new Advent14().process(argv);
    }

    
}
