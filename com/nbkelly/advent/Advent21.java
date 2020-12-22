package com.nbkelly.advent;
import com.nbkelly.helper.DebugLogger;
import com.nbkelly.helper.ConceptHelper;
import java.util.Scanner;

import java.util.*;
import java.util.Collections;

public class Advent21 extends ConceptHelper {
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
	var res = new ArrayList<String>();
	while(hasNextLine())
	    res.add(nextLine());	
	
	return res;
    }    

    static int _ID = 0;
    
    private class Food implements Comparable<Food> {	
	public int id = ++_ID;
	
	ArrayList<String> ingredients;
	ArrayList<String> allergens;

	public int compareTo(Food f) {
	    return id - f.id;
	}
	
	public Food(String line) {
	    ingredients = new ArrayList<String>();
	    allergens = new ArrayList<String>();

	    String[] tmp_1 = line.split(" \\(");
	    println(tmp_1[0]);

	    String[] _ingredients = tmp_1[0].split(" ");
	    for(int i = 0; i < _ingredients.length; i++)
		ingredients.add(_ingredients[i]);

	    String[] _allergens = tmp_1[1].substring(0, tmp_1[1].length() - 1).split(", ");	    
	    for(int i = 0; i < _allergens.length; i++) {
		String str = _allergens[i];
		str = str.replaceAll("contains ", "");
		allergens.add(str);
	    }
	}
    }
    
    //this is basically your main method
    public void solveProblem() throws Exception {
	Timer t = new Timer().start();

	var input = readInput();

	ArrayList<Food> foods = new ArrayList<Food>();
	
	for(String s : input) {
	    Food f = new Food(s);
	    foods.add(f);
	}

	//create a mapping of allergen -> foods that contain it
	TreeMap<String, ArrayList<Food>> containers = new TreeMap<String, ArrayList<Food>>();

	for(Food f : foods) {
	    for(String allergen : f.allergens) {
		if(!containers.containsKey(allergen))
		    containers.put(allergen, new ArrayList<Food>());				   
		containers.get(allergen).add(f);
	    }
	}

	//now we should have a list of allergens
	Set<String> allergens = containers.keySet();

	TreeMap<String, TreeSet<String>> potentials = new TreeMap<>();

	for(String s : allergens) {
	    TreeSet<String> possibleIngredients = new TreeSet<String>();
	    var foods_containing = containers.get(s);

	    possibleIngredients.addAll(foods_containing.get(0).ingredients);

	    for(int i = 1; i < foods_containing.size(); i++) {
		TreeSet<String> thisFood = new TreeSet<String>(foods_containing.get(i).ingredients);
		possibleIngredients.retainAll(thisFood);
	    }

	    potentials.put(s, possibleIngredients);
	}

	for(String s : allergens) {
	    printf("%s : { ", s);
	    for(String pot: potentials.get(s)) {
		printf("%s ", pot);
	    }
	    println("}");
	}

	TreeMap<String, String> solved = new TreeMap<String, String>();

	String singleton = "";
	while((singleton = contains_singleton(potentials)) != null) {
	    var single_res = potentials.get(singleton).first();

	    solved.put(singleton, single_res);

	    potentials.remove(singleton);

	    for(var remaining : potentials.keySet()) {
		potentials.get(remaining).remove(single_res);
	    }
	}

	ArrayList<Ingr> sorted = new ArrayList<>();
	
	for(String s : solved.keySet()) {
	    println("SOLVED: " + s + " . " + solved.get(s));
	    sorted.add(new Ingr(solved.get(s), s));
	}

	for(String s: potentials.keySet()) {
	    println("UNSOLVED: " + s);
	}

	int ct_safe = 0;
	int total_ingredients = 0;
	for(Food f : foods) {	    
	    for(String s : f.ingredients) {
		total_ingredients++;
		if(solved.values().contains(s))
		    ct_safe++;
	    }
	}

	Collections.sort(sorted);

	String res = "";
	for(var i : sorted) {
	    res = res + i + ",";
	}
	res = res.substring(0,res.length()-1);
	println(res);

	println(total_ingredients - ct_safe);

	
	t.total("Finished processing of file. ");
    }

    private class Ingr implements Comparable<Ingr>{
	public int compareTo(Ingr i) {
	    //may be backwards?
	    return i.allergen.compareTo(allergen);
	}
	
	private String ingredient;
	private String allergen;
	public Ingr(String ingredient, String allergen) {
	    this.ingredient = ingredient;
	    this.allergen = allergen;
	}
	public String toString() {
	    return ingredient;
	}	    
    }

    public String contains_singleton(TreeMap<String, TreeSet<String>> potentials) {
	for(String s : potentials.keySet()) {
	    if(potentials.get(s).size() == 1) {
		return s;
	    }
	}

	return null;
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
    
    public Advent21() {
	super();
    }
    
    public static void main(String[] argv) {
	new Advent21().process(argv);
    }

    
}
