Mooov-Around
============

Implementing a polynomial-time algorithm for the Mooov Around problem with general preference. A program that takes as input the student preferences and determines whether there exists an allocation that is individually rational and strictly Pareto-efficient

Compilation Comments:

	\*C.java contains main class with Algorithm C.*\
	\*make runs javac C.java*\
	\*./go runs java -ea A $1 $2*\

Compilation instructions(Run as follows):

	make 
	./go [input] [output] 


General Comments:
	
	-This is an implementation on java
	 The program uses breadth first search to compute the distance from
	  space j to students in the set of unsatisfied student.
