# Sequence_Alignment
Aligns two strings optimally as to minimize the cost of alignment. This algorithm has applications in aligning DNA, RNA, or protein. There are two versions of this algorithm that accomplish the same exact goal. The memory-efficient version runs a little slower than the basic version, but the memory-efficient version uses much less memory than the basic version. 

## Basic version
The basic version runs in O(m * n) time and uses O(m * n) auxillary space where m is the length of the first string and n is the length of the second string.

## Efficient version
The memory-efficient version runs in O(m * n) time (since there is a geometric reduction at each iteration) and only uses O(m) auxillary space where m is the length of the first string and n is the length of the second string.

## How to install and run
Step 1: Fork the repo and download locally onto your machine.

Step 2: Download a JVM to run Java programs.

Step 3: Make the two provided scripts executable (chmod +x *Tests.sh).

Step 4: Run the scripts (./BasicTests.sh or ./EfficientTests.sh).
