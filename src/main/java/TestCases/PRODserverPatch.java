package TestCases;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import CustomReporter.emailReportPROD;
import java.io.IOException;

import utils.BrowserFactory;
import utils.XlsUtil;
import utils.latestFile;

@Listeners(CustomReporter.TesgNGListenerPRODServerPatch.class)
public class PRODserverPatch extends BrowserFactory {

	// Declare the Test type as Pre OR Post
//	public String testType = "Pre-Test";
	public String testType = "Post-Test";

	// Declare the column for Server Name
	int serverNameEnterpriseColumn = 1;
	int serverNameShowColumn = 2;
	
	// Declare the column for Link
	int LinkColumn = 6;
	
	//Declare the column for Showcode
	int showCodeColumn = 4;
	 
	WebDriverWait wait;
	WebElement element;	 
	
	public static int row = 0;
	public static int column = 0;

	public int getRow() {
		return row;
	}
	
	@BeforeClass	
	public void initialization() throws IOException, Exception {

//		downloadedfile.downloadExcelSheet("PROD Server Patch Automation");			
		filepath = latestFile.lastFileModified(config.getServerPatchFilePath());
		xls = new XlsUtil(filepath.getAbsolutePath());
		getColumn();
	}

	
	public int getColumn() {
		if (testType == "Pre-Test") {
			column = 9;
		} else {
			column = 12;
		}
		return column;
	}

	@Test(priority = 0, retryAnalyzer = CustomReporter.RetryListener.class, enabled = true)
	public void executeRow2() throws IOException, Exception {
		
		row = 2;
		
		//Start the Chrome browser and login 		
		driver = BrowserFactory.getBrowser("Chrome");
		
		String RowLink = xls.getCellData("Sheet1", LinkColumn, row);
		System.out.println("URL- "+RowLink);
		driver.get(RowLink.trim());
		Thread.sleep(2000);
		
		Assert.assertFalse(driver.getCurrentUrl().toLowerCase().contains("error"), "Page shows Appology Error");
		Assert.assertFalse(driver.getTitle().toLowerCase().contains("404"), "Page shows 404 Error");
		Assert.assertFalse(driver.getTitle().toLowerCase().contains("403"), "Page shows 403 Error");
//		Assert.assertFalse(driver.findElements(By.xpath("//*[contains(.,'error')]")).size()>0 , "Page has Error");

		System.out.println("Test for Row "+row+" executed successfully");

	}

	@Test(priority = 1, retryAnalyzer = CustomReporter.RetryListener.class, enabled = true)
	public void executeRow3() {
		row = 3;

		System.setProperty("webdriver.chrome.driver", config.getChromePath());
		driver = new ChromeDriver();

		driver.manage().window().maximize();
		
		String RowLink = xls.getCellData("Sheet1", LinkColumn, row);
		System.out.println("URL- "+RowLink);
		driver.get(RowLink.trim());
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		driver.findElement(By.id("UserName")).sendKeys(config.LoginCredentails("USER_NAME"));
		driver.findElement(By.id("Password")).sendKeys(config.LoginCredentails("PASSWORD"));
		driver.findElement(By.xpath("//input[@value='Log in']")).click();
		
		wait = new WebDriverWait(driver, 100);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='Logout']")));
		Assert.assertTrue(driver.findElement(By.xpath("//a[text()='Logout']")).isDisplayed(),"Could not Login");
		
		Assert.assertTrue(driver.findElement(By.xpath("//a[text()='Dashboard']")).isDisplayed(),"Home tab did not work to open Dashboard");
		
		driver.findElement(By.xpath("//a[text()='Events']")).click();
		Assert.assertTrue(driver.findElement(By.xpath("//h3[text()='Manage Events']")).isDisplayed(),"Events tab did not work to open Manage Events");
		
		driver.findElement(By.xpath("//a[text()='Groups']")).click();
		Assert.assertTrue(driver.findElement(By.xpath("//h3[text()='Manage Groups']")).isDisplayed(),"Groups tab did not work to open Manage Groups");
		
		driver.findElement(By.xpath("//a[text()='RAB']")).click();
		Assert.assertTrue(driver.findElement(By.xpath("//h3[text()='New/Pending Block Requests']")).isDisplayed(),"RAB tab did not work to open Block Requests");
		
