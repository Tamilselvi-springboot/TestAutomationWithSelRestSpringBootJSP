package com.example.demo.appModules;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.example.demo.controller.controller;
import com.example.demo.model.AppPropertyObj;
import com.example.demo.model.inputData;
import com.example.demo.utilities.Reporting;
import com.example.demo.utilities.SeleniumUtil;

public class SeleniumAppFunctions {

	ConcurrentHashMap<String, AppPropertyObj> appObjProp = controller.mad.getObjRep();
	ConcurrentHashMap<String,String> ScenInputData1;
	
	
	SeleniumUtil SelObj1;
	
	
	public SeleniumAppFunctions(ConcurrentHashMap<String,String> ScenInputData) {
		this.SelObj1 = new SeleniumUtil();
		
		this.ScenInputData1 = ScenInputData;
		for (Map.Entry<String,String> entry : ScenInputData1.entrySet())
		{
		    System.out.println("key: " + entry.getKey()  + " Value" + entry.getValue());
		}
		
		
	}
	
	public boolean EnterDetails(String pageName, int stepNo) {
		
		
		boolean boolentersignupinfo = false;
		if (pageName.equalsIgnoreCase("Sign Up")) {
			String keyName ="Param_URL" + "_" + stepNo;
		String URL = ScenInputData1.get("Param_URL" + "_" + stepNo);
		boolean a = ScenInputData1.containsKey(keyName);
		boolentersignupinfo = SelObj1.navigatetobrowser(URL);
		if (boolentersignupinfo) {
			if (ScenInputData1.containsKey("Param_Name" + "_" + stepNo)) {
				boolentersignupinfo = SelObj1.EnterElement(appObjProp.get("SignUp_Name").getSeleniumAppObj(), ScenInputData1.get("Param_Name" + "_" + stepNo) );
			}
			if (ScenInputData1.containsKey("Param_UserName" + "_" + stepNo)) {
				boolentersignupinfo = SelObj1.EnterElement(appObjProp.get("SignUp_UserName").getSeleniumAppObj(), ScenInputData1.get("Param_UserName"+ "_" + stepNo));
			}
			if (ScenInputData1.containsKey("Param_Pwd" + "_" + stepNo)) {
				boolentersignupinfo = SelObj1.EnterElement(appObjProp.get("SignUp_Password").getSeleniumAppObj(), ScenInputData1.get("Param_Pwd"+ "_" + stepNo));
			}
			if (ScenInputData1.containsKey("Param_ConfirmPwd" + "_" + stepNo)) {
				boolentersignupinfo = SelObj1.EnterElement(appObjProp.get("SignUp_ConfirmPwd").getSeleniumAppObj(), ScenInputData1.get("Param_ConfirmPwd"+ "_" + stepNo));
			}
			boolentersignupinfo = SelObj1.ClickElement(appObjProp.get("SignUp_Create").getSeleniumAppObj());
		}
		}
		return boolentersignupinfo;
	}
	
	public String GetErrorMessage(String pageName) {
		String ActualErrorMessage = "";
		if (pageName.equalsIgnoreCase("Sign Up")) {
		ActualErrorMessage =	SelObj1.getErrorMessage(appObjProp.get("SignUp_Error").getSeleniumAppObj());
		}
		//SelObj1.closebrowser();
		return ActualErrorMessage;
	}
	
	public String GetObjExistance(String pageName) {
		String ActualResult = "No";
		
		switch (pageName.toUpperCase()) {
		case "SIGN UP":
			
			break;
		case "VERIFY EMAIL ADDRESS":
			if(SelObj1.existsElement(appObjProp.get("VerifyEmailAddress_header").getSeleniumAppObj()))
					ActualResult = "Yes";
			break;
		default:
			System.out.println( "******* " + "No case is defined for the page identification in object existence method" + "******* " );
			Reporting.ReportLog = Reporting.ReportLog + "\n\r" +  "No automation case is defined for the page identification";
		}
		//SelObj1.closebrowser();
		return ActualResult;
	}
	
	public boolean Closebrowser() {		
		
		return SelObj1.closebrowser();
	}
	
}
