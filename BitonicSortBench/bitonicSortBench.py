import random
import time

# Python program for Bitonic Sort. Note that this program 
# works only when size of input is a power of 2.
def compAndSwap(a, i, j, dire): 
    if (dire==1 and a[i] > a[j]) or (dire==0 and a[i] < a[j]): 
        a[i],a[j] = a[j],a[i] 

def bitonicMerge(a, low, cnt, dire): 
    if cnt > 1: 
        k = cnt/2
        for i in range(low , low+k): 
            compAndSwap(a, i, i+k, dire) 
        bitonicMerge(a, low, k, dire) 
        bitonicMerge(a, low+k, k, dire) 

def bitonicSort(a, low, cnt,dire): 
    if cnt > 1: 
        k = cnt/2
        bitonicSort(a, low, k, 1) 
        bitonicSort(a, low+k, k, 0) 
        bitonicMerge(a, low, cnt, dire) 

#Driver code for tests
arrLength = 8388608
warmupLength = 4194304
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
    bitonicSort(warmupArr, 0, warmupLength, 1)
#END WARMUP#

#BENCHMARK#
j = 1
for i in range(timeDifLength):
    perc = ((j + 1) / 25) * 100
    arr = [random.randint(0, 10000001) for j in range(arrLength)]

    print("Benchmarking: " + str(perc) + "%\r")

    startTime = int(round(time.time() * 1000))
    bitonicSort(arr, 0, arrLength, 1)
    endTime = int(round(time.time() * 1000))

    starting[i] = startTime
    ending[i] = endTime
    timeDif[i] = endTime - startTime
    j += 1
#END BENCHMARK#

#PRINT RESULTS#
print("Writing Results to XML Format. . . This may take a few minutes.\r")

data = open("bitonicSortDataPy.xml", "w")
sum = 0
data.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
data.write("<bitonicSortBench>\n")
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
    data.write("\t\t<test id=\"" + str((i + 1)) + "\">\n")
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
data.write("</bitonicSortBench>\n")
data.close()

print("Benchmark Complete - All Benchmark Data Saved to bitonicSortDataPy.xml\r")