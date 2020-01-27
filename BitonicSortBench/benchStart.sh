#!/bin/bash
sudo javac bitonicSortBench.java;
sudo g++ -O3 bitonicSortBench.cpp -o bitonicSortBench;
sudo xterm -e java bitonicSortBench &
sudo xterm -e ./bitonicSortBench;
sudo xterm -hold -e less bitonicSortDataJava.xml &
sudo xterm -hold -e less bitonicSortDataCpp.xml;
