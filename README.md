# Sequence_Alignment
Aligns two strings optimally as to minimize the cost of alignment. This algorithm has applications in aligning DNA, RNA, or protein. There are two versions of this algorithm that accomplish the same exact goal. The memory-efficient version runs a little slower than the basic version, but the memory-efficient version uses much less memory than the basic version. 

## Basic version
The basic version runs in O(m * n) time and uses O(m * n) auxillary space where m is the length of the first string and n is the length of the second string.

## Efficient version
The memory-efficient version runs in O(m * n) time (since there is a geometric reduction at each iteration) and only uses O(m) auxillary space where m is the length of the first string and n is the length of the second string.

## How to install and prepare
Step 1: Fork the repo and download locally onto your machine.

Step 2: Download a JVM to run Java programs.

## How to run individual test cases and print to output file

Step 1: Compile the java programs with 

javac sequencealignment/utils/Utilities.java


javac sequencealignment/symbol/Symbol.java


javac sequencealignment/output/Output.java


javac sequencealignment/input/Input.java


javac sequencealignment/basic/Basic.java


javac sequencealignment/efficient/Efficient.java


javac sequencealignment/Basic_Alignment.java


javac sequencealignment/Efficient_Alignment.java

Step 2.a: Run the basic algorithm with test case 20 (java sequencealignment/Basic_Alignment TestCases/input20.txt) or with test case 1 (java sequencealignment/Basic_Alignment TestCases/input1.txt) or any test case inbetween.

Step 2.b: Run the memory-efficient algorithm with test case 20 (java sequencealignment/Efficient_Alignment TestCases/input20.txt) or with test case 1 (java sequencealignment/Efficient_Alignment TestCases/input1.txt) or any test case inbetween.

Step 3: Check the output to console and the generated "output.txt" file for the program's output. The output file will be over-written each time the program is run.

## How to run all of the test cases and print to one giant output file

Step 1: Make the two provided scripts executable (chmod +x *Tests.sh).

Step 2: Run the scripts (./BasicTests.sh and ./EfficientTests.sh).

Step 3: Check the generated "BasicTestOutput.txt" and "EfficientTestOutput.txt" for a giant output file containing each of the test case's output.
