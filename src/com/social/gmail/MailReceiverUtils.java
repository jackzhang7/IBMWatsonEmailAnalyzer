package com.social.gmail;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.social.UI.model.MySocialContent;
import com.social.UI.model.MySocialContentRepository;
import com.social.db.DatabaseMgr;
import com.social.util.Log;
import com.social.util.Util;
import com.social.watson.SentimentNLU;

/**
 * Auxiliary class for receive mail and search of the link for tenant creation into email content.
 * Class works only gmail boxes. IMAP must be enabled for correct working. Also must be disabled 2
 * step verification.
 */
public class MailReceiverUtils {
  private String user;
  private String password;
  private Store mailStore;
  private Folder mailBoxFolder;
  private Matcher matcher;
  private String host = "imap.gmail.com";

  /**
   * convenient to use if will be used a simple operations with a mail box (receive, delete, read)
   * for init user name, password and tenant name
   *
   * @param user test user mail name
   * @param password password for test mailbox
   */
  public MailReceiverUtils(String user, String password) {
    this.user = user;
    this.password = password;
  }

  /**
   * Creates session with email server with using IMAP - mail protocol. Return array of the messages
   * from the test mailbox
   *
   * @param user login to test mail box that is used for mail connection
   * @param password password for test mailbox
   * @return array of messages from the mail box exceptions that may be produced during working with
   *     test mailbox:
   * @throws MessagingException
   */
  private Message[] receiveMailsFromInbox(String user, String password) throws MessagingException {
    Properties properties = System.getProperties(); // Get system properties
    Session session = Session.getDefaultInstance(properties); // Get the default Session object.
    mailStore =
        session.getStore("imaps"); // Get a Store object that implements the specified protocol.
    mailStore.connect(
        host, user,
        password); // Connect to the current host using the specified username and password.
    mailBoxFolder =
        mailStore.getFolder("inbox"); // Create a Folder object corresponding to the given name.
    mailBoxFolder.open(Folder.READ_ONLY); // Open the Folder.
    Message[] msgs = mailBoxFolder.getMessages();
    return msgs;
  }

  /**
   * delete all mails in the test mailbox, exceptions that may be produced during working with test
   * mailbox:
   *
   * @throws MessagingException
   */
  public void cleanMailBox() throws MessagingException {
    Properties properties = System.getProperties(); // Get system properties
    Session session = Session.getDefaultInstance(properties); // Get the default Session object.
    Store store =
        session.getStore("imaps"); // Get a Store object that implements the specified protocol.
    store.connect(
        host, user,
        password); // Connect to the current host using the specified username and password.
    Folder folder =
        store.getFolder("inbox"); // Create a Folder object corresponding to the given name.
    folder.open(Folder.READ_WRITE); // Open the Folder.
    Message[] messages = folder.getMessages();
    for (int i = 0, n = messages.length; i < n; i++) {
      messages[i].setFlag(Flags.Flag.DELETED, true);
    }
    closeStoreAndFolder(folder, store);
  }

  /**
   * @param keyWordInMail key word for matching in emails list, should be unique
   * @param regexpPatterForLink predefined regexp for getting defined url in the body of mail. It
   *     may be confirmation for workspace creation, reset password, add members into organizations
   *     and etc. You can use patterns from TestConstants.RegexpPatternsForEmails class
   *     <p>exceptions that may be produced during working with test mailbox:
   * @throws IOException
   * @throws MessagingException
   * @return URL link for defined type of message
   */
  public String getExpectedLinkFromEmail(String keyWordInMail, String regexpPatterForLink)
      throws IOException, MessagingException {
    String confirmLink = null;
    Message[] messages = receiveMailsFromInbox(user, password);
    Pattern pattern = Pattern.compile(regexpPatterForLink);
    for (int i = 0; i < messages.length; i++) {
      Message currentMessage = messages[i];
      String currentContent = getTextFromMessage(new MimeMessage((MimeMessage) currentMessage));
      if (currentContent.contains(keyWordInMail)
          && currentMessage.getReceivedDate().before(new Date())) {
        matcher = pattern.matcher(currentContent);
        if (matcher.find()) {
          confirmLink = matcher.group();
        }
      }
    }
    closeStoreAndFolder(mailBoxFolder, mailStore);
    return confirmLink;
  }
  
