# ADVENT OF CODE 2020
This is a set of all my solutions for Advent of Code 2020. They are (mostly) cleaned up, and are all produced in Java using my own workflow tools (with a few hacks thrown in).

# Table of Contents
1. [Project Structure](#Project-Structure)
2. [Lore](#lore)
2. [Problem Ratings](#Problem-Ratings)
3. [Solutions](#Solutions)
    1. [Day 01](#day-01-report-repair)
    2. [Day 02](#day-02-password-philosophy)
    3. [Day 03](#day-03-toboggan-trajectory)
    4. [Day 04](#day-04-passport-processing)
    5. [Day 05](#day-05-binary-boarding)
    6. [Day 06](#day-06-custom-customs)
    7. [Day 07](#day-07-handy-haversacks)
    8. [Day 08](#day-08-handheld-halting)
    9. [Day 09](#day-09-encoding-error)
    10. [Day 10](#day-10-adapter-array)
    11. [Day 11](#day-11-seating-system)
    12. [Day 12](#day-12-rain-risk)
    13. [Day 13](#day-13-shuttle-search)
    13. [Day 14](#day-14-docking-data)
    15. [Day 15](#day-15-rambunctious-recitation)
    16. [Day 16](#day-16-ticket-translation)
    18. [Day 18](#day-18-operation-order)
    23. [Day 23](#day-23-crab-cups)
    24. [Day 24](#day-24-lobby-layout)
    25. [Day 25](#day-24-combo-breaker)

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
## Lore
Here's a brief summary of the 2020 advent of code deep lore.

| Problem | Plot |
| :-----: | :--- |
| Day 01  | You're tired of saving Christmas. Time to go on a vacation to a tropical resort. Before we leave, the elves need us to fix an *expense report*
| Day 02  | Our flight departs in a few days. We need to *toboggan* to the coast. Before we can buy the toboggan, we find out the rental store has faulty computers. We need to look at a database of passwords and identify valid ones.
| Day 03  | We have a toboggan now, and we need to get to the coast. First, we need to determine how to get down the slope while hitting the smallest number of trees.
| Day 04  | We've made it to the airport, only to find out we have the wrong credentials. Even worse, the automatic scanners seem to be malfunctioning. We hack the passport scanners to not only "fix the bugs", but [falsify our credentials](https://netrunnerdb.com/en/card/21064) and get in. We also let a bunch of people with invalid passports through by mistake. Whoops!
| Day 05 | Oops, it seems we *"dropped"* our boarding pass. We skim the boarding pass of every other passenger, then use that to forge a new boarding pass for ourself.
| Day 06 | We have to fill out customs declaration forms. We also have to fill out customs declaration forms for the other passengers, for some reason. Once we're done, we see that we actually filled all the instructions out wrong for the other passengers. Oopsie!
| Day 07 | We landed at the regional airport. Unfortunately, our flight is delayed due to obtuse luggage processing rules. It turns out that in order to take our shiny gold bag onboard, we need to stuff it with approximately 35,000 other bags.
| Day 08 | We've departed and we're in flight. Now a kid wants us to debug his nintendo.
| Day 09 | Now that the kid has his nintendo fixed, we get bored and decide to hack a random data port on the plane. Fortunately, it uses an old, outdated, easily breakable encryption routine.
| Day 10 | Now that we've hacked the plane, we steal it's weather forecasts. There's going to be a storm. Additionally, our laptop just ran out of battery. We happen to have about 300 different adapters, and we need to use them all if we want to download that weather data.
| Day 11 | The plane touched down with plenty of time to spare. Now we need to find a seat on the ferry. To get the best seat, we model the loading of all the passengers.
| Day 12 | The storm, which we stole data about and then ignored, has hit the ferry. The navigation computer seems to be malfunctioning. The captain needs us to to interpret the route to safety.
| Day 13 | The ferry is wrecked, but it will make it to a nearby port. There is no way to get to the vacation island by boat from here. But what we can do is catch a bus to the airport! We crunch numbers to discover when the earliest bus we can catch is, and also we enter a competition held by the bus company where they ask us about some obscure oriental math theorem.
| Day 14 | The docking computer for the fairy isn't compatible with the port computer. We need to emulate a legal ship using software and pretend to be authorized to dock.
| Day 15 | We've made it to port, and caught the shuttle to the airport. The flight isn't direct, but there's nothing we can do.  While we wait, we check in with work and they make us play a game of memory.
| Day 16 | The flight has touched down, and we're on the way to the next one. We need to catch a high-speed train, but we "can't read" our ticket. We hack the security cameras to steal the tickets of every other passenger, and using the values given, reverse engineer our own ticket (so we can find out the boarding time, of course).
| Day 17 | On our next connecting flight, the *MIB* calls and wants us to work remotely. We need to debug thier conway-cube power source, by confirming that their states match the expected states.
| Day 18 | Still in flight, the kid next to us requires us to do their math homework.
| Day 19 | Work calls again, and they want us to debug their spy satellites. We need to validate signals using a finite state machine.
| Day 20 | Our flight lands, and our train trip begins. We're still working remotely for the *MIB*. Now they need us to stitch together a bunch of satellite images so we can count sea monsters in the ocean. Eric needs you to buy the shirt.
| Day 21 | We reach the end of the line. Now we need to build a raft and "legally enter" the vacation island the hard way. To stock supplies, we need to figure out which foods we are allergic to.
| Day 22 | The raft has left, but floating at sea is boring. Fortunately, we find a crab to play games against. The crab beats us, so we get upset, challenge him to a double-for-nothing, and concoct a set of rules where we can't possible lose.
| Day 23 | The crab is upset we cheated. The crab tries challenging us to a game instead. We have to play a million-cup shell game to cheat him out of another two stars.
| Day 24 | We fell asleep at some point and the crab navigated the raft to shore. We make our way to the resort, only to find out that the lobby is inaccessible. They're re-tiling the floor. In order to get in, we decode the tiling instructions for the work crew. Afterwards, we find out that the tiles are actually a "living art" exhibit. Every day they flip some of the tiles (in something dangerously close to racist 'Conways Game of Life') based on the orientations of neighboring tiles. Neato!
| Day 25 | We've finally touched down in the hotel, and we're ready to relax. Unfortunately, our keycard "doesn't work". Oh well, we can just hack the hotel doors to "get access to our room". Once we're in and ready to relax, we get a call. It's time to go to work. We pay all of our hard earned stars in pennance and go back to the grind.

## Problem Ratings
Here are my ratings for each problem, and what the time complexity of my solutions happens to be.
In almost every case, N is equal to the line count. Otherwise, N will be noted.

| Problem | Complexity (Part One) | Complexity (Part Two) | Comments |
| ------- |:---------------------:|:---------------------:|:-------- |
| Day 01  | *O(N)*                  | *O(N<sup>2</sup>)*      |
| Day 02  | *O(NK)* | *O(N)* | K=average_password_size |
| Day 03  | *O(N)* | *O(NK)* | K=slope_count |
| Day 04  | *O(N)* | *O(N)* |
| Day 05  | *O(N)* | *O(N)* | 
| Day 06  | *O(SE)*| *O(SE)* | S = average number of elements to put into set, E = input count |
| Day 07  | *O(NK)* | amortized *O(NK)* | K = average number of children or ancestors per element. Amortized through memoization. |
| Day 08  | *O(N)* | *O(N)* |
| Day 09  | *O(N W)*| *O(N log(K))* | W = window_size, K = max_sequence_size* |
| Day 10  | *O(N + M)* | *O(M + NJ)* | Abuses properties of data. M = maximum size in list. J = maximum joltage jump. 
| Day 11  | *O(CI)*| *O(C(I+H+W))*  | Where C is the number of cells and I is the number of iterations needed to terminate. This really just depends on how many iterations the given input will generate. I don't know how to estimate that. Each iteration should be computable linear to the input size (and smaller than the last iteration), and a small amount of pre-processing needs to be done on part 2, which takes *O(C(H+W))*, where H and W are the Height and Width of the grid. The worst case for the absolute worst possible input couldn't be worse than *O(C<sup>2</sup>)* for either of these problems.
| Day 12  | *O(N)* |  *O(N)* |
| Day 13  | *O(N)* |  ? ? ?  | No clue on part two. I might put the research in later.
| Day 14  | *O(N)* |  *O(X)* or *O(B)* | X is number of memory addresses (niave solution), B is the number of branch points in the binary decision tree.
| Day 15  | *O(K)* | *O(K)* | K = number of cycles
| Day 16  | *O(NC)* | O(C<sup>2</sup> | C is number of classes
| Day 17  |
| Day 18  | O(N) | O(NT) | T = average token count per line 
| Day 19  |
| Day 20  |
| Day 21  |
| Day 22  |
| Day 23  | *O(N+K)* | *O(N+K)* | N = Token count, K = number of cycles. 
| Day 24  | *O(C)* | *O(KT<sup>E</sup>)* | C = character count, K = cycle count, T = (initial) Tile count, E = expansion factor. Complexity of p2 is mostly based on the input state and the iteration count.
| Day 25  | *O(fuckyou<sup>n)* | O(1) | Part 2 works on my input :^)
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

### Day 03: Toboggan Trajectory
Do you know  how to use the modulo command? There's not much more to it than that.
Ignore the first line of input, it isn't relevant.

Part two is just a check that you either know functions exist, or are willing to copy-paste your code four times.

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
We know from the problem description that the data constists of a contigious sequence with one gap

This means there are two easy ways to solve this problem.

1. Sort the list. Find any entry ```entry[E]``` (other than the first) where entry ```entry[E-1] + 1 != entry[E]```. The sort means this takes n log(n) time.
2. Map all the values into an array of size ```entry[min(entry)] - entry[max(max)]```. Find the empty spot in the array. Your target belongs here. This can be done in linear time, and means that both parts of the problem will be solved in linear time.

### Day 06: Custom Customs
This is another really easy one. Seperate the input by empty lines, then you have sets of inputs to process.

#### Part One
You just want the union of each input in your set. This is as easy as adding every single element to a set.

#### Part Two
You want the intersection of each input in your set. In java, this is A.retainAll(B).

### Day 07: Handy Haversacks
This is a combination of string parsing and tree-construction.
First, it is necessary to process all of the input strings. They are all in the form of:

1) [key] bags contain ([num] [key] bag(s?), )*[num [key] bag(s?).
2) [key] bags contain no other bags.

This data can be organized in the form ```map<key, list<key, quantity>>```.
For the sake of simplicity, a similar collection can be made to track the (possible) ancestors of each bag. This is organized in the form ```map<key, list<key>>```

#### Part One
To get the set of all bags which can contain gold bags, simply start with the gold bag, and follow the tree upwards using the map of ancestors. Add all the ancestors to a set. You can additionally keep a set of all the seen ancestors to reduce checking nodes more than once.

The general formula looks like this:

1) Start with an empty stack, S, and an empty set, E
2) Add all ancestors of the target to S and to E
3) While the stack is not empty, take the first element of the stack. For each of it's ancestors not in E, add that ancestor to S and E

The set E then becomes the set of all unique ancestors of the bag.

#### Part Two
Now we want to find out how many bags a gold bag contains. This can be done recursively with memoization. Keep a set of all resolved bags. Then, for every bag in the current bag, check if it's children have been resolved. If they have, add up the number of bags they possess, then add the number of bags this bag itself possesses, making sure to store this value. Because of memoization, this can be done in amortized O(n\*k), where k is the average number of bag types that each bag contains.

The algorithm looks something like this:
```
def count_children(key, map<key, [(key, quantity)]> all_children, map<key, long> memo):
    var sum;
    //this is all the children for a given key
    [(key, quantity)] children = all_children.get(key)
    
    for (subkey, quantity) in children:
        var intermediate = memo(subkey)
        if ! intermediate:
            intermediate = count_children(subkey, all_children, memo)
            memo.put(subkey, intermediate)
        sum += (intermediate + 1) * quantity
    return sum
```

### Day 08: Handheld Halting
This problem is a little bit interesting. A program is given to you consisting of three operation types: A) acc [val] B) jmp [val] and C) nop [val].
The given program does not halt. At some point, it will enter into a loop. Because there is no choice in this language, there is no way to escape a loop, and therefore the program can never halt.

The instructions work as follows:

```
acc [val]: modifies the accumulator register by the given value
jmp [val]: modifies the program counter by the given value
nop [val]: does nothing (the program counter increments by one)
```

#### Part One
Find the first instruction (the value of the program counter) that is executed more than once.
This is pretty simple to accompish. Keep a set of executed instructions, and the first time your pc clashes with that set then you have the answer. Alternatively, make a boolean array the size of the program and use that to keep track. Both are O(n).

#### Part Two
One of the instructions in the program can be changed, either from *nop x* to *jmp x*, or from *jmp x* to *nop x*. You need to find out which one, change it, and then execute the program. When the program has finished executing, the program counter should read equal to the line count of the program.

This, too, can be done in O(n). To determine which instruction can be flipped, use the following steps:

1) Build a mapping PC to PC based on the instructions in the list. Every instruction leads to exactly one other PC, but some PC's have multiple direct ancestors. Hence, Map<Int, [Int]>. We want a mapping of a given PC, and all instructions that result in that PC.
2) Starting at the terminating state (PC = program size), determine all states which lead to this state. Add these (and also the terminating state itself) to a set T of terminating states, and then for each of these states, repeat the process. The end result should be a set of all states which, if reached, guarantee the termination of the program. 
3) Run the program as normal. The first time that replacing a nop/jmp instruction would result in the PC entering the set of terminating states, then that instruction is replaced.
4) Execution then continues as normal until the program halts. The value of the accumulator is the answer.

All of this can be done in O(n) time. Observe that:
1) To build a mapping from pc->[pc] can be done iteratively in linear time
2) Use of hashmaps allows for O(1) lookups and insertions
3) Determining the set of terminating states requires that each state be looked at no more than once, so this is O(n)
4) The program has no loops, so execution occurs in O(n)
Because no operation is greater than O(n), the entire process is O(n).

### Day 09: Encoding Error
Day 9 involves reading through a stream of encoded data and finding the first invalid entry.
The rules of the encoding are as follows:

1) The first sequence of numbers transmitted is the preamble, which has size N, and has no restrictions
2) A window is kept of the last N numbers seen
3) For a number to be valid, it must be the sum of two (numerically) *different* numbers in the window

For this problem, the preamble/window size given is 25.

#### Part One
Part one asks to find the first number in the sequence which does not adhere to the encoding rules. Solving this requires several steps:

1) Read the preamble, add all to window of size *W*
2) Read the next number, *N*
3) If *N* is not the sum of two *different* numbers in the window, halt
4) remove the oldest number in the window
5) Add *N* to the window
6) GOTO 2

