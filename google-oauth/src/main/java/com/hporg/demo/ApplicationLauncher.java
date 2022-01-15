package com.hporg.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 * @author hrishabh.purohit
 * <p>Launcher class for the demo application.
 * <p>This application primarily uses <code>JavaMail</code> API for operating on user's email inbox.
 * <p><b>NOTE:</b> OAuth 2.0 is provided by Google OAuth client libraries, declared as dependencies to this application.
 */
@SpringBootApplication
@PropertySource("classpath:application.properties")
public class ApplicationLauncher 
{
    private static final Logger LOGGER = LogManager.getLogger(ApplicationLauncher.class);

    public static void main( String[] args )
    {
        LOGGER.debug("Application is launching");
        SpringApplication.run(ApplicationLauncher.class, args);
    }
}
