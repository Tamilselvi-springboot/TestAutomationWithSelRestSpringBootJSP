package com.example.demo.utilities;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.controllerService.googleAppTestService;

public class SeleniumUtil {
	public WebDriver driver;
	private static Object firstHandle;
	private static Object lastHandle;
	

	//googleAppTestService serviceimp = new googleAppTestService(); ;
	
	
	public SeleniumUtil()  {
		
		
		System.setProperty("webdriver.chrome.driver", googleAppTestService.AppProperites.get("ChromeDriverPath") );
		this.driver = new ChromeDriver();	
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//driver.navigate().to("www.google.com");
	}

	public boolean navigatetobrowser(String URL) {
		driver.get(URL);
		/**try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}**/
		driver.manage().window().maximize();
		System.out.println("******* " + "Navigated to the given URL " + "******* ");
		Reporting.ReportLog = Reporting.ReportLog + "\n\r" + "Navigated to the given URL ";
		
		return true;
	}
	
	public boolean closebrowser() {
		boolean retvalue= false;
		//driver.quit();
		
		try {
			//Thread.sleep(1000000);
			Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
		} catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		retvalue = true;
		return retvalue;
	}
	
	public boolean existsElement(By obj) {
		try {
			driver.findElement(obj);
			System.out.println("******* " + "Element exists" + obj.toString() + "******* ");
			Reporting.ReportLog = Reporting.ReportLog + "\n\r" +  "Element exists" + obj.toString();
		} catch (NoSuchElementException e) {
			System.out.println("******* " + "Element does not exist" + obj.toString()+"******* ");
			Reporting.ReportLog = Reporting.ReportLog + "\n\r" +  "Element does not exist" + obj.toString();
			return false;
		}
		return true;
	}
	
	public boolean EnterElement(By obj, String ParamValue) {
		boolean retvalue = false;

		if (existsElement(obj)) {
			try {
				driver.findElement(obj).sendKeys(ParamValue);
				
				System.out.println( "******* " + "Entered " + ParamValue + " in obj " + obj.toString() + "******* ");
				Reporting.ReportLog = Reporting.ReportLog + "\n\r" + "Entered " + ParamValue + " in obj " + obj.toString();
				retvalue = true;
			} catch (NoSuchElementException e) {
				System.out.println("***************Unable to enter " + ParamValue + " in obj " + obj.toString());
				Reporting.ReportLog = Reporting.ReportLog + "\n\r" + "***************Unable to enter " + ParamValue + " in obj " + obj.toString();

			}
		}
		return retvalue;
	}
	
	public boolean ClickElement(By obj) {
		boolean retvalue = false;

		if (existsElement(obj)) {
			if (movetoelement(obj)) {

				try {
					WebElement element = driver.findElement(obj);
					element.click();	
			
					System.out.println("******* " + "Clicked on obj " + obj.toString()+ "******* ");
					Reporting.ReportLog = Reporting.ReportLog + "\n\r" + "Clicked on obj " + obj.toString();
					retvalue = true;
				} catch (WebDriverException e) {
					System.out.println("*********** Unable to click on obj " + obj.toString());
					Reporting.ReportLog = Reporting.ReportLog + "\n\r" + "*********** Unable to click on obj " + obj.toString();
				}
			}
		}
		return retvalue;
	}
	
	public boolean movetoelement(By obj) {
		boolean retvalue = false;
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		Actions actions = new Actions(driver);

		try {
			WebElement element = driver.findElement(obj);
			actions.moveToElement(element).build().perform();
			jse.executeScript("arguments[0].scrollIntoView()", element);

			System.out.println("******* " + "Moved on obj " + obj.toString() + "******* ");
			retvalue = true;
		} catch (NoSuchElementException e) {
			System.out.println("*********** Unable to move on obj " + obj.toString());

		}

		return retvalue;
	}
	
	public void switchToWindowsPopup() {
		Set<String> handles = driver.getWindowHandles();
		Iterator<String> itr = handles.iterator();
		firstHandle = itr.next();
		lastHandle = firstHandle;
		while( itr.hasNext()) {
			lastHandle=itr.next();
		}
		
		driver.switchTo().window(lastHandle.toString());
	}
	
	public void switchToMainWindow() {
		Set<String> handles = driver.getWindowHandles();
		Iterator<String> itr = handles.iterator();
		firstHandle = itr.next();
		lastHandle = firstHandle;
			
		driver.switchTo().window(firstHandle.toString());
		
	}
	
	public String getErrorMessage(By obj) {
		String ErrMsgList = "";
		List<WebElement> elements=driver.findElements(obj);
			
		for (int i=0; i<elements.size();i++){
			if(!elements.get(i).getText().isEmpty())
				if (ErrMsgList.isEmpty()) {
					ErrMsgList = ErrMsgList + elements.get(i).getText();	
				}
				else
				{ErrMsgList = ErrMsgList +"###" + elements.get(i).getText();}
			
		    }
		System.out.println("******* " + "Retrieved Error Messages: " + ErrMsgList + " in obj " + obj.toString() + "******* ");
			Reporting.ReportLog = Reporting.ReportLog + "\n\r" + "Retrieved Error Messages: " + ErrMsgList;
		return ErrMsgList;
	}

}
