# ADVENT OF CODE 2020
This is a set of all my solutions for Advent of Code 2020. They are (mostly) cleaned up, and are all produced in Java using my own workflow tools (with a few hacks thrown in).

## Project Structure
All of the solutions are available in the ```com/nbkelly/advent``` folder. They can all be run using ```run.sh``` script like so:

1. Run a solution on the problem input with ```./run.sh 17```
2. Run a solution with a given input with ```./run.sh 17 [input_location]```
3. Run a solution using the debug/timer/except flags with ```./run.sh -d -t -se 17 [input_location]```

### Adding a solution
Open a soltuon to edit using the ```./workflow.sh``` command, like so:

1. ```./workflow.sh 17b``` If 17b exists, open it in emacs. Otherwise, prompt for the creation of that file.

### Replace.txt
The workflow tool was original designed to cull all empty lines in input. To work around this, the run.sh pipeline will perform replacements on any input file mentioned in the replace.txt file. 

For example, the line ```04 empty``` means that when running problem 04, all empty lines are replaced with the word empty. This is very hacky, but it works well enough for now. I'll probably fix my tool (or replace it entirely) at some point to do away with this requirement.

## Solutions
Here's a write-up of all the solutions as I've done them.
### Day 01
#### Part One: Find the two entries in a list that add up to 2020. Multiply them.
This is a standard 2-sum problem. The good approaches are:

1. put all the entries in a set or hashmap, for each entry E see if a key 2020 - E exists. Since all the numbers are in the range [0, 2020], and you know there are no duplicate numbers, you could even just use an array and solve the whole thing in linear time.
2. Sort the collection once, iterate front/back. This is what I did, and the solution takes n log(n) + n time. The slowest thing about this is sorting the list.

#### Part Two: Find the three entries that add up to 2020. Multiply them.
Similar to part one, just iterate through the set of numbers and let target = chosenNumber. Then apply part 1, but with the following differences:

1. Instead of solving for 2020, you solve for 2020-target
2. Target is not a valid choice for 2-sum
