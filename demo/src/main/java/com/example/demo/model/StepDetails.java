package com.example.demo.model;

public class StepDetails {
	
	String StepDesc;
	String Expected;
	public String getStepDesc() {
		return StepDesc;
	}
	public void setStepDesc(String stepDesc) {
		StepDesc = stepDesc;
	}
	public String getExpected() {
		return Expected;
	}
	public void setExpected(String expected) {
		Expected = expected;
	}
	public StepDetails(String stepDesc, String expected) {
		super();
		StepDesc = stepDesc;
		Expected = expected;
	}
	

}
