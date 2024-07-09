package util;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsUtil {
    public static final String ACCOUNT_SID = "AC630b1985fc9802da3c49cc705d341f60";
    public static final String AUTH_TOKEN = "914ea592f02bc104ce7b5b1b6eff79fe";
    public static final String TWILIO_PHONE_NUMBER = "+15074185389";

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public static void sendSms(String to, String message) {
        Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(TWILIO_PHONE_NUMBER),
                message
        ).  create();
    }
}