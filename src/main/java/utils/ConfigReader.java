package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Properties;

public class ConfigReader {

	Properties pro;

	public String LoginCredentails(String key) {

		String path = "N:\\QA\\R&DQA\\Selenium\\GlobalCredentials\\LoginCredentials.properties";

		FileReader fio;
		Properties objRepoProp = null;
		try {
			fio = new FileReader(path);
			objRepoProp = new Properties();
			objRepoProp.load(fio);
		} catch (Exception e) {

			e.printStackTrace();
		}

		return objRepoProp.getProperty(key);

	}

	public ConfigReader() {

		try {
			File src = new File(System.getProperty("user.dir")+"\\Configuration\\Config.property");
			FileInputStream fis = new FileInputStream(src);
			pro = new Properties();
			pro.load(fis);
		} catch (Exception e) {
			System.out.println("Exception was === " + e.getMessage());
		}
	}

	public String getChromePath() {

		return pro.getProperty("ChromeDriver");
	}

	public String getServerPatchFilePath() {

		return pro.getProperty("ServerPatchFilePath");
	}

	public String getsecuremitUserID() {

		return pro.getProperty("securemitUserID");
	}

	public String getsecuremitpsw() {

		return pro.getProperty("securemitpsw");
	}

	public String getuatsecuremitUserID() {

		return pro.getProperty("uatsecuusername");
	}

	public String getuatsecuremitpsw() {

		return pro.getProperty("uatpassword");
	}

	public String getshowManagerUserID() {

		return pro.getProperty("showManagerUserID");
	}

	public String getshowManagerPsw() {

		return pro.getProperty("showManagerPsw");
	}

	public String getServerPatchFileURL() {
		
		return pro.getProperty("ServerPatchFileURL");
	}

	public String getUSER_EMAILID() {

		return pro.getProperty("USER_EMAILID");
	}

}
