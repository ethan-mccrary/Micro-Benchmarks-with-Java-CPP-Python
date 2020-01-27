#!/bin/bash
#BENCH BITONIC
clear;
cd BitonicSortBench;
echo "Benchmarking BitonicSort for Python";
sudo python bitonicSortBench.py;
cd ..;
#BENCH HEAP
clear;
cd HeapSortBench;
echo "Benchmarking HeapSort for Python";
sudo python heapSortBench.py;
cd ..;
#BENCH MERGE
clear;
cd MergeSortBench;
echo "Benchmarking MergeSort for Python";
sudo python mergeSort.py;
cd ..;
#BENCH QUICK
clear;
cd QuickSortBench;
echo "Benchmarking QuickSort for Python";
sudo python quickSort.py;
cd ..;
#BENCH TIM
clear;
cd TimSortBench;
echo "Benchmarking TimSort for Python";
sudo python timSort.py;

echo "Benchmarks Completed";