		driver.findElement(By.xpath("//a[text()='People']")).click();
		Assert.assertTrue(driver.findElement(By.id("LastName")).isDisplayed(),"People tab did not work to open Customer Search");
		
		driver.findElement(By.xpath("//a[text()='Reports']")).click();
		Assert.assertTrue(driver.findElement(By.xpath("//h3[text()='Manage Reports']")).isDisplayed(),"Reports tab did not work to open Manage Reports");
		
		driver.findElement(By.xpath("//a[text()='Logout']")).click();
		Assert.assertTrue(driver.findElement(By.id("UserName")).isDisplayed(),"Did not Log Out");
		
		System.out.println("Test for Row  "+row+"  executed successfully");
	}

	@Test(priority = 2, retryAnalyzer = CustomReporter.RetryListener.class, enabled = true)
	public void executeRow4() {
		row = 4;

		System.setProperty("webdriver.chrome.driver", config.getChromePath());
		driver = new ChromeDriver();

		driver.manage().window().maximize();
		
		String RowLink = xls.getCellData("Sheet1", LinkColumn, row);
		System.out.println("URL- "+RowLink);
		driver.get(RowLink.trim());
		
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

		Assert.assertFalse(driver.getCurrentUrl().toLowerCase().contains("error"), "Page shows Appology Error");
		Assert.assertFalse(driver.getTitle().toLowerCase().contains("404"), "Page shows 404 Error");
		Assert.assertFalse(driver.getTitle().toLowerCase().contains("403"), "Page shows 403 Error");
//		Assert.assertFalse(driver.findElements(By.xpath("//*[contains(.,'error')]")).size()>0 , "Page has Error");
		
		System.out.println("Test for Row  "+row+"  executed successfully");

	}

	@Test(priority = 3, retryAnalyzer = CustomReporter.RetryListener.class, enabled = false)
	public void executeRow5tes() throws Exception {
		row = 5;
		
		System.setProperty("webdriver.chrome.driver", config.getChromePath());
		driver = new ChromeDriver();

		driver.manage().window().maximize();
		
		String RowLink = xls.getCellData("Sheet1", LinkColumn, row);
		System.out.println("URL- "+RowLink);
		driver.get(RowLink.trim());
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		login.login(driver);
		wait = new WebDriverWait(driver, 100);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Logout')]")));
		Assert.assertTrue(driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).isDisplayed(),"Could not Login");

		driver.navigate().refresh();

		WebElement element = driver.findElement(By.id("CellServerName"));

		String RowServerNameShow = xls.getCellData("Sheet1", serverNameShowColumn, row);

		Assert.assertTrue(element.getText().contains(RowServerNameShow.trim()), element.getText()+" was appeared instead of "+RowServerNameShow);

        List<WebElement> b=driver.findElements(By.xpath("//td[contains(@id,'ctl00WebMenuShow')]"));
		
		for (int i=0; i<b.size(); i++) {
			
			List<WebElement> a=driver.findElements(By.xpath("//td[contains(@id,'ctl') and contains(@id,'WebMenuShow')]"));
	        a.get(i).click();
			Thread.sleep(2000);
			
			Assert.assertFalse(driver.getCurrentUrl().toLowerCase().contains("error"), "Page shows Appology Error");
			Assert.assertFalse(driver.getTitle().toLowerCase().contains("404"), "Page shows 404 Error");
			Assert.assertFalse(driver.getTitle().toLowerCase().contains("403"), "Page shows 403 Error");
//			Assert.assertFalse(driver.findElements(By.xpath("//*[contains(.,'error')]")).size()>0 , "Page has Error");
			
		}
		
		Select sel = new Select(driver.findElement(By.id("ctl08_DropDownListMode")));
		sel.selectByValue("ENT");
		driver.navigate().refresh();
		
