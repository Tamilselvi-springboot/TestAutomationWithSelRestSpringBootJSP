package com.example.demo.utilities;



import java.util.concurrent.ConcurrentHashMap;

import com.example.demo.model.StepResult;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ServiceHandler {
	
	ConcurrentHashMap<String,String> ScenInputData2;
	

	public ServiceHandler(ConcurrentHashMap<String, String> scenInputData) {
	
		ScenInputData2 = scenInputData;
	}
	
	public ServiceHandler() {
		
	}


	public String LoveCalcService(int StepNum) {
		String ServiceResponse = "";
	String ServiceURL = "https://love-calculator.p.rapidapi.com/getPercentage?fname=" + ScenInputData2.get("Param_fname" + "_" + StepNum) + "&sname=" + ScenInputData2.get("Param_sname" + "_" + StepNum);
	RequestSpecification req = RestAssured.with();
	
	ConcurrentHashMap<String,String> HeadersMap = new ConcurrentHashMap<String,String>();
	HeadersMap.put("x-rapidapi-host",  "love-calculator.p.rapidapi.com");
	HeadersMap.put("x-rapidapi-key","289a48fd78mshd68163c40d01c43p1df722jsned47b64429ff");
	
	Response res = req.headers(HeadersMap).get(ServiceURL);
	if (res.getStatusCode() ==200) {
		System.out.println("Tamilllllllllllllllllllll" + res.prettyPrint());
		ServiceResponse = res.jsonPath().get("percentage") ;
		System.out.println("******* " + "Hit the service and retrieved the actual value: " + ServiceResponse + "******* ");
		Reporting.ReportLog = Reporting.ReportLog + "\n\r" +  "Hit the service and retrieved the actual value: " + ServiceResponse  ;	
		
	
	
	
	}
	else {
		System.out.println("******* " + "Service is down" + "******* ");
		Reporting.ReportLog = Reporting.ReportLog + "\n\r" +  "Service is down" ;	
	}
	
	
	return ServiceResponse;
	}
	
	public StepResult parallelservicecall(StepResult stepResObj, int StepNum, ConcurrentHashMap<String,String> ScenInputData2) {
		
		String ServiceResponse = "";
		String ServiceURL = "https://love-calculator.p.rapidapi.com/getPercentage?fname=" + ScenInputData2.get("Param_fname" + "_" + StepNum) + "&sname=" + ScenInputData2.get("Param_sname" + "_" + StepNum);
		RequestSpecification req = RestAssured.with();
		
		ConcurrentHashMap<String,String> HeadersMap = new ConcurrentHashMap<String,String>();
		HeadersMap.put("x-rapidapi-host",  "love-calculator.p.rapidapi.com");
		HeadersMap.put("x-rapidapi-key","289a48fd78mshd68163c40d01c43p1df722jsned47b64429ff");
		
		Response res = req.headers(HeadersMap).get(ServiceURL);
		if (res.getStatusCode() ==200) {
			System.out.println("Tamilllllllllllllllllllll" + res.prettyPrint());
			ServiceResponse = res.jsonPath().get("percentage") ;
			System.out.println("******* " + "Hit the service and retrieved the actual value: " + ServiceResponse + "******* ");
			Reporting.ReportLog = Reporting.ReportLog + "\n\r" +  "Hit the service and retrieved the actual value: " + ServiceResponse  ;	
			
		}
		else {
			System.out.println("******* " + "Service is down" + "******* ");
			Reporting.ReportLog = Reporting.ReportLog + "\n\r" +  "Service is down" ;	
		}
		
		stepResObj.setActual(ServiceResponse);
		stepResObj.setStepResultLog(Reporting.ReportLog);
		Reporting.ReportLog ="";
		return stepResObj;
		
	}
}
