package com.example.demo.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.controllerService.googleAppTestService;
import com.example.demo.model.allData;
import com.example.demo.utilities.Reporting;

import cucumber.api.cli.Main;


@Controller

public class controller {
	
	@Autowired 
	public static allData mad;
	//public static allData mad = new allData();
	@Autowired 
	public Reporting Reporter;
	@Autowired
	googleAppTestService serviceImp;
	long startTime;	 
	long stopTime;
	long TCExecutionTime = 0;
	
	@RequestMapping(value ="/", method = RequestMethod.GET)
	public ModelAndView renderHomePage() {
		serviceImp.GetProperties("application.properties");
		serviceImp.GetTextFileinMap("AutomationKeywordDef.txt");
		serviceImp.GetTextFileinMap("GlobalMessages.txt");
		ModelAndView home = new ModelAndView("home");
		return home;
	}
	

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ModelAndView uploadFileHandler(@RequestParam("name") String name,@RequestParam(value="parallel", required=false ) String ParallelChecked,@RequestParam(value="UI", required=false) String UIChecked,@RequestParam(value="Service",required=false) String ServiceChecked,
            @RequestParam("file") MultipartFile file) throws IOException {
    	startTime = System.nanoTime();
    	
    	serviceImp.UploadFileInServer(file);    
    	Reporter.CreateResultFile(file);
    	mad = serviceImp.getAlldataInMap();
    	if (!(ServiceChecked ==null)) {
    		googleAppTestService.TypeofTest = ServiceChecked;
    	} else {
    		googleAppTestService.TypeofTest = "UI";
    	} 
    	if (!(ParallelChecked ==null)) {
    		googleAppTestService.Parallel = ParallelChecked;
    	}
    	
    
    	//String [] argv = new String[]{ "-g","com.example.demo.StepDefinition","./src/main/java/com/example/demo/model/GoogleFeatures.feature"};
    	//System.out.println("22222222222222222 " + serviceImp.AppProperites.get("newfeatureFilePath") );
    	//String [] argv = new String[]{ "-g","com.example.demo.StepDefinition",serviceImp.AppProperites.get("newfeatureFilePath"),"1"};
    	String [] argv = new String[]{ "-g","com.example.demo.StepDefinition",serviceImp.AppProperites.get("newfeatureFilePath")};
		//String [] argv = new String[]{ "-g","cucumberdemo","./src/main/java/Featurefile/GoogleFeatures.feature","-p",
		//"com.vimalselvam.cucumber.listener.ExtentCucumberFormatter" + ReportPath };
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		try {
			byte exitstatus = Main.run(argv, contextClassLoader);
		} 
		catch (IOException e) {
			
			e.printStackTrace();
		}
		stopTime = System.nanoTime();
		TCExecutionTime =  stopTime - startTime;
		long TCExecutionseconds = TimeUnit.SECONDS.convert(TCExecutionTime, TimeUnit.NANOSECONDS);
		long TCExecutionminutes = TimeUnit.MINUTES.convert(TCExecutionTime, TimeUnit.NANOSECONDS);
		//long TCExecutionminutes = TimeUnit.NANOSECONDS
			 //   .toMinutes(TCExecutionTime);
		System.out.println("Execution is completed. Time taken for execution is " + TCExecutionseconds + " seconds");
		System.out.println("Execution is completed. Time taken for execution is " + TCExecutionminutes + " minutes");
		startTime = 0;
		stopTime =0;
		//System.out.println("Done");
		if (!(ServiceChecked ==null)) {
    		googleAppTestService.TypeofTest = "";
    	} else {
    		googleAppTestService.TypeofTest = "";
    	} 
    	if (!(ParallelChecked ==null)) {
    		googleAppTestService.Parallel = "";
    	}
		
    	ModelAndView home = new ModelAndView("result");
    	return home;
    	
    	
    }
    
    @RequestMapping(value="/downloadLogFile")
    public void getLogFile(HttpSession session,HttpServletResponse response) throws Exception {
        try {
            String filePathToBeServed = googleAppTestService.AppProperites.get("resultFilePath");
            File fileToDownload = new File(filePathToBeServed);
            InputStream inputStream = new FileInputStream(fileToDownload);
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment; filename="+googleAppTestService.AppProperites.get("resultFileName")+".xlsx"); 
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            inputStream.close();
        } catch (Exception e){
           // LOGGER.debug("Request could not be completed at this moment. Please try again.");
            e.printStackTrace();
        }

    }

    
  

}
