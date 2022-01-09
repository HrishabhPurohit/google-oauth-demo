package com.hporg.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author hrishabh.purohit
 * Launcher class for the demo application.
 *
 */
public class ApplicationLauncher 
{
    private static final Logger LOGGER = LogManager.getLogger(ApplicationLauncher.class);
    public static void main( String[] args )
    {
        LOGGER.debug("Launching demo application");
    }
}
