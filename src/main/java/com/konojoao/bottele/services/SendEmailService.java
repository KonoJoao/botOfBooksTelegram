package com.konojoao.bottele.services;

import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.sql.DataSource;
import java.io.File;

@Service
public class SendEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String emailFrom;
    public SendEmailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    public void sendEmail(String toEmail, String subject, String body, String book) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        FileSystemResource file = new FileSystemResource(new File("../archives/" + book));
//        FileSystemResource file = new FileSystemResource(new File("/C:/Users/joaov/OneDrive/Documentos/cod3gos/archives/Sistemas de Banco de Dados navathe 6ª Edicao.pdf"));


        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(new InternetAddress(toEmail));
            helper.setFrom(new InternetAddress(emailFrom));
            helper.setSubject(subject);
            helper.setText(body);
            helper.addAttachment(book, file);
        } catch (MessagingException e){
            e.printStackTrace();
        }


        try {
            mailSender.send(mimeMessage);
        }
        catch (MailException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
