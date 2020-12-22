package com.nbkelly.helper;
import java.math.BigInteger;
import java.lang.StringBuilder;
import java.util.*;

public abstract class ConceptHelper {    
    //If you are lost, your code probably begins on line 67
    public enum Color {
	//reset

	// Regular Colors
	BLACK("\033[0;30m"),   // BLACK
	RED("\033[0;31m"),     // RED
	GREEN("\033[0;32m"),   // GREEN
	YELLOW("\033[0;33m"),  // YELLOW
	BLUE("\033[0;34m"),   // BLUE
	PURPLE("\033[0;35m"),  // PURPLE
	CYAN("\033[0;36m"),    // CYAN
	WHITE("\033[0;37m"),   // WHITE

	/*// Bold
	BLACK_BOLD = "\033[1;30m";  // BLACK
	RED_BOLD = "\033[1;31m";    // RED
	GREEN_BOLD = "\033[1;32m";  // GREEN
	YELLOW_BOLD = "\033[1;33m"; // YELLOW
	BLUE_BOLD = "\033[1;34m";   // BLUE
	PURPLE_BOLD = "\033[1;35m"; // PURPLE
	CYAN_BOLD = "\033[1;36m";   // CYAN
	WHITE_BOLD = "\033[1;37m";  // WHITE*/
	RESET("\033[0m");

	public final String label;
	private Color(String label) {
	    this.label = label;
	}
    };

    public String color(String s, Color c) {
	return c.label + s + Color.RESET.label;
    }
    
    //navigate input files
    private Scanner sc = new Scanner(System.in);
    private Scanner ln;
    private int line = 0;
    private int token = 0;
    protected String pagedString = "";
    
    //is the program running in debug mode
    //things like timer, DEBUG(), etc will only work this way
    protected boolean DEBUG = false;
    protected boolean TIMER = false;
    protected boolean clean_exit = true;
    protected boolean PAGE_ENABLED = false;
    //number of significant places to use when running the timer
    //timer runs in nanoseconds, units = xxx.000000000 s, where
    //l(x..x) = magnitude
    protected int DEBUG_TIME_MAGNITUDE = 3;
    protected boolean IGNORE_UNCLEAN = true;
    protected Exception exception = null;

    protected DebugLogger logger = null;
    protected boolean ALLOW_EMPTY = false;
    protected String[] COMMENT_LINE = {"#"};
    
    public ConceptHelper() {
	self = this;
    }
    protected ConceptHelper self;
    
    // Input:
    //   * NOTE: checking must be explicit (existence is assumed)
    //   hasNextInt()        -> boolean, input contains another int
    //   hasNextBigInteger() -> boolean, same as above but bigint
    //   hasNextLine()       -> boolean, same as above but String (full line)
    //   hasNext()           -> boolean, same as above but String (token)
    //   hasNextDouble()     -> boolean, same as above but double
    //   hasNextBigDecimal() -> boolean, same as above but bigdecimal
    //   nextInt()           -> integer, gets next int from stream
    //   nextDouble()        -> integer, gets next double from stream
    //   next()              -> string, gets next token from the stream
    //   nextBigInteger()    -> bigint,  gets next bigint from stream
    //   nextBigDecimal()    -> bigdec,  gets next bigdecimal from stream
    //   nextLine()          -> String,  gets all text from stream up to next line break
    
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
    //   Single pairs, or lists of numbers are supported
    public abstract void solveProblem() throws Exception; /* {
	/**
	 *  Timer t = new Timer().start();
	 *  
	 *  while(hasNextInt()) {
	 *      t.split("Started scenario at " + line);
	 *
	 *      //your logic here
	 *  }
	 *  
	 *  t.total("Finished processing of file. 
	 */
    //}



    
    /* ^^^^ YOUR WORK
     *
     * LIB IMPLEMENTATIONS BELOW HERE
     * 
     * vvvv NOT YOUR WORk
     */
    /*public static void main(String[] argv) {
	//use of non-static members allows replacement of solveProblem for inherited subclasses
	new ConceptHelper().process(argv);
	}*/

