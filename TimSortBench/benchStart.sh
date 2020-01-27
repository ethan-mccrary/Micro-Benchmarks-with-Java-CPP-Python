#!/usr/bin/env bash

sudo javac timSortBench.java;
sudo g++ -O3 timSortBench.cpp -o timSortBench;
sudo xterm -e java timSortBench &
sudo xterm -e ./timSortBench;
sudo xterm -hold -e less timSortDataJava.xml &
sudo xterm -hold -e less timSortDataCpp.xml;
