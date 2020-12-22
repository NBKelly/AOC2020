package com.nbkelly.advent;
import com.nbkelly.helper.DebugLogger;
import com.nbkelly.helper.ConceptHelper;
import java.util.Scanner;

import java.util.*;
//import java.uti


public class Advent22 extends ConceptHelper {
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
	ArrayList<String> res = new ArrayList<>();
	while(hasNextLine())
	    res.add(nextLine());

	return res;
    }

    public class State {
	Deck p1;
	Deck p2;

	int victory_status = 0;
	
	public boolean player_victory() {
	    return p2.size() == 0;
	}
	
	public State(Deck p1, Deck p2) {
	    this.p1 = new Deck();
	    this.p1.cards.addAll(p1.cards);

	    this.p2 = new Deck();
	    this.p2.cards.addAll(p2.cards);
	}

	private State() {
	    p1 = new Deck();
	    p2 = new Deck();
	}

	public boolean equals(State s) {
	    return p1.equals(s.p1) && p2.equals(s.p2);
	}

	public State subgame(int p1_size, int p2_size) {
	    State s2 = new State();

	    var iter = p1.cards.iterator();
	    for(int i = 0; i < p1_size; i++) {
		//println(i);
		s2.p1.cards.add(iter.next());
	    }
	    
	    var iter2 = p2.cards.iterator();
	    for(int i = 0; i < p2_size; i++)
		s2.p2.cards.add(iter2.next());
	    
	    return s2;
	}

	public void printGame() {
	    String s = "P1: { ";
	    for(int i : p1.cards)
		s += i + " ";
	    s += "}, P2: {";
	    for(int i : p2.cards)
		s += i + " ";
	    s += "}";
	    println(s);
	}
    }
    
    public class Deck {
	ArrayDeque<Integer> cards;
	public Deck() {
	    cards = new ArrayDeque<>();
	}

	public void addCard(int card) {
	    cards.add(card);
	}

	public int take() {
	    return cards.pop();
	}

	public int size() {
	    return cards.size();
	}

	public boolean equals(Deck d) {
	    if(d.size() != size())
		return false;

	    var iter_a = cards.iterator();
	    var iter_b = d.cards.iterator();

	    while(iter_a.hasNext() && iter_b.hasNext()) {
		if(iter_a.next() != iter_b.next())
		    return false;
	    }
	    
	    return true;
	}

	public String toString() {
	    if(cards.size() == 0)
		return "";

	    boolean first = true;
	    String res = "";
	    for(int i : cards) {
		if(first) {
		    first = false;
		    res += i + "";		    
		}
		else {
		    res += ", " + i;
		}
	    }

	    return res;
	}
    }
    
    //this is basically your main method
    public void solveProblem() throws Exception {
	Timer t = new Timer().start();

	var input = readInput();

	Deck p1 = new Deck();
	Deck p2 = new Deck();

	int player = 1;
	for(String s : input) {
	    if(s.equals("Player 1:")) {
	    }
	    else if (s.equals("Player 2:"))
		player++;
	    else if (player == 1)
		p1.addCard(Integer.parseInt(s));
	    else
		p2.addCard(Integer.parseInt(s));
	}

	ArrayList<State> states = new ArrayList<State>();
	
	int rc = 0;
	
	State game = new State(p1, p2);

	//game.printGame();
	
	//println("t1");
	game = play_game(game, GAME_NO);
	//game.printGame();
	//println("t2");
	Deck winner = game.p1;
	if(game.p2.size() > 0)
	    winner = game.p2;
	
	int sum = 0;
	int mult = 1;
	var iter = winner.cards.descendingIterator();
	while(iter.hasNext()) {
	    sum += (iter.next() * mult++);
	}

	DEBUGF("PART TWO: ");
	println(sum);
	
        //println("Hello, world!");

	t.total("Finished processing of file. ");
    }


    private static int GAME_NO = 1;
    public State play_game(State starting_state, int level) {
	//play a set number of rounds
	ArrayList<State> history = new ArrayList<State>();
	State current = new State(starting_state.p1, starting_state.p2);
	
	DEBUG(color("=== Game " +level + " ===", Color.GREEN));
	DEBUG();
	
	int round = 0;
	while(current.p1.size() > 0 & current.p2.size()  > 0) {
	    round++;
	    if(DEBUG) {
		DEBUG(color("-- Round " + round + " (Game " + level + ") --", Color.GREEN));
		DEBUG("Player 1's deck: " + current.p1.toString());
		DEBUG("Player 2's deck: " + current.p2.toString());		
	    }
	    if(containsState(history, current)) {
		current.victory_status = WIN;
		DEBUG("State has already been seen this game");
		DEBUG("The winner of game " + level + " is player 1!");		
		return current; //the player wins
	    }

	    //println("GAME " + level + " ROUND " + round++ );
	    //println("CARDS LEFT: (" + current.p1.size() + ", " + current.p2.size() + ")");
	    history.add(new State(current.p1, current.p2));

	    //rounds cards must be a new config, draw cards
	    int p1_card = current.p1.take();
	    int p2_card = current.p2.take();
	    //println("P1: " + p1_card + ", P2: " + p2_card);
	    DEBUGF("Player 1 plays: %d%n", p1_card);
	    DEBUGF("Player 2 plays: %d%n", p2_card);
	    
	    if(p1_card <= current.p1.size() &&
	       p2_card <= current.p2.size()) {
		//play a new game
		DEBUG("Playing a sub-game to determine the winner...");
		DEBUG();
		
		State newGame = play_game(current.subgame(p1_card, p2_card), ++GAME_NO);
		DEBUG(color("...anyway, back to game " + level, Color.GREEN));
		if(newGame.victory_status == WIN) {
		    current.p1.addCard(p1_card);
		    current.p1.addCard(p2_card);
		    DEBUG("Player 1 wins round " + round + " of game " + level + "!");
		    DEBUG();
		}
		else {
		    current.p2.addCard(p2_card);
		    current.p2.addCard(p1_card);
		    DEBUG("Player 2 wins round " + round + " of game " + level + "!");
		    DEBUG();
		}
	    }
	    else {
		//round winner has the highest card
		if(p1_card > p2_card) {
		    current.p1.addCard(p1_card);
		    current.p1.addCard(p2_card);
		    DEBUG("Player 1 wins round " + round + " of game " + level + "!");
		    DEBUG();
		}
		else {
		    current.p2.addCard(p2_card);
		    current.p2.addCard(p1_card);
		    DEBUG("Player 2 wins round " + round + " of game " + level + "!");
		    DEBUG();
		}
	    }
	}

	if(current.p2.size() == 0) {
	    current.victory_status = WIN;
	}

	return current;
	    

    }

    int WIN = 1;
    
    /*public State play_game_2(State starting_state) {
	//the game starts with two decks of cards (we'll make copies of these)
	State thisGame = new State(starting_state.p1, starting_state.p2);

	//each game has a history
	ArrayList<State> history = new ArrayList<State>();

	while(thisGame.p1.size() > 0 && thisGame.p2.size() >= 0) {	    
	    int match_res = play_round(history, thisGame);
	    if(match_res == WIN) {
		thisGame.victory_status = WIN;
		return thisGame;
	    }
	    else if (match_res == WIN_ROUND) {
		//move cards to p1
		State newState = new State(thisGame.p1, thisGame.p2);
		newState.p1.addCard(newState.p1.take());
		newState.p1.addCard(newState.p2.take());
		thisGame = newState;
	    }
	    else {
		//crab won round
		State newState = new State(thisGame.p1, thisGame.p2);
		newState.p2.addCard(newState.p2.take());
		newState.p2.addCard(newState.p1.take());
		thisGame = newState;
	    }

	    history.add(new State(thisGame.p1, thisGame.p2));
	}

	return thisGame;
    }

    int WIN = 1;
    int WIN_ROUND = 2;
    int LOSE_ROUND = 0;
    public int play_round(ArrayList<State> history, State thisRound) {
	//if the current state exists in our history, then we return true (player wins)
	if(containsState(history, thisRound))
	    return WIN;

	//new config: the players start the round by each drawing a new card
	int p1_card = thisRound.p1.take();
	int p2_card = thisRound.p2.take();

	//if both players have as many cards (or more) as thier face, it's a draw
	if(p1_card <= thisRound.p1.size()
	   && p2_card <= thisRound.p2.size()) {
	    //the winner is determined by playing a new game with the current decks
	    State res = play_game(thisRound.subgame(p1_card, p2_card));
	    if(res.victory_status == 1) {		
		thisRound.p1.addCard(p1_card);
		thisRound.p1.addCard(p2_card);
		return WIN_ROUND;
	    }
	    else {
		thisRound.p2.addCard(p2_card);
		thisRound.p2.addCard(p1_card);
		return LOSE_ROUND;
	    }
	}

	//otherwise, at least one player has not enough cards. The winner has the highest value card
	if(p1_card > p2_card) {
	    thisRound.p1.addCard(p1_card);
	    thisRound.p1.addCard(p2_card);
	    return WIN_ROUND;
	}
	else {
	    thisRound.p2.addCard(p2_card);
	    thisRound.p2.addCard(p1_card);
	    return LOSE_ROUND;
	}	
	//	return p1_card > p2_card;
	}*/

    public boolean containsState(ArrayList<State> states, State state) {
	for(var s : states)
	    if(s.equals(state))
		return true;
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
    
    public Advent22() {
	super();
    }
    
    public static void main(String[] argv) {
	new Advent22().process(argv);
    }

    
}