    public abstract boolean processArgs(String[] argv); /*{
	for(int i = 0; i < argv.length; i++) {
	    switch(argv[i]) {
	    case "-se" : IGNORE_UNCLEAN = false; break;
	    case "-d"  : DEBUG = true; IGNORE_UNCLEAN = false;
	    case "-t"  : TIMER = true; break;
	    case "-dt" :
		Scanner tst = null;
		if(i + 1 < argv.length &&
		   (tst = new Scanner(argv[i+1])).hasNextInt()) {
		    DEBUG_TIME_MAGNITUDE = tst.nextInt();
		    i++;
		    break;
		}
		
	    default :
		System.err.
		    println("Usage: -se       = (show exceptions),\n" +
			    "       -d        = debug mode,\n" +
			    "       -t        = timer mode (debug lite),\n" +
			    "       -dt <int> = set timer digits");
		return false;
	    }
	}

	return true;
	}*/

    

    public int Page() {
	if(!PAGE_ENABLED)
	    throw new UnsupportedOperationException("ERROR: Paging mode has not been enabled");

	return line;
    }

    public int Reset(int page) {
	if(!PAGE_ENABLED)
	    throw new UnsupportedOperationException("ERROR: Paging mode has not been enabled");

	ln = null;
	sc = new Scanner(pagedString);
	
	line = 0;
	while(line != page)
	    nextLine();
	
	return page;
    }
    
    public void process(String[] argv) {
	if(!processArgs(argv))
	    return;

	if(PAGE_ENABLED) {
	    StringBuilder ps = new StringBuilder("");
	    while(sc.hasNextLine()) {
		ps.append(sc.nextLine() + "\n");
	    }
	    pagedString = ps.toString();
	    sc = new Scanner(pagedString);
	}
	
	try {
	    logger = getDebugLogger();
	    solveProblem();
	} catch (Exception e) {
	    clean_exit = false;
	    exception = e;
	}
	finally {
	    DEBUG();
	    
	    if(clean_exit) {
		if(!hasNextLine())
		    DEBUG("Program terminated cleanly at line '" + line +
			  "', token '" + token + "' due to no input");
		else {
		    StringBuilder remlines = new StringBuilder();
		    int ct = 0;
		    while(hasNextLine()) {
			if(ct < 5)
			    remlines.append(nextLine() + "\n");
			else
			    break;
			ct++;
		    }
		    
		    DEBUG("Program terminated cleanly at line '" + line +
			  "', token '" + token + "' with '" + ct + "' lines of input remaining");
		    if(ct <= 5)
			DEBUG(remlines.toString());
		}
	    }
	    else
		DEBUG("Program terminated at line '" + line +"'");
	    
	    DEBUG("Clean exit: " + clean_exit);

	    if(!(clean_exit || IGNORE_UNCLEAN) && exception != null)
		exception.printStackTrace();
	}
    }
    
    public static int GCD(List<Integer> li) {
	if(li.size() < 1)
	    return 1;

	int st = li.get(0);

	for(int i = 1; i < li.size(); i++) {
	    if(st == 1)
		return 1;
	    st = GCD(st, li.get(i));	    
	}
	
	return st;
    }

    public static int GCD(int[] li) {
	if(li.length < 1)
	    return 1;

	int st = li[0];

	for(int i = 1; i < li.length; i++) {
	    if(st == 1)
		return 1;
	    st = GCD(st, li[i]);	    
	}
	
	return st;
    }
    
    public static int GCD(int a, int b) {
	if (a <= 0)
	    return b;

	while (b > 0) {
	    if (a > b)
		a = a - b;
	    else
		b = b - a;
	}

	return a;
    }

    public static int LCM(List<Integer> li) {
	if(li.size() < 1)
	    return 1;

	int st = li.get(0);

	for(int i = 1; i < li.size(); i++) {
	    int val = li.get(i);
	    st = (st * val) / GCD(st, val);
	}
	
	return st;
    }

