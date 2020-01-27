import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class bitonicSortBench {

    public static void main(String[] args) {
        int[] arr = new int[8388608];
        int[] warmupArr = new int[4194304];
        long[] starting = new long[500];
        long[] ending = new long[500];
        long[] timeDif = new long[500];
        DecimalFormat fmt = new DecimalFormat("#");
        Random rand = new Random();

        //SYSTEM INFO//
        /* Total number of processors or cores available to the JVM */
        System.out.println("Available processors (cores): " +
            Runtime.getRuntime().availableProcessors());

        /* Total amount of free memory available to the JVM */
        System.out.println("Free memory (bytes): " +
            Runtime.getRuntime().freeMemory());

        /* This will return Long.MAX_VALUE if there is no preset limit */
        long maxMemory = Runtime.getRuntime().maxMemory();
        /* Maximum amount of memory the JVM will attempt to use */
        System.out.println("Maximum memory (bytes): " +
            (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));

        /* Total memory currently available to the JVM */
        System.out.println("Total memory available to JVM (bytes): " +
            Runtime.getRuntime().totalMemory());
        //END SYSTEM INFO//

        //WARMUP//
        for (double j = 1; j <= 100; j++) {
          for (int k = 0; k < warmupArr.length; k++) {
            warmupArr[k] = rand.nextInt(10000000);
          }
          double perc = (j / 100) * 100;
          System.out.print("Warming Up: " + fmt.format(perc) + "%\r");

          bitonicSort(warmupArr, 0, warmupArr.length, 1);
        }
        //END WARMUP//

        //BENCHMARK//
        double j = 1;
        for (int l = 0; l < timeDif.length; l++) {
          double perc = ((j + 1) / 500) * 100;
          for (int m = 0; m < arr.length; m++) {
            arr[m] = rand.nextInt(10000000);
          }
          System.out.print("Benchmarking. . . This may take a few minutes: " + fmt.format(perc) + "%\r");

          long startTime = System.currentTimeMillis();
          bitonicSort(arr, 0, arr.length, 1);
          long endTime = System.currentTimeMillis();

          starting[l] = startTime;
          ending[l] = endTime;
          timeDif[l] = endTime - startTime;
          j++;
        }
        //END BENCHMARK//

        //PRINT RESULTS//
        System.out.print("Writing Results to XML Format. . . This may take a few minutes.\r");

        int sum = 0;
        String nameOS = "os.name";
        String versionOS = "os.version";
        String architectureOS = "os.arch";
        String jreName = "java.specification.name";
        String jVMName = "java.vm.name";
        String jVersion = "java.version";
        String jVMVersion = "java.vm.specification.version";
        String jClassFormat = "java.class.version";
        try {
          FileWriter pw = new FileWriter("bitonicSortDataJava.xml");
          pw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
          pw.write("<bitonicSortBench>\n");

          pw.write("\t<systemInfo>\n");
          pw.write("\t\t<java>\n");
            pw.write("\t\t\t<jreName>" + System.getProperty(jreName) + "</jreName>\n");
            pw.write("\t\t\t<jVMName>" + System.getProperty(jVMName) + "</jVMName>\n");
            pw.write("\t\t\t<jVersion>" + System.getProperty(jVersion) + "</jVersion>\n");
            pw.write("\t\t\t<jVMVersion>" + System.getProperty(jVMVersion) + "</jVMVersion>\n");
            pw.write("\t\t\t<jClassFormat>" + System.getProperty(jClassFormat) + "</jClassFormat>\n");
          pw.write("\t\t</java>\n");
            pw.write("\t\t<os>\n");
              pw.write("\t\t\t<osName>" + System.getProperty(nameOS) + "</osName>\n");
              pw.write("\t\t\t<osVersion>" + System.getProperty(versionOS) + "</osVersion>\n");
              pw.write("\t\t\t<osArchitechture>" + System.getProperty(architectureOS) + "</osArchitechture>\n");
            pw.write("\t\t</os>\n");
            pw.write("\t\t<processor>\n");
              pw.write("\t\t\t<coreCount>" + Runtime.getRuntime().availableProcessors() + "</coreCount>\n");
            pw.write("\t\t</processor>\n");
            pw.write("\t\t<memory>\n");
              pw.write("\t\t\t<freeMemoryBytes>" + Runtime.getRuntime().freeMemory() + "</freeMemoryBytes>\n");
              maxMemory = Runtime.getRuntime().maxMemory();
              pw.write("\t\t\t<maxMemory>" + (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory) + "</maxMemory>\n");
              pw.write("\t\t\t<remainingMemory>" + Runtime.getRuntime().totalMemory() + "</remainingMemory>\n");
            pw.write("\t\t</memory>\n");
          pw.write("\t</systemInfo>\n");

          pw.write("\t<tests>\n");
          for (int i = 0; i < timeDif.length; i++) {
            pw.write("\t\t<test id=\"" + (i + 1) + "\">");
            pw.write("\t\t\t<startTime>" + starting[i] + "</startTime>\n");
            pw.write("\t\t\t<endTime>" + ending[i] + "</endTime>\n");
            pw.write("\t\t\t<excTime>" + timeDif[i] + "</excTime>\n");
            pw.write("\t\t</test>\n");
            sum += timeDif[i];
          }
          int avg = sum / timeDif.length;
          pw.write("\t</tests>\n");
          pw.write("\t<results>\n");
          pw.write("\t\t<totalBenchTime>" + (ending[timeDif.length - 1] - starting[0]) + "</totalBenchTime>\n");
          pw.write("\t\t<avg>" + avg + "</avg>\n");
          pw.write("\t</results>\n");
          pw.write("</bitonicSortBench>\n");
          pw.close();
        } catch (IOException e) {

        }
        System.out.print("Benchmark Complete - All Benchmark Data Saved to bitonicSortDataJava.xml\r");
    }

    static void compAndSwap(int a[], int i, int j, int dir) 
    { 
        if ( (a[i] > a[j] && dir == 1) || 
             (a[i] < a[j] && dir == 0)) 
        { 
            // Swapping elements 
            int temp = a[i]; 
            a[i] = a[j]; 
            a[j] = temp; 
        } 
    } 
  
    /* It recursively sorts a bitonic sequence in ascending 
       order, if dir = 1, and in descending order otherwise 
       (means dir=0). The sequence to be sorted starts at 
       index position low, the parameter cnt is the number 
       of elements to be sorted.*/
    static void bitonicMerge(int a[], int low, int cnt, int dir) 
    { 
        if (cnt>1) 
        { 
            int k = cnt/2; 
            for (int i=low; i<low+k; i++) 
                compAndSwap(a,i, i+k, dir); 
            bitonicMerge(a,low, k, dir); 
            bitonicMerge(a,low+k, k, dir); 
        } 
    } 
  
    /* This funcion first produces a bitonic sequence by 
       recursively sorting its two halves in opposite sorting 
       orders, and then  calls bitonicMerge to make them in 
       the same order */
    static void bitonicSort(int a[], int low, int cnt, int dir) 
    { 
        if (cnt>1) 
        { 
            int k = cnt/2; 
  
            // sort in ascending order since dir here is 1 
            bitonicSort(a, low, k, 1); 
  
            // sort in descending order since dir here is 0 
            bitonicSort(a,low+k, k, 0); 
  
            // Will merge wole sequence in ascending order 
            // since dir=1. 
            bitonicMerge(a, low, cnt, dir); 
        } 
    }
}