Steps 1, 2, 4, 5 and 6 are all constant time operations. However, step 3 requires one of the follwing:
1) If the window is sorted, it can be solved in O(W) time.
2) Otherwise, you can use the hashtable method of solving 2sum, which will take O(W) time.

Thus, this can be solved in O(*N W*) time.

#### Part Two
Find the first sequence of numbers with size of at least two which sums to the non-conforming number from Part One.

This can be done in O(*N log(K)*) time (I think - the sequence has the property that numbers tend to increase, which means sequences get shorter the further we search), where *K* is the length of the maximum sequence during the search.

The following algorithm is used:
```
[values], target
--------------------
start = end = 0
sequence = new list()
sum = 0
while(true):
  //cannot have sequence of size < 2
  if sum < target || end - start < 2:
    sequence.add(values[end])
    sum += values[end]
    end++;
  else if sum < target:
    sum -= list.pop() //first element
    start++
  else
    return sequence
```

The weakness can then be found with ```min(sequence) * max(sequence)```.

### Day 10: Adapter Array
Our laptop is out of power and need to be charged. We have a bag of adapters, each with unique *joltages*, and our device has a *joltage* of max(input) + 3. Each adapter can take an input of *j-1*, *j-2*, or *j-3* jolts. The charging outlet has a charge of 0 *jolts*.

