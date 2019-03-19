package CustomReporter;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import TestCases.PRODserverPatch;
import TestCases.UATserverPatch;
import utils.BrowserFactory;
import utils.XlsUtil;

public class TesgNGListenerPRODServerPatch extends BrowserFactory implements ITestListener{

	@Override
	public void onTestStart(ITestResult result) {
		System.out.println("TestCase Started and details are : "+result.getName());
	}

	@Override
	public void onTestSuccess(ITestResult result) {

		System.out.println("TestCase Passed and details are : "+result.getName());	
		
		XlsUtil util = PRODserverPatch.xls;
		
		util.setCellData("Sheet1",PRODserverPatch.column , PRODserverPatch.row, "Passed");
		util.setCellData("Sheet1",PRODserverPatch.column-1 , PRODserverPatch.row, System.getProperty("user.name"));
 }
	

	@Override
	public void onTestFailure(ITestResult result) {

		
		System.out.println("TestCase failed and details are : "+result.getName());
		
		XlsUtil util = PRODserverPatch.xls;
		
		util.setCellData("Sheet1",PRODserverPatch.column , PRODserverPatch.row, "Failed");
		util.setCellData("Sheet1",PRODserverPatch.column-1 , PRODserverPatch.row, System.getProperty("user.name"));
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
