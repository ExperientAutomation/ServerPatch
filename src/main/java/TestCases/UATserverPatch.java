package TestCases;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import java.io.IOException;
import CustomReporter.emailReportUAT;
import utils.BrowserFactory;
import utils.XlsUtil;
import utils.latestFile;

@Listeners(CustomReporter.TesgNGListenerUATServerPatch.class)
public class UATserverPatch extends BrowserFactory {

	// Declare the Test type as Pre OR Post
	public String testType = "Pre-Test";
//	public String testType = "Post-Test";

	// Declare the column for ShowCode
	int showCodeColumn = 5;

	// Declare the column for Server Name
	int serverNameColumn = 1;
	
	// Declare the column for Link
	int LinkColumn = 3;

	WebDriverWait wait;

	public static int row = 0;
	public static int column = 0;	

	public int getRow() {
		return row;
	}	
	
	@BeforeClass	
	public void initialization() throws IOException, Exception {

		downloadedfile.downloadExcelSheet("UAT Server");			
		filepath = latestFile.lastFileModified(config.getServerPatchFilePath());
		xls = new XlsUtil(filepath.getAbsolutePath());
		getColumn();
	}	

	public int getColumn() throws Exception {				
		
		if (testType == "Pre-Test") {
			column = 7;
		} else {
			column = 9;
		}
		return column;
	}

	@Test(priority = 0, retryAnalyzer = CustomReporter.RetryListener.class, enabled=true)
	public void executeRow1() throws Exception {
		
		row = 2;				
			
		//Start the Chrome browser and login 		
		driver = BrowserFactory.getBrowser("Chrome");		
		
		String Row2Link = xls.getCellData("Pre Req Servers", LinkColumn, row);
		driver.get(Row2Link.trim());
		
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		
		// Login into Show Manager 
		login.login(driver);
		
		// Creating a new explicit wait
		wait = new WebDriverWait(driver, 200);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Logout')]")));
		
		// Adding assertion to confirm login
		Assert.assertTrue(driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).isDisplayed(),"Could not Login");
		
		//Get the showcode from excel sheet
		String Row2Showcode = xls.getCellData("Pre Req Servers", showCodeColumn, row);

		driver.findElement(By.xpath("//input[@class='inputShowCode']")).sendKeys(Row2Showcode.trim());
		driver.findElement(By.xpath("//input[@value='Go!']")).click();
		
		System.out.println("Focused to the ShowCode");

		driver.navigate().refresh();

		WebElement element = driver.findElement(By.id("CellServerName"));

		String Row2ServerName = xls.getCellData("Pre Req Servers", serverNameColumn, row);

		//Validate the server name
		Assert.assertTrue(element.getText().contains(Row2ServerName.trim()), element.getText()+" was appeared instead of "+Row2ServerName);