#### Part One
If we use every adapter at once, what is the distribution of joltages we achieve? The answer is given as ```sum(1-jolt-jumps)``` \* ```sum(3-jolt-jumps)```.

To achieve this result, there are two general methods:
1) We know the size of the collection, and we can determine in linear time what the max and min values are. A hashmap can be made of adapter joltages, which will have worst case space of *3N*. Then, keys can be checked iteratively. Where a match is found, compare that to the last match to determine the joltage jump. This takes, in the worst case, 3N time and space.
2) The collection can be sorted, which takes *O(N log N)* time. Then, it can be walked iterated through.
Don't forget to add the +1 for jumping from the adapter to your device.

#### Part Two
We know a single way to connect the adapters using every single adapter, but how many ways can we connect them using any number of adapters that bridges 0 to max_value? This can actually be done in "linear" time too, because of the properties of our data. The entries are all unique, and each value is only "linked" with values 3 joltages above or below it. With the sorted list, perform the following:
* Create a map <Integer, Integer> using our original collection as the key, and initializing each value to 0.
* For each key in the collection, if that key is in range of 0, set the value to 1. Then, if (key-1, key-2, key-3) exist, add thier values to the value for this key.
The result is the value for the final key in the collection, which is the number of valid ways to link 0 joltages to the laptop using our collection of adapters.

