package com.example.demo.utilities;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

//import com.example.demo.utilities.Reporting;
import com.example.demo.appModules.SeleniumAppFunctions;
import com.example.demo.controller.controller;
import com.example.demo.controllerService.googleAppTestService;
import com.example.demo.model.AppPropertyObj;
import com.example.demo.model.StepDetails;
import com.example.demo.model.StepResult;
import com.example.demo.model.inputData;
import com.example.demo.model.testcaseResult;

public class StepHandler {
	public static String validationType = "";
	public testcaseResult ExecuteStep(testcaseResult scenresobj , ConcurrentHashMap<String,String> ScenInputData) throws Throwable {
		boolean boolisExecuted = false; 
		boolean isActResCaptured = false;
		int step =0;
		CopyOnWriteArrayList<StepResult> ArrStepResObj =scenresobj.getAllStepResult();
		
		CopyOnWriteArrayList<StepResult> ArrStepResObj2 = scenresobj.getAllStepResult();
		
		if (googleAppTestService.TypeofTest.equalsIgnoreCase("Service")) {

			ServiceHandler ServiceHandle = new ServiceHandler(ScenInputData);
			LinkedHashMap<String, Future<StepResult>> resultList = null;
			ExecutorService executor = null;

			if (googleAppTestService.Parallel.equalsIgnoreCase("parallel")) {
				executor = Executors.newFixedThreadPool(10);
				resultList = new LinkedHashMap<String, Future<StepResult>>();
			}

			for (StepResult eachResStep : ArrStepResObj) {
				step = step + 1;

				if (googleAppTestService.Parallel.equalsIgnoreCase("parallel")) {

					Callable<StepResult> task = new LoveServiceParallelModule(eachResStep, step, ScenInputData);

					Future<StepResult> result = executor.submit(task);
					resultList.put(eachResStep.getStepDesc().concat(eachResStep.getExpected()), result);

				} else {
					eachResStep.setActual(ServiceHandle.LoveCalcService(step));
					eachResStep.setStepResultLog(Reporting.ReportLog);
					Reporting.ReportLog = "";

				}

			}

			if (googleAppTestService.Parallel.equalsIgnoreCase("parallel")) {
				executor.shutdown();
				try {
					executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (googleAppTestService.Parallel.equalsIgnoreCase("parallel")) {
				for (StepResult eachResStep : ArrStepResObj) {
					String stepdescExp = eachResStep.getStepDesc().concat(eachResStep.getExpected());
					Future<StepResult> future = resultList.get(stepdescExp);
					StepResult eachstepf = (StepResult) future.get();
					String stepdescExpf = eachstepf.getStepDesc().concat(eachResStep.getExpected());
					if (stepdescExpf.equalsIgnoreCase(stepdescExp)) {
						eachResStep.setActual(eachstepf.getActual());
						eachResStep.setStepResultLog(eachstepf.getStepResultLog());
					}

				}

			}

		} else {
		
		 SeleniumAppFunctions SelAppFuncHandler = new SeleniumAppFunctions( ScenInputData);
				for (StepResult eachResStep :ArrStepResObj) {
					step = step+ 1;	
					System.out.println(eachResStep.getExpected());
				
				 String stepDetails=eachResStep.getStepDesc();
				 String stepExpected = eachResStep.getExpected();
				 String funcToExecute = "";
				 String pageName = "";
				 	
				 String ExpectedpageName = "";
				
				 
					for (Map.Entry<String, String> entry : googleAppTestService.AutomationKeywords.entrySet()) {
						//System.out.println(entry.getKey());
						if (stepDetails.toUpperCase().contains(entry.getKey().toUpperCase())) {
							funcToExecute = entry.getValue();
						}
						
						if (stepExpected.toUpperCase().contains(entry.getKey().toUpperCase())) {
							validationType = entry.getValue();
							eachResStep.setStepValidationType(validationType);
						}
					}
				 
					for (Map.Entry<String, AppPropertyObj> entry : controller.mad.getObjRep().entrySet()) {
						AppPropertyObj propobj = entry.getValue();
						
						if (stepDetails.toUpperCase().contains(propobj.getPageName().toUpperCase())) {
							pageName = propobj.getPageName();
						}
						if (stepExpected.toUpperCase().contains(propobj.getPageName().toUpperCase())) {
							ExpectedpageName = propobj.getPageName();
						}
						
					}
				 
					if (!(funcToExecute.isEmpty() & pageName.isEmpty() & validationType.isEmpty() &ExpectedpageName.isEmpty() )) 
					{
									
							switch (funcToExecute.toUpperCase()) {
							case "ENTERDETAILS":
								boolisExecuted = SelAppFuncHandler.EnterDetails(pageName, step);
								System.out.println("******* " + "All the preconditions steps and Step descriptions are performed"+ "******* " );
							break;
							default:
								System.out.println("******* " + "No matching keyword with step description and precondition"+ "******* " );
							}
						
					
							if (boolisExecuted) {
								switch (validationType.toUpperCase()) {
								case "GETERRORMESSAGE":
									eachResStep.setActual(SelAppFuncHandler.GetErrorMessage(ExpectedpageName));
									//System.out.println("Actual result is captured " + scenresobj.getActualResult());
									break;
								case "GETOBJECTEXISTANCE":
									eachResStep.setActual(SelAppFuncHandler.GetObjExistance(ExpectedpageName));
									//System.out.println("Actual result is captured " + scenresobj.getActualResult());
									break;
								default:
									System.out.println( "******* " + "No matching keyword with Expected Result" + "******* " );
									
								}
								//ArrStepResObj.set(index, eachResStep);
								
							}
		
					}
					else
					{
					System.out.println("******* " + "One of them is empty: " + "funcToExecute: " + funcToExecute + " or pageName: " + pageName+ " or validationType: " + validationType+ " or ExpectedpageName: " + ExpectedpageName+ " ******* "  );
					Reporting.ReportLog = "******* " + "One of them is empty: " + "funcToExecute: " + funcToExecute + " or pageName: " + pageName+ " or validationType: " + validationType+ " or ExpectedpageName: " + ExpectedpageName+ " ******* ";
					}	
			 
					eachResStep.setStepResultLog(Reporting.ReportLog);	
					Reporting.ReportLog ="";
				}
				
				SelAppFuncHandler.Closebrowser();
				
		
				CopyOnWriteArrayList<StepResult> ArrStepResObj1 = scenresobj.getAllStepResult();
				
				for (StepResult eachResStep : ArrStepResObj1) {	
					System.out.println(eachResStep.getExpected());
				}
				
	}
	
	scenresobj.setAllStepResult(ArrStepResObj);
		return scenresobj;
	
	}
}
