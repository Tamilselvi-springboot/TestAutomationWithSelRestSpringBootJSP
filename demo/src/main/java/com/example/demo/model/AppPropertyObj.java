/**
 * 
 */
package com.example.demo.model;

import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

/**
 * @author Tamil
 *
 */


public class AppPropertyObj {
	
	private String ObjName;
	private By SeleniumAppObj;
	private String PageName;
	public String getPageName() {
		return PageName;
	}
	public void setPageName(String pageName) {
		PageName = pageName;
	}
	public String getObjName() {
		return ObjName;
	}
	public void setObjName(String objName) {
		ObjName = objName;
	}
	public By getSeleniumAppObj() {
		return SeleniumAppObj;
	}
	public void setSeleniumAppObj(By seleniumAppObj) {
		SeleniumAppObj = seleniumAppObj;
	}
	public AppPropertyObj(String objName, By seleniumAppObj, String pageName) {
		super();
		ObjName = objName;
		SeleniumAppObj = seleniumAppObj;
		PageName = pageName;
	}
	
	
	
	
	
	
	
	
	

}