At most, each key takes three comparisons, so this can be done in *O(N)*.  The data had to be sorted first, however, so that makes this *O(N log N)*. A similar trick to the hashmap one in part 1 can be done here, We need to track through all indices from 1 to M, however, so this takes it to *O(3N + M)*, where M is the maximum value.

### Day 11: Seating System
We need to fully resolve a cellular automata based on a set of rules.

#### Part One
The rules are as follows:
1) An empty seat becomes full if there are no adjacent empty seats
2) An occupied seat becomes empty if there are four or more adjacent occupied seats

It's simple enough to build a loop which computers this for every character in the matrix at every iteration, but first let's examine our automata:
1) If a cell doesn't change over one iteration, it will never change again
2) If every single cell which changed in one iteration then changes in the next iteration, then this automata can never resolve.
3) Otherwise, this automata must resolve.
4) If the automata is capable of resolving, then every second iteration must reduce the number of active tiles by at least one.

The important thing to take note of is that the only cells we need to actively track are those that changed. Thus, we can greatly cut down on computation costs by only tracking these cells (and their direct neighbors).

Additionally, it is possible to compute all of the cell changes in parallel, then apply them all at the same time. I'm uncertain how bad the overhead is here, it might not be worth it. I did it anyway.

I believe the time complexity here is (at the worst case) O(C<sup>2</sup>), where C is the number of cells. I think that in the average case, however, it will be much much lower. I tended to get 70-90 iterations on inputs that were in the 10k cell count.

