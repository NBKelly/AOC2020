package com.nbkelly.advent;
import com.nbkelly.helper.DebugLogger;
import com.nbkelly.helper.ConceptHelper;
import java.util.Scanner;
import java.util.Collections;

import java.util.*;

public class Advent20 extends ConceptHelper {
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
    
    //this is basically your main method
    public void solveProblem() throws Exception {
	Timer t = new Timer().start();

	var input = readInput();
	var tiles = readTiles(input);
	TreeMap<String, ArrayList<Tile>>  matches = new TreeMap<String, ArrayList<Tile>>();

	for(Tile tile : tiles) {
	    var edges = tile.edges();
	    for(String s : edges) {
		if(matches.containsKey(s))
		    matches.get(s).add(tile);
		else {
		    ArrayList<Tile> tmp = new ArrayList<>();
		    tmp.add(tile);
		    matches.put(s, tmp);
		}		
	    }		
	}

	ArrayList<Tile> candidates = new ArrayList<Tile>();
	ArrayList<Tile> sides = new ArrayList<Tile>();
	ArrayList<Tile> insides = new ArrayList<>();
	
	println();
		
	for(Tile tile : tiles) {
	    //decide if it only fits two tiles
	    var edges = tile.edges();
	    int ct = 0;
	    for(String edge : edges) {
		//println(edge);
		//ideal: ct = 4 + 8 = 12
		int size = matches.get(edge).size(); //each match should contain itself, + 1 (?) other edge
		ct += size;
	    }
	    
	    if(ct == 12)
		candidates.add(tile);
	    else if (ct == 14)
		sides.add(tile);
	    else if (ct == 16)
		insides.add(tile);
	    else
		println("huh?");
	}

	println("CORNERS: " + candidates.size());
	println("EDGES: " + sides.size());
	println("INSIDES: " + insides.size());
	long mult = 1;
	for(Tile tile : candidates)
	    mult *= tile.ID;

	println(mult);
	
        println("Hello, world!");
	var stitched = stitch(tiles, candidates, sides, matches);
	
	var oriented = orientedTiles(stitched, matches);
	
	
	
	var assembled = stitched_image(oriented);

	var inserted = findImage(assembled, dragonImage());

	printlist(inserted);
	
	println("ROUGHNESS: " + count_char(inserted));

	t.total("Finished processing of file. ");
    }

    public int count_char(ArrayList<String> image) {
	int ct = 0;
	for(int y = 0; y < image.size(); y++) {
	    String l = image.get(y);
	    for(int x = 0; x < image.get(0).length(); x++) {
		if(l.charAt(x) == '#')
		    ct++;
	    }
	}

	return ct;
    }
    
    public ArrayList<String> dragonImage() {
	ArrayList<String> res = new ArrayList<String>();
	res.add("                  # ");
	res.add("#    ##    ##    ###");
	res.add(" #  #  #  #  #  #   ");
	return res;
    }
    
    public ArrayList<String> findImage(ArrayList<String> map, ArrayList<String> image) {
	//get the indices of all the hashes in the image
	//get the indices to satisfy
	ArrayList<Point> seaPoints = new ArrayList<>();
	for(int y = 0; y < image.size(); y++) {
	    String l = image.get(y);
	    for(int x = 0; x < l.length(); x++)
		if(l.charAt(x) == '#')
		    seaPoints.add(new Point(x, y));
	}

	int image_width = image.get(0).length();
	int image_height = 3;

	int map_size = map.size();
	//int rot = 0;
	boolean xflip = false;

	ArrayList<Point> locs = new ArrayList<Point>();


	
	for(int flips = 0; flips < 4; flips++) {
	    //see how many monsters appear
	    xflip = (flips %2 == 1);
	    boolean yflip = flips >= 2;
		
	    for(int rot = 0; rot < 4; rot++) {
		int num_monsters = 0;
		var map_rotated = rotate(map, rot);
		if(xflip)
		    map_rotated = flip_x(map_rotated);

		if(yflip)
		    map_rotated = flip_y(map_rotated);
		
		for(int y = 0; y < map_size - image_height; y++)
		    for(int x = 0; x < map_size - image_width; x++) {
			if(imageExists(map_rotated, x, y, seaPoints)) {
			    num_monsters++;
			    printf("Monster at (%d, %d)%n", x, y);
			    locs.add(new Point(x, y));
			}
		    }
		
		if(num_monsters > 0) {
		    ArrayList<String> res = new ArrayList<>(map_rotated);
		    for(var loc : locs) {
			insertImage(res, loc.x, loc.y, seaPoints);
		    }

		    println("NUM DRAGONS: " + num_monsters);
		    return res;
		}
	    }	    		
	}

	return null;
    }

