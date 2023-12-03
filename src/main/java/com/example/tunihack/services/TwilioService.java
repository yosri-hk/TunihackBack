package com.example.tunihack.services;



import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
public class TwilioService {
    // Find your Account SID and Auth Token at twilio.com/console
    // and set the environment variables. See http://twil.io/secure
    public static final String ACCOUNT_SID = "ACeb9258d57ca2ad626c16053aceee5ea4";
    public static final String AUTH_TOKEN = "19cdb174ed345213917b99ccb3363df4";

    public void sendAlert(String numTel, String hospital) {
        log.info("HAEHEAHEAHEAEJAHEAEJoajeHAJEAJea");
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber("+216"+numTel),
                        new com.twilio.type.PhoneNumber("+18153174091"),
                        "We seek your help to donate blood to a sick patient in "+ hospital+".The patient has the same Blood type as you. We Would really appreciate a good initiative from you.\nHave a good day.\nFlick Masters,")
                .create();

    }

}