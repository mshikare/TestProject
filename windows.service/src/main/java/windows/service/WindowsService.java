package windows.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Scanner;

import javax.swing.filechooser.FileSystemView;

public class WindowsService {
	
	public static String runProcess(String servicename) throws IOException
	{
	    ProcessBuilder builder = new ProcessBuilder(
	            		"cmd.exe","/c", "sc query " +"\"\"" + servicename+ "\"\"" +" | findstr STATE");
	    builder.redirectErrorStream(true);
	    Process p = builder.start();
	    BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
	    String line;
	    line = r.readLine();
	    if (line == null)
	    { 
	    	System.out.println("Error");
	    }
	    return line;
	}

	public static void main(String[] args) throws IOException {
		String STATE_PREFIX = "STATE              : ";
		Scanner sc=new Scanner(System.in);
		String servicename=sc.nextLine();
		String service=runProcess(servicename);
		int ix = service.indexOf(STATE_PREFIX);
		if (ix >= 0) {
		  // compare status number to one of the states
		  String stateStr = service.substring(ix+STATE_PREFIX.length(), ix+STATE_PREFIX.length() + 1);
		  int state = Integer.parseInt(stateStr);
		  switch(state) {
		    case (1): 
		    	// service stopped
		    	System.out.println("Service stopped");
		      break;
		    case (4): 
		    	// service started
		    	System.out.println("Service running");
		      break;
		   }
		  FileSystemView filesys = FileSystemView.getFileSystemView();
	         
	        File[] drives = File.listRoots();
	       
	        if (drives != null && drives.length > 0) {
	            for (File aDrive : drives) {
	                System.out.println("Drive Letter: " + aDrive);
	                System.out.println("\tType: " + filesys.getSystemTypeDescription(aDrive));
	                System.out.println("\tTotal space: " + aDrive.getTotalSpace()/1073741824);
	                System.out.println("\tFree space: " + aDrive.getFreeSpace()/1073741824);
	                System.out.println();
	            }
	        }
	            ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
	            //OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
	            //System.out.println("CPU Usage : " + operatingSystemMXBean.getSystemLoadAverage());
	            //System.out.println("CPU Average Usage : "+operatingSystemMXBean.getAvailableProcessors());
	            //System.out.println("Total Memory : "+ operatingSystemMXBean.getTotalPhysicalMemorySize());
	            //System.out.println("Free Memory : "+ operatingSystemMXBean.getFreePhysicalMemorySize());
	            for(Long threadID : threadMXBean.getAllThreadIds()) {
	                ThreadInfo info = threadMXBean.getThreadInfo(threadID);
	                System.out.println("Thread name: " + info.getThreadName());
	                System.out.println("Thread State: " + info.getThreadState());
	                System.out.println(String.format("CPU time: %s ns", 
	                  threadMXBean.getThreadCpuTime(threadID)));
	              }
	        	MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
	        	System.out.println(String.format("Initial memory: %.2f GB", 
	        	  (double)memoryMXBean.getHeapMemoryUsage().getInit() /1073741824));
	        	System.out.println(String.format("Used heap memory: %.2f GB", 
	        	  (double)memoryMXBean.getHeapMemoryUsage().getUsed() /1073741824));
	        	System.out.println(String.format("Max heap memory: %.2f GB", 
	        	  (double)memoryMXBean.getHeapMemoryUsage().getMax() /1073741824));
	        	System.out.println(String.format("Committed memory: %.2f GB", 
	        	  (double)memoryMXBean.getHeapMemoryUsage().getCommitted() /1073741824));
	            
		}
	}
	}
