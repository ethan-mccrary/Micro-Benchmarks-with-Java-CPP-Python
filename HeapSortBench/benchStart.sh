#!/usr/bin/env bash

sudo javac heapSortBench.java;
sudo g++ -O3 heapSortBench.cpp -o heapSortBench;
sudo xterm -e java heapSortBench &
sudo xterm -e ./heapSortBench;
sudo xterm -hold -e less heapSortDataJava.xml &
sudo xterm -hold -e less heapSortDataCpp.xml;