    private boolean imageExists(ArrayList<String> map, int x, int y, ArrayList<Point> image) {
	for(Point p : image)
	    if(map.get(y + p.y).charAt(x + p.x) != '#')
		return false;

	return true;
    }

    private void insertImage(ArrayList<String> map, int x, int y, ArrayList<Point> image) {
	for(Point p : image) {
	    String res = map.get(y + p.y);
	    res = res.substring(0, x + p.x) + "O" + res.substring(x + 1 + p.x);
	    //if(map.get(y + p.y).charAt(x + p.x) != '#')
	    map.set(y + p.y, res);
	}
    }

    private class Point { int x; int y;	public Point(int x, int y) { this.x = x; this.y = y; }    }
    
    public ArrayList<String> stitched_image(ArrayList<ArrayList<OrientedTile>> oriented) {
	ArrayList<String> res = new ArrayList<String>();

	for(int i = 0; i < oriented.size(); i++) {
	    var line = x_stitched(oriented.get(i));
	    printlist(line);

	    for(int j = 1; j < line.size() - 1; j++) {
		String s = line.get(j).replaceAll(".\\|.", "");
		res.add(s.substring(1, s.length()-1));
	    }
	}
	    
	printlist(res);
	
	return res;
    }

    public ArrayList<String> x_stitched(ArrayList<OrientedTile> oriented) {
	for(var v : oriented) {
	    //println();
	    //println("ROT = " + v.rot);
	    //println("XFLIP: " + v.X_FLIPPED + ", Y_FLIPPED: " + v.Y_FLIPPED);
	    
	    var grid = v.t.grid;

	    var grid2 = grid;

	    if(v.rot == 2)
		grid2 = rot2(grid2);
	    
	    if(v.Y_FLIPPED)
		grid2 = flip_x(grid2);
	    if(v.X_FLIPPED)
		grid2 = flip_y(grid2);

	    //grid2 = rotate(grid2, v.rot);	    
	    
	    //printlist2(grid, grid2);
	}
	//if(1 == 1)
	//    return null;
	
	//int height =
	ArrayList<String> res = new ArrayList<String>();
	
	var o1 = oriented.get(0);
	var g1 = o1.t.grid;
	int r1 = o1.rot;

	if(r1 == 2)
	    g1 = rot2(g1);
		
	if(o1.X_FLIPPED)
	    g1 = flip_y(g1);
	if(o1.Y_FLIPPED)
	    g1 = flip_x(g1);

//rotate(g1, r1);
	
	res.addAll(g1);

	for(int x = 1; x < oriented.size(); x++) {
	    var on = oriented.get(x);
	    var gn = on.t.grid;
	    int rn = on.rot;

	    if(rn == 2)
		gn = rot2(gn);//rotate(gn, rn);
	    
	    if(on.X_FLIPPED)
		gn = flip_y(gn);
	    if(on.Y_FLIPPED)
		gn = flip_x(gn);
	    

	    for(int y = 0; y < res.size(); y++)
		res.set(y, res.get(y) + "|" + gn.get(y));
	}
	
	return res;
	
    }

    public void printlist(ArrayList<String> li) {
	for(String s : li)
	    println(s);
	println();
    }

