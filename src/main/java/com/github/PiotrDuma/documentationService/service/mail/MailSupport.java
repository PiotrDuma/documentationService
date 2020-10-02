package com.github.PiotrDuma.documentationService.service.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import com.github.PiotrDuma.documentationService.SystemProperties;

@Service(value = "mailSupport")
public class MailSupport implements MailService {

	private String supportContactMail = SystemProperties.SUPPORT_CONTACT_MAIL;
	private String supportSystemUser = SystemProperties.SUPPORT_SYSTEM_MAIL;
	private String supportSystemPassword = SystemProperties.SUPPORT_SYSTEM_PASSWORD;
	private static String host = "smtp.gmail.com";

	@Override
	public boolean sendMail(String subject, String message) {
		Properties properties = setProperties();
		Session session = Session.getDefaultInstance(properties);
		Transport transport = null;
		
		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(supportSystemUser));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(supportContactMail));
			msg.setSubject(subject);
			msg.setText(message);

			transport = session.getTransport("smtp");
			transport.connect(host, supportSystemUser, supportSystemPassword);
			transport.sendMessage(msg, msg.getAllRecipients());
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (transport != null) {
				try {
					transport.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private Properties setProperties() {
		Properties properties = System.getProperties();

		properties.put("mail.smtp.user", supportSystemUser);
		properties.put("mail.smtp.password", supportSystemPassword);
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		return properties;
	}

}
