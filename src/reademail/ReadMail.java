//https://pepipost.com/tutorials/send-email-in-java-using-gmail-smtp/
package reademail;

import java.io.IOException;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.activation.DataHandler;

public class ReadMail {

    public String[][] ReadEmail(final String email, final String password, int amount) throws IOException {
    	String[][] messageInfo = new String[amount][3];
    	
    	String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, null); 
        
        try {
        	Store store = session.getStore("imaps");
			store.connect("smtp.gmail.com", email, password);
			
			Folder inbox = store.getFolder("inbox");
            inbox.open(Folder.READ_ONLY);
            int messageCount = inbox.getMessageCount();
            
            //System.out.println("Total Messages:- " + messageCount);
            
            Message[] messages = inbox.getMessages();
            System.out.println("------------------------------");
            
            for (int i = messageCount - amount; i < messageCount; i++) {
                //System.out.println("Mail Subject:- " + messages[i].getSubject());
                //System.out.println("From: " + messages[i].getFrom()[0]);
                MimeMultipart mimeMultipart = (MimeMultipart) messages[i].getContent();
                String text = getTextFromMimeMultipart(mimeMultipart);
                //System.out.println("Mail Content:- " + text);
                
                messageInfo[i - messageCount + amount][0] = messages[i].getSubject();
                messageInfo[i - messageCount + amount][1] = messages[i].getFrom()[0].toString();
                messageInfo[i - messageCount + amount][2] = text;
            }
 
            inbox.close(true);
            store.close();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return messageInfo;

    }
    
    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart)  throws MessagingException, IOException{
        String result = "";
        
        int count = mimeMultipart.getCount();
        
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } 
            else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
            } 
            else if (bodyPart.getContent() instanceof MimeMultipart){
                result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
            }
        }
        return result;
    }
    
}