    public static int LCM(int[] li) {
	if(li.length < 1)
	    return 1;

	int st = li[0];

	for(int i = 1; i < li.length; i++) {
	    int val = li[i];
	    st = (st * val) / GCD(st, val);
	}
	
	return st;
    }

    
    //LCM = (a * b) / GCD(a b)
    public static int LCM(int a, int b) {
	long m = a * (long)b;
	return (int) (m / GCD(a, b));
    }
    
    // NOW HERE IS SOME TIMER STUFF
    public class Timer {
	//we cheat with this timer, and when we split, we account
	//for the time taken in the split
	//the timer only does anything when debug is enabled
	private long startTime;
	private long splitTime;

	private String smult(String s, int period) {
	    if(period <= 0)
		return "";

	    return new String(new char[period]).replace("\0", s);
	}
	
	public Timer start() {
	    if(TIMER) {
		this.startTime = this.splitTime = System.nanoTime();
		TEBUG("Event - Timer Started");
	    }
	    return this;
	}

	public void split() {
	    split(null);
	}
	
	public void split(String val) {
	    if(TIMER) {
		val = (val == null ? "" : (" - event: " + val));
		
		long split = System.nanoTime() - splitTime;

		if(split < 0)
		    split *= -1;
		StringBuilder splitStr = new StringBuilder("" + split);

		while(splitStr.length() < 9 + DEBUG_TIME_MAGNITUDE)
		    splitStr.insert(0, '0');
		    //splitStr = "0" + splitStr;
		
		//get the location of the decimal point
		splitStr.insert(splitStr.length() - 9, '.');
		
		//splitStr = splitStr.
		TEBUG("Split time: " + splitStr + val);

		//account for the time spent in output
		splitTime = System.nanoTime() - split;
	    }
	}

	public void total(String val, boolean reset) {
	    if(TIMER) {
		val = (val == null ? "" : (" - event: " + val));
		long split = System.nanoTime() - startTime;

		if(split < 0)
		    split *= -1;
		
		StringBuilder splitStr = new StringBuilder("" + split);

		while(splitStr.length() < 9 + DEBUG_TIME_MAGNITUDE)
		    splitStr.insert(0, '0');
		    //splitStr = "0" + splitStr;
		
		//get the location of the decimal point
		splitStr.insert(splitStr.length() - 9, '.');

		//splitStr = splitStr.
		TEBUG("Total time: " + splitStr.toString() + val);
		if(reset) reset();
	    }
	}

	public void total(boolean val) {
	    total(null, val);
	}
	
	public void total(String val) {
	    total(val, false);
	}

	public void total() {
	    total(null, false);
	}

	public void reset() {
	    if(TIMER) {
		self.DEBUG("Event - Timer Reset");
		startTime = splitTime = System.nanoTime();
	    }
	}
    }
    
    public String nextLine() {
	if(!hasNextLine())
	    DEBUG("No such element [string] at line " + line
		  + " token " + token);
	token++;
	return ln.nextLine();
    }
    
    public boolean hasNextLine() {	
	if(ln == null || !ln.hasNextLine()) {
	    //see if sc has another line	    
	    //while lines exist to be read, and we haven't just fetched
	    //a fresh scanner
	    while(sc.hasNextLine() && !checkNextLine());
	}

	//if the inputstream is null, then this will return false
	if(ln == null)
	  return false;
	
	if(ln.hasNextLine())
	    return true;
	
	boolean res = false;

	while(!ln.hasNextLine()) {
	    //if the current line has no integer
	    //then we scan through until we find
	    //another valid line, or there are
	    //no more lines
	    while(sc.hasNextLine() && !(res = checkNextLine()));
	    
	    if(!sc.hasNextLine())
		break;
	}
	//ln is gauranteed to be non-null here

	//res = true iff there is an int to read	
	return res;
    }

    
    // HERE IS SOME CONVENIENCE JUNK TO GET THE NEXT BIGINT ALWAYS
    public BigInteger nextBigInteger() {
	if(!hasNextBigInteger())
	    DEBUG("No such element [bigint] at line " + line
		  + " token " + token);
	token++;
	return ln.nextBigInteger();
    }
    
