import random
import time
  
RUN = 32

def insertionSort(arr, left, right):  
   
    for i in range(left + 1, right+1):  
       
        temp = arr[i]  
        j = i - 1 
        while arr[j] > temp and j >= left:  
           
            arr[j+1] = arr[j]  
            j -= 1
           
        arr[j+1] = temp  

def merge(arr, l, m, r):
    len1, len2 =  m - l + 1, r - m  
    left, right = [], []  
    for i in range(0, len1):  
        left.append(arr[l + i])  
    for i in range(0, len2):  
        right.append(arr[m + 1 + i])  
    
    i, j, k = 0, 0, l 
    while i < len1 and j < len2:  
       
        if left[i] <= right[j]:  
            arr[k] = left[i]  
            i += 1 
           
        else: 
            arr[k] = right[j]  
            j += 1 
           
        k += 1

    while i < len1:  
       
        arr[k] = left[i]  
        k += 1 
        i += 1
    
    while j < len2:  
        arr[k] = right[j]  
        k += 1
        j += 1
      
def timSort(arr, n):  
    for i in range(0, n, RUN):  
        insertionSort(arr, i, min((i+31), (n-1)))  

    size = RUN 
    while size < n:
        for left in range(0, n, 2*size):
            mid = min((left + size - 1), (n - 1))
            right = min((left + 2*size - 1), (n-1))
            merge(arr, left, mid, right)  
          
        size = 2*size 

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
    timSort(warmupArr, len(warmupArr))
#END WARMUP#

#BENCHMARK#
j = 1
for l in range(timeDifLength):
    perc = ((j + 1) / 25) * 100
    arr = [random.randint(0, 10000001) for m in range(arrLength)]

    print("Benchmarking. . . This may take up to 15 minutes: " + str(perc) + "%\r")
    startTime = int(round(time.time()*1000))
    timSort(arr, len(arr))
    endTime = int(round(time.time()*1000))

    starting[l] = startTime
    ending[l] = endTime
    timeDif[l] = endTime - startTime
    j += 1
#PRINT RESULTS#
print("Writing Results to XML Format. . . This may take a few minutes.\r")
data = open("timSortDataPy.xml", "w")
sum = 0
data.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
data.write("<timSortBench>\n")
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
data.write("</timSortBench>\n")
data.close()

print("Benchmark Complete - All Benchmark Data Saved to timSortDataPy.xml\r")