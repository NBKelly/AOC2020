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
### Day 01: Report Repair
#### Part One: Find the two entries in a list that add up to 2020. Multiply them.
This is a standard 2-sum problem. The good approaches are:

1. put all the entries in a set or hashmap, for each entry E see if a key 2020 - E exists. Since all the numbers are in the range [0, 2020], and you know there are no duplicate numbers, you could even just use an array and solve the whole thing in linear time.
2. Sort the collection once, iterate front/back. This is what I did, and the solution takes n log(n) + n time. The slowest thing about this is sorting the list.

#### Part Two: Find the three entries that add up to 2020. Multiply them.
Similar to part one, just iterate through the set of numbers and let target = chosenNumber. Then apply part 1, but with the following differences:

1. Instead of solving for 2020, you solve for 2020-target
2. Target is not a valid choice for 2-sum

### Day 02: Password Philosophy
This is just a bog-standard string parsing puzzle. 

#### Part One: check that each string has between A and B instances of character C
You can probably make a regex for this pretty easily.
#### Part Two: check that each string has character C at index A XOR at index B
Also easily doable with a regex. I did it like a caveman.

### Day 03: Taboggan Trajectory
Do you know  how to use the modulo command? There's not much more to it than that.
Ignore the first line of input, it isn't relevant.

### Day 04: Passport Processing
This is more string parsing.

Split the entire input into categories based on empty lines, then for each category, place all entries into a map using the field name as the key.

#### Part One
Just print a count of all credentials where every field (except maybe the place of origin) exists

#### Part Two
The same as part one, but now you have a few strings to parse to make sure all the fields are valid.

### Day 05: Binary Boarding
Each seat is assigned based on a binary space partition in the range (0 <= y <= 127), (0 <= x <= 7).

#### Part One
Most of the heavy liting can be done with the following function:

```java
int[] binary(int lower, int upper, boolean matched) {
  if(matched)
    return new int[]{lowHalf(lower, upper) +1, upper};
  else
    return new int[]{lower, lowHalf(lower, upper)};
}

int lowHalf(int low, int height) {
  return (high + low)/2;
}
```
To get the highest one, just sort (n log(n)) and take the top value, or seek through the list for the maximum value.

#### Part Two
We know from the problem description tha that the data constists of a contigious sequence with one gap

This means there are two easy ways to solve this problem.

1. Sort the list. Find any entry ```E``` (other than the first) where entry ```(E-1) + 1 != E```. The sort means this takes n log(n) time.
2. Map all the values into an array of size E[min] - E[max]. Find the empty spot in the array. Your target belongs here. This can be done in linear time.

