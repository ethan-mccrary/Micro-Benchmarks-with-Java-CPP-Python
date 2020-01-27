#include <iostream>
#include <fstream>
#include <ctime>
#include <cmath>
#include <chrono>
#include <thread>
#include <algorithm>
#include <vector>
#include <cstdlib>
//#include <windows.h> //Windows Based OS
#include <unistd.h> // UNIX Based OS

using namespace std::chrono;
using namespace std;

unsigned long long getTotalSystemMemory();
void compAndSwap(std::vector<int>&, int, int, int);
void bitonicMerge(std::vector<int>&, int, int, int);
void bitonicSort(std::vector<int>&, int, int, int);

int main() {

	int arrLength = 8388608;
	int warmupLength = 4194304;
	int timeDifLength = 500;
	auto* starting = new int[timeDifLength];
	auto* ending = new int[timeDifLength];
	auto* timeDif = new int[timeDifLength];
	ofstream data;
	//SYSTEM INFO//
	/* Total number of processors or cores available*/
	std::cout << "Available processors (cores): " << thread::hardware_concurrency() << endl;

	/* Total amount of memory*/
	std::cout << "Total memory (bytes): " << getTotalSystemMemory() << endl;

	//WARMUP//
	for (double i = 0; i < 100; i++) {
		vector<int> warmupArr(4194304);
		srand(time(0));
		for (int j = 0; j < warmupLength; j++) {
			warmupArr[j] = rand() % (10000001);
		}
		double perc = (i / 100) * 100;
		std::cout << "Warming Up: " << round(perc) << "%\r";
		bitonicSort(warmupArr, 0, warmupLength, 1);
		warmupArr.clear();
	}
	//END WARMUP//

	//BENCHMARK//
	double j = 1;
	for (int l = 0; l < 500; l++) {
		vector<int> arr(8388608);
		for (int m = 0; m < arrLength; m++) {
			arr[m] = rand() % (10000001);
		}
		double perc = ((j + 1) / 500) * 100;

		std::cout << "Benchmarking. . . This may take a few minutes: " << round(perc) << "%\r";
		auto startTime = high_resolution_clock::now();
		bitonicSort(arr, 0, arrLength, 1);
		auto endTime = high_resolution_clock::now();
		arr.clear();

		starting[l] = duration_cast<milliseconds>(startTime.time_since_epoch()).count();
		ending[l] = duration_cast<milliseconds>(endTime.time_since_epoch()).count();
		timeDif[l] = duration_cast<milliseconds>(endTime.time_since_epoch() - startTime.time_since_epoch()).count();
		j++;
	}

	//PRINT RESULTS//
	std::cout << "Writing Results to XML Format. . . This may take a few minutes.\r";

	data.open("bitonicSortDataCpp.xml");
	int sum = 0;
	data << "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	data << "<quickSortBench>\n";
	data << "\t<systemInfo>\n";
	data << "\t\t<processor>";
	data << "\t\t\t<coreCount>" << thread::hardware_concurrency() << "</coreCount>";
	data << "\t\t</processor>\n";
	data << "\t\t<memory>\n";
	data << "\t\t\t<freeMemoryBytes>" << getTotalSystemMemory() << "</freeMemoryBytes>\n";
	data << "\t\t</memory>\n";
	data << "\t</systemInfo>\n";
	data << "\t<tests>\n";
	for (int i = 0; i < timeDifLength; i++) {
		data << "\t\t<test id=\"" << (i + 1) << "\">\n";
		data << "\t\t\t<startTime>" << starting[i] << "</startTime>\n";
		data << "\t\t\t<endTime>" << ending[i] << "</endTime>\n";
		data << "\t\t\t<excTime>" << timeDif[i] << "</excTime>\n";
		data << "\t\t</test>\n";
		sum += timeDif[i];
	}
	int avg = sum / timeDifLength;
	data << "\t</tests>\n";
	data << "\t<results>\n";
	data << "\t\t<totalBenchTime>" << (ending[timeDifLength - 1] - starting[0]) << "</totalBenchTime>\n";
	data << "\t\t<avg>" << avg << "</avg>\n";
	data << "\t</results>\n";
	data << "</bitonicSortData>\n";
	data.close();

	std::cout << "Benchmark Complete - All Benchmark Data Saved to bitonicSortDataCpp.xml\r";
	delete[] starting;
	delete[] ending;
	delete[] timeDif;
}

void compAndSwap(std::vector<int>& a, int i, int j, int dir) 
{ 
    if ((a[i] > a[j] && dir == 1) || 
             (a[i] < a[j] && dir == 0))
    {
        int temp = a[i]; 
        a[i] = a[j]; 
        a[j] = temp;
    }
} 
  
/*It recursively sorts a bitonic sequence in ascending order, 
  if dir = 1, and in descending order otherwise (means dir=0). 
  The sequence to be sorted starts at index position low, 
  the parameter cnt is the number of elements to be sorted.*/
void bitonicMerge(std::vector<int>& a, int low, int cnt, int dir) 
{ 
    if (cnt>1) 
    { 
        int k = cnt/2; 
        for (int i=low; i<low+k; i++) 
            compAndSwap(a, i, i+k, dir); 
        bitonicMerge(a, low, k, dir); 
        bitonicMerge(a, low+k, k, dir); 
    } 
} 
  
/* This function first produces a bitonic sequence by recursively 
    sorting its two halves in opposite sorting orders, and then 
    calls bitonicMerge to make them in the same order */
void bitonicSort(std::vector<int>& a,int low, int cnt, int dir) 
{ 
    if (cnt>1) 
    { 
        int k = cnt/2; 
  
        // sort in ascending order since dir here is 1 
        bitonicSort(a, low, k, 1); 
  
        // sort in descending order since dir here is 0 
        bitonicSort(a, low+k, k, 0); 
  
        // Will merge wole sequence in ascending order 
        // since dir=1. 
        bitonicMerge(a,low, cnt, dir); 
    } 
} 

/*unsigned long long getTotalSystemMemory() //Windows Based OS
{
	MEMORYSTATUSEX status;
	status.dwLength = sizeof(status);
	GlobalMemoryStatusEx(&status);
	return status.ullTotalPhys;
}*/

unsigned long long getTotalSystemMemory() //Unix Based OS
{
	long pages = sysconf(_SC_PHYS_PAGES);
	long page_size = sysconf(_SC_PAGE_SIZE);
	return pages * page_size;
}
