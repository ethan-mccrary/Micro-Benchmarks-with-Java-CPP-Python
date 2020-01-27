#include <iostream>
#include <fstream>
#include <ctime>
#include <cmath>
#include <chrono>
#include <thread>
#include <algorithm>
#include <vector>
#include <algorithm>    // std::min
//#include <windows.h> //Windows Based OS
#include <unistd.h> // UNIX Based OS

using namespace std::chrono;

unsigned long long getTotalSystemMemory();
void merge(std::vector<int>&, int, int);
void timSort(std::vector<int>, int);
int main() {
	int arrLength = 10000000;
	int warmupLength = 5000000;
	int timeDifLength = 500;
	auto* starting = new int[timeDifLength];
	auto* ending = new int[timeDifLength];
	auto* timeDif = new int[timeDifLength];
	std::vector<int> warmupArr(5000000);
	std::vector<int> arr(10000000);
	std::ofstream data;

	//SYSTEM INFO//
	/* Total number of processors or cores available*/
	std::cout << "Available processors (cores): " << std::thread::hardware_concurrency() << std::endl;

	/* Total amount of memory*/
	std::cout << "Total memory (bytes): " << getTotalSystemMemory() << std::endl;

	//WARMUP//
	for (double i = 0; i < 100; i++) {
		srand(time(0));
		for (int j = 0; j < warmupLength; j++) {
			warmupArr[j] = rand() % (10000000 - 0 + 1) + 0;
		}
		double perc = (i / 100) * 100;
		std::cout << "Warming Up: " << round(perc) << "%\r";
		timSort(warmupArr, warmupLength);
	}
	//END WARMUP//

	//BENCHMARK//
	double j = 1;
	for (int l = 0; l < timeDifLength; l++) {
		double perc = ((j + 1) / 500) * 100;
		for (int m = 0; m < arrLength; m++) {
			arr[m] = rand() % (10000000 - 0 + 1) + 0;
		}

		std::cout << "Benchmarking. . . This may take a few up to 15 minutes: " << round(perc) << "%\r";
		auto startTime = high_resolution_clock::now();
		timSort(arr, arrLength);
		auto endTime = high_resolution_clock::now();

		starting[l] = duration_cast<milliseconds>(startTime.time_since_epoch()).count();
		ending[l] = duration_cast<milliseconds>(endTime.time_since_epoch()).count();
		timeDif[l] = duration_cast<milliseconds>(endTime.time_since_epoch() - startTime.time_since_epoch()).count();
		j++;
	}

	//PRINT RESULTS//
	std::cout << "Writing Results to XML Format. . . This may take a few minutes.\r";

	data.open("timSortDataCpp.xml");
	int sum = 0;
	data << "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	data << "<timSortBench>\n";
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
	data << "</timSortBench>\n";
	data.close();

	std::cout << "Benchmark Complete - All Benchmark Data Saved to timSortDataCpp.xml\r";
	delete[] starting;
	delete[] ending;
	delete[] timeDif;
}

// this function sorts array from left index to
// to right index which is of size atmost RUN
void insertionSort(std::vector<int>& arr, int left, int right) {
	for (int i = left + 1; i <= right; i++)
	{
		int temp = arr[i];
		int j = i - 1;
		while (j >= left && arr[j] > temp)
		{
			arr[j + 1] = arr[j];
			j--;
		}
		arr[j + 1] = temp;
	}
}

// merge function merges the sorted runs
void merge(std::vector<int>& arr, int l, int m, int r) {
	// original array is broken in two parts
	// left and right array
	int len1 = m - l + 1, len2 = r - m;
	std::vector<int> left(len1);
	std::vector<int> right(len2);
	for (int x = 0; x < len1; x++)
	{
		left[x] = arr[l + x];
	}
	for (int x = 0; x < len2; x++)
	{
		right[x] = arr[m + 1 + x];
	}

	int i = 0;
	int j = 0;
	int k = l;

	// after comparing, we merge those two array
	// in larger sub array
	while (i < len1 && j < len2)
	{
		if (left[i] <= right[j])
		{
			arr[k] = left[i];
			i++;
		}
		else
		{
			arr[k] = right[j];
			j++;
		}
		k++;
	}

	// copy remaining elements of left, if any
	while (i < len1)
	{
		arr[k] = left[i];
		k++;
		i++;
	}

	// copy remaining element of right, if any
	while (j < len2)
	{
		arr[k] = right[j];
		k++;
		j++;
	}
}

static int RUN = 32;

// iterative Timsort function to sort the
// array[0...n-1] (similar to merge sort)
void timSort(std::vector<int> arr, int n) {

	// Sort individual subarrays of size RUN
	for (int i = 0; i < n; i += RUN) {
		insertionSort(arr, i, std::min((i + 31), (n - 1)));
	}

	// start merging from size RUN (or 32). It will merge
	// to form size 64, then 128, 256 and so on ....
	for (int size = RUN; size < n; size = 2 * size) {

		// pick starting point of left sub array. We
		// are going to merge arr[left..left+size-1]
		// and arr[left+size, left+2*size-1]
		// After every merge, we increase left by 2*size
		for (int left = 0; left < n; left += 2 * size) {

			// find ending point of left sub array
			// mid+1 is starting point of right sub array
			int mid = left + size - 1;
			int right = std::min((left + 2 * size - 1), (n - 1));

			// merge sub array arr[left.....mid] &
			// arr[mid+1....right]
			if (!(mid >= n - 1)) {
				merge(arr, left, mid, right);
			}
		}
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
