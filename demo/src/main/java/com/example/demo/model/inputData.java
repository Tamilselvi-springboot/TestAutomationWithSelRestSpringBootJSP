/**
 * 
 */
package com.example.demo.model;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;

/**
 * @author Tamil
 *
 */


public class inputData {
	
	private String ScenarioDesc;
	//private ConcurrentHashMap <String,String> stepDescription;
	private CopyOnWriteArrayList<StepDetails> StepDetails;
	private String ScenarioNum;
	
	
	public String getScenarioDesc() {
		return ScenarioDesc;
	}
	public void setScenarioDesc(String preCondition) {
		ScenarioDesc = preCondition;
	}
	public CopyOnWriteArrayList<StepDetails> getStepDetails() {
		return StepDetails;
	}
	public void setStepDetails(CopyOnWriteArrayList<StepDetails> stepDetails) {
		StepDetails = stepDetails;
	}
	public String getScenarioNum() {
		return ScenarioNum;
	}
	public void setScenarioNum(String scenarioNum) {
		ScenarioNum = scenarioNum;
	}
	public inputData(String scenarioDesc, CopyOnWriteArrayList<com.example.demo.model.StepDetails> stepDetails,
			String scenarioNum) {
		super();
		ScenarioDesc = scenarioDesc;
		StepDetails = stepDetails;
		ScenarioNum = scenarioNum;
	}
	
	
	

}