    public void printlist2(ArrayList<String> li, ArrayList<String> li2) {
	for(int i = 0; i < li.size(); i++)
	    println(li.get(i) + "|" + li2.get(i));
	println();
    }

    public ArrayList<String> flip_y(ArrayList<String> input) {
	ArrayList<String> res = new ArrayList<String>();
	for(int i = input.size() - 1; i >= 0; i--)
	    res.add(input.get(i));
	return res;
    }
    
    public ArrayList<String> flip_x(ArrayList<String> input) {
	ArrayList<String> res = new ArrayList<String>();
	for(String s : input)
	    res.add(new StringBuilder(s).reverse().toString());
	return res;
    }

    public ArrayList<String> rot2(ArrayList<String> input) {
	input = rotate(input, 1);
	input = flip_y(input);
	//input = flip_y(input);
	//input = flip_x(input);
	return input;
    }
    
    public ArrayList<String> rotate(ArrayList<String> input, int n) {
	if(n > 1)
	    input = rotate(input, n-1);

	if(n == 0)
	    return input;

	int height = input.size();
	char[][] matrix = new char[height][height];
	for(int x = 0; x < height; x++)
	    for(int y = 0; y < height; y++)
		matrix[x][y] = input.get(y).charAt(x);

	char[][] matrix2 = new char[height][height];

	for(int i = 0; i < height; i++) {
	    for(int j = 0; j < height; j++) {
		matrix2[i][j] = matrix[height - j - 1][ i];
	    }
	}    

	ArrayList<String> res = new ArrayList<String>();
	for(int y = 0; y < height; y++) {
	    String s = "";
	    for(int x = 0; x < height; x++)
		s = s + matrix2[x][y];
	    res.add(s);
	}

	return res;
    }
    
    public ArrayList<ArrayList<Tile>> stitch(TreeSet<Tile> tiles, ArrayList<Tile> corners,
					     ArrayList<Tile> sides, TreeMap<String, ArrayList<Tile>> matches) {
	Tile topLeft = corners.remove(0);

	//the grid is 12 x 12
	int grid_size = 12;
	ArrayList<Tile> toprow = new ArrayList<Tile>();

	Tile last = topLeft;
	toprow.add(last);

	assemble:
	while(toprow.size() < 11) {
	    //find something in edges which matches last
	    for(Tile t : sides) {
		//does the tile have an edge which matches last
		var _edge_tile = t.edges();
		var _edge_curr = last.edges();
		if(matches(_edge_tile, _edge_curr)) {
		    last = t;
		    sides.remove(t);
		    toprow.add(t);
		    continue assemble;
		}
	    }

	    break;
	}

	//find the corner which matches the top row
	for(Tile t : corners) {
	    if(matches(t, last)) {
		toprow.add(t);
		corners.remove(t);
		break;
	    }
	}

	for(Tile t : toprow) {
	    printf(" %d ", t.ID);
	}

	println();

	TreeSet<Tile> ctiles = new TreeSet<Tile>(tiles);
	ctiles.removeAll(toprow);

	ArrayList<ArrayList<Tile>> res = new ArrayList<ArrayList<Tile>>();
	res.add(toprow);

	ArrayList<Tile> lastrow = toprow;
	for(int i = 1; i < 12; i++) {
	    ArrayList<Tile> currentRow = new ArrayList<Tile>();
	    for(int x = 0; x < 12; x++) {
		//find a tile which matches the one above
		last = lastrow.get(x);

		match:
		for(Tile t : ctiles)
		    if(matches(t, last)) {
			currentRow.add(t);
			ctiles.remove(t);
			last = t;
			break match;
		    }	    
	    }

	    res.add(currentRow);
	    lastrow = currentRow;
	}
	println();

	//Collections.reverse(res);
	
	for(var row : res) {
	    for(Tile t : row)
		printf(" %d ", t.ID);
	    println();
	}
	
	return res;	
    }

