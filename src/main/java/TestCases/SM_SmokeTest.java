package TestCases;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import utils.ConfigReader;
import utils.WinUtils;
import CustomReporter.emailReportSM_SmokeTest;

public class SM_SmokeTest {
		

//	String ShowManagerURL = "http://rdshowmanager.experientevent.com";
	
	String ShowManagerURL = "http://rdqashowman.experientevent.com";
	
	String ShowCode = "TST000";

	WebDriver driver;
	smlogin login = new smlogin();
	static ConfigReader config = new ConfigReader();
	WinUtils util = new WinUtils();
	StringBuffer showAllError;

	// The below method will check for errors on page.
	public Boolean testConnection(String errorButton, StringBuffer showAllError) {
		boolean result = true;

		try {
			Assert.assertFalse(driver.getCurrentUrl().toLowerCase().contains("error"), "Page shows Apology Error");
			Assert.assertFalse(driver.getTitle().toLowerCase().contains("404"), "Page shows 404 Error");
			Assert.assertFalse(driver.getTitle().toLowerCase().contains("403"), "Page shows 403 Error");
			Assert.assertFalse(driver.getTitle().toLowerCase().contains("error"), "Page shows Apology Error");
//			Assert.assertFalse(driver.findElements(By.xpath("//*[starts-with(@*,'error')]")).size()>0, "Page has Error");
			Assert.assertFalse(driver.getTitle().toLowerCase().contains("Not Found"), "Page shows 'Not Found' error");
//			Assert.assertFalse(driver.findElements(By.xpath("//*[contains(.,'Error')]")).size()>0 , "Page has Error");				
			
		} catch (AssertionError ex) {
			result = false;
			System.out.println(errorButton + " showed error upon clicking on it");
//			showAllError.append(errorButton + " showed error upon clicking on it , \b \t \n");
			showAllError.append(errorButton + " showed error upon clicking on it, "+"\r\n\r\n");
			driver.navigate().back();
		}
		return result;
	}

	@Test//(retryAnalyzer = CustomReporter.RetryListener.class)
	public void execute() throws Exception {

		// Creating an object of string buffer to store all error messages in
		// append mode to be printed at comments at excel.
		showAllError = new StringBuffer();

		// Creating a list to store all the results to fail the method if
		// atleast one result is failed.
		List<Boolean> results = new ArrayList<Boolean>();

		System.out.println("Execution starts");

		// Start the chrome browser.
		System.setProperty("webdriver.chrome.driver", config.getChromePath());
		driver = new ChromeDriver();

		driver.manage().window().maximize();

		// Get the URL to be tested
		driver.get(ShowManagerURL);
		driver.manage().timeouts().implicitlyWait(80, TimeUnit.SECONDS);

		// Login to showman
		login.login(driver);
		driver.findElement(By.xpath("//input[@class='inputShowCode']")).sendKeys(ShowCode);
		driver.findElement(By.xpath("//input[@value='Go!']")).click();

		System.out.println("Focused to the ShowCode");

		List<WebElement> listOfTabs = driver.findElements(By.xpath("//td[contains(@id,'WebMenuShow')]"));

		for (int i = 0; i < listOfTabs.size(); i++) {

			List<WebElement> listOfTabs1 = driver.findElements(By.xpath("//td[contains(@id,'WebMenuShow')]"));
			listOfTabs1.get(i).click();

			List<WebElement> listOfSubtabs = driver.findElements(By.xpath("//div[contains(@id,'MainNav_') and contains(@id,'Item_')]"));

			for (int j = 0; j < listOfSubtabs.size(); j++) {

				List<WebElement> listOfSubtabs1 = driver.findElements(By.xpath("//div[contains(@id,'MainNav_') and contains(@id,'Item_')]"));
				String errorButton = listOfSubtabs1.get(j).getText();
				String mainWin = driver.getWindowHandle();

				listOfSubtabs1.get(j).click();				
				Thread.sleep(2000);
				results.add(testConnection(errorButton, showAllError));

				if (driver.getWindowHandles().size() > 1) {
					util.switchIfWindowsAre(driver, 2);

				if (errorButton.contains("Report Manager") && driver
						.findElements(By.xpath("//input[contains(@id,'Username') or contains(@id,'UserName')]")).size() > 0) {

					// Login to report manager
					login.login(driver);

				}

				results.add(testConnection(errorButton, showAllError));

				if (driver.getWindowHandles().size() > 1) {

					driver.close();
					driver.switchTo().window(mainWin);
				}

			}	
				
			}

		}
		if (showAllError.length() > 0)
			System.out.println(showAllError.toString() + "  had error");

		if (results.contains(false)) {
			Assert.fail("One of the links not working");
		}
	}

	@AfterMethod
	public void tearDown() {
		driver.quit();
	}

	@AfterClass
	public void sendReport() throws Exception {
		Thread.sleep(1000);
		emailReportSM_SmokeTest sendReport = new emailReportSM_SmokeTest();
		sendReport.sendEmail(showAllError.toString(), ShowCode, ShowManagerURL);
	}
}
