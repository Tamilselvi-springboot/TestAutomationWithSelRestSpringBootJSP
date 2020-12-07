package com.example.demo.utilities;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.controllerService.googleAppTestService;
import com.example.demo.model.StepResult;
import com.example.demo.model.testcaseResult;

@Component
public class Reporting {
	
	SystemUtilities SysUtilAgent = new SystemUtilities();
	public static excelUtil excelAgent;
	
	public static String ReportLog = "";
	
	public boolean CreateResultFile(MultipartFile file) throws IOException {
		boolean iscreated = false;

		if (file.isEmpty()) {
			System.out.println("Please select a file and try again");
		} else {
			byte[] bytes = file.getBytes();
			String strResultfileName = "Result_" + file.getOriginalFilename();

			iscreated = SysUtilAgent.CreateFile(googleAppTestService.AppProperites.get("resultPath"),
					strResultfileName, bytes);

			googleAppTestService.AppProperites.put("resultFilePath",
					googleAppTestService.AppProperites.get("resultPath") + strResultfileName);
			googleAppTestService.AppProperites.put("resultFileName",
					strResultfileName);

			this.excelAgent = new excelUtil(googleAppTestService.AppProperites.get("resultFilePath"));
			this.excelAgent.addResultColumn("Scenario", "TestStepStatus", "TestSteplog","TestcaseStatus");
			System.out.println("******* " + "Result file is created successfully" + " ************");
		}

		return iscreated;

	}
	
	public boolean WriteResultInFile(testcaseResult testcaseResultObj) {
		boolean boolIsWritten = false;
		
		CopyOnWriteArrayList<StepResult> ArrStepResObj = testcaseResultObj.getAllStepResult();
		
		for (StepResult eachResStep : ArrStepResObj) {
		
		boolIsWritten =Reporting.excelAgent.WriteResInExcel("Scenario" ,testcaseResultObj.getTestcaseid(),eachResStep.getStepDesc(),eachResStep.getStepResultStatus(), eachResStep.getStepResultLog(),testcaseResultObj.getTestcaseStatus() );
		
		}
		return boolIsWritten;
	}
	
	

}
