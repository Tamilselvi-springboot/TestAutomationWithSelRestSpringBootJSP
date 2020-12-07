package com.example.demo.utilities;

import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.example.demo.controllerService.googleAppTestService;
import com.example.demo.model.StepResult;
import com.example.demo.model.testcaseResult;

public class Validation {
	
	int ExpErrorCount = 0;
	
	public testcaseResult compareResults(testcaseResult testcaseResObj) {

		CopyOnWriteArrayList<StepResult> ArrStepResObj = testcaseResObj.getAllStepResult();
		String isanyStepFailed = "";
		for (StepResult eachResStep : ArrStepResObj) {

			eachResStep.setStepResultStatus("Fail");
			// testcaseResult testcaseResObj1 = testcaseResObj;
			String Expected = eachResStep.getExpected();
			String Actual = eachResStep.getActual();

			if (googleAppTestService.TypeofTest.equalsIgnoreCase("Service")) {

				if (Expected.equalsIgnoreCase(Actual)) {
					eachResStep.setStepResultStatus("Pass");
					Reporting.ReportLog = Reporting.ReportLog + "\n\r" + "Actual is matching with Expected";
					System.out.println("******* " + "Actual is matching with Expected" + " ******* ");

				} else {
					isanyStepFailed = "Yes";
					Reporting.ReportLog = Reporting.ReportLog + "\n\r" + "Actual is not matching with Expected";
					System.out.println("******* " + "Actual is not matching with Expected" + " ******* ");

				}

				eachResStep.setStepResultLog(eachResStep.getStepResultLog() + Reporting.ReportLog);
				Reporting.ReportLog = "";

			} else {

				String ReqId = "";
				String validationType = eachResStep.getStepValidationType();

				if (Expected.contains("ER_")) {
					System.out.println("Tamil");
					for (Map.Entry<String, String> entry : googleAppTestService.GlobalMessages.entrySet()) {
						// System.out.println("4444444444444" + entry.getKey().trim());
						ReqId = entry.getKey().trim();
						if (Expected.contains(ReqId)) {
							ExpErrorCount = ExpErrorCount + 1;
							Expected = Expected.toString().replaceAll(ReqId, entry.getValue().trim());

						}

					}
				}

				eachResStep.setExpected(Expected);

				if (eachResStep.getStepValidationType().toUpperCase().contains("GETERRORMESSAGE")) {
					// System.out.println(testcaseResObj.getExpectedResult().toUpperCase());
					// System.out.println(testcaseResObj.getActualResult().toUpperCase());

					if (CompareErrors(Expected, Actual)) {
						eachResStep.setStepResultStatus("Pass");
						Reporting.ReportLog = Reporting.ReportLog + "\n\r" + "Actual is matching with Expected";
						System.out.println("******* " + "Actual is matching with Expected" + " ******* ");
					} else {
						isanyStepFailed = "Yes";
						Reporting.ReportLog = Reporting.ReportLog + "\n\r" + "Actual is not matching with Expected";
						System.out.println("******* " + "Actual is not matching with Expected" + " ******* ");
					}

				} else if (validationType.toUpperCase().contains("GETOBJECTEXISTANCE")) {

					if (eachResStep.getActual().toUpperCase().contains("Yes")) {
						eachResStep.setStepResultStatus("Pass");
					} else {
						isanyStepFailed = "Yes";
						Reporting.ReportLog = Reporting.ReportLog + "\n\r" + "Expected is not matching with Actual";
						System.out.println("******* " + "Expected is not matching with Actual" + " ******* ");
					}
				}

				eachResStep.setStepResultLog(eachResStep.getStepResultLog() + Reporting.ReportLog);
				Reporting.ReportLog = "";
			}

		}

		if (isanyStepFailed.equalsIgnoreCase("Yes"))
			testcaseResObj.setTestcaseStatus("Fail");
		else
			testcaseResObj.setTestcaseStatus("Pass");

		return testcaseResObj;
	}

	public boolean CompareErrors(String strExpected, String strActual) {
		boolean ComparisionResult = false;
		int ActErrorCount = 0;

		String[] arrErr = strActual.split("###");

		for (String Err : arrErr) {
			// System.out.println(Err);
			if (strExpected.toUpperCase().trim().contains(Err.toUpperCase().trim())) {
				ActErrorCount = ActErrorCount + 1;
			}
		}

		if (ActErrorCount == arrErr.length) {

			System.out.println("******* " + "All the errors from Actual are present in expected." + "******* ");
			Reporting.ReportLog = Reporting.ReportLog + "\n\r" + "All the errors from Actual are present in expected.";

			if (ActErrorCount == ExpErrorCount) {
				ComparisionResult = true;
				System.out.println("******* " + "All errors in expected result are validated." + "******* ");
				Reporting.ReportLog = Reporting.ReportLog + "\n\r"
						+ "All errors in expected result are available in Actual.";

			} else {
				System.out.println("******* " + "Some errors in expected result are not in Actual." + "******* ");
				Reporting.ReportLog = Reporting.ReportLog + "\n\r"
						+ "\"Some errors in expected result are not in Actual. Refer errors for more info";

			}

		} else {
			System.out.println("******* " + "All the errors from Actual are not present in expected." + "******* ");
			Reporting.ReportLog = Reporting.ReportLog + "\n\r"
					+ "All the errors from Actual are not present in expected.";
		}
		return ComparisionResult;
	}
}