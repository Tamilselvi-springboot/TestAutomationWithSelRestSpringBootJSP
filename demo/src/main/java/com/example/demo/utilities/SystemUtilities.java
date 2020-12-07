package com.example.demo.utilities;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;

import com.example.demo.controller.controller;
import com.example.demo.model.StepDetails;
import com.example.demo.model.StepResult;
import com.example.demo.model.inputData;
import com.example.demo.model.testcaseResult;



@Component

public class SystemUtilities {

	public boolean CreateFile(String folderPath, String fileName, byte[] content) {
		// read and write the file to the selected location-
		boolean isFileCreated = false;
		try {

			File dir = new File(folderPath);
			if (!dir.exists()) {
				dir.mkdirs();
				System.out.println("************** " + "Created directory" + "**************");
			}

			Path path = Paths.get(folderPath + fileName);
			Files.write(path, content);
			 System.out.println("******* " + fileName + " file is created successfully" + "******* ");
			isFileCreated = true;

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("******* " + "problem in uploading file" + " ************");
		}
		return isFileCreated;
	}
	
	public String generateFeatureFile(String NewFeaturefileFolderPath, String featurefileformatwithFilePath,String featureFileName,
			ConcurrentHashMap<String, inputData> allScenarios) {
		String newFeatureFilePath = "";

		byte[] filecont = null;
		try {
			filecont = Files.readAllBytes(Paths.get(featurefileformatwithFilePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (CreateFile(NewFeaturefileFolderPath, featureFileName, filecont)) {
			String textToWrite = "";
			ConcurrentHashMap<String, inputData> allscenarios = allScenarios;
			
			for (Map.Entry<String, inputData> entry : allscenarios.entrySet()) {
				String key = entry.getKey();
				inputData value = entry.getValue();
				
				CopyOnWriteArrayList<StepDetails> ArrStepDetObj =value.getStepDetails();
				String scenariosteps=value.getScenarioDesc();
				/**
				for (StepDetails eachStep :ArrStepDetObj) {
					scenariosteps = 	scenariosteps + "\r\n" + eachStep.getStepDesc() + "-" + eachStep.getExpected() ;
				}
				**/
				textToWrite = textToWrite + "\r\n | " + key + " | ScenarioDesc: " + 
						 scenariosteps + " | ";
				
			}

			newFeatureFilePath = NewFeaturefileFolderPath + featureFileName;
			try {
				System.out.println("******* " + "Feature file is created successfully" + "******* ");
				Files.write(Paths.get(newFeatureFilePath), textToWrite.getBytes(), StandardOpenOption.APPEND);
			} catch (IOException e) {
				// exception handling left as an exercise for the reader
			}

		}
		return newFeatureFilePath;
	}
	
	 public ConcurrentHashMap<String, String> ReadProperties(String fileName) throws IOException {
		  ConcurrentHashMap<String, String> readprop = new ConcurrentHashMap<String,String>();
		   Properties prop = new Properties();
			String propFileName = fileName;
			//System.out.println("*******" + controller.class.getClassLoader() );
			InputStream inputStream = controller.class.getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
				System.out.println("******* " + "Storing app properties in map is successful"+ "******* ");
			} else {
				System.out.println(" ******* " + "Inputfile is empty" + " ************" );
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
				
			}
			Enumeration<?> properites = prop.propertyNames();
				// get the property value and print it out
			while(properites.hasMoreElements()) {
			String key =(String) properites.nextElement();
			String value =prop.getProperty(key);
			readprop.put(key, value);
			}
			return readprop;
	 }
	 
	 public ConcurrentHashMap<String, String> ReadNotePadInMap(String fileName) throws IOException {
		  ConcurrentHashMap<String, String> readprop = new ConcurrentHashMap<String,String>();
		   Properties prop = new Properties();
			String propFileName = fileName;
			//File currentDirectory = new File(new File(".").getAbsolutePath());
			//System.out.println(currentDirectory.getCanonicalPath());
			File file = new File("src/main/resources/" + fileName);
			String absolutePath = file.getAbsolutePath();
			System.out.println(absolutePath);
			
			//System.out.println("*******" + new File("").getAbsolutePath() );
			//System.out.println("*******" + this.getClass().getClassLoader().getResource("").getPath() );
			//InputStream inputStream = Files.newInputStream(absolutePath);
			//InputStream inputStream = controller.class.getClassLoader().getResourceAsStream(propFileName);
			InputStream inputStream = new FileInputStream(absolutePath);
			new BufferedInputStream(inputStream);
			if (inputStream != null) {
				prop.load(inputStream);
				System.out.println("******* " + "Reading properties from " + fileName + " to map is successful" + " *******");
			} else {
				System.out.println(" ******* " + "Inputfile is empty" + " ************" );
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
				
			}
			Enumeration<?> properites = prop.propertyNames();
				// get the property value and print it out
			while(properites.hasMoreElements()) {
			String key =(String) properites.nextElement();
			String value =prop.getProperty(key);
			readprop.put(key, value);
			}
			return readprop;
	 }
	 public testcaseResult createScenarioResultObj(inputData ScenarioInputData) {
		 	testcaseResult resobj ;
		 	String testcaseid;			
			CopyOnWriteArrayList<StepResult> AllStepResult = new CopyOnWriteArrayList<StepResult>();
			CopyOnWriteArrayList<StepDetails> StepDet=new CopyOnWriteArrayList<StepDetails>();
			
			String testcaseStatus;
			// String preConditionaAndStepDescription=ScenarioInputData.getPreCondition();
			 testcaseid= ScenarioInputData.getScenarioNum();
			 testcaseStatus ="Not Started";
			 StepDet = ScenarioInputData.getStepDetails();
			// Iterator<StepDetails> itr1 = StepDet.iterator();
			 for (StepDetails eachstep : StepDet) { 
				 String StepDesc;
				String Expected;
				String Actual;
				String StepResultStatus;
				String stepResultLog;	
				String stepValidationType;
				StepDesc =eachstep.getStepDesc();
				Expected=eachstep.getExpected();
				Actual="";
				StepResultStatus="Not Started";
				stepResultLog="";
				
				stepValidationType ="";
				
				StepResult stepres = new StepResult(StepDesc,Expected,Actual,StepResultStatus, stepResultLog,stepValidationType);
				AllStepResult.add(stepres) ;
		          	
		      }			 
	
			 resobj = new  testcaseResult(testcaseid,testcaseStatus,AllStepResult);
			 CopyOnWriteArrayList<StepResult> ArrStepResObj = resobj.getAllStepResult();
				
				for (StepResult eachResStep : ArrStepResObj) {	
					System.out.println("Tamilllllllllll" + eachResStep.getExpected());
				}
			 System.out.println("******* " + "Creating Result object is successful" + "******* ");
			 return 	resobj;	 
		 
	 }

}