    public boolean matches(Tile t, Tile last) {
	var _edge_tile = t.edges();
	var _edge_curr = last.edges();

	return matches(_edge_tile, _edge_curr);
    }

    public boolean matches(String s, Tile last) {
	return last.edges().contains(s);	
    }
    public boolean matches(String s, String t) {
	return s.equals(t);
    }
    
    public boolean matches(ArrayList<String> a, ArrayList<String> b) {
	var _a = new TreeSet<String>(a);//TreeSet<String>
	var _b = new TreeSet<String>(b);//TreeSet<String>

	_a.retainAll(_b);
	return (_a.size() > 0);
    }

    public TreeSet<Tile> readTiles(ArrayList<String> input) {
	TreeSet<Tile> res = new TreeSet<Tile>();

	int tile_size = 10;
	int i = 0;
	while(i < input.size()) {
	    int id = Integer.parseInt(input.get(i).split(" |:")[1]);
	    println(id);

	    ArrayList<String> lines = new ArrayList<>();
	    for(int j = 0; j < 10; j++)
		lines.add(input.get(j + i + 1));

	    i += 12;

	    res.add(new Tile(id, lines));
	}

	return res;//return null;
    }

    final static int TOP = 0;
    final static int RIGHT = 1;
    final static int BOT = 2;
    final static int LEFT = 3;

    //public OrientedTile topRow

    public ArrayList<ArrayList<OrientedTile>> orientedTiles(ArrayList<ArrayList<Tile>> stitched,
						       TreeMap<String, ArrayList<Tile>>  matches) {
	
	ArrayList<ArrayList<OrientedTile>> res = new ArrayList<>();
	ArrayList<OrientedTile> lastrow = new ArrayList<>();
	OrientedTile last = null;
	for(int y = 0; y < 12; y++) {	    
	    ArrayList<OrientedTile> currentLine = new ArrayList<>();
	    for(int x = 0; x < 12; x++) {
		printf("ORIENTING TILE (x,y) = (%d, %d)%n", x, y);
		if(y == 0) {
		    if(x == 0) {
			last = topLeft(stitched.get(y).get(x),   //target, right, bot, matches
				       stitched.get(y).get(x+1), //right
				       stitched.get(y+1).get(x), //below
				       matches);
		    }
		    else if (x < 11) {
			last = topRow(stitched.get(y).get(x),    //target
				      last,                      //left
				      stitched.get(y).get(x+1),  //right				   
				      stitched.get(y+1).get(x),  //below
				      matches);
		    }
		    else { //x = 12
			last = topRight(stitched.get(y).get(x),   //this
					last,                     //left
					stitched.get(y+1).get(x), //below
					matches);			
		    }
		}
		else if (y < 11) {
		    if(x == 0) {
			last = stdLeft(stitched.get(y).get(x),    //this
				       lastrow.get(x),            //above
				       stitched.get(y).get(x+1),  //right
				       stitched.get(y+1).get(x),  //below
				       matches);
		    }
		    else if (x < 11) {
			last = stdMid(stitched.get(y).get(x),   //this
				      last,                     //left
				      lastrow.get(x),           //above
				      stitched.get(y).get(x+1), //right
				      stitched.get(y+1).get(x), //below
				      matches);
		    }
		    else {// (x == 12) {
			last = stdRight(stitched.get(y).get(x),   //this
					last,                     //left
					lastrow.get(x),           //above
					stitched.get(y+1).get(x), //below
					matches);
		    }
		}
		else { //y == 12
		    if(x == 0) {
			last = botLeft(stitched.get(y).get(x),   //this
				       lastrow.get(x),           //above
				       stitched.get(y).get(x+1), //right
				       matches);
		    }
		    else if (x < 11) {
			last = botMid(stitched.get(y).get(x),   //this
				      last,                     //left
				      lastrow.get(x),           //above
				      stitched.get(y).get(x+1), //right
				      matches);
		    }
		    else {//if (x == 12)
			last = botRight(stitched.get(y).get(x), //this
					last,                   //left
					lastrow.get(x),         //above					
					matches);
		    }
		}

		currentLine.add(last);		
	    }

	    res.add(currentLine);
	    lastrow = currentLine;
	}
	
	return res;
    }



