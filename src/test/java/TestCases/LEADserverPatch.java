package TestCases;

import java.io.IOException;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import CustomReporter.emailReportLead;
import utils.BrowserFactory;
import utils.XlsUtil;
import utils.latestFile;

public class LEADserverPatch extends BrowserFactory {

	// Declare the Test type as Pre OR Post
//	 public final String testType = "Pre-Test";
	public final String testType = "Post-Test";	 

	// Declare the column for ShowCode
	int showCodeColumn = 5;

	// Declare the column for Link
	int LinkColumn = 3;
		
	public static int row = 0;
	public static int column = 0;
	
	WebDriverWait wait;
	
	public int getRow() {
		return row;
	}
	
	@BeforeClass	
	public void initialization() throws IOException, Exception {

		downloadedfile.downloadExcelSheet("LEAD Server");			
		filepath = latestFile.lastFileModified(config.getServerPatchFilePath());
		xls = new XlsUtil(filepath.getAbsolutePath());
		getColumn();
	}

	public int getColumn() {

		if (testType.equals("Pre-Test")) {
			column = 7;
		} else if (testType.equals("Post-Test")) {
			column = 10;
		}
		return column;
	}	

	@Test(priority = 0, enabled = true)
	public void executeRow2to9() throws Exception {	
	

		for (row = 2; row <= 9; row++) {
			
			System.out.println("Starting execution row number " + row);
			String linkToTest = xls.getCellData("Prod Servers", LinkColumn, row);
			driver = BrowserFactory.getBrowser("Chrome");
			
			System.out.println("URL-"+linkToTest);
			driver.get(linkToTest);
			
			testErrorOnPage(driver, row);
			System.out.println("Executed row number " + row);
		}

	}

	@Test(priority = 1, enabled = true)
	public void executeRow10to13() throws Exception {
			
		for (row = 10; row <= 13; row++) {

			System.out.println("Starting execution row number " + row);
			String url = xls.getCellData("Prod Servers", LinkColumn, row);
			System.out.println("URL = " + url);

			// StringBuffer strBuf = new StringBuffer(url);
			// strBuf.insert(7, config.getshowManagerUserID() + ":" +
			// config.getshowManagerPsw() + "@");
			// System.out.println("URL without converting = "+(strBuf.insert(7,
			// config.getshowManagerUserID() + ":" + config.getshowManagerPsw()
			// + "@")));

			// String linkToTest = strBuf.toString();
			// System.out.println("Updated URL= "+linkToTest);
			
//			Calling Chrome browser to add Multi pass extension 
			driver = BrowserFactory.getBrowser("ChromeOptions"); 
			driver.get("chrome-extension://enhldmjbphoeibbpdhmjkchohnidgnah/options.html");
			driver.findElement(By.id("url")).sendKeys(url);
			driver.findElement(By.id("username")).sendKeys(config.LoginCredentails("USER_NAME"));
			driver.findElement(By.id("password")).sendKeys(config.LoginCredentails("PASSWORD"));
			driver.findElement(By.className("credential-form-submit")).click();
			driver.get(url);
			Thread.sleep(2000);

			wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ContentPlaceHolder1_TextBoxShowCode")));

			String showCode = xls.getCellData("Prod Servers", showCodeColumn, row);
			driver.findElement(By.id("ContentPlaceHolder1_TextBoxShowCode")).sendKeys(showCode, Keys.ENTER);		
			
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ContentPlaceHolder1_TextBoxChangeShowCode")));
			driver.findElement(By.name("ctl00$ContentPlaceHolder1$GridViewLeads$ctl15$ctl00")).click();
			driver.findElement(By.name("ctl00$ContentPlaceHolder1$GridViewLeads$ctl01$ButtonInclude")).click();
			
			testErrorOnPage(driver, row);
			System.out.println("Executed row number " + row);
		}
			
	}

	@Test(priority = 3, enabled = true)
	public void executeRows14to17() throws Exception {

		for (row = 14; row <= 17; row++) {

			System.out.println("Starting execution row number " + row);
			String linkToTest = xls.getCellData("Prod Servers", LinkColumn, row);
			driver = BrowserFactory.getBrowser("Chrome");
			driver.get(linkToTest);			
			

			wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//label[contains(text(),' I have reviewed, understand, and accept')]")));

			driver.findElement(By.xpath("//label[contains(text(),' I have reviewed, understand, and accept')]"))
					.click();
			driver.findElement(By.id("terms-continue")).click();

			testErrorOnPage(driver, row);
			System.out.println("Executed row number " + row);
		}

	}

