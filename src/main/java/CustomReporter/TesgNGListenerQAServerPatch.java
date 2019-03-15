package CustomReporter;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import TestCases.QAServerPatch;
import TestCases.UATserverPatch;
import utils.XlsUtil;

public class TesgNGListenerQAServerPatch implements ITestListener{

	@Override
	public void onTestStart(ITestResult result) {
		System.out.println("TestCase Started and details are : "+result.getName());
	}

	@Override
	public void onTestSuccess(ITestResult result) {

		System.out.println("TestCase Passed and details are : "+result.getName());
		
		XlsUtil util = QAServerPatch.xls;
		
		util.setCellData("Stage Servers",QAServerPatch.column , QAServerPatch.row, "Passed");
		util.setCellData("Stage Servers",QAServerPatch.column-1 , QAServerPatch.row, System.getProperty("user.name"));
 }
	
	@Override
	public void onTestFailure(ITestResult result) {
		
		System.out.println("TestCase failed and details are : "+result.getName());
		
		XlsUtil util = QAServerPatch.xls;
		
		util.setCellData("Stage Servers",QAServerPatch.column , QAServerPatch.row, "Failed");
		util.setCellData("Stage Servers",QAServerPatch.column-1 , QAServerPatch.row, System.getProperty("user.name"));
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		System.out.println("TestCase skipped and details are : "+result.getName());
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		
	}

	@Override
	public void onStart(ITestContext context) {
		
	}

	@Override
	public void onFinish(ITestContext context) {
		
	}

}
