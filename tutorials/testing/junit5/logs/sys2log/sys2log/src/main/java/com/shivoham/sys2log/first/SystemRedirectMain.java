package com.shivoham.sys2log.first;

public final class SystemRedirectMain
{
    public final void doSysOut()
    {
	System.out.println("This is a message from MyClass:out");
	System.out.println("Another message to check:out");
    }

    public final void doSysErr()
    {
	System.err.println("This is a message from MyClass:err");
	System.err.println("Another message to check:err");
    }
}
