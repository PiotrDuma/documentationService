package com.github.PiotrDuma.documentationService.service.mail;

import java.time.Instant;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.PiotrDuma.documentationService.SystemProperties;
import com.github.PiotrDuma.documentationService.security.AuthTokens;

/**
 * source:
 * https://kgiann78.github.io/java/gmail/2017/03/16/JavaMail-send-mail-at-Google-with-XOAUTH2.html
 */
@Service(value = "mailServiceImpl")
public class MailServiceImpl implements MailService {
	private final AuthTokens userTokens;

	private static String HOST = "smtp.gmail.com";
	private String username;
	private String accessToken;
	private long tokenExpires;

	@Autowired
	public MailServiceImpl(AuthTokens userTokens) {
		this.userTokens = userTokens;
	}

	private void init() {
		username = userTokens.getEmailAdress();
		accessToken = userTokens.getAccessToken();

		Instant val = userTokens.accessTokenExpiresAt();
		tokenExpires = val.toEpochMilli();
	}

	@Override
	public boolean sendMail(String subject, String message) {
		init();
		Properties props = configProperties();
		Session session = Session.getDefaultInstance(props);

		if (System.currentTimeMillis() > tokenExpires) {
			accessToken = userTokens.refreshAccessToken();
		}

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(username));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(SystemProperties.SUPPORT_CONTACT_MAIL));
			msg.setSubject(subject);
			msg.setSentDate(new Date());
			msg.setText(message);

			Transport transport = session.getTransport("smtp");
			transport.connect(HOST, username, accessToken);
			msg.saveChanges();

			transport.sendMessage(msg, msg.getAllRecipients());
			if (transport != null)
				transport.close();
			return true;
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return false;
	}

	private Properties configProperties() {
		Properties props = new Properties();
		props.put("mail.smtp.sasl.enable", "true");
		props.put("mail.smtp.sasl.mechanisms", "XOAUTH2");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");
		props.put("mail.smtp.auth.login.disable", "true");
		props.put("mail.smtp.auth.plain.disable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		return props;
	}

}
