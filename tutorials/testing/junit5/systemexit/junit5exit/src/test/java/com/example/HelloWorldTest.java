package com.example;

import com.ginsberg.junit.exit.ExpectSystemExit;
import com.ginsberg.junit.exit.ExpectSystemExitWithStatus;
import com.ginsberg.junit.exit.SystemExitExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SystemExitExtension.class)
public class HelloWorldTest
{
    @Test
    @ExpectSystemExitWithStatus(42)
    void testSystemExit()
    {
	new HelloWorld().sayHelloAndExit();
    }

    @Test
    @ExpectSystemExitWithStatus(42)
    void testSystemExit2() throws InterruptedException
    {
	new Thread(() -> {
	    new HelloWorld().sayHelloAndExit();
	}).start();

	Thread.sleep(1000);  // Give it some time to execute
    }
    @Test
    @ExpectSystemExit
    void testWithExitBot() {
	System.exit(0);
    }
}
