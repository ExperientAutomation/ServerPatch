package utils;

import java.io.File;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class WinUtils {

	public static boolean isElementPresent(WebDriver driver, By by) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		try {
			driver.findElement(by);
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		}
	}

	public void switchIfWindowsAre(WebDriver driver, int num) throws Exception {
		
			if (driver.getWindowHandles().size() == num) {
				for (String handle : driver.getWindowHandles()) {
					driver.switchTo().window(handle);
					Thread.sleep(1000);
				}
				
			} else {
				Thread.sleep(1000);
			}
	}
	
	public void getscreenshot(WebDriver driver, String path) throws Exception 
    {
            File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
         //The below method will save the screen shot in d drive with name "screenshot.png"
            FileUtils.copyFile(scrFile, new File(path+"\\screenshot.png"));
    }
	
}