//		int loginPage = driver.findElements(By.xpath("//input[contains(@id,'Username') or contains(@id,'UserName')]")).size();
//		if(loginPage>0) login.login(driver);

		element = driver.findElement(By.id("CellServerName"));

		String RowServerNameEnt = xls.getCellData("Sheet1", serverNameEnterpriseColumn, row);

		Assert.assertTrue(element.getText().contains(RowServerNameEnt.trim()), element.getText()+" was appeared instead of "+RowServerNameEnt);
		
		System.out.println("Test for Row  "+row+"  executed successfully");
	}

	@Test(priority = 3, retryAnalyzer = CustomReporter.RetryListener.class, enabled = true)
	public void executeRow5() throws Exception {
		row = 5;

		System.setProperty("webdriver.chrome.driver", config.getChromePath());
		driver = new ChromeDriver();

		driver.manage().window().maximize();
		
		String RowLink = xls.getCellData("Sheet1", LinkColumn, row);
		System.out.println("URL- "+RowLink);	
		
		driver.get(RowLink.trim());
		
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		
		login.login(driver);
		wait = new WebDriverWait(driver, 100);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Logout')]")));
		Assert.assertTrue(driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).isDisplayed(),"Could not Login");
		
		//Enter the Showcode 
		String showCode = xls.getCellData("Sheet1", showCodeColumn, row);
		driver.findElement(By.xpath("//input[@class='inputShowCode']")).sendKeys(showCode.trim());
		driver.findElement(By.xpath("//input[@value='Go!']")).click();

		WebElement element = driver.findElement(By.id("CellServerName"));

		String RowServerNameShow = xls.getCellData("Sheet1", serverNameShowColumn, row);

		Assert.assertTrue(element.getText().contains(RowServerNameShow.trim()), element.getText()+" was appeared instead of "+RowServerNameShow);

		for (int i=1; i<10; i++) {
			
			driver.findElement(By.xpath("//td[contains(@id,'WebMenuShow_"+i+"')]")).click();
			Thread.sleep(2000);
			Assert.assertFalse(driver.getCurrentUrl().toLowerCase().contains("error"), "Page shows Appology Error");
			Assert.assertFalse(driver.getTitle().toLowerCase().contains("404"), "Page shows 404 Error");
			Assert.assertFalse(driver.getTitle().toLowerCase().contains("403"), "Page shows 403 Error");
//			Assert.assertFalse(driver.findElements(By.xpath("//*[contains(.,'error')]")).size()>0 , "Page has Error");
		}
		
		Select sel = new Select(driver.findElement(By.id("ctl06_DropDownListMode")));
		sel.selectByValue("ENT");
		driver.navigate().refresh();
		
