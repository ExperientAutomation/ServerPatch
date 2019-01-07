package TestCases;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utils.BrowserFactory;
import utils.WinUtils;
import utils.XlsUtil;
import utils.latestFile;
import CustomReporter.emailReportQA;

//@Listeners(CustomReporter.TesgNGListenerQAServerPatch.class)
public class QAServerPatch extends BrowserFactory {

//  Declare the Test type as Pre OR Post
	
//	public String testType = "Pre-Test";
	public String testType = "Post-Test";

	// Declare the column for ShowCode
	int showCodeColumn = 7;

	// Declare the column for Server Name
	int serverNameColumn = 1;
	
	// Declare the column for Link
	int LinkColumn = 6;
	
	WebDriverWait wait;
	
	WinUtils util = new WinUtils();

	public static int row = 0;
	public static int column = 0;

	public int getRow() {
		return row;
	}

	@BeforeTest
	public int getColumn() {
		if (testType == "Pre-Test") {
			column = 9;
		} else if (testType == "Post-Test") {
			column = 12;
		}
		return column;
	}

	//The below method will check for errors on page.
	public Boolean testConnection(String errorButton, StringBuffer showAllError) {
		boolean result = true;
//		String errorButton = driver.findElement(By.xpath(xpath)).getText();
//		driver.findElement(By.xpath(xpath)).click();
		driver.manage().timeouts().implicitlyWait(200, TimeUnit.SECONDS);

		try {
			Assert.assertFalse(driver.getCurrentUrl().toLowerCase().contains("error"), "Page shows Appology Error");
			Assert.assertFalse(driver.getTitle().toLowerCase().contains("404"), "Page shows 404 Error");
			Assert.assertFalse(driver.getTitle().toLowerCase().contains("403"), "Page shows 403 Error");
			Assert.assertFalse(driver.getTitle().toLowerCase().contains("Not Found"), "Page shows 'Not Found' error");
			//Assert.assertFalse(driver.findElements(By.xpath("//*[contains(.,'Error')]")).size()>0 , "Page has Error");
//			Assert.assertFalse(driver.findElements(By.xpath("//*[starts-with(@*,'error')]")).size()>0 , "Page has Error");

		} catch (AssertionError ex) {
			result = false;
			System.out.println(errorButton + " showed error upon clicking on it");
			showAllError.append(errorButton + " showed error upon clicking on it \n");
			xls.setCellData("Stage Servers", column + 1, row, showAllError.toString());
			driver.navigate().back();
		}
		return result;
	}

