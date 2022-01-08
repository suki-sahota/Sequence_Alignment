#!/bin/sh
javac sequencealignment/utils/Utilities.java
javac sequencealignment/symbol/Symbol.java
javac sequencealignment/output/Output.java
javac sequencealignment/input/Input.java
javac sequencealignment/basic/Basic.java
javac sequencealignment/Basic_Alignment.java

java sequencealignment/Basic_Alignment TestCases/input1.txt > BasicTestOutput.txt
java sequencealignment/Basic_Alignment TestCases/input2.txt >> BasicTestOutput.txt
java sequencealignment/Basic_Alignment TestCases/input3.txt >> BasicTestOutput.txt
java sequencealignment/Basic_Alignment TestCases/input4.txt >> BasicTestOutput.txt
java sequencealignment/Basic_Alignment TestCases/input5.txt >> BasicTestOutput.txt
java sequencealignment/Basic_Alignment TestCases/input6.txt >> BasicTestOutput.txt
java sequencealignment/Basic_Alignment TestCases/input7.txt >> BasicTestOutput.txt
java sequencealignment/Basic_Alignment TestCases/input8.txt >> BasicTestOutput.txt
java sequencealignment/Basic_Alignment TestCases/input9.txt >> BasicTestOutput.txt
java sequencealignment/Basic_Alignment TestCases/input10.txt >> BasicTestOutput.txt
java sequencealignment/Basic_Alignment TestCases/input11.txt >> BasicTestOutput.txt
java sequencealignment/Basic_Alignment TestCases/input12.txt >> BasicTestOutput.txt
java sequencealignment/Basic_Alignment TestCases/input13.txt >> BasicTestOutput.txt
java sequencealignment/Basic_Alignment TestCases/input14.txt >> BasicTestOutput.txt
java sequencealignment/Basic_Alignment TestCases/input15.txt >> BasicTestOutput.txt
java sequencealignment/Basic_Alignment TestCases/input16.txt >> BasicTestOutput.txt
java sequencealignment/Basic_Alignment TestCases/input17.txt >> BasicTestOutput.txt
java sequencealignment/Basic_Alignment TestCases/input18.txt >> BasicTestOutput.txt
java sequencealignment/Basic_Alignment TestCases/input19.txt >> BasicTestOutput.txt
java sequencealignment/Basic_Alignment TestCases/input20.txt >> BasicTestOutput.txt
