package com.shivoham.sys2log.logs.slf4j;

public final class SOutRedirectSLF4J
{
    public final void run(boolean succeed)
    {
	if (succeed)
	{
	    System.out.println("SUCCESS: Operation completed.");
	}
	else
	{
	    System.err.println("ERROR: Operation failed.");
	}
    }
}