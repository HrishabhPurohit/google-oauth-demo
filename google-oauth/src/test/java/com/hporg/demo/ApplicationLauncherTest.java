package com.hporg.demo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Test class for <code>ApplicationLauncher</code>. Tests loading of application properties and not the main method flow.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationLauncher.class)
public class ApplicationLauncherTest {

    @Value("${debug}")
    private String debug;

    @Value("${server.port}")
    private String serverPort;

    @Value("${logging.config}")
    private String loggingConfig;

    @Value("${logging.file.name}")
    private String logFileName;

    @Test
    public void isApplicationPropertyReady(){
        assertNotNull(debug);
        assertNotNull(serverPort);
        assertNotNull(loggingConfig);
        assertNotNull(logFileName);
    }
}