    public boolean hasNextBigInteger() {
	if(ln == null || !ln.hasNextBigInteger()) {
	    //see if sc has another line	    
	    //while lines exist to be read, and we haven't just fetched
	    //a fresh scanner
	    while(sc.hasNextLine() && !checkNextLine());
	}

	if(ln.hasNextBigInteger())
	    return true;
	
	boolean res = false;

	while(!ln.hasNextBigInteger()) {
	    //if the current line has no integer
	    //then we scan through until we find
	    //another valid line, or there are
	    //no more lines
	    while(sc.hasNextLine() && !(res = checkNextLine()));
	    
	    if(!sc.hasNextLine())
		break;
	}
	//ln is gauranteed to be non-null here

	//res = true iff there is an int to read	
	return res;
    }

    // HERE IS SOME CONVENIENCE JUNK TO GET THE NEXT BIGINT ALWAYS
    public String next() {
	if(!hasNext())
	    DEBUG("No such element [any] at line " + line
		  + " token " + token);
	token++;
	return ln.next();
    }
    
    public boolean hasNext() {
	if(ln == null || !ln.hasNext()) {
	    //see if sc has another line	    
	    //while lines exist to be read, and we haven't just fetched
	    //a fresh scanner
	    while(sc.hasNextLine() && !checkNextLine());
	}

	if(ln.hasNext())
	    return true;
	
	boolean res = false;

	while(!ln.hasNext()) {
	    //if the current line has no integer
	    //then we scan through until we find
	    //another valid line, or there are
	    //no more lines
	    while(sc.hasNextLine() && !(res = checkNextLine()));
	    
	    if(!sc.hasNextLine())
		break;
	}
	//ln is gauranteed to be non-null here

	//res = true iff there is an int to read	
	return res;
    }

    
    // HERE IS SOME CONVENIENCE JUNK TO GET THE NEXT BIGINT ALWAYS
    public double nextDouble() {
	if(!hasNextDouble())
	    DEBUG("No such element [double] at line " + line
		  + " token " + token);
	token++;
	return ln.nextDouble();
    }
    
    public boolean hasNextDouble() {
	if(ln == null || !ln.hasNextDouble()) {
	    //see if sc has another line	    
	    //while lines exist to be read, and we haven't just fetched
	    //a fresh scanner
	    while(sc.hasNextLine() && !checkNextLine());
	}

	if(ln.hasNextDouble())
	    return true;
	
	boolean res = false;

	while(!ln.hasNextDouble()) {
	    //if the current line has no integer
	    //then we scan through until we find
	    //another valid line, or there are
	    //no more lines
	    while(sc.hasNextLine() && !(res = checkNextLine()));
	    
	    if(!sc.hasNextLine())
		break;
	}
	//ln is gauranteed to be non-null here

	//res = true iff there is an int to read	
	return res;
    }

    
    // HERE IS SOME CONVENIENCE JUNK TO GET THE NEXT BIGDECIMAL ALWAYS
    public BigInteger nextBigDecimal() {
	if(!hasNextBigInteger())
	    DEBUG("No such element [bigdecimal] at line " + line
		  + " token " + token);
	token++;
	return ln.nextBigInteger();
    }
    
    public boolean hasNextBigDecimal() {
	if(ln == null || !ln.hasNextBigDecimal()) {
	    //see if sc has another line	    
	    //while lines exist to be read, and we haven't just fetched
	    //a fresh scanner
	    while(sc.hasNextLine() && !checkNextLine());
	}

	if(ln.hasNextBigDecimal())
	    return true;
	
	boolean res = false;

	while(!ln.hasNextBigDecimal()) {
	    //if the current line has no integer
	    //then we scan through until we find
	    //another valid line, or there are
	    //no more lines
	    while(sc.hasNextLine() && !(res = checkNextLine()));
	    
	    if(!sc.hasNextLine())
		break;
	}
	//ln is gauranteed to be non-null here

	//res = true iff there is an int to read	
	return res;
    }

    
    // HERE IS SOME CONVENIENCE JUNK TO GET THE NEXT INTEGER ALWAYS
    public int nextInt() {
	if(!hasNextInt())
	    DEBUG("No such element [int] at line " + line
		  + " token " + token);
	token++;
	return ln.nextInt();
    }
    
