# ADVENT OF CODE 2020
This is a set of all my solutions for Advent of Code 2020. They are (mostly) cleaned up, and are all produced in Java using my own workflow tools (with a few hacks thrown in).

# Table of Contents
1. [Project Structure](#Project-Structure)
    1. [Adding a Solution](#Adding-a-solution)
    2. [Replace.txt](#Replace.txt)
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
    18. [Day 18](#day-18-operation-order)

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
| Day 22 | The raft has left, but floating at sea is boring. Fortunately, we find a crab to play games against. The crab beats u s, so we get upset and concoct a set of rules where we can't possible lose.

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
| Day 10  |
| Day 11  |
| Day 12  |
| Day 13  |
| Day 14  |
| Day 15  |
| Day 16  |
| Day 17  |
| Day 01  |
| Day 18  | O(NT) | O(NT) | T = average token count per line 
| Day 19  |
| Day 20  |
| Day 21  |
| Day 22  |
| Day 23  |
| Day 24  |
| Day 25  |
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