#### Part Two
The rules change a tiny bit here. First, we no longer consider neighbors. Only the first chair we can see from our position in each of the orthogonal directions. This can be precomputed for each chair, however, so the cost of that is negligible.
Next, rule 2 has changed. We now need to see 5 chairs to vacate a chair.

Everything from above applies, and this can be solved in the exact same way.

### Day 12: Rain Risk
Day 12 is quite simple. Part one requires that you apply a set of directions to a single point. There's not really too much to say about this problem. Part two is the same, except most of your instructions refer to moving a dynamic point around your point, then performing movements in the direction of the dynamic point. Both of these are purely O(N).

### Day 13: Shuttle Search
Day 13 is a bit of a cop out puzzle. Part one can easily be done by hand, and part two simply asks if you know Chinese Remainder Theorem.

For part one, check every number in order. We want to find the number where ```our_departure_time % bus_departure_interval``` is maximized, or the value where ```(-1 * our_departure_time) % bus_departure_interval``` is minimized.

For part two, implement chinese remainder theorem, where each denominator is ```INTERVAL - ORDER``` and the nominator is is ```INTERVAL```.

### Day 14: Docking Data
At face value, this looks like an interesting puzzle. Part one asks you to mask values according to rules and track the results in a memory, and part two asks you do something similar with addresses. In my opinion, there are two major flaws with this problem:
1) The sample input for part one is designed to make you waste time on part two, and doesn't resemble even a single piece of input you can get
2) The inputs are so simple, and have such low overlap rates (0.3%), that the general purpose good solution (binary decision trees) happens to be worse than the brute force solution. This is a combinatorics question that's been designed so that literally the best case is testing every single combination. For a problem this far in to the event, that's just absolutely abysmal.

Now for the problem itself:
#### Part One
Given data in the form ```(mask = [[01X]{36}]) | (mem[NUMBER] = [NUMBER]))*```, perform the follwing:
1) Whenever a mask is given, save that as the current mask
2) Whenever a value is given, apply the mask to that value, then put that value in memory
3) To apply the mask to a value, perform the following:
    a) If the index on the mask is 0 or 1, overwrite that index on the value with the given bit
    b) If the index on the mask reads X, then keep the original bit at that index

