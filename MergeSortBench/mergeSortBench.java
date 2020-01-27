import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class mergeSortBench {

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

          mergeSort(warmupArr, 0, warmupArr.length-1);
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
          mergeSort(arr, 0, arr.length-1);
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
          FileWriter pw = new FileWriter("mergeSortDataJava.xml");
          pw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
          pw.write("<mergeSortBench>\n");

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
          pw.write("</mergeSortBench>\n");
          pw.close();
        } catch (IOException e) {

        }
        System.out.print("Benchmark Complete - All Benchmark Data Saved to mergeSortDataJava.xml\r");
    }

    static void mergeSort(int[] arr, int l, int r) {
    	if (l < r) {
    		int m = l + (r - l) / 2;
    		mergeSort(arr, l, m);
    		mergeSort(arr, m + 1, r);
    		merge(arr, l, m, r);
    	}
    }

    static void merge(int[] arr, int l, int m, int r) {
    	int i, j, k;
    	int n1 = m - l + 1;
    	int n2 = r - m;

    	/* create temp arrays */
    	int[] L = new int[n1];
      int[] R = new int[n2];

    	for (i = 0; i < n1; i++) {
    		L[i] = arr[l + i];
    	}

    	for (j = 0; j < n2; j++) {
    		R[j] = arr[m + 1 + j];
    	}

    	i = 0;
    	j = 0;
    	k = l;

    	while (i < n1 && j < n2) {
    		if (L[i] <= R[j]) {
    			arr[k] = L[i];
    			i++;
    		} else {
    			arr[k] = R[j];
    			j++;
    		}
    		k++;
    	}

    	while (i < n1) {
    		arr[k] = L[i];
    		i++;
    		k++;
    	}

    	while (j < n2) {
    		arr[k] = R[j];
    		j++;
    		k++;
    	}
    }
}