	@Test(priority = 0, enabled = true)
	public void executeRow4() throws Exception {		

		downloadedfile.downloadExcelSheet("QA Server");	
		
		filepath = latestFile.lastFileModified(config.getServerPatchFilePath());
		xls = new XlsUtil(filepath.getAbsolutePath());
			
		//Start the Chrome browser and login 		
		driver = BrowserFactory.getBrowser("Chrome");
		
		row = 4;
		// Creating an object of string buffer to store all error messages in append mode to be printed at comments at excel.
		StringBuffer showAllError = new StringBuffer();
		
		// Creating a list to store all the results to fail the method if atleast one result is failed.
		List<Boolean> results = new ArrayList<Boolean>();
		System.out.println("Row 4 execution starts");
				
		// Get the URL to be tested from excel sheet and launch the URL.
		String Row4Link = xls.getCellData("Stage Servers", LinkColumn, row);
		driver.get(Row4Link.trim());		
		
		//Login to showman
		login.smloginChandra(driver);
		
		//Get the showcode and focus to it
		String Row4Showcode = xls.getCellData("Stage Servers", showCodeColumn, row);
		driver.findElement(By.xpath("//input[@class='inputShowCode']")).sendKeys(Row4Showcode.trim());
		driver.findElement(By.xpath("//input[@value='Go!']")).click();
		System.out.println("Focused to the ShowCode: "+Row4Showcode);

		driver.navigate().refresh();		
		
		// Get the server name from excel to validate it
		String Row4ServerName = xls.getCellData("Stage Servers", serverNameColumn, row);
		WebElement element = driver.findElement(By.id("CellServerName"));

		try {
			Assert.assertTrue(element.getText().contains(Row4ServerName.trim()),"The Server Name appeared is " + element.getText());
			results.add(true);
			System.out.println("Validated the server name");
		} catch (AssertionError e) {
			results.add(false);
			showAllError.append(element.getText() + " was appeared instead of " + Row4ServerName + "\n");
			xls.setCellData("Stage Servers", column + 1, row, showAllError.toString());
		}
		
		List<WebElement> listOfTabs = driver.findElements(By.xpath("//td[contains(@id,'WebMenuShow')]"));

		for (int i = 0; i < listOfTabs.size(); i++) {

			List<WebElement> listOfTabs1 = driver.findElements(By.xpath("//td[contains(@id,'WebMenuShow')]"));
			listOfTabs1.get(i).click();
			Thread.sleep(2000);

			List<WebElement> listOfSubtabs = driver
					.findElements(By.xpath("//div[contains(@id,'MainNav_') and contains(@id,'Item_')]"));

			for (int j = 0; j < listOfSubtabs.size(); j++) {

				List<WebElement> listOfSubtabs1 = driver.findElements(By.xpath("//div[contains(@id,'MainNav_') and contains(@id,'Item_')]"));
				String errorButton = listOfSubtabs1.get(j).getText();
				String mainWin = driver.getWindowHandle();

				listOfSubtabs1.get(j).click();
				Thread.sleep(2000);

				if (driver.getWindowHandles().size() > 1)
					util.switchIfWindowsAre(driver, 2);

/*//				Login to Report Manager
				if (errorButton.contains("Report Manager") && driver.findElements(By.xpath("//input[contains(@id,'Username') or contains(@id,'UserName')]")).size() > 0) 
				  login.smloginChandra(driver);
*/
				results.add(testConnection(errorButton, showAllError));

				if (driver.getWindowHandles().size() > 1) {

					driver.close();
					driver.switchTo().window(mainWin);
				}
			}
		}
		if(showAllError.length()>0)	System.out.println(showAllError.toString() + "  had error");

		if (results.contains(false)) {
			Assert.fail("One of the links not working in Row4");
		}
	}

	@Test(priority = 1,enabled = true)
	public void executeRow5() throws Exception {
		row = 5;
		StringBuffer showAllError = new StringBuffer();
		List<Boolean> results = new ArrayList<Boolean>();
		System.out.println("Row 5 execution starts");

		//Start the Chrome browser and login 		
		driver = BrowserFactory.getBrowser("Chrome");
		
		String Row5Link = xls.getCellData("Stage Servers", LinkColumn, row);
		driver.get(Row5Link.trim());
		
		login.smloginChandra(driver);

		String Row5Showcode = xls.getCellData("Stage Servers", showCodeColumn, row);
		driver.findElement(By.xpath("//input[@class='inputShowCode']")).sendKeys(Row5Showcode.trim());
		driver.findElement(By.xpath("//input[@value='Go!']")).click();
	
		System.out.println("Focused to the ShowCode: "+Row5Showcode);

		driver.navigate().refresh();		

		String Row5ServerName = xls.getCellData("Stage Servers", serverNameColumn, row);
		WebElement element = driver.findElement(By.id("CellServerName"));

		try {
			Assert.assertTrue(element.getText().contains(Row5ServerName.trim()),
					"The Server Name appeared is " + element.getText());
			System.out.println("Validated the server name");
		} catch (AssertionError e) {
			results.add(false);
			showAllError.append(element.getText() + " was appeared instead of " + Row5ServerName + "\n");
			xls.setCellData("Stage Servers", column + 1, row, showAllError.toString());
		}
		
		List<WebElement> listOfTabs = driver.findElements(By.xpath("//td[contains(@id,'WebMenuShow')]"));

		for (int i = 0; i < listOfTabs.size(); i++) {

			List<WebElement> listOfTabs1 = driver.findElements(By.xpath("//td[contains(@id,'WebMenuShow')]"));
			listOfTabs1.get(i).click();
			Thread.sleep(2000);

			List<WebElement> listOfSubtabs = driver.findElements(By.xpath("//div[contains(@id,'MainNav_') and contains(@id,'Item_')]"));

			for (int j = 0; j < listOfSubtabs.size(); j++) {

				List<WebElement> listOfSubtabs1 = driver.findElements(By.xpath("//div[contains(@id,'MainNav_') and contains(@id,'Item_')]"));
				String errorButton = listOfSubtabs1.get(j).getText();
				String mainWin = driver.getWindowHandle();

				listOfSubtabs1.get(j).click();
				Thread.sleep(2000);

				if (driver.getWindowHandles().size() > 1)
					util.switchIfWindowsAre(driver, 2);
				
/*//				Login to Report Manager
				if (errorButton.contains("Report Manager") && driver.findElements(By.xpath("//input[contains(@id,'Username') or contains(@id,'UserName')]")).size() > 0) 
				  login.smloginChandra(driver);*/

				results.add(testConnection(errorButton, showAllError));

				if (driver.getWindowHandles().size() > 1) {

					driver.close();
					driver.switchTo().window(mainWin);
				}

			}

		}
		if(showAllError.length()>0)	System.out.println(showAllError.toString() + "  had error");
		
		if (results.contains(false)) {
			Assert.fail("One of the links not working in row "+row);
		}
	}

