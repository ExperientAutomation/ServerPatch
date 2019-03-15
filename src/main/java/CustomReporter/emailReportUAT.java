package CustomReporter;

import java.util.Properties;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.testng.annotations.Test;

import utils.ConfigReader;
import utils.XlsUtil;

import TestCases.UATserverPatch;

public class emailReportUAT {
	
	UATserverPatch spt ;
	String html;
	ConfigReader config = new ConfigReader();
	
	@Test
	public void sendEmail(String filePath){
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
        
//        message.setRecipients(Message.RecipientType.TO,
//        			InternetAddress.parse("sreejak@infinite.com,Chandrasekar.kulandasamy@infinite.com,sandy.young@experient-inc.com"));
        message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("sreejak@infinite.com,Chandrasekhar.Kulandasamy@experient-inc.com,Sirasanambati.Anudeep@infinite.com"));
//        message.setRecipients(Message.RecipientType.TO,	InternetAddress.parse("Chandrasekhar.Kulandasamy@experient-inc.com"));
        
        spt = new UATserverPatch();
        if (spt.testType.equalsIgnoreCase("Pre-Test")) message.setSubject("Automation Report for UAT Server Patch Pre-Test");
        if (spt.testType.equalsIgnoreCase("Post-Test")) message.setSubject("Automation Report for UAT Server Patch Post-Test");
        
        //message.setText("PFA");
        
       

        //MimeBodyPart messageBodyPart = new MimeBodyPart();
        
        BodyPart messageBodyPart = new MimeBodyPart();
        BodyPart attachmentPart = new MimeBodyPart();

        Multipart multipart = new MimeMultipart();

        messageBodyPart = new MimeBodyPart();
        String file = filePath;
        String fleName = "UATServerPatchTestResults.xlsx";
        DataSource source = new FileDataSource(file);
        attachmentPart.setDataHandler(new DataHandler(source));
        attachmentPart.setFileName(fleName);
        
        //Get the count of Failures
        XlsUtil util = UATserverPatch.xls;
        int count =0;
        for (int i=1; i<=util.getRowCount("Pre Req Servers"); i++){
        	String result = util.getCellData("Pre Req Servers", UATserverPatch.column, i);
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
        
        messageBodyPart.setContent(html,"text/html");
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