Then, count the entire memory.

#### Part Two
Now the problem has changed. Instead of masking the value, you need to mask the address. 
1) If the mask contains 1, then that bit MUST be set to 1.
2) If the mask contains 0, then that bit is the original bit of the address.
3) If the mask contains X, then that bit takes on **both** the values 1 and 0 (both addresses are written to).

It sounds like the naive approach of writing to every single memory address would be bad, but that's not the cast. "2020 has been a tough year, so the puzzles are easy this time :)". Because of the inputs, the fastest way to do this problem for every sinle participant will be to write to every single address in order and then count the values.

For a general-purpose faster approach, perform the following steps:
1) Read all of the input, applying the mask to the address at each step. Then, reverse that list (so the final write happens first, and the first write happens last). Note that processing the data in this direction, once an address is set it can NEVER be changed.
2) Create a decision tree. Every time we "write" to an address, place that address into the decision tree
3) The result is an enumeration on the tree

The decision tree is constructed as follows:
1) Each non-leaf node has three potential children, 0 1 X.
2) Leaf nodes have a value and no children
3) To insert an address-value mapping into a node, consider the first character of the address. Insert the substring (1, len) of the address into the matching node.
4) If a node contains an X child, and a 1-child is to be inserted, split the X child into a 0 and a 1 child
5) A node can never be overwritten (this memory is write-once)
6) If a node contains a 0 or a 1 child, and an x-child is to be inserted, instead insert into the 0 and 1 childs

The first sample using this method makes three tree traversals for example. The brute force method would take hours (and run you out of ram, as it did for a lot of competitors).
```
mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
mem[8] = 11
mem[7] = 101
mem[8] = 0
```

But despite that being used as a sample, it flat out does not resemble ANY of the problem input.

### Day 15: Rambunctious Recitation
This problem basically asks you to implement the [Van-Eck sequence](https://oeis.org/A181391).

The properties of this sequence are as follows:
1) First, an input sequence of numbers is given
2) Then, every time a number would be generated, look at the previous number in the sequence
3) If the previous number is unique (not in the map), next = 0
4) Otherwise next = (current time) - (last time number spoken)

A quick implementation basically goes like this:

```
<Integer, Integer> memory;
for(i = 1; i <= input.length; i++):
    memory.put(input[i-1], i)
    
//After a unique sequence, the next number is always 0 
int next = 0

for(int i = input.length+1; i < lim; i++) {
    //it it's the first time the number appears, then next_next is zero
    int next_next = 0;
    
    //if the number is seen, next_next is difference between i and last time spoken
    if(memory.containsKey(next))
        next_next = i - memory.get(next);
        
    memory.put(next, i);
    next = next_next
}

return next;
```

It's trivial to see that this takes *O(K)* time, where K is the number to generate.

In terms of possible speedups, I think the answer is "we don't know." I definitely don't. I believe a summary of the properties of the sequence can be see here:

![van-eck](https://raw.githubusercontent.com/NBKelly/AOC2020/master/van-eck.jpeg "We don't know")

### Day 16: Ticket Translation
Part one is just input validation again. Read a bunch of values, then determine which ones can't possibly be valid.

Part two is just gaussian elimination.

https://en.wikipedia.org/wiki/Gaussian_elimination

### Day 18: Operation Order
This problem is way too easy for how late it is. The first one is just casting eval on your input strings in most languages, and the second one can be done nearly as easily. I chose to parse and evaluate the input using my own programming. Because there's no complicated problem, everything here is done in linear time.

#### Part One
Add all of your tokens to a stack (eric can only count on his fingers, so no number exceeds 9). Then, you just need to evaluate a stack of tokens.
I used the pseudocode function here:

```
eval(stack):
  #deal with empty inputs
  if(stack is empty)
    return 0
  let token = stack.pop()
  if(token == ")")
    stack.push(eval(stack))
    return eval(stack)
  else
    val = int(token)
    token = stack.pop()
    switch(token) {
      "(": return val
      "+": return val + eval(stack)
      "*": return val * eval(stack)
  ```
  
  #### Part Two
  The change for part two is that addition now has higher precedence than multiplication. This can be done with a simple change to the eval function:
  
```
eval(stack, component):
  #deal with empty inputs
  if(stack is empty)
    return 0
  let token = stack.pop()
  if(token == ")")
    stack.push(eval(stack, 0))
    return eval(stack, component)
  else
    val = int(token) + component
    token = stack.pop()
    switch(token) {
      "(": return val
      "+": return eval(stack, val)
      "*": return val * eval(stack, 0)
  ```


### Day 23: Crab Cups
The crab has now challenged us to a shell game. This can be done in linear time by using a linkedlist, where every node is also kept in a map by value. So each node can be accessed in O(1), and insertion/removal can also be done in O(1)

#### Part One
For each round:

1) pick the first cup in the list
2) set aside the next three cups
3) Find the target cup with a value of first-1. If that doesn't exist, decrement that value until we find a cup.
4) Insert the cups set aside in 2) after the cup selected in 3)
5) Put the first cup at the end of the list (rotate the list counterclockwise once)