		System.out.println("Test for Row 2 executed successfully");
		driver.findElement(By.xpath("//a[@class='Logout']")).click();
		

	}

	@Test(priority = 1, retryAnalyzer = CustomReporter.RetryListener.class, enabled=true)
	public void executeRow2() throws IOException{
		row = 2;
		
		//Start the Chrome browser and login 		
		driver = BrowserFactory.getBrowser("Chrome");
		
		String Row3Link = xls.getCellData("Pre Req Servers", LinkColumn, row);
		driver.get(Row3Link.trim());
		
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

		login.login(driver);
		wait = new WebDriverWait(driver, 200);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Logout')]")));
		Assert.assertTrue(driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).isDisplayed(),"Could not Login");
		
		//get the showcode from excel sheet
		String Row3Showcode = xls.getCellData("Pre Req Servers", showCodeColumn, row);

		driver.findElement(By.xpath("//input[@class='inputShowCode']")).sendKeys(Row3Showcode.trim());
		driver.findElement(By.xpath("//input[@value='Go!']")).click();
		
		System.out.println("Focused to the ShowCode");

		driver.navigate().refresh();
		
		WebElement element = driver.findElement(By.id("CellServerName"));

		String Row3ServerName = xls.getCellData("Pre Req Servers", serverNameColumn, row);
		
		// Validate the server name
		Assert.assertTrue(element.getText().contains(Row3ServerName.trim()),element.getText() + " was appeared instead of " + Row3ServerName);

		System.out.println("Test for Row 3 executed successfully");
		driver.findElement(By.xpath("//a[@class='Logout']")).click();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		System.out.println("Logged Out Successfully");

	}

	@Test(priority = 2, retryAnalyzer = CustomReporter.RetryListener.class, enabled=true)
	public void executeRow3() throws IOException {

		row = 3;

		//Start the Chrome browser and login 		
		driver = BrowserFactory.getBrowser("Chrome");
		
		String Row4Link = xls.getCellData("Pre Req Servers", LinkColumn, row);
		driver.get(Row4Link.trim());
		
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

		login.login(driver);
		wait = new WebDriverWait(driver, 200);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Logout')]")));
		Assert.assertTrue(driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).isDisplayed(),"Could not Login");

		driver.navigate().refresh();
				
		//Validate the server name as Enterprise 
		WebElement element = driver.findElement(By.id("CellServerName"));
		Assert.assertTrue(element.getText().contains("FRQAPROD02"), element.getText() + " was appeared instead of FRQAPROD02");
		System.out.println("Validated the Enterprise server Name");
		
		//Get the showcode from excel
		String Row4Showcode = xls.getCellData("Pre Req Servers", showCodeColumn, row);
		
		driver.findElement(By.xpath("//input[@class='inputShowCode']")).sendKeys(Row4Showcode.trim());
		driver.findElement(By.xpath("//input[@value='Go!']")).click();
		System.out.println("Focused to the ShowCode");

		driver.navigate().refresh();
		
		WebElement element1 = driver.findElement(By.id("CellServerName"));

		//Validate the server name
		Assert.assertTrue(element1.getText().contains("FRQAPROD02") ? true : element1.getText().contains("FRQAPROD03"), element1.getText()+" was appeared instead");

		System.out.println("Test for Row 4 executed successfully");
		driver.findElement(By.xpath("//a[@class='Logout']")).click();
		System.out.println("Logged Out Successfully");

	}

	@Test(priority = 3, retryAnalyzer = CustomReporter.RetryListener.class, enabled=true)
	public void executeRow4() throws IOException {

		row = 4;
		//Start the Chrome browser and login 		
		driver = BrowserFactory.getBrowser("Chrome");		 
		
		String Row5Link = xls.getCellData("Pre Req Servers", LinkColumn, row);
		driver.get(Row5Link.trim());
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		//Verify for errors on page
		Assert.assertFalse(driver.getCurrentUrl().toLowerCase().contains("error"), "Page shows Appology Error");
		Assert.assertFalse(driver.getTitle().toLowerCase().contains("404"), "Page shows 404 Error");
		Assert.assertFalse(driver.getTitle().toLowerCase().contains("403"), "Page shows 403 Error");
		Assert.assertFalse(driver.getTitle().toLowerCase().contains("Not Found"), "Page shows 'Not Found' error");
		Assert.assertFalse(driver.findElements(By.xpath("//*[contains(.,'Error')]")).size()>0 , "Page has Error");
		
		System.out.println("Test for Row 5 executed successfully");
	}

	@Test(priority = 4, retryAnalyzer = CustomReporter.RetryListener.class, enabled=true)
	public void executeRow5() throws IOException{

		row = 5;

		//Start the chrome browser and login and validate the same
		
		driver = BrowserFactory.getBrowser("Chrome");
		
		String Row6Link = xls.getCellData("Pre Req Servers", LinkColumn, row);
		driver.get(Row6Link.trim());
		
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

		login.login(driver);
		wait = new WebDriverWait(driver, 200);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Logout')]")));
		Assert.assertTrue(driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).isDisplayed(),"Could not Login");
		
		//Get the showcode from excel
		String Row6Showcode = xls.getCellData("Pre Req Servers", showCodeColumn, row);

		driver.findElement(By.xpath("//input[@class='inputShowCode']")).sendKeys(Row6Showcode.trim());
		driver.findElement(By.xpath("//input[@value='Go!']")).click();
		System.out.println("Focused to the ShowCode");

		driver.navigate().refresh();
		
		WebElement element = driver.findElement(By.id("CellServerName"));

		String Row6ServerName = xls.getCellData("Pre Req Servers", serverNameColumn, row);
		
		//Validate the server Name
		Assert.assertTrue(element.getText().contains(Row6ServerName.trim()),element.getText() + " was appeared instead of " + Row6ServerName);

		System.out.println("Test for Row 6 executed successfully");
		driver.findElement(By.xpath("//a[@class='Logout']")).click();
		System.out.println("Logged Out Successfully");

	}

	@Test(priority = 5, retryAnalyzer = CustomReporter.RetryListener.class, enabled=false)
	public void executeRow6() throws IOException{

		row = 6;
		
		//Start the Chrome browser and login 		
		driver = BrowserFactory.getBrowser("Chrome");
		
		String Row7Link = xls.getCellData("Pre Req Servers", LinkColumn, row);
		driver.get(Row7Link.trim());
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		login.login(driver);
		wait = new WebDriverWait(driver, 200);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Logout')]")));
		Assert.assertTrue(driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).isDisplayed(),"Could not Login");

		//Get the showcode from excel
		String Row7Showcode = xls.getCellData("Pre Req Servers", showCodeColumn, row);

		driver.findElement(By.xpath("//input[@class='inputShowCode']")).sendKeys(Row7Showcode.trim());
		driver.findElement(By.xpath("//input[@value='Go!']")).click();
		
		System.out.println("Focused to the ShowCode");

		driver.navigate().refresh();
		

		WebElement element = driver.findElement(By.id("CellServerName"));
		
		//Get the server name from excel and validate the same
		String Row7ServerName = xls.getCellData("Pre Req Servers", serverNameColumn, row);

		Assert.assertTrue(element.getText().contains(Row7ServerName.trim()),element.getText() + " was appeared instead of " + Row7ServerName);

		System.out.println("Test for Row 7 executed successfully");
		driver.findElement(By.xpath("//a[@class='Logout']")).click();
		System.out.println("Logged Out Successfully");

	}

	@Test(priority = 6, retryAnalyzer = CustomReporter.RetryListener.class, enabled=false)
	public void executeRow7() throws Exception {

		row = 7;
		//start the chrome browser and login 
		
		driver = BrowserFactory.getBrowser("ChromeOptions");
		wait = new WebDriverWait(driver, 120);
		
				
		String Row9Link = xls.getCellData("Pre Req Servers", LinkColumn, row).trim();
		
//		driver.get(Row9Link.trim());
		
		String uatsecuusername ="uatsecuremit\\youngs";
		String uatpassword = "Bootsie69";
		
		StringBuffer strBuf = new StringBuffer(Row9Link.trim());
		strBuf.insert(7, uatsecuusername + ":" + uatpassword + "@");
		
		driver.get("chrome-extension://enhldmjbphoeibbpdhmjkchohnidgnah/options.html");
		driver.findElement(By.id("url")).sendKeys((Row9Link.trim()));
		driver.findElement(By.id("username")).sendKeys(uatsecuusername);
		driver.findElement(By.id("password")).sendKeys(uatpassword);
		driver.findElement(By.className("credential-form-submit")).click();
		driver.get(Row9Link);
		Thread.sleep(2000);
		
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		
		// Verify errors on page
		Assert.assertFalse(driver.getCurrentUrl().toLowerCase().contains("error"), "Page shows Appology Error");
		Assert.assertFalse(driver.getTitle().toLowerCase().contains("404"), "Page shows 404 Error");
		Assert.assertFalse(driver.getTitle().toLowerCase().contains("403"), "Page shows 403 Error");
		Assert.assertFalse(driver.getTitle().toLowerCase().contains("Not Found"), "Page shows 'Not Found' error");
		Assert.assertFalse(driver.findElements(By.xpath("//*[contains(.,'Error')]")).size()>0 , "Page has Error");		
		System.out.println("Test for Row 9 executed successfully");

	}

	@Test(priority = 7, retryAnalyzer = CustomReporter.RetryListener.class, enabled=false)
	public void executeRow8() throws IOException{

		row = 8;
		
		//Start the Chrome browser and login 		
		driver = BrowserFactory.getBrowser("Chrome");
		
		String Row10Link = xls.getCellData("Pre Req Servers", LinkColumn, row);
		driver.get(Row10Link.trim());
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		driver.findElement(By.xpath("//input[@id='Username']")).sendKeys(config.getsecuremitUserID());
		driver.findElement(By.xpath("//input[@id='Password']")).sendKeys(config.getsecuremitpsw());
		driver.findElement(By.xpath("//button[@class='btn btn-primary']")).click();
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		
		// verify errors on page
		Assert.assertFalse(driver.getCurrentUrl().toLowerCase().contains("error"), "Page shows Appology Error");
		Assert.assertFalse(driver.getTitle().toLowerCase().contains("404"), "Page shows 404 Error");
		Assert.assertFalse(driver.getTitle().toLowerCase().contains("403"), "Page shows 403 Error");
		Assert.assertFalse(driver.findElements(By.xpath("//*[starts-with(@*,'error')]")).size()>0 , "Page has Error");		
		System.out.println("Test for Row 10 executed successfully");
	}

	@AfterTest
	public void quitBrowser() throws Exception {
		driver.quit();
	}

	@AfterClass(enabled =true)
	public void sendReport() throws Exception {

		Thread.sleep(1000);
		emailReportUAT sendReport = new emailReportUAT();
		sendReport.sendEmail(filepath.getAbsolutePath());
	}
}