  public String getMessageFromEmail()
	      throws IOException, MessagingException {
	    String confirmLink = null;
	    DatabaseMgr db = new DatabaseMgr();
	    SentimentNLU sentNLU = new SentimentNLU();
	    Message[] messages = receiveMailsFromInbox(user, password);
	    for (int i = 0; i < messages.length; i++) {
	    	//Log.print(messages[i].getReceivedDate().toString());
	    	Date date = new Date (messages[i].getReceivedDate().getTime());
			String timeString = Util.getLocalTimeString(date, "America/New_York");
			Log.print("------------------------------------");
			Log.print("gmail@"+timeString);
	    	//if (MySocialContentRepository.socialCache.isKeyInCache("gmail@"+timeString)) {
	    	//	continue;
	    	//}
	    	
	      MySocialContent sContent = new MySocialContent(); 
	      
	      Message currentMessage = messages[i];
	      String currentContent = getTextFromMessage(new MimeMessage((MimeMessage) currentMessage));
	      sContent.setContent(currentContent);
	     // double sentimentOfContent = sentNLU.getSentimentFromNLU(currentContent);
	      double sentimentOfContent = 1.1;
	      sContent.setSentiment(sentimentOfContent);
	      sContent.setReceivedTime(new Date(messages[i].getReceivedDate().getTime()));
	      System.out.println(currentContent);
	      Address[] a;
	      Message m = messages[i];
	      String str = "";
	      
	      
	      
	      if ((a = m.getFrom()) != null) {
	         for (int j = 0; j < a.length; j++) {
	         System.out.println("FROM: " + a[j].toString());
	         str = str + a[j].toString();
	         }
	      }
	      sContent.setFromWho(str);
	      
	      str = "";
	      // TO
	      if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
	         for (int j = 0; j < a.length; j++) {
	         System.out.println("TO: " + a[j].toString());
	         str = str + a[j].toString();
	         }
	      }
	      sContent.setToMe(str);

	      // SUBJECT
	      if (m.getSubject() != null) {
	         System.out.println("SUBJECT: " + m.getSubject());
	         sContent.setSubject(m.getSubject());
	      }
	      sContent.setKind("gmail");
	      
	      db.insertSocialContentEntry(sContent);
	      MySocialContentRepository.socialCache.insertToCache(sContent);
	      
	    }
	    closeStoreAndFolder(mailBoxFolder, mailStore);
	    return confirmLink;
	  }
  

  private String getTextFromMessage(Message message) throws MessagingException, IOException {
    if (message.isMimeType("text/plain")) {
      return (String) message.getContent();
    } else if (message.isMimeType("multipart/*")) {
      MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
      return getTextFromMimeMultipart(mimeMultipart);
    } else if (message.isMimeType("text/html")) {
      return (String) org.jsoup.Jsoup.parse((String) message.getContent()).text();
    }

    return "";
  }

  private String getTextFromMimeMultipart(MimeMultipart mimeMultipart)
      throws MessagingException, IOException {
    StringBuilder result = new StringBuilder();
    int count = mimeMultipart.getCount();
    for (int i = 0; i < count; i++) {
      BodyPart bodyPart = mimeMultipart.getBodyPart(i);
      if (bodyPart.isMimeType("text/plain")) {
        result.append("\n").append(bodyPart.getContent());
      } else if (bodyPart.isMimeType("text/html")) {
    	  
        result.append("\n").append((String) org.jsoup.Jsoup.parse((String) bodyPart.getContent()).text());
      } else if (bodyPart.getContent() instanceof MimeMultipart) {
        result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
      }
    }
    return result.toString();
  }

  public static void main(String args[]) throws Exception {
	  String gmailUser= Util.DATABASE_USER;
	  String gmailPassword = Util.DATABASE_PASSWD;
	  MailReceiverUtils mailUtils = new MailReceiverUtils(gmailUser, gmailPassword);  
	  String returnStr = mailUtils.getMessageFromEmail();
	  
  }

  /**
   * check Folder and Store objects from current test email box and close this
   *
   * @param folder the current mail folder that should be closed
   * @param store the current mail store that should be closed
   * @throws MessagingException
   */
  private void closeStoreAndFolder(Folder folder, Store store) throws MessagingException {
    if (folder != null) {
      folder.close(true);
    }

    if (store != null) {
      store.close();
    }
  }
}