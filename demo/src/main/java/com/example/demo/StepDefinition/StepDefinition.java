package com.example.demo.StepDefinition;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.example.demo.controller.controller;
import com.example.demo.controllerService.googleAppTestService;
import com.example.demo.model.StepDetails;
import com.example.demo.model.StepResult;
import com.example.demo.model.allData;
import com.example.demo.model.inputData;
import com.example.demo.model.testcaseResult;
import com.example.demo.utilities.Reporting;
import com.example.demo.utilities.StepHandler;
import com.example.demo.utilities.SystemUtilities;
import com.example.demo.utilities.Validation;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class StepDefinition {
	boolean canContinue = false;
	StepHandler stephandler = new StepHandler();
	SystemUtilities sysutil = new SystemUtilities();
	Reporting reporting = new Reporting();
	testcaseResult testResultObj ;
	//testcaseResult testResultObj1 ;
	ConcurrentHashMap<String,String> ScenInputData;
	Validation Validator = new Validation();
	boolean ReportingStatus = false;
	
	
	
	@Given("^Testing environment is set up for \"([^\"]*)\"$")
	
	public void testing_environment_is_set_up_for(String ScenarioNo) throws Throwable {
		//System.out.println("Given condition............ " + arg1);
		System.out.format("Thread ID - " + Thread.currentThread().getId());
	
			//allData mad1 = controller.mad;
			Reporting.ReportLog ="";
			ConcurrentHashMap<String,inputData> allScenarios = controller.mad.getAllScenarios();
			inputData givenScenarioInput  =allScenarios.get(ScenarioNo);
			
			 CopyOnWriteArrayList<StepDetails> ArrStepResObj =givenScenarioInput.getStepDetails();
			for (StepDetails eachStep :ArrStepResObj) {
				System.out.println("step desc: " + eachStep.getStepDesc() + "; step exp: " + eachStep.getExpected());
			}
			
			testResultObj = sysutil.createScenarioResultObj(givenScenarioInput);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ConcurrentHashMap<String,ConcurrentHashMap<String,String>> allInputTestData = controller.mad.getAllInputTestData();
			ScenInputData = allInputTestData.get(ScenarioNo);
			//ConcurrentHashMap<String,SelObj> ObjRep = new ConcurrentHashMap<String,SelObj>();
			
	}
	

@When("^Executing testcase : \"([^\"]*)\"$")
public void executing_testcase(String arg1) throws Throwable {
	
	testResultObj =stephandler.ExecuteStep(testResultObj,ScenInputData);
}

@Then("^Expected should match with actual$")
public void expected_should_match_with_actual() throws Throwable {
	if (!testResultObj.getAllStepResult().isEmpty()) {
		
	
		CopyOnWriteArrayList<StepResult> ArrStepResObj = testResultObj.getAllStepResult();
		
		for (StepResult eachResStep : ArrStepResObj) {	
			System.out.println(eachResStep.getExpected());
		}
		
		testResultObj =Validator.compareResults(testResultObj);
		
	}
//	testResultObj.setResultLog(Reporting.ReportLog);
	//System.out.println(testResultObj.getResultLog());
	ReportingStatus = reporting.WriteResultInFile(testResultObj);
	
	System.out.println("Is result written?: " + ReportingStatus);

}	

}
