package com.example.demo.utilities;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import com.example.demo.model.StepResult;

public class LoveServiceParallelModule implements Callable<StepResult>{
	private StepResult StepResObj;
	private int stepNo;
	private ConcurrentHashMap<String,String> ScenInputData;
	
	
	public LoveServiceParallelModule(StepResult stepResObj, int stepNo,
			ConcurrentHashMap<String, String> scenInputData) {
		
		StepResObj = stepResObj;
		this.stepNo = stepNo;
		ScenInputData = scenInputData;
	}


	@Override
    public StepResult call() throws Exception {
		
		ServiceHandler SH = new ServiceHandler();
		return  SH.parallelservicecall(StepResObj, stepNo, ScenInputData);
		
	}
	
	

}
