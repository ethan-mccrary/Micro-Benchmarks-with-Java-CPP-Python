import random
import time

def partition(arr, low, high):
    i = (low-1)
    pivot = arr[high]

    for j in range(low, high):

        if arr[j] < pivot:
            i = i+1
            arr[i], arr[j] = arr[j], arr[i]

    arr[i+1], arr[high] = arr[high], arr[i+1]
    return (i+1)

def quickSort(arr, low, high):
    if low < high:
        pi = partition(arr, low, high)

        quickSort(arr, low, pi-1)
        quickSort(arr, pi+1, high)

# Driver code to test above
arrLength = 10000000
warmupLength = 5000000
timeDifLength = 25
starting = [None] * timeDifLength
ending = [None] * timeDifLength
timeDif = [None] * timeDifLength
warmupArr = [None] * warmupLength
arr = [None] * arrLength

#SYSTEM INFO#
#Total number of processors or cores available#
#print("Available processors (cores): " + hardware_concurrency() + "\n")#

#Total amount of memory#
#print("Total memory (bytes): " + getTotalSystemMemory() + "\n")#


#WARMUP#
for i in range(10):
    warmupArr = [random.randint(0, 10000001) for j in range(warmupLength)]
    perc = (i/10) * 100
    print("Warming up:" + str(perc) + "%\r")
    quickSort(warmupArr, 0, warmupLength - 1)
#END WARMUP#

#BENCHMARK#
j = 1
for l in range(timeDifLength):
    perc = ((j + 1) / 25) * 100
    arr = [random.randint(0, 10000001) for m in range(arrLength)]

    print("Benchmarking. . . This may take up to 15 minutes: " + str(perc) + "%\r")
    startTime = int(round(time.time()*1000))
    quickSort(arr, 0, arrLength - 1)
    endTime = int(round(time.time()*1000))

    starting[l] = startTime
    ending[l] = endTime
    timeDif[l] = endTime - startTime
    j += 1

#PRINT RESULTS#
print("Writing Results to XML Format. . . This may take a few minutes.\r")
data = open("quickSortDataPy.xml", "w")
sum = 0
data.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
data.write("<quickSortBench>\n")
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
data.write("</quickSortBench>\n")
data.close()

print("Benchmark Complete - All Benchmark Data Saved to quickSortDataPy.xml\r")