Note that 1, 2 both occur right at the start of the list, so should be o(1) with a linked list.
Then, if we can quickly lookup arbitrary nodes and insert directly after then, then every cycle should be O(n). To do this, we just need to combine our linkedlist with a map.

#### Part Two
The same thing but for a million numbers and ten million cycles. If you didn't optimise, you'll be waiting until Christmas.

### Day 24: Lobby Layout
We've made it to the resort, but they need help redesigning the lobby!

Instructions are given in the form ```[dir]*```, where dir is one of ```{e, w, ne, nw, se, sw}```. These map to directions in a hexagonal grid.
Every line contains one instruction.

#### Part One
We construct a grid of active (black) tiles by following the rules:

1) each round we start at the center tile
2) follow every direction given in the input line
3) flip that tile (white->black or black->white)

There are a few different ways you can represent a hexagonal grid. I chose to represent it as a staggered 2d grid, using the following mapping:

```
      EAST: (x + 2, y)
      WEST: (x - 2, y)
NORTH-EAST: (x + 1, y + 1)
NORTH-WEST: (x - 1, y + 1)
SOUTH-EAST: (x + 1, y - 1)
SOUTH-WEST: (x - 1, y - 1)
```

Then, just decode each line into a list of directions, and keep a set of all active (black) tiles.

#### Part Two
It's just dangerously close to racist Conway's Game of Life.

1) keep a set of blacks, BLACKS
2) for each black, get the set of white neighbors, WHITE_NEIGHBORS
3) add those white neighbors to another set, WHITES
4) if(WHITE_NEIGHBORS) is size 6, or size < 4, add it to the set FLIPS
5) for each WHITE in WHITE_NEIGHBORS, count the number of BLACKS neighboring it. If that number is 2, add it to the set FLIPS
6) BLACKS = Symmetric difference(BLACKS, FLIPS)

Time complexity is *O(C)* for part one, and *O(KT<sup>E</sup>)* for part 2, where C = character count, K = cycle count, T = (initial) Tile count, E = expansion factor. Complexity of p2 is mostly based on the input state and the iteration count.

### Day 25: Combo Breaker

We can't get into our room. In order to get in, we must hack the security system. The details are as follows:

1) Both the card and the door have a public keys, K<sup>c</sup> and K<sup>d</sup>.
2) A public key is computed by applying the transform function to (value: 1) and SUBJECT_NUMBER a number of times equal to the private key.
3) The transform function takes a value and a subject number as input. It gives in return ```(value * subject_number) % 20201227```.
4) The default subject number used to generate a public key is 7
5) The card generates an encryption key, K<sup>E</sup> by performing the transform function a number of times equal to it's private key, K<sup>c</sup>, with the subject_number equal to the public key of the door, K<sup>d</sup>
6) The door generates an encryption key, K<sup>E</sup> by performing the transform function a number of times equal to it's private key, K<sup>d</sup>, with the subject_number equal to the public key of the card, K<sup>c</sup>

Because the log is quite low, you can brute force this easily. Once again, the hardest part of this problem is figuring out what the heck eric was trying to say. This problem felt like it was written by a person who had no clue what any of the terms used might have meant.

To beat part 2, just don't get filtered anywhere else.

## SUMMARY
Most of the problems this year felt pretty weak. I'll put a poll up and see what I get.
