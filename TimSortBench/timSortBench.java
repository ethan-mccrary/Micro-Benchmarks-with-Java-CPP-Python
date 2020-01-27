import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class timSortBench {

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

          timSort(warmupArr, warmupArr.length);
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
          timSort(arr, arr.length);
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
          FileWriter pw = new FileWriter("timSortDataJava.xml");
          pw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
          pw.write("<timSortBench>\n");

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
          pw.write("</timSortBench>\n");
          pw.close();
        } catch (IOException e) {

        }
        System.out.print("Benchmark Complete - All Benchmark Data Saved to timSortDataJava.xml\r");
    }

    static int RUN = 32;

    // this function sorts array from left index to
    // to right index which is of size atmost RUN
    public static void insertionSort(int[] arr, int left, int right)
    {
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
    public static void merge(int[] arr, int l,
                                int m, int r)
    {
        // original array is broken in two parts
        // left and right array
        int len1 = m - l + 1, len2 = r - m;
        int[] left = new int[len1];
        int[] right = new int[len2];
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

    // iterative Timsort function to sort the
    // array[0...n-1] (similar to merge sort)
    public static void timSort(int[] arr, int n)
    {

        // Sort individual subarrays of size RUN
        for (int i = 0; i < n; i += RUN)
        {
            insertionSort(arr, i, Math.min((i + 31), (n - 1)));
        }

        // start merging from size RUN (or 32). It will merge
        // to form size 64, then 128, 256 and so on ....
        for (int size = RUN; size < n; size = 2 * size)
        {

            // pick starting point of left sub array. We
            // are going to merge arr[left..left+size-1]
            // and arr[left+size, left+2*size-1]
            // After every merge, we increase left by 2*size
            for (int left = 0; left < n; left += 2 * size)
            {

                // find ending point of left sub array
                // mid+1 is starting point of right sub array
                int mid = left + size - 1;
                int right = Math.min((left + 2 * size - 1), (n - 1));

                // merge sub array arr[left.....mid] &
                // arr[mid+1....right]
				if (!(mid >= n-1)) {
					merge(arr, left, mid, right);
				}
            }
        }
    }
}
