package CustomReporter;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import TestCases.UATserverPatch;
import utils.BrowserFactory;
import utils.XlsUtil;

public class TesgNGListenerUATServerPatch extends BrowserFactory implements ITestListener{

	@Override
	public void onTestStart(ITestResult result) {
		System.out.println("TestCase Started and details are : "+result.getName());
	}

	@Override
	public void onTestSuccess(ITestResult result) {

		System.out.println("TestCase Passed and details are : "+result.getName());
		
		XlsUtil util = UATserverPatch.xls;//new XlsUtil(UATserverPatch);
		
		util.setCellData("Pre Req Servers",UATserverPatch.column , UATserverPatch.row, "Passed");
		util.setCellData("Pre Req Servers",UATserverPatch.column-1 , UATserverPatch.row, System.getProperty("user.name"));
 }
	

	@Override
	public void onTestFailure(ITestResult result) {

		
		System.out.println("TestCase failed and details are : "+result.getName());
		
		XlsUtil util = UATserverPatch.xls;//new XlsUtil(UATserverPatch.filePath);
		
		util.setCellData("Pre Req Servers",UATserverPatch.column , UATserverPatch.row, "Failed");
		util.setCellData("Pre Req Servers",UATserverPatch.column-1 , UATserverPatch.row, System.getProperty("user.name"));
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
