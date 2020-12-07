package com.example.demo.controllerService;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


import com.example.demo.model.AppPropertyObj;
import com.example.demo.model.StepDetails;
import com.example.demo.model.StepResult;
import com.example.demo.model.allData;
import com.example.demo.model.inputData;
import com.example.demo.utilities.Reporting;
import com.example.demo.utilities.SystemUtilities;
import com.example.demo.utilities.excelUtil;

@Component

public class googleAppTestService {
	
	public static ConcurrentHashMap<String, String> AppProperites = new ConcurrentHashMap<String,String>();
	public static ConcurrentHashMap<String, String> AutomationKeywords = new ConcurrentHashMap<String,String>();
	public static ConcurrentHashMap<String, String> GlobalMessages = new ConcurrentHashMap<String,String>();
	public static String TypeofTest = "";
	public static String Parallel = "";
	
	SystemUtilities SysUtilAgent = new SystemUtilities();
	excelUtil excelAgent;
	
	public googleAppTestService() {
		super();
		
	//	AppProperites = GetProperties();
	}

	public boolean UploadFileInServer(MultipartFile file) throws IOException {
		boolean isUploaded = false;

		if (file.isEmpty()) {
			System.out.println("Please select a file and try again");
		} else {
			byte[] bytes = file.getBytes();

			isUploaded = SysUtilAgent.CreateFile(AppProperites.get("inputPath"), file.getOriginalFilename(), bytes);

			AppProperites.put("inputFilePath", AppProperites.get("inputPath") + file.getOriginalFilename());
			AppProperites.put("inputFileName", file.getOriginalFilename());

			System.out.println("******* " + "Uploaded input file successfully" + " ************");
		}

		return isUploaded;

	}
	
		
	 public boolean GetProperties(String propfileName){
		 ConcurrentHashMap<String, String> getprop= new ConcurrentHashMap<String, String>();
		 boolean isPropretrieved = false;
		   try {
			   getprop= SysUtilAgent.ReadProperties(propfileName);
			   isPropretrieved = true;			 
			   this.AppProperites =getprop;
			   
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   return isPropretrieved;
	   }
	 
	 public boolean GetTextFileinMap(String propfileName){
		 ConcurrentHashMap<String, String> getprop= new ConcurrentHashMap<String, String>();
		 boolean isPropretrieved = false;
		   try {
			   getprop= SysUtilAgent.ReadNotePadInMap(propfileName);
			   isPropretrieved = true;
			    if (propfileName.equalsIgnoreCase("AutomationKeywordDef.txt")) {
				   this.AutomationKeywords = getprop;
			   } else if (propfileName.equalsIgnoreCase("GlobalMessages.txt")) {
				   this.GlobalMessages = getprop;
			   }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   return isPropretrieved;
	   }
	 
	
	public  allData getAlldataInMap(){
		
		allData allData1 = new allData();
		
		ConcurrentHashMap<String,inputData> allScenarios1 = new ConcurrentHashMap<String,inputData>();
		ConcurrentHashMap<String,ConcurrentHashMap<String,String>> allInputTestData1 = new ConcurrentHashMap<String,ConcurrentHashMap<String,String>>();
		ConcurrentHashMap<String,AppPropertyObj> allSeleniumObj = new 	ConcurrentHashMap<String,AppPropertyObj>();

		allScenarios1=Reporting.excelAgent.getAllScenarios("Scenario", 1);
		for (Map.Entry<String,inputData> entry : allScenarios1.entrySet())
		{
		    System.out.println("key: " + entry.getKey() );
		    
		    CopyOnWriteArrayList<StepDetails> ArrStepResObj =entry.getValue().getStepDetails();
			
			for (StepDetails eachStep :ArrStepResObj) {
				System.out.println("step desc: " + eachStep.getStepDesc() + "; step exp: " + eachStep.getExpected());
			}
		}
		allInputTestData1=Reporting.excelAgent.getAllTestData("Data");
		allSeleniumObj=Reporting.excelAgent.getObjectRep("ObjRep");
		AppProperites.put("newfeatureFilePath",SysUtilAgent.generateFeatureFile(AppProperites.get("DynamicfeatureFileFolderPath"),AppProperites.get("featurefileFormatfilePath"),AppProperites.get("DynamicfeatureFileName"),allScenarios1));
		
		
		allData1.setAllScenarios(allScenarios1);
		allData1.setAllInputTestData(allInputTestData1);		
		allData1.setObjRep(allSeleniumObj);
		
		return allData1;
		
	}
}
