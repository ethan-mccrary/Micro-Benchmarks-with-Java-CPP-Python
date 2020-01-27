#!/usr/bin/env bash

sudo javac quickSortBench.java;
sudo g++ -O3 quickSortBench.cpp -o quickBench;
sudo xterm -e java quickSortBench &
sudo xterm -e ./quickBench;
sudo xterm -hold -e less quickSortData.xml &
sudo xterm -hold -e less quickSortDataCpp.xml;
