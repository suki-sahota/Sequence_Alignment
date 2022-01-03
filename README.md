# Sequence_Alignment
Aligns two strings optimally as to minimize the cost of alignment. This algorithm has applications in aligning DNA, RNA, or protein. There are two versions of this algorithm that accomplish the same exact goal. The memory-efficient version runs a little slower than the basic version, but the memory-efficient version uses much less memory than the basic version. 

## Basic version
The basic version runs in O(m * n) time and uses O(m * n) auxillary space where m is the length of the first string and n is the length of the second string.

## Efficient version
The memory-efficient version runs in O(m * n) time (since there is a geometric reduction at each iteration) and only uses O(m) auxillary space where m is the length of the first string and n is the length of the second string.

## How to install and prepare
Step 1: Fork the repo and download locally onto your machine.

Step 2: Download a JVM to run Java programs.

Step 3: Compile the java programs (javac *_Alignment.java).

## How to run individual test cases and print to output file

Step 1.a: Run the basic algorithm with test case 20 (java Basic_Alignment TestCases/input20.txt) or with test case 1 (java Basic_Alignment TestCases/input1.txt) or any test case inbetween.

Step 1.b: Run the memory-efficient algorithm with test case 20 (java Efficient_Alignment TestCases/input20.txt) or with test case 1 (java Efficient_Alignment TestCases/input1.txt) or any test case inbetween.

Step 2: Check the generated "output.txt" file for the program's output. The output file will be over-written each time the program is run.

## How to all the test cases and print to one giant output file

Step 1: Make the two provided scripts executable (chmod +x *Tests.sh).

Step 2: Uncomment the print lines in Basic_Alignment.java and Efficient_Alignment.java that are located below the following lines "// Test Printing".

Step 3: Compile the java programs again (javac *_Alignment.java).

Step 4: Run the scripts (./BasicTests.sh and ./EfficientTests.sh).

Step 5: Check the generated "BasicTestOutput.txt" and "EfficientTestOutput.txt" for a giant output file containing each of the test case's output.