    private interface Matcher {
	public boolean match(ArrayList<String> values);
    }

    boolean[] flips = new boolean[]{false, false,
					true, false,
					false, true,
					true, true};
    
    public OrientedTile botMid(Tile target, OrientedTile left, OrientedTile top,
			       Tile right,
			       TreeMap<String, ArrayList<Tile>>  matches) {
	println(target.ID);
	
	Matcher m = new Matcher() {
		public boolean match(ArrayList<String> vals) {
		    return matches(vals.get(TOP), top.getEdge(BOT)) //top matches with bot edge
			&& matches(vals.get(RIGHT), right) //right matches with right tile
			&& matches.get(vals.get(BOT)).size() == 1 //no tiles beneath
			&& matches(vals.get(LEFT), left.getEdge(RIGHT));		
		}
	    };

	for(int flip = 0; flip < 4; flip++) {
	    boolean flip_x = flips[2*flip];
	    boolean flip_y = flips[2*flip + 1];
	    for(int rot = 0; rot < 4; rot++) {
		var vals = target.edges_oriented(rot, flip_x, flip_y);
		if(m.match(vals)) {
		    printf("BOT_MID at rotation %d xlip=%b yflip=%b", rot, flip_x, flip_y);
		    return new OrientedTile(target, rot, flip_x, flip_y, vals);
		}
	    }
	}

	println("help BOT_MID!");

	return null;
    }

    public OrientedTile botRight(Tile target, OrientedTile left, OrientedTile top,
				 TreeMap<String, ArrayList<Tile>>  matches) {
	println(target.ID);

	Matcher m = new Matcher() {
		public boolean match(ArrayList<String> vals) {
		    return matches(vals.get(TOP), top.getEdge(BOT)) //top matches with bot edge
			&& matches.get(vals.get(RIGHT)).size() == 1 //no tiles right
			&& matches.get(vals.get(BOT)).size() == 1 //no tiles beneath
			&& matches(vals.get(LEFT), left.getEdge(RIGHT));		
		}
	    };

	for(int flip = 0; flip < 4; flip++) {
	    boolean flip_x = flips[2*flip];
	    boolean flip_y = flips[2*flip + 1];
	    for(int rot = 0; rot < 4; rot++) {
		var vals = target.edges_oriented(rot, flip_x, flip_y);
		if(m.match(vals)) {
		    printf("BOT_RIGHT at rotation %d xflip=%b yflip=%b%n", rot, flip_x, flip_y);
		    return new OrientedTile(target, rot, flip_x, flip_y, vals);
		}
	    }
	}

	println("help BOT_RIGHT!");

	return null;
    }
    
