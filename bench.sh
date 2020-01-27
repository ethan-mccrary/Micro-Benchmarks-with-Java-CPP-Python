#!/bin/bash

#BitonicSort
cd BitonicSortBench;
sudo javac bitonicSortBench.java;
sudo g++ -O3 bitonicSortBench.cpp -o bitonicSortBench;
clear;
echo "Running BitonicSort Bench for Java";
echo "==================================";
sudo java bitonicSortBench;
clear;
echo "Running BitonicSort Bench for C++";
echo "==================================";
sudo ./bitonicSortBench;
clear;
cd ..;
#HeapSort
cd HeapSortBench;
sudo javac heapSortBench.java;
sudo g++ -O3 heapSortBench.cpp -o heapSortBench;
echo "Running HeapSort Bench for Java";
echo "==================================";
sudo java heapSortBench;
clear;
echo "Running HeapSort Bench for C++";
echo "==================================";
sudo ./heapSortBench;
clear;
cd ..;
#MergeSort
cd MergeSortBench;
sudo javac mergeSortBench.java;
sudo g++ -O3 mergeSortBench.cpp -o mergeSortBench;
echo "Running MergeSort Bench for Java";
echo "==================================";
sudo java mergeSortBench;
clear;
echo "Running MergeSort Bench for C++";
echo "==================================";
sudo ./mergeSortBench;
clear;
cd ..;
#QuickSort
cd QuickSortBench;
sudo javac quickSortBench.java;
sudo g++ -O3 quickSortBench.cpp -o quickSortBench;
echo "Running QuickSort Bench for Java";
echo "==================================";
sudo java quickSortBench;
clear;
echo "Running QuickSort Bench for C++";
echo "==================================";
sudo ./quickSortBench;
clear;
cd ..;
#TimSort
cd TimSortBench;
sudo javac timSortBench.java;
sudo g++ -O3 timSortBench.cpp -o timSortBench;
echo "Running TimSort Bench for Java";
echo "==================================";
sudo java timSortBench;
clear;
echo "Running TimSort Bench for C++";
echo "==================================";
sudo ./timSortBench;
clear;
cd ..;
