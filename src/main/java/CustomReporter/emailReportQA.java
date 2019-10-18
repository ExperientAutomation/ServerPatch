package CustomReporter;

import java.util.Properties;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.testng.annotations.Test;

import TestCases.QAServerPatch;
import utils.ConfigReader;
import utils.XlsUtil;

public class emailReportQA {

	QAServerPatch spt;
	String html;
	ConfigReader config = new ConfigReader();

	@Test
	public void sendEmail(String filePath) {
		
//		final String username = config.LoginCredentails("USER_NAME");	
		final String username = config.LoginCredentails("USER_EMAILID");

		Properties props = new Properties();
		props.put("mail.smtp.auth", false);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.smtp.host", "smtp2.expoexchange.com");
		props.put("mail.smtp.port", "25");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, config.LoginCredentails("PASSWORD"));
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			// message.setRecipients(Message.RecipientType.TO,
			// InternetAddress.parse("mahesh.mishra@experient-inc.com,sreejak@infinite.com,Chandrasekar.kulandasamy@infinite.com,sandy.young@experient-inc.com"));
			
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("sreejak@infinite.com,Chandrasekhar.Kulandasamy@experient-inc.com"));
//			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("Chandrasekhar.Kulandasamy@experient-inc.com"));
			
			spt = new QAServerPatch();
			if (spt.testType.equalsIgnoreCase("Pre-Test"))
				message.setSubject("Automation Report for QA Server Patch Pre-Test");
			if (spt.testType.equalsIgnoreCase("Post-Test"))
				message.setSubject("Automation Report for QA Server Patch Post-Test");

			// message.setText("PFA");

			// MimeBodyPart messageBodyPart = new MimeBodyPart();

			BodyPart messageBodyPart = new MimeBodyPart();
			BodyPart attachmentPart = new MimeBodyPart();

			Multipart multipart = new MimeMultipart();

			messageBodyPart = new MimeBodyPart();
			String file = filePath;
			String fileName = "QAServerPatchTestResults.xlsx";
			DataSource source = new FileDataSource(file);
			attachmentPart.setDataHandler(new DataHandler(source));
			attachmentPart.setFileName(fileName);

			//Get the count of Failures
	        XlsUtil util = QAServerPatch.xls;
	        int count =0;
	        for (int i=1; i<=util.getRowCount("Stage Servers"); i++){
	        	String result = util.getCellData("Stage Servers", QAServerPatch.column, i);
	        	if (result.equalsIgnoreCase("failed")){
	        		count++;
	        	}
	        }
	        if(count==0) {
	        	html = "<p>Hi,</p><p>PFA the Automation Test report.</p><p>Note: All are Passed :-) </p><p>Thanks,</p><p>Chandra</p>";
	        }else if (count==1) {
	        	html = "<p>Hi,</p><p>PFA the Automation Test report.</p><p>Note: There is one failure.. </p><p>Thanks,</p><p>Chandra</p>";
	        } else {
	        	html = "<p>Hi,</p><p>PFA the Automation Test report.</p><p>Note: There are "+count+" failures.. </p><p>Thanks,</p><p>Chandra</p>";
	        }
	        
			messageBodyPart.setContent(html, "text/html");
			multipart.addBodyPart(messageBodyPart);
			multipart.addBodyPart(attachmentPart);

			message.setContent(multipart);

			System.out.println("Sending");

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}