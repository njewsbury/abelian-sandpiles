#The Behaviour of Sandpile Models
###An exploration of the behaviours of a set of Sandpile Models

#### A PHYS 4250 assignment by Nathan Jewsbury

###Object
The following experiment was conducted to examine the relationship between a given sandpile model and the variety of results that occur per sandgrain placement.  Results of interest are the maximum 'avalanches' that can take place and it's dependence on grid size as well as processing time required to reach a steady state.

###Abelian Sandpile Model
The Abelian sandpile model follows a very basic set of rules.  For each grain placed if the receiver cell has a height greater than it's "critical" height it topples into it's neighbouring cells.  Once a single cell has toppled it reverts back to zero height.  This experiment used a critical height of four however other numbers would produced similar results at a slower pace.  This behaviour results in cascading "avalanches" where multiple receiving cells continue to topple and overflow their neighbours but the overall sandpile never reaches a height greater than the critical height.

###Realistic Sandpile Model
The Realistic sandpile model is meant to simulate a real sangrain dropping scenario where toppled cells don't revert to a zero height but continue to increase resulting in a conical structure.  The realistic model will follow a more complex set of rules than the Abelian model.  When testing the topple condition the cell will examine the height difference between itself and its adjacent cells.  When the height difference is larger than a "critical" difference then it will topple only into the cells where the difference is sufficient.  This behaviour will result in cells not toppling back "into" the sandpile and only expanding the pile in size and height.

###The Simulation
Running the sandpile simulation requires Java JRE version 1.6+ and can be run only through command line or by running the provided unix-run.sh
which assumes a unix type OS and installed bash shell.  

The arguments for the jar file itself follows the format:
[integer trial id]
[string cpu id]
[integer square grid size]
[integer stable step count]
[string model type "ABELIAN" or "REALISTIC"]
[integer how often to render the pile]

An example run command is as follows:

java -jar njewsbury-sandpile-model.jar 1 MyCPU 101 10000 ABELIAN 5000

