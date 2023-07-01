package com.system.moneybank.service.smsService;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmsService {
    private static String SID = System.getenv("SID");
    private static String authToken = System.getenv("TWILIO_AUTH_TOKEN");
    private static String from =  System.getenv("TWILIO_PHONE_NUMBER");

    private final SimpMessagingTemplate webSocket;
    private static final String TOPIC_NAME = "/topic/sms";
    public void sendSms(Sms sms){
        try {
            Twilio.init(SID, authToken);
            Message message = Message.creator(new PhoneNumber(sms.getTo()),
                    new PhoneNumber(from), sms.getMessage()).create();
            log.info("Sms triggered successfully", message.getSid());
        }catch (Exception ex){
            webSocket.convertAndSend(TOPIC_NAME, "Failed to send an sms");
        }
        webSocket.convertAndSend(TOPIC_NAME, "Message was delivered");
    }

}