	@Test(priority = 4, enabled = true)
	public void executeRow18to21() throws Exception {
	
		for (row = 18; row <= 21; row++) {

			System.out.println("Starting execution row number " + row);
			String linkToTest = xls.getCellData("Prod Servers", LinkColumn, row);
			int fail = 0;
			StringBuffer allErrors = new StringBuffer();

			driver = BrowserFactory.getBrowser("Chrome");
			driver.get(linkToTest);			

			wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[text()='Guide']")));

			// Testing all the tabs under Guide tab
			List<WebElement> list = driver.findElements(By.xpath("//*[@id='mainContent']/ul[2]/li"));
			for (int i = 0; i < list.size(); i++) {

				List<WebElement> list11 = driver.findElements(By.xpath("//*[@id='mainContent']/ul[2]/li"));
				String errorTabName = list11.get(i).getText();
				try {
					list11.get(i).click();
					Thread.sleep(500);

					if (driver.getCurrentUrl().toLowerCase().contains("Error")) {

						System.out.println("Failed to load " + errorTabName);
						driver.navigate().back();
						
						allErrors.append("Failed to load " + errorTabName + "\n");
						fail++;

					} else if (driver.getTitle().toLowerCase().contains("403")) {

						System.out.println("Failed to load " + errorTabName);
						driver.navigate().back();
						
						allErrors.append("Failed to load " + errorTabName + "\n");
						fail++;

					} else if (driver.getTitle().toLowerCase().contains("404")) {

						System.out.println("Failed to load " + errorTabName);
						driver.navigate().back();
						
						allErrors.append("Failed to load " + errorTabName + "\n");
						fail++;

					} else if (driver.getTitle().toLowerCase().contains("error")) {

						System.out.println("Failed to load " + errorTabName);
						driver.navigate().back();
						
						allErrors.append("Failed to load " + errorTabName + "\n");
						fail++;

					} 
					
					/*else if (driver.findElements(By.xpath("//*[starts-with(@*,'error')]")).size() > 0) {

						System.out.println("Failed to load " + errorTabName);
						driver.navigate().back();
						
						allErrors.append("Failed to load " + errorTabName + "\n");
						fail++;
					}*/

				} catch (Exception e) {

					e.printStackTrace();
				}
			}

			// Testing all the tabs under Data Types tab
			driver.findElement(By.xpath("//*[text()='Data Types']")).click();
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id='mainContent']/ul[2]/li")));
			List<WebElement> list1 = driver.findElements(By.xpath("//*[@id='mainContent']/ul[2]/li"));

			for (int i = 0; i < list1.size(); i++) {

				List<WebElement> list12 = driver.findElements(By.xpath("//*[@id='mainContent']/ul[2]/li"));
				String errorTabName = list12.get(i).getText();
				try {
					list12.get(i).click();
					Thread.sleep(500);

					if (driver.getCurrentUrl().toLowerCase().contains("error")) {

						System.out.println("Failed to load " + errorTabName);
						allErrors.append("Failed to load " + errorTabName + "\n");
						driver.navigate().back();
						
						fail++;

					} else if (driver.getTitle().toLowerCase().contains("403")) {

						System.out.println("Failed to load " + errorTabName);
						allErrors.append("Failed to load " + errorTabName + "\n");
						driver.navigate().back();
						
						fail++;

					} else if (driver.getTitle().toLowerCase().contains("404")) {
						allErrors.append("Failed to load " + errorTabName + "\n");
						System.out.println("Failed to load " + errorTabName);
						driver.navigate().back();
						
						fail++;

					} else if (driver.getTitle().toLowerCase().contains("error")) {
						System.out.println("Failed to load " + errorTabName);
						allErrors.append("Failed to load " + errorTabName + "\n");
						driver.navigate().back();
						
						fail++;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			// Testing all the tabs under Query Builder tab
			driver.findElement(By.xpath("//*[text()='Query Builder']")).click();
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//*[@id='mainContent']/ul[2]/li")));

			List<WebElement> list2 = driver.findElements(By.xpath("//*[@id='mainContent']/ul[2]/li"));
			for (int i = 0; i < list2.size(); i++) {

				List<WebElement> list22 = driver.findElements(By.xpath("//*[@id='mainContent']/ul[2]/li"));
				String errorTabName = list22.get(i).getText();
				try {
					list22.get(i).click();
					Thread.sleep(500);

					if (driver.getCurrentUrl().toLowerCase().contains("error")) {

						System.out.println("Failed to load " + errorTabName);
						driver.navigate().back();
						allErrors.append("Failed to load " + errorTabName + "\n");
						
						fail++;

					} else if (driver.getTitle().toLowerCase().contains("403")) {

						System.out.println("Failed to load " + errorTabName);
						driver.navigate().back();
						allErrors.append("Failed to load " + errorTabName + "\n");
						
						fail++;

					} else if (driver.getTitle().toLowerCase().contains("404")) {

						System.out.println("Failed to load " + errorTabName);
						driver.navigate().back();
						
						allErrors.append("Failed to load " + errorTabName + "\n");
						fail++;

					} else if (driver.getTitle().toLowerCase().contains("error")) {

						System.out.println("Failed to load " + errorTabName);
						driver.navigate().back();
						
						allErrors.append("Failed to load " + errorTabName + "\n");
						fail++;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			try {
				if (fail > 0) {

					xls.setCellData("Prod Servers", column, row, "Failed");
					xls.setCellData("Prod Servers", column - 1, row, System.getProperty("user.name"));
					xls.setCellData("Prod Servers", column + 1, row, allErrors.toString());

				} else {
					xls.setCellData("Prod Servers", column, row, "Pass");
					xls.setCellData("Prod Servers", column - 1, row, System.getProperty("user.name"));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			System.out.println("Executed row number " + row);
		}

	}

	@Test(priority = 5, enabled = true)
	public void executeRow30to33() throws Exception {

		for (row = 30; row <= 33; row++) {

			System.out.println("Starting execution row number " + row);
			String linkToTest = xls.getCellData("Prod Servers", LinkColumn, row);

			driver = BrowserFactory.getBrowser("Chrome");
			driver.get(linkToTest);				

			testErrorOnPage(driver, row);
			System.out.println("Executed row number " + row);
		}

	}

	@Test(priority = 2, enabled = true)
	public void executeRow34to37() throws Exception {		
		
		
//		driver = BrowserFactory.getBrowser("Chrome");		

		for (row = 34; row <= 37; row++) {

			System.out.println("Starting execution row number " + row);
			String url = xls.getCellData("Prod Servers", LinkColumn, row);
			System.out.println("URL = " +url);			
			
			/* StringBuffer strBuf = new StringBuffer(url); strBuf.insert(7,
			config.LoginCredentails("USER_NAME") + ":" + config.LoginCredentails("PASSWORD") + "@"); 			
			 String linkToTest = strBuf.toString();			
			 System.out.println("URL:"+linkToTest);
			 
			 driver = BrowserFactory.getBrowser("Chrome");
			 driver.get(linkToTest); 
			 
			 driver.get(linkToTest);*/

//			Calling Chrome browser to add Multi pass extension 
			driver = BrowserFactory.getBrowser("ChromeOptions"); 
			wait = new WebDriverWait(driver, 60);
			
			driver.get("chrome-extension://enhldmjbphoeibbpdhmjkchohnidgnah/options.html");
			driver.findElement(By.id("url")).sendKeys(url);
			driver.findElement(By.id("username")).sendKeys(config.LoginCredentails("USER_NAME"));
			driver.findElement(By.id("password")).sendKeys(config.LoginCredentails("PASSWORD"));
			driver.findElement(By.className("credential-form-submit")).click();
			driver.get(url);
			Thread.sleep(2000);

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("TextBoxShowCode")));

			String showCode = xls.getCellData("Prod Servers", showCodeColumn, row);
			driver.findElement(By.id("TextBoxShowCode")).sendKeys(showCode, Keys.ENTER);
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("TextBoxShowCode")));

			testErrorOnPage(driver, row);
			System.out.println("Executed row number " + row);
		}
			driver.close();
	}

	@Test(priority = 6, enabled = true)
	public void executeRow42to45() throws Exception {

		for (row = 42; row <= 45; row++) {

			System.out.println("Starting execution row number " + row);
			String linkToTest = xls.getCellData("Prod Servers", LinkColumn, row);

			driver = BrowserFactory.getBrowser("Chrome");
			driver.get(linkToTest);
			
			

			testErrorOnPage(driver, row);
			System.out.println("Executed row number " + row);
		}

	}

	@Test(priority = 7, enabled = true)
	public void executeRow46to49() throws Exception {

		for (row = 46; row <= 49; row++) {

			System.out.println("Starting execution row number " + row);
			String linkToTest = xls.getCellData("Prod Servers", LinkColumn, row);

			driver = BrowserFactory.getBrowser("Chrome");
			driver.get(linkToTest);				

			wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ContentMain_TextBoxActCode")));

			String actCode = xls.getCellData("Prod Servers", 4, row);
			driver.findElement(By.id("ContentMain_TextBoxActCode")).sendKeys(actCode);

			String accKey = xls.getCellData("Prod Servers", 5, row);
			driver.findElement(By.id("ContentMain_TextBoxKey")).sendKeys(accKey);

			driver.findElement(By.id("ContentMain_ButtonSubmit")).click();

			testErrorOnPage(driver, row);
			System.out.println("Executed row number " + row);
		}

	}

	@Test(priority = 8, enabled = true)
	public void executeRow51to54() throws Exception {

		for (row = 51; row <= 54; row++) {

			System.out.println("Starting execution row number " + row);
			String linkToTest = xls.getCellData("Prod Servers", LinkColumn, row);

			driver = BrowserFactory.getBrowser("Chrome");
			driver.get(linkToTest);					

			wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("MainContent_TextBoxActivationCode_zip")));

			String actCode = xls.getCellData("Prod Servers", 4, row);
			driver.findElement(By.id("MainContent_TextBoxActivationCode_zip")).sendKeys(actCode);

			String accKey = xls.getCellData("Prod Servers", 5, row);
			driver.findElement(By.id("MainContent_TextBoxConnectKey_zip")).sendKeys(accKey, Keys.ENTER);

			testErrorOnPage(driver, row);
			System.out.println("Executed row number " + row);
		}

	}
	
	// This method validates various errors on the page
		public void testErrorOnPage(WebDriver ldriver, int lrow) throws Exception {

			if (ldriver.getCurrentUrl().toLowerCase().contains("error")) {
				Thread.sleep(1000);
				System.out.println("Failed");
				xls.setCellData("Prod Servers", column, lrow, "Failed");
				xls.setCellData("Prod Servers", column - 1, lrow, System.getProperty("user.name"));
				xls.setCellData("Prod Servers", column + 1, lrow, "Our Apology Page appeared");

			} else if (ldriver.getTitle().toLowerCase().contains("403")) {
				Thread.sleep(1000);
				System.out.println("Failed");
				xls.setCellData("Prod Servers", column, lrow, "Failed");
				xls.setCellData("Prod Servers", column - 1, lrow, System.getProperty("user.name"));
				xls.setCellData("Prod Servers", column + 1, lrow, "Error 403 was occured");

			} else if (ldriver.getTitle().toLowerCase().contains("404")) {
				Thread.sleep(1000);
				System.out.println("Failed");
				xls.setCellData("Prod Servers", column, lrow, "Failed");
				xls.setCellData("Prod Servers", column - 1, lrow, System.getProperty("user.name"));
				xls.setCellData("Prod Servers", column + 1, lrow, "Error 404 was occured");

			} /*else if (ldriver.findElements(By.xpath("//*[starts-with(@*,'error')]")).size() > 0) {
				Thread.sleep(1000);
				System.out.println("Failed");
				xls.setCellData("Prod Servers", column, lrow, "Failed");
				xls.setCellData("Prod Servers", column - 1, lrow, System.getProperty("user.name"));
				xls.setCellData("Prod Servers", column + 1, lrow, "The Page was not loaded Or there is Error on page");

			}*/ else {
				Thread.sleep(1000);
				xls.setCellData("Prod Servers", column, lrow, "Pass");
				xls.setCellData("Prod Servers", column - 1, lrow, System.getProperty("user.name"));
			}

		}

	@AfterSuite
	public void tearDown() throws Exception {		
		driver.quit();
	}

	@AfterClass(enabled = true)
	public void sendReport() throws Exception {
		Thread.sleep(1000);
		emailReportLead sendReport = new emailReportLead();
		sendReport.sendEmail(filepath.getAbsolutePath());
	}
}
