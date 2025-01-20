package com.footpath.store.global.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.footpath.store.model.MailResponse;
import com.footpath.store.model.MailStatus;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
@EnableAsync
public class MailService {

	private static final String MAIL_SENDER = "debashishmon87@gmail.com";

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private Configuration config;

	@Async
	public MailStatus sendActivationMail(MailResponse request, Map<String, Object> model) {

		MailStatus status = new MailStatus();;

		MimeMessage message = javaMailSender.createMimeMessage();

		try {

			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());

			Template t = config.getTemplate("email-template.ftl");
			String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

			helper.setText(html,true);
			helper.setFrom(MAIL_SENDER);
			helper.setTo(request.getTo());
			helper.setSubject(request.getSubjectLine()); 

			javaMailSender.send(message);


			status.setStatus(true);
			status.setMessage("Mail sent successfully.");

		} catch (MessagingException | IOException | TemplateException e) {
			status.setStatus(false);
			status.setMessage("Mail sent un-successful!!!");
		}

		return status;
	}

}
