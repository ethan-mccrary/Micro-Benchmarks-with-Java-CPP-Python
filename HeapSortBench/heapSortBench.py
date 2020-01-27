import random
import time

# Python program for implementation of heap Sort 

def heapify(arr, n, i): 
    largest = i
    l = 2 * i + 1
    r = 2 * i + 2 
  
    if l < n and arr[i] < arr[l]: 
        largest = l 
   
    if r < n and arr[largest] < arr[r]: 
        largest = r 
  
    if largest != i: 
        arr[i],arr[largest] = arr[largest],arr[i] 
 
        heapify(arr, n, largest) 

def heapSort(arr): 
    n = len(arr) 

    for i in range(n, -1, -1): 
        heapify(arr, n, i) 

    for i in range(n-1, 0, -1): 
        arr[i], arr[0] = arr[0], arr[i]
        heapify(arr, i, 0)        

#Driver code for tests
arrLength = 10000000
warmupLength = 5000000
timeDifLength = 25
starting = [None] * timeDifLength
ending = [None] * timeDifLength
timeDif = [None] * timeDifLength
warmupArr = [None] * warmupLength
arr = [None] * arrLength

#WARMUP#
for i in range(10):
    warmupArr = [random.randint(0, 10000001) for j in range(warmupLength)]
    perc = (i/10) * 100
    print("Warming Up: " + str(perc) + "%\r")
    heapSort(warmupArr)
#END WARMUP#

#BENCHMARK#
j = 1
for i in range(timeDifLength):
    perc = ((j + 1) / 25) * 100
    arr = [random.randint(0, 10000001) for j in range(arrLength)]

    print("Benchmarking: " + str(perc) + "%\r")

    startTime = int(round(time.time() * 1000))
    heapSort(arr)
    endTime = int(round(time.time() * 1000))

    starting[i] = startTime
    ending[i] = endTime
    timeDif[i] = endTime - startTime
    j += 1
#END BENCHMARK#

#PRINT RESULTS#
print("Writing Results to XML Format. . . This may take a few minutes.\r")
data = open("heapSortDataPy.xml", "w")
sum = 0
data.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
data.write("<heapSortBench>\n")
data.write("\t<systemInfo>\n")
data.write("\t\t<processor>")
# data.write("\t\t\t<coreCount>" << thread::hardware_concurrency() << "</coreCount>")
data.write("\t\t</processor>\n")
data.write("\t\t<memory>\n")
# data.write("\t\t\t<freeMemoryBytes>" << getTotalSystemMemory() << "</freeMemoryBytes>\n")
data.write("\t\t</memory>\n")
data.write("\t</systemInfo>\n")
data.write("\t<tests>\n")

for i in range(timeDifLength):
    data.write("\t\t<test id=\"" + str(i + 1) + "\">\n")
    data.write("\t\t\t<startTime>" + str(starting[i]) + "</startTime>\n")
    data.write("\t\t\t<endTime>" + str(ending[i]) + "</endTime>\n")
    data.write("\t\t\t<excTime>" + str(timeDif[i]) + "</excTime>\n")
    data.write("\t\t</test>\n")
    sum += timeDif[i]

avg = (sum / timeDifLength)
data.write("\t</tests>\n")
data.write("\t<results>\n")
data.write("\t\t<totalBenchTime>" + str(ending[timeDifLength - 1] - starting[0]) + "</totalBenchTime>\n")
data.write("\t\t<avg>" + str(avg) + "</avg>\n")
data.write("\t</results>\n")
data.write("</heapSortBench>\n")
data.close()

print("Benchmark Complete - All Benchmark Data Saved to heapSortDataPy.xml\r")