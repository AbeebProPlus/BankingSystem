package com.system.moneybank.service.emailService;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{
    private final JavaMailSender mailSender;
    @Override
    public void sendMail(EmailDetails emailDetails) {
        //Mail
    }
}
