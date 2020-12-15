package com.nbkelly.advent;
import com.nbkelly.helper.DebugLogger;
import com.nbkelly.helper.ConceptHelper;
import java.util.Scanner;

import java.util.*;

public class Advent08 extends ConceptHelper {
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

    private class State {
	public int pc = 0;
	public int acc = 0;
	public State(int pc, int acc) {
	    this.pc = pc;
	    this.acc = acc;
	}
    }

    public class ProgramState {
	public ArrayList<State> history = new ArrayList<State>();
	public State currentState = new State(0, 0);
	//a program terminates if it doesn't get stuck in an infinite loop
	public boolean terminates = false;
	//an exit is graceful if the pc is exactly the line count
	public boolean graceful = false;
	public int cycles = 0;
	public ProgramState(ArrayList<State> history, State currentState,
			    boolean terminates, boolean graceful,
			    int cycles) {
	    this.history = history;
	    this.currentState = currentState;
	    this.terminates = terminates;
	    this.graceful = graceful;
	    this.cycles = cycles;
	}	
    }

    public final int ACC = 0;
    public final int JMP = 1;
    public final int NOP = 2;

    public class Line {
	public int type = 0;
	public int component = 1;
	public Line(String line) {
	    String[] c = line.split(" ");
	    this.component = Integer.parseInt(c[1]);
	    switch(c[0]) {
	    case "acc":
		type = ACC;
		break;		
	    case "jmp":
		type = JMP;
		break;
	    default:
		type = NOP;
		break;
	    }
	}
    }    

    public class Program {
	Line[] lines = null;
	public Program(ArrayList<String> input) {
	    lines = new Line[input.size()];
	    for(int i = 0; i < input.size(); i++) {
		try {
		    lines[i] = new Line(input.get(i));
		} catch (Exception e) {
		    printf("Unrecognizable line at PC=%d (hard-%s)%n", i, input.get(i));
		    lines[i] = new Line("nop +1");	    
		}
	    }
	}
	
	public int size() {
	    return lines.length;
	}

	public Line line(int line) {
	    return lines[line];
	}

	public int term_point() {
	    return size();
	}
    }

    //this is basically your main method
    public void solveProblem() throws Exception {
	Timer t = new Timer().start();

	ArrayList<String> input = new ArrayList<String>();
	while(hasNextLine())
	    input.add(nextLine());

	Program program = new Program(input);

	t.split("read file");
	
	var p1 = runCode(program, new State(0, 0), null);
	DEBUGF("PART ONE: ACC = ");
	print(p1.currentState.acc);
	DEBUGF(", PC = %d", p1.currentState.pc);
	println();
	//printf("ACC: %d, PC: %d%n", original.currentState.acc, original.currentState.pc);

	t.split("Start p2");
	
	DEBUGF("PART TWO: ACC = ");
	var p2 = evaluate_graph(program);
	print(p2.currentState.acc);
	DEBUGF(", PC = %d", p2.currentState.pc);
	println();
	
	//to work backwards, we start with our history
	t.total("Finished processing of file. ");
    }

    private ProgramState runCode(Program program, State startingState,
				 TreeSet<Integer> term) {
	int pc = startingState.pc;
	int acc = startingState.acc;
	ArrayList<State> history = new ArrayList<State>();
	boolean[] visited = new boolean[program.size()];
	int cycles = 0;
	boolean first = term != null && term.size() > 0;
	
	while(true) {
	    cycles++;
	    if(pc < 0 || pc > program.size()) //if it's out of bounds in a bad way, we terminate
		return new ProgramState(history, new State(pc, acc), true, false, cycles);
	    else if(pc == program.size())     //if it's out of bounds in a good way, we terminate
		return new ProgramState(history, new State(pc, acc), true, true, cycles);
	    else if (visited[pc])             //if it's a duplicate line, we also terminate
		return new ProgramState(history, new State(pc, acc), false, false, cycles);
	    
	    visited[pc] = true;
	    //mark the current state
	    history.add(new State(pc, acc));		
	    
	    Line instruction = program.line(pc);

	    switch(instruction.type) {
	    case ACC:
		acc += instruction.component;
		pc++;
		break;
	    case NOP:
		if(first && term.contains(pc + instruction.component)) {
		    pc += instruction.component;
		    first = false;
		}
		else
		    pc++;
		break;
	    case JMP:
		if(first && term.contains(pc + 1)) {
		    pc += 1;
		    first = false;
		}
		else
		    pc += instruction.component;
	    }	    
	}
    }
    
    private ProgramState evaluate_graph(Program program) {
	int target_pc = program.size();
	
	TreeMap<Integer, ArrayList<Integer>> targets = new TreeMap<>();
	
	for(int i = 0; i < program.size(); i++) {
	    Line instruction = program.line(i);//String[] inst = program.get(i).split(" ");
	    //int number = Integer.parseInt(inst[1]);

	    int from = i;
	    int to = i+1;
	    switch(instruction.type) {
	    case JMP:
		to = i+instruction.component;
		break;		
	    }

	    //we also make a mapping of every target -> all of the inputs
	    if(targets.containsKey(to))
		targets.get(to).add(from);
	    else {
		ArrayList<Integer> _from = new ArrayList<>();
		_from.add(from);
		targets.put(to, _from);
	    }		
	}

	//now we want to start at the end and backtrack until we have exhausted all options
	TreeSet<Integer> terminating_states = terminating(targets, target_pc);

	//terminating states should now be a list of all states that lead to program termination
	//now all we need to do is run the program, replacing an instruction the first time that replacement
	//would put us in the range of terminating states
	return runCode(program, new State(0, 0), terminating_states);
    }

    public TreeSet<Integer> terminating(TreeMap<Integer, ArrayList<Integer>> map, int target) {
	LinkedList<Integer> valid = new LinkedList<Integer>();
	TreeSet<Integer> term = new TreeSet<Integer>();

	valid.add(target);

	while(valid.size() > 0) {
	    int current = valid.pop();
	    term.add(current);
	    
	    var list = map.get(current);
	    if(list != null)
		valid.addAll(list);
	}

	return term;
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
	PAGE_ENABLED = false;
	
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
    
    public Advent08b() {
	super();
    }
    
    public static void main(String[] argv) {
	new Advent08b().process(argv);
    }

    
}