	@Test(priority = 2, enabled = true)
	public void executeRow6() {
		row = 6;
		System.out.println("Row 6 execution starts");

		//Start the Chrome browser and login 		
		driver = BrowserFactory.getBrowser("Chrome");
		
		String Row6Link = xls.getCellData("Stage Servers", LinkColumn, row);
		driver.get(Row6Link.trim());			

		// Log in to Report Manager
		
		if (driver.findElements(By.xpath("//input[contains(@id,'Username') or contains(@id,'UserName')]")).size() > 0)  
			
		login.smloginChandra(driver);
		
//		driver.findElement(By.xpath("//*[@id='LoginControl1_TextBoxUsername']")).sendKeys(config.LoginCredentails("USER_NAME"));
//		driver.findElement(By.xpath("//*[@id='LoginControl1_TextBoxPassword']")).sendKeys(config.LoginCredentails("PASSWORD"));
//		driver.findElement(By.xpath("//input[@value='Login']")).click(); }
		 
		// Choose the showcode from the dropdown

		wait = new WebDriverWait(driver, 120);
		driver.switchTo().frame("menu");
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//select[@id='DropDownListShow']")));
		
		String Row6Showcode = xls.getCellData("Stage Servers", showCodeColumn, row);
		
		Select dropdown = new Select(driver.findElement(By.xpath("//select[@id='DropDownListShow']")));
		dropdown.selectByValue(Row6Showcode.trim());
		driver.switchTo().defaultContent();
		driver.switchTo().frame("dashboard");
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(.,'Full Inventory Report')]")));
		driver.findElement(By.xpath("//a[contains(.,'Full Inventory Report')]")).click();
		driver.switchTo().defaultContent();
		driver.switchTo().frame("main");
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='LabelReportName']")));
		WebElement reportTable = driver.findElement(By.xpath("//*[@id='LabelReportName']"));
		Assert.assertEquals(true, reportTable.isDisplayed());

	}

	/*@Test(priority = 3, retryAnalyzer = CustomReporter.RetryListener.class, enabled = false)
	public void executeRow7() throws Exception {
		row = 7;
		StringBuffer showAllError = new StringBuffer();
		List<Boolean> results = new ArrayList<Boolean>();
		System.out.println("Row 7 execution starts");

		// driver = new FirefoxDriver();
		System.setProperty("webdriver.chrome.driver", config.getChromePath());
		driver = new ChromeDriver();

		driver.manage().window().maximize();
		
		String Row7Link = xls.getCellData("Stage Servers", LinkColumn, row);
		driver.get(Row7Link.trim());
		
		driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);

		login.smloginChandra(driver);

		String Row7Showcode = xls.getCellData("Stage Servers", showCodeColumn, row);
		driver.findElement(By.xpath("//input[@class='inputShowCode']")).sendKeys(Row7Showcode.trim());
		driver.findElement(By.xpath("//input[@value='Go!']")).click();
		driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);

		driver.navigate().refresh();
		driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);

		String Row7ServerName = xls.getCellData("Stage Servers", serverNameColumn, row);
		WebElement element = driver.findElement(By.id("CellServerName"));

		try {
			Assert.assertTrue(element.getText().contains(Row7ServerName.trim()), "The Server Name appeared instead is " + element.getText());
		} catch (AssertionError e) {
			results.add(false);
			showAllError.append(element.getText() + " was appeared instead of " + Row7ServerName + "\n");
			xls.setCellData("Stage Servers", column + 1, row, showAllError.toString());
		}
		
		List<WebElement> listOfTabs = driver.findElements(By.xpath("//td[contains(@id,'WebMenuShow')]"));

		for (int i = 0; i < listOfTabs.size(); i++) {

			List<WebElement> listOfTabs1 = driver.findElements(By.xpath("//td[contains(@id,'WebMenuShow')]"));
			listOfTabs1.get(i).click();
			Thread.sleep(2000);

			List<WebElement> listOfSubtabs = driver.findElements(By.xpath("//div[contains(@id,'MainNav_') and contains(@id,'Item_')]"));

			for (int j = 0; j < listOfSubtabs.size(); j++) {

				List<WebElement> listOfSubtabs1 = driver
						.findElements(By.xpath("//div[contains(@id,'MainNav_') and contains(@id,'Item_')]"));
				String errorButton = listOfSubtabs1.get(j).getText();
				String mainWin = driver.getWindowHandle();

				listOfSubtabs1.get(j).click();
				Thread.sleep(2000);

				if (driver.getWindowHandles().size() > 1)
					util.switchIfWindowsAre(driver, 2);

//				Login to Report Manager
				if (errorButton.contains("Report Manager") && driver.findElements(By.xpath("//input[contains(@id,'Username') or contains(@id,'UserName')]")).size() > 0) 
				  login.smloginChandra(driver);
				
				results.add(testConnection(errorButton, showAllError));

				if (driver.getWindowHandles().size() > 1) {

					driver.close();
					driver.switchTo().window(mainWin);
				}

			}

		}
		if(showAllError.length()>0)	System.out.println(showAllError.toString() + "  had error");

		if (results.contains(false)) {
			Assert.fail("One of the links not working in Row7");
		}
	}

	@Test(priority = 4, retryAnalyzer = CustomReporter.RetryListener.class, enabled = false)
	public void executeRow8() throws Exception{
		row = 8;
		StringBuffer showAllError = new StringBuffer();
		List<Boolean> results = new ArrayList<Boolean>();
		System.out.println("Row 8 execution starts");

		// driver = new FirefoxDriver();
		System.setProperty("webdriver.chrome.driver", config.getChromePath());
		driver = new ChromeDriver();

		driver.manage().window().maximize();
		
		String Row8Link = xls.getCellData("Stage Servers", LinkColumn, row);
		driver.get(Row8Link.trim());
		
		driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);

		login.smloginChandra(driver);

		String Row8Showcode = xls.getCellData("Stage Servers", showCodeColumn, row);
		driver.findElement(By.xpath("//input[@class='inputShowCode']")).sendKeys(Row8Showcode.trim());
		driver.findElement(By.xpath("//input[@value='Go!']")).click();
//		driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
		System.out.println("Focused to the ShowCode");

		driver.navigate().refresh();
//		driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);

		String Row8ServerName = xls.getCellData("Stage Servers", serverNameColumn, row);
		WebElement element = driver.findElement(By.id("CellServerName"));

		try {
			Assert.assertTrue(element.getText().contains(Row8ServerName.trim()),
					"The Server Name appeared instead is " + element.getText());
			System.out.println("Validated the server name");
		} catch (AssertionError e) {
			results.add(false);
			showAllError.append(element.getText() + " was appeared instead of " + Row8ServerName + "\n");
			xls.setCellData("Stage Servers", column + 1, row, showAllError.toString());
		}
		
		List<WebElement> listOfTabs = driver.findElements(By.xpath("//td[contains(@id,'WebMenuShow')]"));

		for (int i = 0; i < listOfTabs.size(); i++) {

			List<WebElement> listOfTabs1 = driver.findElements(By.xpath("//td[contains(@id,'WebMenuShow')]"));
			listOfTabs1.get(i).click();
			Thread.sleep(2000);

			List<WebElement> listOfSubtabs = driver
					.findElements(By.xpath("//div[contains(@id,'MainNav_') and contains(@id,'Item_')]"));

			for (int j = 0; j < listOfSubtabs.size(); j++) {

				List<WebElement> listOfSubtabs1 = driver
						.findElements(By.xpath("//div[contains(@id,'MainNav_') and contains(@id,'Item_')]"));
				String errorButton = listOfSubtabs1.get(j).getText();
				String mainWin = driver.getWindowHandle();

				listOfSubtabs1.get(j).click();
				Thread.sleep(2000);

				if (driver.getWindowHandles().size() > 1)
					util.switchIfWindowsAre(driver, 2);
				
//				Login to Report Manager
				if (errorButton.contains("Report Manager") && driver.findElements(By.xpath("//input[contains(@id,'Username') or contains(@id,'UserName')]")).size() > 0) 
				  login.smloginChandra(driver);

				results.add(testConnection(errorButton, showAllError));

				if (driver.getWindowHandles().size() > 1) {

					driver.close();
					driver.switchTo().window(mainWin);
				}

			}

		}
		if(showAllError.length()>0)	System.out.println(showAllError.toString() + "  had error");

		if (results.contains(false)) {
			Assert.fail("One of the links not working in Row8");
		}
	}*/

	@Test(priority = 5,enabled = true)
	public void executeRow9() throws Exception {
		
		row = 9;
		StringBuffer showAllError = new StringBuffer();
		List<Boolean> results = new ArrayList<Boolean>();
		System.out.println("Row 9 execution starts");
		
		//Start the Chrome browser and login 		
		driver = BrowserFactory.getBrowser("Chrome");
		
		String Row9Link = xls.getCellData("Stage Servers", LinkColumn, row);
		driver.get(Row9Link.trim());
		driver.manage().timeouts().implicitlyWait(200, TimeUnit.SECONDS);

		login.smloginChandra(driver);
//		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@class='inputShowCode']")));

		String Row9Showcode = xls.getCellData("Stage Servers", showCodeColumn, row);
		driver.findElement(By.xpath("//input[@class='inputShowCode']")).sendKeys(Row9Showcode.trim());
		driver.findElement(By.xpath("//input[@value='Go!']")).click();
		System.out.println("Focused to the ShowCode: "+Row9Showcode);
		driver.navigate().refresh();


		String Row9ServerName = xls.getCellData("Stage Servers", serverNameColumn, row);
		WebElement element = driver.findElement(By.id("CellServerName"));

		try {

			Assert.assertTrue(element.getText().contains(Row9ServerName.trim()),
					"The Server Name appeared instead is " + element.getText());
			System.out.println("Validated the server name");
		} catch (AssertionError e) {
			results.add(false);
			showAllError.append(element.getText() + " was appeared instead of " + Row9ServerName + "\n");
			xls.setCellData("Stage Servers", column + 1, row, showAllError.toString());
		}
		
		Thread.sleep(2000);
		List<WebElement> listOfTabs = driver.findElements(By.xpath("//td[contains(@id,'WebMenuShow')]"));

		for (int i = 0; i < listOfTabs.size(); i++) {

			List<WebElement> listOfTabs1 = driver.findElements(By.xpath("//td[contains(@id,'WebMenuShow')]"));
			listOfTabs1.get(i).click();
			Thread.sleep(2000);

			List<WebElement> listOfSubtabs = driver
					.findElements(By.xpath("//div[contains(@id,'MainNav_') and contains(@id,'Item_')]"));

			for (int j = 0; j < listOfSubtabs.size(); j++) {

				List<WebElement> listOfSubtabs1 = driver
						.findElements(By.xpath("//div[contains(@id,'MainNav_') and contains(@id,'Item_')]"));
				String errorButton = listOfSubtabs1.get(j).getText();
				String mainWin = driver.getWindowHandle();

				listOfSubtabs1.get(j).click();
				Thread.sleep(2000);

				if (driver.getWindowHandles().size() > 1)
					util.switchIfWindowsAre(driver, 2);

/*//				Login to Report Manager
				if (errorButton.contains("Report Manager") && driver.findElements(By.xpath("//input[contains(@id,'Username') or contains(@id,'UserName')]")).size() > 0) 
				  login.smloginChandra(driver);*/
				
				results.add(testConnection(errorButton, showAllError));

				if (driver.getWindowHandles().size() > 1) {

					driver.close();
					driver.switchTo().window(mainWin);
				}

			}

		}
		if(showAllError.length()>0)	System.out.println(showAllError.toString() + "  had error");

		if (results.contains(false)) {
			Assert.fail("One of the links not working in Row9");
		}

	}

	@Test(priority = 6, enabled = true)
	public void executeRow10() {
		row = 10;
		System.out.println("Row 10 execution starts");

		//Start the Chrome browser and login 		
		driver = BrowserFactory.getBrowser("Chrome");
		
		String Row10Link = xls.getCellData("Stage Servers", LinkColumn, row);
		driver.get(Row10Link.trim());
		driver.manage().timeouts().implicitlyWait(200, TimeUnit.SECONDS);

		Assert.assertFalse(driver.getCurrentUrl().toLowerCase().contains("error"), "Page shows Appology Error");
		Assert.assertFalse(driver.getTitle().toLowerCase().contains("404"), "Page shows 404 Error");
		Assert.assertFalse(driver.getTitle().toLowerCase().contains("403"), "Page shows 403 Error");
		Assert.assertFalse(driver.getTitle().toLowerCase().contains("Not Found"), "Page shows 'Not Found' error");
		//Assert.assertFalse(driver.findElements(By.xpath("//*[contains(.,'Error')]")).size()>0 , "Page has Error");
//		Assert.assertFalse(driver.findElements(By.xpath("//*[starts-with(@*,'error')]")).size()>0 , "Page has Error");

	}

	@Test(priority = 7, enabled = true)
	public void executeRow11() {
		row = 11;
		System.out.println("Row 11 execution starts");
 
		//Start the Chrome browser and login 		
		driver = BrowserFactory.getBrowser("Chrome");
		
		// Get the URL from excel and launch it.
		String Row11Link = xls.getCellData("Stage Servers", LinkColumn, row);
		driver.get(Row11Link.trim());
		driver.manage().timeouts().implicitlyWait(200, TimeUnit.SECONDS);

		// Validate the page for errors
		Assert.assertFalse(driver.getCurrentUrl().toLowerCase().contains("error"), "Page shows Appology Error");
		Assert.assertFalse(driver.getTitle().toLowerCase().contains("404"), "Page shows 404 Error");
		Assert.assertFalse(driver.getTitle().toLowerCase().contains("403"), "Page shows 403 Error");
		Assert.assertFalse(driver.getTitle().toLowerCase().contains("Not Found"), "Page shows 'Not Found' error");
		//Assert.assertFalse(driver.findElements(By.xpath("//*[contains(.,'Error')]")).size()>0 , "Page has Error");
//		Assert.assertFalse(driver.findElements(By.xpath("//*[starts-with(@*,'error')]")).size()>0 , "Page has Error");
		
	}
	
	@Test(priority =8, enabled = true)
	public void executeRow12(){
		row = 12;
		System.out.println("Row 12 execution starts");
		
		//Start the Chrome browser and login 		
		driver = BrowserFactory.getBrowser("Chrome"); 
		
		//Get the URL from the excel and launch it
		String Row12Link = xls.getCellData("Stage Servers", LinkColumn, row);
		driver.get(Row12Link.trim());
		driver.manage().timeouts().implicitlyWait(200, TimeUnit.SECONDS);
		
		//Validate the page for Errors
		Assert.assertFalse(driver.getCurrentUrl().toLowerCase().contains("error"), "Page shows Appology Error");
		Assert.assertFalse(driver.getTitle().toLowerCase().contains("404"),"Page shows 404 Error");
		Assert.assertFalse(driver.getTitle().toLowerCase().contains("403"), "Page shows 403 Error");
		Assert.assertFalse(driver.getTitle().toLowerCase().contains("Not Found"), "Page shows 'Not Found' error");
		//Assert.assertFalse(driver.findElements(By.xpath("//*[contains(.,'Error')]")).size()>0 , "Page has Error");
		
	
	}
	
	@Test(priority =9, enabled = true)
	public void executeRow13(){
		
		row=13;
		System.out.println("Row 13 execution starts");
		
		//Start the Chrome browser and login 		
		driver = BrowserFactory.getBrowser("Chrome");
		
//		Get the URL from the excel and launch it
		String Row13Link = xls.getCellData("Stage Servers", LinkColumn, row);
		driver.get(Row13Link.trim());
		driver.manage().timeouts().implicitlyWait(200, TimeUnit.SECONDS);
		
//		Validate the page for Errors
		Assert.assertFalse(driver.getCurrentUrl().toLowerCase().contains("error"), "Page shows Apology Error");
		Assert.assertFalse(driver.getTitle().toLowerCase().contains("404"), "Page shows 404 Error");
		Assert.assertFalse(driver.getTitle().toLowerCase().contains("403"), "Page shows 403 Error");
		Assert.assertFalse(driver.getTitle().toLowerCase().contains("Not Found"), "Page shows 'Not Found' error");
		//Assert.assertFalse(driver.findElements(By.xpath("//*[contains(.,'Error')]")).size()>0 , "Page has Error");
	}
	
	@Test(priority = 10, enabled = true)
	public void executeRow16() throws Exception {

		row = 16;
		//start the chrome browser and login 
		
		driver = BrowserFactory.getBrowser("ChromeOptions");
		wait = new WebDriverWait(driver, 120);		
				
		String Row16Link = xls.getCellData("Stage Servers", LinkColumn, row).trim();
		
//		driver.get(Row9Link.trim());
		
		String uatsecuusername = config.getuatsecuremitUserID();
		String uatpassword = config.getuatsecuremitpsw();
		
//		StringBuffer strBuf = new StringBuffer(Row16Link.trim());
//		strBuf.insert(20, uatsecuusername + ":" + uatpassword + "@");
		
		driver.get("chrome-extension://enhldmjbphoeibbpdhmjkchohnidgnah/options.html");
		driver.findElement(By.id("url")).sendKeys((Row16Link.trim()));
		driver.findElement(By.id("username")).sendKeys(uatsecuusername);
		driver.findElement(By.id("password")).sendKeys(uatpassword);
		driver.findElement(By.className("credential-form-submit")).click();
		driver.get(Row16Link);
		Thread.sleep(2000);
		
		driver.manage().timeouts().implicitlyWait(200, TimeUnit.SECONDS);
		
		// Verify errors on page
		Assert.assertFalse(driver.getCurrentUrl().toLowerCase().contains("error"), "Page shows Appology Error");
		Assert.assertFalse(driver.getTitle().toLowerCase().contains("404"), "Page shows 404 Error");
		Assert.assertFalse(driver.getTitle().toLowerCase().contains("403"), "Page shows 403 Error");
		Assert.assertFalse(driver.getTitle().toLowerCase().contains("Not Found"), "Page shows 'Not Found' error");
		Assert.assertFalse(driver.findElements(By.xpath("//*[contains(.,'Error')]")).size()>0 , "Page has Error");		
		System.out.println("Test for Row 9 executed successfully");

	}

	@Test(priority = 11, enabled = true)
	public void executeRow17() throws IOException{

		row = 17;
 
		//Start the Chrome browser and login 		
		driver = BrowserFactory.getBrowser("Chrome");
		
		String Row17Link = xls.getCellData("Stage Servers", LinkColumn, row);
		driver.get(Row17Link.trim());
		
		driver.manage().timeouts().implicitlyWait(200, TimeUnit.SECONDS);

		driver.findElement(By.xpath("//input[@id='Username']")).sendKeys(config.getsecuremitUserID());
		driver.findElement(By.xpath("//input[@id='Password']")).sendKeys(config.getsecuremitpsw());
		driver.findElement(By.xpath("//button[@class='btn btn-primary']")).click();
		
		// verify errors on page
		Assert.assertFalse(driver.getCurrentUrl().toLowerCase().contains("error"), "Page shows Appology Error");
		Assert.assertFalse(driver.getTitle().toLowerCase().contains("404"), "Page shows 404 Error");
		Assert.assertFalse(driver.getTitle().toLowerCase().contains("403"), "Page shows 403 Error");
		Assert.assertFalse(driver.findElements(By.xpath("//*[starts-with(@*,'error')]")).size()>0 , "Page has Error");		
		System.out.println("Test for Row 10 executed successfully");
	}	
	
	@AfterMethod
	public void tearDown() {
		driver.quit();		
	}

	@AfterClass
	public void sendReport() throws Exception {
		
		Thread.sleep(1000);
		emailReportQA sendReport = new emailReportQA();
		sendReport.sendEmail(filepath.getAbsolutePath());
	}
}
