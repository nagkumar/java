package com.shivoham.sys2log.first;

import java.util.Scanner;

public final class SInputReader
{
    public String readUserInput()
    {
	Scanner scanner = new Scanner(System.in);
	System.out.println("Enter your name:");
	String name = scanner.nextLine();
	return "Hello, " + name;
    }
}
