#include <iostream>
#include <fstream>
#include <ctime>
#include <cmath>
#include <chrono>
#include <thread>
#include <algorithm>
#include <vector>
//#include <windows.h> //Windows Based OS
#include <unistd.h> // UNIX Based OS

using namespace std::chrono;
using namespace std;

unsigned long long getTotalSystemMemory();
void mergeSort(vector<int>&, int, int);

int main() {

	int arrLength = 10000000;
	int warmupLength = 5000000;
	int timeDifLength = 500;
	auto* starting = new int[timeDifLength];
	auto* ending = new int[timeDifLength];
	auto* timeDif = new int[timeDifLength];
	ofstream data;
	//SYSTEM INFO//
	/* Total number of processors or cores available*/
	std::cout << "Available processors (cores): " << std::thread::hardware_concurrency() << std::endl;

	/* Total amount of memory*/
	std::cout << "Total memory (bytes): " << getTotalSystemMemory() << std::endl;

	//WARMUP//
	for (double i = 0; i < 100; i++) {
		vector<int> warmupArr(5000000);
		srand(time(0));
		for (int j = 0; j < warmupLength; j++) {
			warmupArr[j] = rand() % (10000001);
		}
		double perc = (i / 100) * 100;
		std::cout << "Warming Up: " << round(perc) << "%\r";
		mergeSort(warmupArr, 0, warmupLength - 1);
		warmupArr.clear();
	}
	//END WARMUP//

	//BENCHMARK//
	double j = 1;
	for (int l = 0; l < 500; l++) {
		vector<int> arr(10000000);
		for (int m = 0; m < arrLength; m++) {
			arr[m] = rand() % (10000001);
		}
		double perc = ((j + 1) / 500) * 100;

		std::cout << "Benchmarking. . . This may take a few minutes: " << round(perc) << "%\r";
		auto startTime = high_resolution_clock::now();
		mergeSort(arr, 0, arrLength - 1);
		auto endTime = high_resolution_clock::now();
		arr.clear();
		
		starting[l] = duration_cast<milliseconds>(startTime.time_since_epoch()).count();
		ending[l] = duration_cast<milliseconds>(endTime.time_since_epoch()).count();
		timeDif[l] = duration_cast<milliseconds>(endTime.time_since_epoch() - startTime.time_since_epoch()).count();
		j++;
	}

	//PRINT RESULTS//
	std::cout << "Writing Results to XML Format. . . This may take a few minutes.\r";

	data.open("mergeSortDataCpp.xml");
	int sum = 0;
	data << "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	data << "<mergeSortBench>\n";
	data << "\t<systemInfo>\n";
	data << "\t\t<processor>";
	data << "\t\t\t<coreCount>" << std::thread::hardware_concurrency() << "</coreCount>";
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
	data << "</mergeSortBench>\n";
	data.close();

	std::cout << "Benchmark Complete - All Benchmark Data Saved to mergeSortDataCpp.xml\r";
	delete[] starting;
	delete[] ending;
	delete[] timeDif;
}

void merge(vector<int> &arr, int l, int m, int r) {
	int i, j, k;
	int n1 = m - l + 1;
	int n2 = r - m;

	/* create temp arrays */
	vector<int> L(n1), R(n2);

	for (i = 0; i < n1; i++) {
		L[i] = arr[l + i];
	}

	for (j = 0; j < n2; j++) {
		R[j] = arr[m + 1 + j];
	}

	i = 0;
	j = 0;
	k = l;

	while (i < n1 && j < n2) {
		if (L[i] <= R[j]) {
			arr[k] = L[i];
			i++;
		} else {
			arr[k] = R[j];
			j++;
		}
		k++;
	}

	while (i < n1) {
		arr[k] = L[i];
		i++;
		k++;
	}

	while (j < n2) {
		arr[k] = R[j];
		j++;
		k++;
	}
}

void mergeSort(vector<int> &arr, int l, int r) {
	if (l < r) {
		int m = l + (r - l) / 2;
		mergeSort(arr, l, m);
		mergeSort(arr, m + 1, r);
		merge(arr, l, m, r);
	}
}

//Windows Based OS
/*unsigned long long getTotalSystemMemory() {
	MEMORYSTATUSEX status;
	status.dwLength = sizeof(status);
	GlobalMemoryStatusEx(&status);
	return status.ullTotalPhys;
}*/
//Unix Based OS
unsigned long long getTotalSystemMemory() {
	long pages = sysconf(_SC_PHYS_PAGES);
	long page_size = sysconf(_SC_PAGE_SIZE);
	return pages * page_size;
}
