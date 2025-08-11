package com.fx.service;

import com.fx.dto.MessageDTO;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Message parser")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class MessageParserTest {

    TestInfo testInfo;
    TestReporter testReporter;

    @BeforeAll
    static void initial(TestInfo testInfo, TestReporter testReporter) {
        System.out.println("Running " + testInfo.getDisplayName() + " with tag " + testInfo.getTags());
    }

    @AfterAll
    static void finalizing(TestInfo testInfo){
        System.out.println(testInfo.getDisplayName() + " TEST FINISHED");
    }

    @Test
    void correctParseMessage() {
        String message = "1.2.Message.Time";
        MessageDTO mesEqual = new MessageDTO("1", "2", "Message", "Time");
        assertEquals(MessageParser.parseMessage(message), mesEqual);
    }

    @Test
    void parseThrowRuntimeException(){
        assertThrows(
            RuntimeException.class,
            () -> MessageParser.parseMessage("1.2.Message")
        );
    }
}