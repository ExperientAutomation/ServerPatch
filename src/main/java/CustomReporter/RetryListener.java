package CustomReporter;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryListener implements IRetryAnalyzer {

	int retryCount = 0;
	int retrylimit =1;

	@Override
	public boolean retry(ITestResult result) {
		
		if(retryCount<retrylimit){
		retryCount++;
		return true;
		}
		return false;
	}

}
