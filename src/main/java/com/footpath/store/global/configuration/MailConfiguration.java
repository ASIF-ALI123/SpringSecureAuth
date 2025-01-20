package com.footpath.store.global.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

@Configuration
public class MailConfiguration {

	private static final String MAIL_HOST = "debashishmon87@gmail.com";

	@Primary
	@Bean
	public FreeMarkerConfigurationFactoryBean factoryBean() {
		FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
		bean.setTemplateLoaderPath("classpath:/templates");
		return bean;
	}
	
	private SimpleMailMessage preConfigMail(String to, String messageBody, String subjectLine) {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(MAIL_HOST);

		message.setTo(to);
		message.setSubject(subjectLine);
		message.setText(messageBody);

		return message;
	}

}
