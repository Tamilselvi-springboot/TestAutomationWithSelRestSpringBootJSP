package com.example.demo.model;

import java.util.concurrent.ConcurrentHashMap;



public class allData {
	ConcurrentHashMap<String,inputData> allScenarios = new ConcurrentHashMap<String,inputData>();
	ConcurrentHashMap<String,ConcurrentHashMap<String,String>> allInputTestData = new ConcurrentHashMap<String,ConcurrentHashMap<String,String>>();
	ConcurrentHashMap<String,AppPropertyObj> ObjRep = new ConcurrentHashMap<String,AppPropertyObj>();


	
	public ConcurrentHashMap<String, AppPropertyObj> getObjRep() {
		return ObjRep;
	}
	public void setObjRep(ConcurrentHashMap<String, AppPropertyObj> objRep) {
		ObjRep = objRep;
	}
	public ConcurrentHashMap<String, inputData> getAllScenarios() {
		return allScenarios;
	}
	public void setAllScenarios(ConcurrentHashMap<String, inputData> allScenarios) {
		this.allScenarios = allScenarios;
	}
	public ConcurrentHashMap<String, ConcurrentHashMap<String, String>> getAllInputTestData() {
		return allInputTestData;
	}
	public void setAllInputTestData(ConcurrentHashMap<String, ConcurrentHashMap<String, String>> allInputTestData) {
		this.allInputTestData = allInputTestData;
	}}