//		int loginPage = driver.findElements(By.xpath("//input[contains(@id,'Username') or contains(@id,'UserName')]")).size();
//		if(loginPage>0) login.login(driver);
		
		element = driver.findElement(By.id("CellServerName"));

		String RowServerNameEnt = xls.getCellData("Sheet1", serverNameEnterpriseColumn, row);

		Assert.assertTrue(element.getText().contains(RowServerNameEnt.trim()), element.getText()+" was appeared instead of "+RowServerNameEnt);

		System.out.println("Test for Row  "+row+"  executed successfully");
	}

	@Test(priority = 4, retryAnalyzer = CustomReporter.RetryListener.class, enabled = true)
	public void executeRow6() {
		row = 6;
		
		System.setProperty("webdriver.chrome.driver", config.getChromePath());
		driver = new ChromeDriver();

		driver.manage().window().maximize();
		
		String RowLink = xls.getCellData("Sheet1", LinkColumn, row);
		System.out.println("URL- "+RowLink);
		driver.get(RowLink.trim());
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		wait = new WebDriverWait(driver, 100);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@name='ShowCode']")));
		Assert.assertTrue(driver.findElement(By.xpath("//input[@name='ShowCode']")).isDisplayed(),"Page was not loaded");
		
		driver.findElement(By.xpath("//input[@name='ShowCode']")).sendKeys("GTSX");
		driver.findElement(By.xpath("//input[@value='GO']")).click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='GTSX']")));
		Assert.assertTrue(driver.findElement(By.xpath("//span[text()='GTSX']")).isDisplayed(),"Could not focus to show GTSX");
		
		System.out.println("Test for Row  "+row+"  executed successfully");
	}

	@Test(priority = 5, retryAnalyzer = CustomReporter.RetryListener.class, enabled = true)
	public void executeRow7() {
		row = 7;

		System.setProperty("webdriver.chrome.driver", config.getChromePath());
		driver = new ChromeDriver();

		driver.manage().window().maximize();
		
		String RowLink = xls.getCellData("Sheet1", LinkColumn, row);
		System.out.println("URL- "+RowLink);
		driver.get(RowLink.trim());
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		wait = new WebDriverWait(driver, 100);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Sign In']")));
		Assert.assertTrue(driver.findElement(By.xpath("//button[text()='Sign In']")).isDisplayed(),"Exhibitor Portal site was not loaded");
		
		System.out.println("Test for Row "+row+" executed successfully");

	}

	@Test(priority = 6, retryAnalyzer = CustomReporter.RetryListener.class, enabled = true)
	public void executeRow8() throws Exception {
		row = 8;

		System.setProperty("webdriver.chrome.driver", config.getChromePath());
		driver = new ChromeDriver();

		driver.manage().window().maximize();
		
		String RowLink = xls.getCellData("Sheet1", LinkColumn, row);
		System.out.println("URL- "+RowLink);
		driver.get(RowLink.trim());
		
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		
		login.login(driver);
		Thread.sleep(2000);
		
		wait = new WebDriverWait(driver, 200);		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='Customer Search']")));
		Assert.assertTrue(driver.findElement(By.xpath("//a[text()='Customer Search']")).isDisplayed(),"DP did not login");
		
		System.out.println("Test for Row "+row+" executed successfully");
	}
	
	/*@Test(priority = 9, retryAnalyzer = CustomReporter.RetryListener.class, enabled = false)
	public void executeRow9() {
		row = 9;

		System.setProperty("webdriver.chrome.driver", config.getChromePath());
		driver = new ChromeDriver();

		driver.manage().window().maximize();
		
		String RowLink = xls.getCellData("Sheet1", LinkColumn, row);
		driver.get(RowLink.trim());
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		wait = new WebDriverWait(driver, 100);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//span[contains(.,'Welcome')])/span")));
		Assert.assertTrue(driver.findElement(By.xpath("(//span[contains(.,'Welcome')])/span")).isDisplayed(),"Web Reg was not loaded");

		System.out.println("Test for Row  "+row+" executed successfully");
	}*/
	
    // Chandra updated new links sent by Sandy on 09/27
    
    @Test(priority = 10, retryAnalyzer = CustomReporter.RetryListener.class, enabled = true)
    public void executeRow10() throws Exception {
       row = 9;    
           List<Boolean> results = new ArrayList<Boolean>();
           StringBuffer showAllError = new StringBuffer();

           System.out.println("Row "+row+"  execution starts");

           // Start chrome. 
           System.setProperty("webdriver.chrome.driver", config.getChromePath());
           driver = new ChromeDriver();

           driver.manage().window().maximize();
           
           // Get the URL from excel and launch it.
           String Row12Link = xls.getCellData("Sheet1", LinkColumn, row);
           System.out.println("URL- "+Row12Link);
           driver.get(Row12Link.trim());
           driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
           
           login.login(driver);            
           driver.navigate().refresh();             

           String Row12ServerName = xls.getCellData("Sheet1", serverNameEnterpriseColumn, row);
           
           try {
                  element = driver.findElement(By.id("CellServerName"));
           } catch (TimeoutException e1) {
                  results.add(false);
                  showAllError.append("The page load timed out" +"\n");
                  xls.setCellData("Sheet1", column + 1, row, showAllError.toString());
           }
           
           try {

                  Assert.assertTrue(element.getText().contains(Row12ServerName.trim()),
                               "The Server Name appeared instead is " + element.getText());
                  System.out.println("Validated the server name");
           } catch (AssertionError e) {
                  results.add(false);
                  showAllError.append(element.getText() + " appeared instead of " + Row12ServerName + "\n");
                  xls.setCellData("Sheet1", column + 1, row, showAllError.toString());
           }

           // Validate the page for errors
           Assert.assertFalse(driver.getCurrentUrl().toLowerCase().contains("error"), "Page shows Appology Error");
           Assert.assertFalse(driver.getTitle().toLowerCase().contains("404"), "Page shows 404 Error");
           Assert.assertFalse(driver.getTitle().toLowerCase().contains("403"), "Page shows 403 Error");
           Assert.assertFalse(results.contains(false), "Server name was incorrect");
//           Assert.assertFalse(driver.findElements(By.xpath("//*[contains(.,'error')]")).size()>0 , "Page has Error");
           
           System.out.println("Test for Row  "+row+"  executed successfully");
    }
        
    @Test(priority = 11, retryAnalyzer = CustomReporter.RetryListener.class, enabled = true)
    public void executeRow11() throws Exception {
    	 row = 10;    
         List<Boolean> results = new ArrayList<Boolean>();
         StringBuffer showAllError = new StringBuffer();

         System.out.println("Row "+row+" execution starts");

         // Start chrome
         System.setProperty("webdriver.chrome.driver", config.getChromePath());
         driver = new ChromeDriver();

         driver.manage().window().maximize();
         
         // Get the URL from excel and launch it.
         String Row13Link = xls.getCellData("Sheet1", LinkColumn, row);
         System.out.println("URL-"+Row13Link);
         driver.get(Row13Link.trim());
         driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
         
         login.login(driver);            
         driver.navigate().refresh();             

         String Row13ServerName = xls.getCellData("Sheet1", serverNameEnterpriseColumn, row);
         
         try {
                element = driver.findElement(By.id("CellServerName"));
         } catch (TimeoutException e1) {
                results.add(false);
                showAllError.append("The page load timed out" +"\n");
                xls.setCellData("Sheet1", column + 1, row, showAllError.toString());
         }
         
         try {
                Assert.assertTrue(element.getText().contains(Row13ServerName.trim()),
                             "The Server Name appeared instead is " + element.getText());
                System.out.println("Validated the server name");
         } catch (AssertionError e) {
                results.add(false);
                showAllError.append(element.getText() + " appeared instead of " + Row13ServerName + "\n");
                xls.setCellData("Sheet1", column + 1, row, showAllError.toString());
         }

         // Validate the page for errors
         Assert.assertFalse(driver.getCurrentUrl().toLowerCase().contains("error"), "Page shows Appology Error");
         Assert.assertFalse(driver.getTitle().toLowerCase().contains("404"), "Page shows 404 Error");
         Assert.assertFalse(driver.getTitle().toLowerCase().contains("403"), "Page shows 403 Error");
         Assert.assertFalse(results.contains(false), "Server name was incorrect");
//         Assert.assertFalse(driver.findElements(By.xpath("//*[contains(.,'error')]")).size()>0 , "Page has Error");
         
         System.out.println("Test for Row "+row+" executed successfully");
    }
		
	@Test(priority = 12, retryAnalyzer = CustomReporter.RetryListener.class, enabled = true)
	public void executeLinkTest12() throws Exception{
		row = 11 ;
		
		int numOfRows = xls.getRowCount("Sheet1");
		
		System.setProperty("webdriver.chrome.driver", config.getChromePath());
		driver = new ChromeDriver();		
		driver.manage().window().maximize();
		
		for (int i=row; i<=numOfRows; i++){			
			
			
			String RowLink = xls.getCellData("Sheet1", LinkColumn, i);
			System.out.println("URL-"+RowLink);
			driver.get(RowLink.trim());
			Thread.sleep(2000);
			
			if (driver.getCurrentUrl().toLowerCase().contains("error")){
				
				System.out.println("Page shows Appology Error");
				xls.setCellData("Sheet1", column-1, i, System.getProperty("user.name"));
				xls.setCellData("Sheet1", column, i, "Failed");
				xls.setCellData("Sheet1", column+1, i, "Page shows Appology Error");
			}
						
			else if (driver.getTitle().toLowerCase().contains("404")){
				
				System.out.println("Page shows 404 Error");
				xls.setCellData("Sheet1", column-1, i, System.getProperty("user.name"));
				xls.setCellData("Sheet1", column, i, "Failed");
				xls.setCellData("Sheet1", column+1, i, "Page shows 404 Error");
			}
						
			else if (driver.getTitle().toLowerCase().contains("403")){
				
				System.out.println("Page shows 403 Error");
				xls.setCellData("Sheet1", column-1, i, System.getProperty("user.name"));
				xls.setCellData("Sheet1", column, i, "Failed");
				xls.setCellData("Sheet1", column+1, i, "Page shows 403 Error");
			}
			
			else {
			xls.setCellData("Sheet1", column-1, i, System.getProperty("user.name"));
			xls.setCellData("Sheet1", column, i, "Passed");
			System.out.println("Test for Row "+i+" executed successfully");	
			}			
		}
		
		driver.quit();		
	}

	@AfterMethod
	public void quitBrowser() throws Exception {
		driver.quit();
	}

	@AfterClass
	public void sendReport() throws Exception {

		Thread.sleep(1000);
		emailReportPROD sendReport = new emailReportPROD();
		sendReport.sendEmail(filepath.getAbsolutePath());
	}
}
