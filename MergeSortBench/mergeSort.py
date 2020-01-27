import random
import time

def mergeSort(arr, p, r):
    q = int((p+r)/2)
    if p+1 <= r:
        mergeSort(arr, p, q)
        mergeSort(arr, q+1, r)
        merge(arr, p, q, r)

def merge(arr, p, q, r):
    i, j, t = p, q+1, 0
    temp = [0] * (r-p+1)

    while i <= q and j <= r:
        if arr[i] < arr[j]:
            temp[t] = arr[i]
            t += 1
            i += 1
        else:
            temp[t] = arr[j]
            t += 1
            j += 1

    while i <= q:
        temp[t] = arr[i]
        t += 1
        i += 1
        if i > q: break

    while j <= r:
        temp[t] = arr[j]
        t+=1
        j+=1
        if j > r: break

    for x in range(r-p+1):
        arr[p+x] = temp[x]

    return arr

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
    print("Warming up: " + str(perc) + "%\r")
    mergeSort(warmupArr, 0, len(warmupArr) - 1)
#END WARMUP#

#BENCHMARK#
j = 1
for l in range(timeDifLength):
    perc = ((j + 1) / 25) * 100
    arr = [random.randint(0, 10000001) for j in range(arrLength)]

    print("Benchmarking. . . This may take up to 15 minutes: " + str(perc) + "%\r")
    startTime = int(round(time.time()*1000))
    mergeSort(arr, 0, len(arr) - 1)
    endTime = int(round(time.time()*1000))

    starting[l] = startTime
    ending[l] = endTime
    timeDif[l] = endTime - startTime
    j += 1

#PRINT RESULTS#
print("Writing Results to XML Format. . . This may take a few minutes.\r")
data = open("mergeSortDataPy.xml", "w")
sum = 0
data.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
data.write("<mergeSortBench>\n")
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
data.write("</mergeSortBench>\n")
data.close()

print("Benchmark Complete - All Benchmark Data Saved to mergeSortDataPy.xml\r")