    public OrientedTile botLeft(Tile target, OrientedTile top, Tile right,
				TreeMap<String, ArrayList<Tile>>  matches) {
	println(target.ID);

	Matcher m = new Matcher() {
		public boolean match(ArrayList<String> vals) {
		    return matches(vals.get(TOP), top.getEdge(BOT)) //top matches with bot edge
			&& matches.get(vals.get(LEFT)).size() == 1 //no tiles left
			&& matches.get(vals.get(BOT)).size() == 1 //no tiles beneath
			&& matches(vals.get(RIGHT),right);
		}
	    };

	for(int flip = 0; flip < 4; flip++) {
	    boolean flip_x = flips[2*flip];
	    boolean flip_y = flips[2*flip + 1];
	    for(int rot = 0; rot < 4; rot++) {
		var vals = target.edges_oriented(rot, flip_x, flip_y);
		if(m.match(vals)) {
		    printf("BOT_LEFT at rotation %d xflip=%b yflip=%b%n", rot, flip_x, flip_y);
		    return new OrientedTile(target, rot, flip_x, flip_y, vals);
		}
	    }
	}

	println("help BOT_LEFT!");

	return null;
    }





    
    public OrientedTile stdMid(Tile target, OrientedTile left, OrientedTile top,
			       Tile right, Tile bot,
			       TreeMap<String, ArrayList<Tile>>  matches) {
	println(target.ID);

	Matcher m = new Matcher() {
		public boolean match(ArrayList<String> e) {
		    return matches(e.get(LEFT), left.getEdge(RIGHT)) //left matches with tile left
			&& matches(e.get(TOP), top.getEdge(BOT))     //top matches with tile above
			&& matches(e.get(RIGHT), right)              //matches right
			&& matches(e.get(BOT), bot);                 //matches beneath
		}
	    };

	for(int flip = 0; flip < 4; flip++) {
	    boolean flip_x = flips[2*flip];
	    boolean flip_y = flips[2*flip + 1];
	    for(int rot = 0; rot < 4; rot++) {
		var vals = target.edges_oriented(rot, flip_x, flip_y);
		if(m.match(vals)) {
		    printf("STD_MID at rotation %d xflip=%b yflip=%b%n", rot, flip_x, flip_y);
		    return new OrientedTile(target, rot, flip_x, flip_y, vals);
		}
	    }
	}

	println("STD_MID help!");

	return null;
    }

    public OrientedTile stdRight(Tile target, OrientedTile left, OrientedTile top,
			       Tile bot,
			       TreeMap<String, ArrayList<Tile>>  matches) {
	println(target.ID);

	Matcher m = new Matcher() {
		public boolean match(ArrayList<String> e) {
		    return matches(e.get(LEFT), left.getEdge(RIGHT)) //left matches with tile left
			&& matches(e.get(TOP), top.getEdge(BOT))     //top matches with tile above
			&& matches.get(e.get(RIGHT)).size() == 1     //no entry right
			&& matches(e.get(BOT), bot);                 //matches beneath
		}
	    };

	for(int flip = 0; flip < 4; flip++) {
	    boolean flip_x = flips[2*flip];
	    boolean flip_y = flips[2*flip + 1];
	    for(int rot = 0; rot < 4; rot++) {
		var vals = target.edges_oriented(rot, flip_x, flip_y);
		if(m.match(vals)) {
		    printf("STD_RIGHT at rotation %d xflip=%b yflip=%b%n", rot, flip_x, flip_y);
		    return new OrientedTile(target, rot, flip_x, flip_y, vals);
		}
	    }
	}

	println("STD_RIGHT help!");

	return null;
    }
    
    public OrientedTile stdLeft(Tile target, OrientedTile top, Tile right, Tile bot,
			       TreeMap<String, ArrayList<Tile>>  matches) {
	println(target.ID);

	Matcher m = new Matcher() {
		public boolean match(ArrayList<String> e) {
		    return matches.get(e.get(LEFT)).size() == 1  //1 on left
			&& matches(e.get(TOP), top.getEdge(BOT)) //top matches with tile above
			&& matches(e.get(RIGHT), right)          //matches right
			&& matches(e.get(BOT), bot);             //matches beneath
		}
	    };

	for(int flip = 0; flip < 4; flip++) {
	    boolean flip_x = flips[2*flip];
	    boolean flip_y = flips[2*flip + 1];
	    for(int rot = 0; rot < 4; rot++) {
		var vals = target.edges_oriented(rot, flip_x, flip_y);
		if(m.match(vals)) {
		    printf("STD_LEFT at rotation %d xflip=%b yflip=%b%n", rot, flip_x, flip_y);
		    return new OrientedTile(target, rot, flip_x, flip_y, vals);
		}
	    }
	}

	println("STD_LEFT help!");

	return null;
    }
    
