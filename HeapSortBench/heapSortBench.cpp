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
void heapSort(vector<int>&);
void heapify(vector<int>&, int n, int i);
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
	std::cout << "Available processors (cores): " << thread::hardware_concurrency() << endl;

	/* Total amount of memory*/
	std::cout << "Total memory (bytes): " << getTotalSystemMemory() << endl;

	//WARMUP//
	for (double i = 0; i < 100; i++) {
		vector<int> warmupArr(5000000);
		srand(time(0));
		for (int j = 0; j < warmupLength; j++) {
			warmupArr[j] = rand() % (10000001);
		}
		double perc = (i / 100) * 100;
		std::cout << "Warming Up: " << round(perc) << "%\r";
		//sort(warmupArr.begin(), warmupArr.end());
		heapSort(warmupArr);
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

		std::cout << "Benchmarking. . . This may take a few up to 15 minutes: " << round(perc) << "%\r";
		auto startTime = high_resolution_clock::now();
		heapSort(arr);
		auto endTime = high_resolution_clock::now();
		arr.clear();

		starting[l] = duration_cast<milliseconds>(startTime.time_since_epoch()).count();
		ending[l] = duration_cast<milliseconds>(endTime.time_since_epoch()).count();
		timeDif[l] = duration_cast<milliseconds>(endTime.time_since_epoch() - startTime.time_since_epoch()).count();
		j++;
	}

	//PRINT RESULTS//
	std::cout << "Writing Results to XML Format. . . This may take a few minutes.\r";

	data.open("heapSortDataCpp.xml");
	int sum = 0;
	data << "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	data << "<heapSortBench>\n";
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
	data << "</heapSortBench>\n";
	data.close();

	std::cout << "Benchmark Complete - All Benchmark Data Saved to heapSortDataCpp.xml\r";
	delete[] starting;
	delete[] ending;
	delete[] timeDif;
}

void heapSort(vector<int>& arr)
{
	int n = arr.size();

	// Build heap (rearrange array)
	for (int i = n / 2 - 1; i >= 0; i--)
		heapify(arr, n, i);

	// One by one extract an element from heap
	for (int i = n - 1; i >= 0; i--)
	{
		// Move current root to end
		int temp = arr[0];
		arr[0] = arr[i];
		arr[i] = temp;

		// call max heapify on the reduced heap
		heapify(arr, i, 0);
	}
}

// To heapify a subtree rooted with node i which is
// an index in arr[]. n is size of heap
void heapify(vector<int>& arr, int n, int i)
{
	int largest = i; // Initialize largest as root
	int l = 2 * i + 1; // left = 2*i + 1
	int r = 2 * i + 2; // right = 2*i + 2

					   // If left child is larger than root
	if (l < n && arr[l] > arr[largest])
		largest = l;

	// If right child is larger than largest so far
	if (r < n && arr[r] > arr[largest])
		largest = r;

	// If largest is not root
	if (largest != i)
	{
		int swap = arr[i];
		arr[i] = arr[largest];
		arr[largest] = swap;

		// Recursively heapify the affected sub-tree
		heapify(arr, n, largest);
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
