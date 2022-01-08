#!/bin/sh
javac sequencealignment/utils/Utilities.java
javac sequencealignment/symbol/Symbol.java
javac sequencealignment/output/Output.java
javac sequencealignment/input/Input.java
javac sequencealignment/basic/Basic.java
javac sequencealignment/efficient/Efficient.java
javac sequencealignment/Efficient_Alignment.java

java sequencealignment/Efficient_Alignment TestCases/input1.txt > EfficientTestOutput.txt
java sequencealignment/Efficient_Alignment TestCases/input2.txt >> EfficientTestOutput.txt
java sequencealignment/Efficient_Alignment TestCases/input3.txt >> EfficientTestOutput.txt
java sequencealignment/Efficient_Alignment TestCases/input4.txt >> EfficientTestOutput.txt
java sequencealignment/Efficient_Alignment TestCases/input5.txt >> EfficientTestOutput.txt
java sequencealignment/Efficient_Alignment TestCases/input6.txt >> EfficientTestOutput.txt
java sequencealignment/Efficient_Alignment TestCases/input7.txt >> EfficientTestOutput.txt
java sequencealignment/Efficient_Alignment TestCases/input8.txt >> EfficientTestOutput.txt
java sequencealignment/Efficient_Alignment TestCases/input9.txt >> EfficientTestOutput.txt
java sequencealignment/Efficient_Alignment TestCases/input10.txt >> EfficientTestOutput.txt
java sequencealignment/Efficient_Alignment TestCases/input11.txt >> EfficientTestOutput.txt
java sequencealignment/Efficient_Alignment TestCases/input12.txt >> EfficientTestOutput.txt
java sequencealignment/Efficient_Alignment TestCases/input13.txt >> EfficientTestOutput.txt
java sequencealignment/Efficient_Alignment TestCases/input14.txt >> EfficientTestOutput.txt
java sequencealignment/Efficient_Alignment TestCases/input15.txt >> EfficientTestOutput.txt
java sequencealignment/Efficient_Alignment TestCases/input16.txt >> EfficientTestOutput.txt
java sequencealignment/Efficient_Alignment TestCases/input17.txt >> EfficientTestOutput.txt
java sequencealignment/Efficient_Alignment TestCases/input18.txt >> EfficientTestOutput.txt
java sequencealignment/Efficient_Alignment TestCases/input19.txt >> EfficientTestOutput.txt
java sequencealignment/Efficient_Alignment TestCases/input20.txt >> EfficientTestOutput.txt
