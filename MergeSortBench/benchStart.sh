#!/usr/bin/env bash

sudo javac mergeSortBench.java;
sudo g++ -O3 mergeSortBench.cpp -o mergeSortBench;
sudo xterm -e java mergeSortBench &
sudo xterm -e ./mergeSortBench;
sudo xterm -hold -e less mergeSortDataJava.xml &
sudo xterm -hold -e less mergeSortDataCpp.xml;
