package com.example.demo.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;

import com.example.demo.model.AppPropertyObj;
import com.example.demo.model.StepDetails;
import com.example.demo.model.inputData;

public class excelUtil {
	
	public static int StatusColNum;
	public static int DetailedLogColumnNum;
	public static int testcaseStatusColumnNum;
	//public static int ScenarioNum;
	
	public String path=null;
	public FileInputStream fis = null;
	public FileOutputStream  fileout=null;
	private XSSFWorkbook workbook = null;
	private XSSFSheet sheet = null;
	private XSSFRow row = null;
	private XSSFCell cell = null;
	
	
	public excelUtil(String excelPath) {
	
		this.path=excelPath;
		try {
		fis = new FileInputStream(excelPath);
		workbook = new XSSFWorkbook(fis);
		fis.close();
		}
		catch(Exception e) {
			System.out.println("Input excel has some problem");
		}
	}
	

	public ConcurrentHashMap<String, inputData> getAllScenarios(String sheetName, int RunFlagColNum) {
		String ScenarioNumber;
		String ScenDesc;
		String Stepdes;
		String ExpRes;
		Row row1;
		Row row2;
		int j;
	String scenNo="";
	
		ConcurrentHashMap<String, inputData> allScenarios = new ConcurrentHashMap<String, inputData>();
		int index = workbook.getSheetIndex(sheetName);

		if (index == -1) {
			return null;
		} else {
			sheet = workbook.getSheetAt(index);

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				row1 = sheet.getRow(i);
				Cell c = null;
				try {
					c = row1.getCell(RunFlagColNum, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				} catch (NullPointerException ex) {
					continue;
				}

				if (c.toString().equalsIgnoreCase("Y") || c.toString().equalsIgnoreCase("Yes")) {

					CopyOnWriteArrayList<StepDetails> StepDescExp=new CopyOnWriteArrayList<StepDetails>();
						ScenarioNumber = row1.getCell(0, MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
						ScenDesc = row1.getCell(2, MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
						j=i;
						int a = sheet.getLastRowNum();
						do{
							row2 = sheet.getRow(j);
							
							
							Stepdes = row2.getCell(5, MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
							ExpRes = row2.getCell(6, MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
							if (!(Stepdes.isEmpty() && ExpRes.isEmpty())){
								StepDetails stepdet = new StepDetails(Stepdes,ExpRes);	
								StepDescExp.add(stepdet);
								
								j = j + 1;		
								if(j< sheet.getLastRowNum())
								scenNo = sheet.getRow(j).getCell(0, MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();	
								
							}
												
						} while (scenNo.isEmpty() && j<= sheet.getLastRowNum());
						
						i=j-1;

						inputData ScenarioObj = new inputData(ScenDesc, StepDescExp, ScenarioNumber);
						System.out.println(ScenarioObj.toString());
						allScenarios.put(ScenarioNumber, ScenarioObj);			

					
				}

			}

		}
		System.out.println("******* " + "All scenario info is read and stored in MAP successfully."+ "******* ");
		return allScenarios;
	}
	
	public boolean addResultColumn(String SheetName, String ResultStatusColName, String ResultLogColName, String TCStatus) {
		boolean retvalue = false;
		int index=workbook.getSheetIndex(SheetName);
		if (index == -1) {
			System.out.println("The sheet is empty");
			return false;
			
		}
		else {
			sheet = workbook.getSheetAt(index);
			int noc = sheet.getRow(0).getLastCellNum();
			StatusColNum = noc;
			DetailedLogColumnNum = StatusColNum +1;
			testcaseStatusColumnNum =StatusColNum +2;
			sheet.getRow(0).createCell(noc).setCellValue(ResultStatusColName);		
			sheet.getRow(0).createCell(noc+1).setCellValue(ResultLogColName);
			sheet.getRow(0).createCell(noc+2).setCellValue(TCStatus);
			if(SaveOutput()) {
				retvalue=true;
			}
		}
		 return retvalue;
	}
	
	public boolean SaveOutput() {
		boolean retvalue = false;
		FileOutputStream outputstream;
		try {
			outputstream = new FileOutputStream(path);
			try {
				workbook.write(outputstream);
				outputstream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			retvalue = true;
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return retvalue;
		
	}
	
	public ConcurrentHashMap<String, ConcurrentHashMap<String, String>> getAllTestData(String sheetName) {
		String ScenarioNumber;
		int ScenarioDataRowCount = 0;

		String ParamVal;
		String ParamKey;

		ConcurrentHashMap<String, ConcurrentHashMap<String, String>> allTestData = new ConcurrentHashMap<String, ConcurrentHashMap<String, String>>();
		

		int index = workbook.getSheetIndex(sheetName);
		
		if (index == -1) {
			return null;
		} else {
			sheet = workbook.getSheetAt(index);

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				row = sheet.getRow(i);
				ConcurrentHashMap<String, String> ParamKeyValueList = new ConcurrentHashMap<String, String>();
				ConcurrentHashMap<String, String> ExistingParamKeyValueList = new ConcurrentHashMap<String, String>();

				ScenarioNumber = row.getCell(0, MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();

				if (!ScenarioNumber.toString().isEmpty()) {
					if (!(allTestData.containsKey(ScenarioNumber))) {
						ScenarioDataRowCount = 0;
					}
					ScenarioDataRowCount = ScenarioDataRowCount + 1;
					int lastColumnNum = row.getLastCellNum();
					for (int j = 1; j <= lastColumnNum - 1; j++) {
						Cell c = null;
						// try {
						c = row.getCell(j, MissingCellPolicy.CREATE_NULL_AS_BLANK);
						// }
						// catch (NullPointerException ex) {
						// continue;
						// }

						if (!c.toString().isEmpty()) {

							ParamVal = c.toString();
							ParamKey = sheet.getRow(0).getCell(j).toString() + "_" + ScenarioDataRowCount;
							ParamKeyValueList.put(ParamKey, ParamVal);

						}
					}

					// allTestData.
					if (allTestData.containsKey(ScenarioNumber)) {
						ExistingParamKeyValueList = allTestData.get(ScenarioNumber);
						ExistingParamKeyValueList.putAll(ParamKeyValueList);
					} else
						allTestData.put(ScenarioNumber, ParamKeyValueList);

				}

			}

		}
		
		System.out.println("******* " + "All test data is read and stored in MAP successfully." + "******* ");
		
		for (Map.Entry<String, ConcurrentHashMap<String, String>> scen : allTestData.entrySet())
		{	System.out.println("key: " + scen.getKey() );
			ConcurrentHashMap<String, String> b= scen.getValue() ;
			for (Map.Entry<String,String> entry : b.entrySet())
			{
			    System.out.println("key: " + entry.getKey() );
			}
		}
		
		return allTestData;
		
		
		
	}
	
	public ConcurrentHashMap<String, AppPropertyObj> getObjectRep(String sheetName) {
	    String ObjName;
		String ObjLocatorMethod;
		String ObjProperty;
		String PageName;
		By SelAppObj = null;
		ConcurrentHashMap<String, AppPropertyObj> allObj = new ConcurrentHashMap<String, AppPropertyObj>();
		int index = workbook.getSheetIndex(sheetName);

		if (index == -1) {
			return null;
		} else {
			sheet = workbook.getSheetAt(index);

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				row = sheet.getRow(i);
				
				ObjName = row.getCell(0, MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
				if (!ObjName.isEmpty()) {
				ObjLocatorMethod = row.getCell(1, MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
				ObjProperty = row.getCell(2, MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
				PageName = row.getCell(3, MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();
				SelAppObj = locatorParser(ObjLocatorMethod,ObjProperty);
				AppPropertyObj selobj = new AppPropertyObj(ObjName, SelAppObj,PageName);
					
				allObj.put(ObjName, selobj);
				}
					
					}
				}		
		System.out.println("******* " + "All obj info is read and stored in MAP successfully." + "******* ");
		return allObj;
	}
	
	public static By locatorParser(String locator, String PropValue) {

		By loc = By.xpath(PropValue) ;

		if (locator.contains("id"))
		    loc = By.id(PropValue);

		else if (locator.contains("name"))
			 loc = By.name(PropValue);

		if (locator.contains("xpath"))
			 loc = By.xpath(PropValue);

		return loc;

		}
	
		public boolean WriteResInExcel(String SheetName, String RowIdentifier, String stepIdentifier, String StepStatus, String StepLog, String TCStatus1) {
			boolean IsWritten = false;
			String scenNo="";

			int index = workbook.getSheetIndex(SheetName);
			if (index == -1) {
				System.out.println("The sheet is empty");
				return false;

			} else {

				sheet = workbook.getSheetAt(index);
				int noc = sheet.getLastRowNum();
				
				for (int i = 1; i <= noc; i++) {
					row = sheet.getRow(i);
					Cell c = row.getCell(0, MissingCellPolicy.CREATE_NULL_AS_BLANK);
					if (c.toString().equalsIgnoreCase(RowIdentifier)) {

						int k = i - 1;
						do {
							k = k + 1;
							row = sheet.getRow(k);
							Cell stepCell = row.getCell(5, MissingCellPolicy.CREATE_NULL_AS_BLANK);
							if (stepCell.toString().equalsIgnoreCase(stepIdentifier.trim())) {
								row.createCell(StatusColNum).setCellValue(StepStatus);
								row.createCell(DetailedLogColumnNum).setCellValue(StepLog);

								if (k == i)
									row.createCell(testcaseStatusColumnNum).setCellValue(TCStatus1);
								break;
							}
							scenNo = sheet.getRow(k+1).getCell(0, MissingCellPolicy.CREATE_NULL_AS_BLANK).toString();

						} while (scenNo.isEmpty() && k <= sheet.getLastRowNum());

						if (SaveOutput()) {
							IsWritten = true;
						}
						break;
					}

					
				}

			}
			return IsWritten;
		}
		
		
}
