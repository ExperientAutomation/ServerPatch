package utils;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Formatter;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;

public class ServerPatchURL extends BrowserFactory {
	
	static File source;
	
//	Download Excel File from Server patch File	
	
	public void downloadExcelSheet(String TestName) throws IOException, Exception {
		
		driver = BrowserFactory.getBrowser("Chrome");
		String downloadfileURL = config.getServerPatchFileURL();
		driver.get(downloadfileURL);
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		
		//Get Current Month from System		
		Formatter fmt = new Formatter();
		Calendar now = Calendar.getInstance();
		fmt.format("%tB", now);
		System.out.println(fmt);
		
		driver.findElement(By.xpath("//div[@class='ms-vb  itx']/a[contains(.,'"+fmt+"')]")).click();		
		driver.findElement(By.xpath("//div[@class='ms-vb  itx']/a[contains(.,'"+TestName+"')]")).click();
		
		Thread.sleep(5000);
		
		// Copy file from Download location to Server Patch File.		
		source = latestFile.lastFileModified("C:\\Users\\"+System.getProperty("user.name")+"\\Downloads");		
		File dec = new File(config.getServerPatchFilePath());		
		FileUtils.copyFileToDirectory(source, dec);
		Thread.sleep(2000);
		System.out.println("The file is copied from Download location to Server Patch Location");				
	}	
}