    public boolean hasNextInt() {
	if(ln == null || !ln.hasNextInt()) {
	    //see if sc has another line	    
	    //while lines exist to be read, and we haven't just fetched
	    //a fresh scanner
	    while(sc.hasNextLine() && !checkNextLine());
	}

	if(ln.hasNextInt())
	    return true;
	
	boolean res = false;

	while(!ln.hasNextInt()) { //if the current line has no integer
	                          //then we scan through until we find
	                          //another valid line, or there are
	                          //no more lines
	    while(sc.hasNextLine() && !(res = checkNextLine()));

	    if(!sc.hasNextLine())
		break;
	}
	//ln is gauranteed to be non-null here

	//res = true iff there is an int to read	
	return res;
    }

    //if the line is empty, or is a comment, return false
    private boolean checkNextLine() {
	String l = sc.nextLine();
	line++;
	token = 0;
	//printf("CHECK: ALLOW %b LEN %d%n", ALLOW_EMPTY, l.length());
	boolean res = (l.length() > 0) || ALLOW_EMPTY;
	if(res)
	    ln = new Scanner(l);
	return res;
    }

    // NOW HERE IS HOW WE PRINT
    public String padr(int output, int len) {
	String s = "" + output;

	while(s.length() < len)
	    s += " ";

	return s;
    }

    public DebugLogger getDebugLogger() {	
	if(logger == null)
	    logger = new DebugLogger() {
		public void print(int output) {
		    self.print(output);
		}
		public void print(Object output) {
		    self.print(output);
		}
		public void printf(String line, Object... output){
		    self.printf(line, output);
		}
		public void println(int output) {
		    self.println(output);
		}
		public void println(Object output) {
		    self.println(output);
		}
		public void println(){
		    self.println();
		}
		public void ERR(){
		    self.ERR();
		}
		public void ERR(Object output){
		    self.ERR(output);
		}
		public void ERR(int output){
		    self.ERR(output);
		}
		public void DEBUG(){
		    self.DEBUG();
		}
		public void TEBUG(Object output){
		    self.TEBUG(output);
		}
		public void DEBUG(Object output){
		    self.DEBUG(output);
		}
		public void TEBUGF(String line, Object... output){
		    self.TEBUGF(line, output);
		}
		public void DEBUGF(String line, Object... output){
		    self.DEBUGF(line, output);
		}
		public void TEBUG(int output){
		    self.TEBUG(output);
		}
		public void DEBUG(int output){
		    self.DEBUG(output);
		}
	    };
	
	return logger;
    }
    
    public void print(int output) {
	System.out.print(output + " ");
    }
    
    public void print(Object output) {
	System.out.print(output);
    }

    public void printf(String line, Object... args) {
	System.out.printf(line, args);
    }

    public void println() {
	System.out.println();
    }
    
    public void println(int output) {
	System.out.println(output);
    }
    
    public void println(Object output) {
	System.out.println(output);
    }

    public void ERR() {
	if(DEBUG || !IGNORE_UNCLEAN)
	    System.err.println();
    }

    public void ERR(Object output) {
	if(DEBUG || !IGNORE_UNCLEAN)
	    System.err.println(output.toString());
    }

    public void ERR(int output) {
	if(DEBUG || !IGNORE_UNCLEAN)
	    System.err.println(output);
    }
    
    public void DEBUG() {
	if(DEBUG) System.err.println();
    }
    
    public void TEBUG(Object output) {
	if(TIMER || DEBUG) System.err.println(output.toString());
    }

    public void TEBUGF(String line, Object... args) {
	if(DEBUG) System.err.printf(line, args);
    }
    
    public void DEBUG(Object output) {
	if(DEBUG) System.err.println(output.toString());
    }

    public void DEBUGF(String line, Object... args) {
	if(DEBUG) System.err.printf(line, args);
    }

    public void DEBUG(int output) {
	if(DEBUG) System.err.println(output);
    }
}