    public OrientedTile topRight(Tile target, OrientedTile left, Tile bot,
			       TreeMap<String, ArrayList<Tile>>  matches) {
	println(target.ID);
	Matcher m = new Matcher() {
		public boolean match(ArrayList<String> e) {
		    return matches(left.getEdge(RIGHT), e.get(LEFT)) //matches on the left edge
			&& matches.get(e.get(TOP)).size() == 1       //only top edge
			&& matches.get(e.get(RIGHT)).size() == 1     //only right edge
			&& matches(e.get(BOT), bot);                 //matches beneath
		}
	    };

	for(int flip = 0; flip < 4; flip++) {
	    boolean flip_x = flips[2*flip];
	    boolean flip_y = flips[2*flip + 1];
	    for(int rot = 0; rot < 4; rot++) {
		var vals = target.edges_oriented(rot, flip_x, flip_y);
		if(m.match(vals)) {
		    printf("TOP_RIGHT at rotation %d fxlip=%b yflip=%b%n", rot, flip_x, flip_y);
		    return new OrientedTile(target, rot, flip_x, flip_y, vals);
		}
	    }
	}
		
	println("help TOP_RIGHT!");

	return null;
    }
    
    public OrientedTile topRow(Tile target, OrientedTile left, Tile right, Tile bot,
			       TreeMap<String, ArrayList<Tile>>  matches) {
	println(target.ID);
	Matcher m = new Matcher() {
		public boolean match(ArrayList<String> e) {
		    return matches(left.getEdge(RIGHT), e.get(LEFT)) //matches on the left edge
			&& matches.get(e.get(TOP)).size() == 1  //only top right edge
			&& matches(e.get(RIGHT), right)         //matches on the right
			&& matches(e.get(BOT), bot);           //matches beneath
		}
	    };

	for(int flip = 0; flip < 4; flip++) {
	    boolean flip_x = flips[2*flip];
	    boolean flip_y = flips[2*flip + 1];
	    for(int rot = 0; rot < 4; rot++) {
		var vals = target.edges_oriented(rot, flip_x, flip_y);
		if(m.match(vals)) {
		    printf("TOP_MID at rotation %d fxlip=%b yflip=%b%n", rot, flip_x, flip_y);
		    return new OrientedTile(target, rot, flip_x, flip_y, vals);
		}
	    }
	}

	println("help TOP_ROW!");

	return null;
    }

    public OrientedTile topLeft(Tile target, Tile right, Tile bot, TreeMap<String, ArrayList<Tile>>  matches) {
	println(target.ID);

	Matcher m = new Matcher() {
		public boolean match(ArrayList<String> e) {
		    return matches.get(e.get(LEFT)).size() == 1 //only one left edge
			&& matches.get(e.get(TOP)).size() == 1  //only top right edge
			&& matches(e.get(RIGHT), right)         //matches on the right
			&& matches(e.get(BOT), bot);           //matches beneath
		}
	    };

	for(int flip = 0; flip < 4; flip++) {
	    boolean flip_x = flips[2*flip];
	    boolean flip_y = flips[2*flip + 1];
	    for(int rot = 0; rot < 4; rot++) {
		var vals = target.edges_oriented(rot, flip_x, flip_y);
		if(m.match(vals)) {
		    printf("TOP_LEFT at rotation %d xflip=%b yflip=%b%n", rot, flip_x, flip_y);
		    return new OrientedTile(target, rot, flip_x, flip_y, vals);
		}
	    }
	}

	println("help TOP_LEFT!");

	return null;
    }

    
    public class OrientedTile {
	Tile t = null;
	int rot = 0;
	boolean X_FLIPPED = false;
	boolean Y_FLIPPED = false;
	ArrayList<String> edges = null;
	public OrientedTile(Tile t, int rot, boolean x, boolean y, ArrayList<String> edges) {
	    this.t = t;
	    this.rot = rot;
	    this.X_FLIPPED = x;
	    this.Y_FLIPPED = y;
	    this.edges = new ArrayList<String>(edges);

	    for(String s: this.edges) {
		//println("OT " + t.ID + ": " + s);
	    }
	}

