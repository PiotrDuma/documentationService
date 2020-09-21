package com.github.PiotrDuma.documentationService.service.mail;

public interface MailService {
	boolean sendMail(String subject, String message);
}
