package me.rrf.utm.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Configuration
@EnableWebMvc
@ComponentScan("me.jmll.utm")
public class JmllAdapter {
	@Bean
	public MultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}
	@Bean
	public JavaMailSender javaMailSender() {
		/**
		 *  Utiliza Gmail como servidor de correo para enviar
		 *  email en el servicio NotificationServiceImpl que consume 
		 *  el bean mailSender. Agregar usuario y password de una cuenta
		 *  genérica
		 * */
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("smtp.gmail.com");
	    mailSender.setPort(587);
	    
	    mailSender.setUsername("SECRET GMAIL ADDRESS");
	    mailSender.setPassword("SECRET PASSWORD");
	    
	    Properties mailProperties = mailSender.getJavaMailProperties();
	    mailProperties.put("mail.transport.protocol", "smtp");
	    mailProperties.put("mail.smtp.auth", "true");
	    mailProperties.put("mail.smtp.starttls.enable", "true");
	    mailProperties.put("mail.debug", "false");
	    return mailSender;
	}
}