	public String getEdge(int side) {
	    return edges.get(side);
	}

	public void printEdges() {
	    for(String s: edges) {
		println("OT " + t.ID + ": " + s);
	    }

	    
	    println("RIGHT (" + RIGHT + "): " + edges.get(RIGHT));
	}
    }

    public class Tile implements Comparable<Tile>{
	public int compareTo(Tile t) {
	    return this.ID - t.ID;
	}
	public int ID;
	ArrayList<String> grid;
	public Tile(int ID, ArrayList<String> grid) {
	    this.ID = ID;
	    this.grid = grid;
	}

	private void swap(ArrayList<String> edges, int a, int b) {
	    String _a = edges.get(a);
	    edges.set(a, edges.get(b));
	    edges.set(b, _a);
	}

	private String reverse(String s) {
	    return new StringBuilder(s).reverse().toString();
	}

	private void reverse(ArrayList<String> edges, int edge) {
	    edges.set(edge, reverse(edges.get(edge)));
	}

	
	public ArrayList<String> edges_oriented(int rot, boolean flip_x, boolean flip_y) {
	    var edges = edges();
	    
	    ArrayList<String> res = new ArrayList<String>();
	    
	    //top right bottom left
	    res.add(edges.get((0+rot)%4)); // 0 - >1
	    res.add(edges.get((3+rot)%4)); // 3 -> 4
	    res.add(edges.get((1+rot)%4)); // 1 -> 2
	    res.add(edges.get((2+rot)%4)); // 2 - >3

	    
	    
	    //res.add(edges.get((0))); // 0 - >1
	    //res.add(edges.get((3))); // 3 -> 4
	    //res.add(edges.get((1))); // 1 -> 2
	    //res.add(edges.get((2))); // 2 - >3
	    //Collections.rotate(res, rot);
	    
	    if(flip_y) {
		//the left and right are reversed
		//the top and bottom swap places
		swap(res, LEFT, RIGHT);
		reverse(res, TOP);
		reverse(res, BOT);		
	    }

	    if(flip_x) {
		//the left and right are reversed
		//the top and bottom swap places
		swap(res, TOP, BOT);
		reverse(res, LEFT);
		reverse(res, RIGHT);
	    }

	    //println("RES SIZE: " + res.size());
	    
	    return res;
	}
	
	//edges; all of the edges, + x-inverted, + y-inverted
	public ArrayList<String> edges() {
	    String edgeTOP = grid.get(0); //top
	    String edgeBOT = grid.get(grid.size() - 1); //bottom

	    String edgeLEFT = "";
	    String edgeRIGHT = "";
	    
	    for(int i = 0; i < grid.size(); i++) {
		edgeLEFT += grid.get(i).charAt(0); //left
		String tmp = grid.get(i);
		edgeRIGHT += tmp.charAt(tmp.length()-1); //right
	    }

	    String edge5 = new StringBuilder(edgeTOP).reverse().toString(); //left
	    String edge6 = new StringBuilder(edgeRIGHT).reverse().toString();; //right

	    String edge7 = new StringBuilder(edgeBOT).reverse().toString(); //top
	    String edge8 = new StringBuilder(edgeLEFT).reverse().toString(); //bottom

	    ArrayList<String> res = new ArrayList<String>();

	    //res.add(edgeTOP); //TOP
	    //res.add(edgeRIGHT); //RIGHT
	    //res.add(edgeBOT); //BOT
	    //res.add(edgeLEFT); //LEFT

	    res.add(edgeTOP); //TOP
	    res.add(edgeBOT); //BOT
	    res.add(edgeLEFT); //LEFT
	    res.add(edgeRIGHT); //RIGHT

	    //inverts
	    res.add(edge5);
	    res.add(edge6);
	    res.add(edge7);
	    res.add(edge8);

	    return res;
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
    
    public Advent20() {
	super();
    }
    
    public static void main(String[] argv) {
	new Advent20().process(argv);
    }

    
}
