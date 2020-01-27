import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class heapSortBench {

    public static void main(String[] args) {
        int[] arr = new int[10000000];
        int[] warmupArr = new int[5000000];
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

          heapSort(warmupArr);
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
          heapSort(arr);
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
          FileWriter pw = new FileWriter("heapSortDataJava.xml");
          pw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
          pw.write("<heapSortBench>\n");

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
          pw.write("</heapSortBench>\n");
          pw.close();
        } catch (IOException e) {

        }
        System.out.print("Benchmark Complete - All Benchmark Data Saved to heapSortDataJava.xml\r");
    }
	
	public static void heapSort(int arr[]) 
    { 
        int n = arr.length; 
  
        // Build heap (rearrange array) 
        for (int i = n / 2 - 1; i >= 0; i--) 
            heapify(arr, n, i); 
  
        // One by one extract an element from heap 
        for (int i=n-1; i>=0; i--) 
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
    public static void heapify(int arr[], int n, int i) 
    { 
        int largest = i; // Initialize largest as root 
        int l = 2*i + 1; // left = 2*i + 1 
        int r = 2*i + 2; // right = 2*i + 2 
  
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
}
