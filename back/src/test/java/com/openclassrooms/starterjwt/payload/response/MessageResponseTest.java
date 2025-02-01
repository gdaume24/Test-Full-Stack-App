package com.openclassrooms.starterjwt.payload.response;
import org.junit.jupiter.api.Test;

public class MessageResponseTest {
    
    @Test
    public void testSetMessage() {
        MessageResponse messageResponse = new MessageResponse("blablabla");

        messageResponse.setMessage("nouveau message");

        assert(messageResponse.getMessage().equals("nouveau message"));
    }
}
