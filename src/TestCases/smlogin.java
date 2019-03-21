package TestCases;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

import utils.ConfigReader;

public class smlogin {
	
	static ConfigReader config = new ConfigReader();
 	
	public void smloginChandra(WebDriver driver){
		
		driver.findElement(By.xpath("//input[contains(@id,'Username') or contains(@id,'UserName')]")).sendKeys(config.LoginCredentails("USER_NAME"), Keys.TAB, config.LoginCredentails("PASSWORD"), Keys.ENTER);

		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

	}
	
	public void smloginMahesh(WebDriver driver){
		
//		driver.findElement(By.xpath("//input[contains(@id,'Username') or contains(@id,'UserName')]")).sendKeys(config.getshowManagerUserID(), Keys.TAB, config.getshowManagerPsw(), Keys.ENTER);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.findElement(By.xpath("//input[contains(@id,'Username') or contains(@id,'UserName')]")).sendKeys(config.LoginCredentails("USER_NAME"));
		driver.findElement(By.xpath("//input[contains(@id,'Password')]")).sendKeys(config.LoginCredentails("PASSWORD"), Keys.ENTER);
		
	}
}
