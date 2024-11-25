package org.company.insurance.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
@Service
@AllArgsConstructor
public class  EmailService {
    private JavaMailSender mailSender;
    @Async
    public void sendEmail(String email, String exception) throws MessagingException, UnsupportedEncodingException {
        String fromAddress = "bryndio.insurance@gmail.com";
        String senderName = "BRYinsurance";
        String subject = "Here is critical error!";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(exception, true);
        mailSender.send(message);
    }

    @Async
    public void sendEmail(String email, String insuranceType, String insuranceDetails)
            throws MessagingException, UnsupportedEncodingException {
        String fromAddress = "bryndio.insurance@gmail.com";
        String senderName = "BRYinsurance";
        String subject = "Your " + insuranceType +" Insurance Details Updated";

        String emailBody = "<html><body>" +
                "<h2>Dear Customer,</h2>" +
                "<p>Your <strong>" + insuranceType + "</strong> insurance has been successfully purchased. Here are the details:</p>" +
                "<table border='1' cellpadding='5' cellspacing='0'>" +
                "<tr><td><strong>Insurance Type</strong></td><td>" + insuranceType + "</td></tr>" +
                "<tr><td><strong>Details</strong></td><td>" + insuranceDetails + "</td></tr>" +
                "</table>" +
                "<p>Thank you for choosing <strong>BRYinsurance!</strong></p>" +
                "<footer><p>Best regards, <br> The BRYinsurance Team</p></footer>" +
                "</body></html>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(emailBody, true);

        mailSender.send(message);
    }

    public void sendTestEmail() {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("bryndio.insurance@gmail.com");  // Ваша електронна пошта
            message.setTo("sanyabryndio@gmail.com");     // Адреса отримувача
            message.setSubject("Test");
            message.setText("This is a test email.");

            mailSender.send(message);
            System.out.println("Email sent successfully.");
        } catch (MailException e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
    }
}