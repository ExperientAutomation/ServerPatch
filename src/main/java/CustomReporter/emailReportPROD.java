package CustomReporter;

import java.util.Properties;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.testng.annotations.Test;

import TestCases.PRODserverPatch;
import utils.ConfigReader;

public class emailReportPROD {
	
	PRODserverPatch spt ;
	ConfigReader config = new ConfigReader();
	
	@Test
	public void sendEmail(String filePath){

//    final String username = config.LoginCredentails("USER_NAME");	
    final String username = config.LoginCredentails("USER_EMAILID");
//    final String password = "%6yhnbgT";

    Properties props = new Properties();
    props.put("mail.smtp.auth", false);
    props.put("mail.smtp.starttls.enable", true);
    props.put("mail.smtp.host", "smtp2.expoexchange.com");
    props.put("mail.smtp.port", "25");

    Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, config.LoginCredentails("PASSWORD"));
                }
            });

    try {

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
//        message.setRecipients(Message.RecipientType.TO,
//        			InternetAddress.parse("mahesh.mishra@experient-inc.com,sreejak@infinite.com,Chandrasekar.kulandasamy@infinite.com,sandy.young@experient-inc.com"));
        message.setRecipients(Message.RecipientType.TO,	InternetAddress.parse("sreejak@infinite.com,Chandrasekhar.Kulandasamy@experient-inc.com,Sirasanambati.Anudeep@infinite.com"));
//        message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("Chandrasekhar.Kulandasamy@experient-inc.com"));
        
        spt = new PRODserverPatch();
        if (spt.testType.equalsIgnoreCase("Pre-Test")) message.setSubject("Automation Report for PROD Server Patch Pre-Test");
        if (spt.testType.equalsIgnoreCase("Post-Test")) message.setSubject("Automation Report for PROD Server Patch Post-Test");
        
        //message.setText("PFA");            

        //MimeBodyPart messageBodyPart = new MimeBodyPart();
        
        BodyPart messageBodyPart = new MimeBodyPart();
        BodyPart attachmentPart = new MimeBodyPart();

        Multipart multipart = new MimeMultipart();

        messageBodyPart = new MimeBodyPart();
        String file = filePath;
        String fileName = "PRODServerPatchTestResults.xlsx";
        DataSource source = new FileDataSource(file);
        attachmentPart.setDataHandler(new DataHandler(source));
        attachmentPart.setFileName(fileName);
        String html = "<p>Hi,</p><p>PFA the Automation Test report.</p><p>Thanks,</p><p>Chandra</p>";
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