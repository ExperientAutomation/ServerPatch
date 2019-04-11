package utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import TestCases.smlogin;

public class BrowserFactory {

	public static ConfigReader config  = new ConfigReader();
	
	public static Map<String, WebDriver> drivers = new HashMap<String, WebDriver>();
	
	public static WebDriver driver = null;
	public static WebDriverWait wait;
	
	public static smlogin login = new smlogin();
	
	public static ServerPatchURL downloadedfile = new ServerPatchURL();
	
	public static File filepath ;  
	public static XlsUtil xls ;  

	/*
	 * Factory method for getting browsers
	 */
	public static WebDriver getBrowser(String browserName) {		
				
		try {
			switch (browserName) {
			case "Firefox":
				driver = drivers.get("Firefox");
				if (driver == null) {
					driver = new FirefoxDriver();
					drivers.put("Firefox", driver);
				}
				break;
			case "IE":
				driver = drivers.get("IE");
				if (driver == null) {

					DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
					capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
							true);
					System.setProperty("webdriver.ie.driver", "N:\\QA\\R&DQA\\Selenium\\Drivers\\IEDriverServer.exe");
					driver = new InternetExplorerDriver(capabilities);
					drivers.put("IE", driver);
					driver.manage().window().maximize();

				}
				break;
			case "Chrome":
				driver = drivers.get("Chrome");
//				if (driver == null) {
					System.setProperty("webdriver.chrome.driver", config.getChromePath());
					driver = new ChromeDriver();
					drivers.put("Chrome", driver);
					driver.manage().window().maximize();
					driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
					driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
//				}
				break;

			case "ChromeOptions":
				driver = drivers.get("ChromeOptions");
//				if (driver == null) {
					System.setProperty("webdriver.chrome.driver", config.getChromePath());
					ChromeOptions cOptions = new ChromeOptions();
					cOptions.addExtensions(new File("MultiPass-for-HTTP-basic-authentication_v0.7.4.crx"));
					driver = new ChromeDriver(cOptions);
					driver.manage().window().maximize();
					drivers.put("ChromeOptions", driver);
					driver.manage().window().maximize();
					driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
//				}
				break;
			}

		}

		catch (Exception ex) {
			ex.printStackTrace();
		}
		return driver;
	}
}