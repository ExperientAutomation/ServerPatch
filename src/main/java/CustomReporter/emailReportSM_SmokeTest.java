package CustomReporter;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.testng.annotations.Test;

import utils.ConfigReader;

public class emailReportSM_SmokeTest {
	
	String html;
	ConfigReader config = new ConfigReader();
	
	@Test
	public void sendEmail(String errorTabs, String ShowCode, String ShowManagerURL){

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
        
        message.setRecipients(Message.RecipientType.TO,	InternetAddress.parse("sreejak@infinite.com,Chandrasekhar.Kulandasamy@experient-inc.com,Sirasanambati.Anudeep@infinite.com"));
      
//        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("Chandrasekar.kulandasamy@infinite.com"));
        
        
        message.setSubject("Automation Report for ShowManager Smoke Test");
                
        //message.setText("PFA");
        
        //MimeBodyPart messageBodyPart = new MimeBodyPart();
        
        BodyPart messageBodyPart = new MimeBodyPart();

        Multipart multipart = new MimeMultipart();

        messageBodyPart = new MimeBodyPart();
                
        html = "<p>Hi,</p><p>ShowManager Smoke Test for below Showcode was executed successfully..</p><p>"+ShowCode+" - "+ShowManagerURL+ "</p><p>Note:</p><p>"+errorTabs+"</p><p>Thanks,</p><p>Chandra</p>";
        
               
        messageBodyPart.setContent(html,"text/html");
        multipart.addBodyPart(messageBodyPart);

        message.setContent(multipart);

        System.out.println("Sending");

        Transport.send(message);

        System.out.println("Done");

    } catch (MessagingException e) {
        e.printStackTrace();
    }
  }
}