package com.oracle;

import java.io.Console;
import java.util.Arrays;

public class ApplicationLauncher
{
    private static final String EXIT_COMMAND = "exit";
    private PhoneNumberTransformer transformer;

    public ApplicationLauncher(PhoneNumberTransformer transformer)
    {
        this.transformer = transformer;
    }

    public static void main(String[] args)
    {
        ApplicationLauncher launcher = new ApplicationLauncher(new PhoneNumberTransformer());
        launcher.runWithArguments(args);
    }

    private void runWithArguments(String[] args)
    {
        if (args == null || args.length == 0)
        {
            consoleInputHandler();
        }
        else
        {
            fileInputHandler(args);
        }
        System.exit(0);
    }

    private void fileInputHandler(String[] args)
    {
        System.out.println("Files to be processed: " + Arrays.toString(args));
        for (String file : args)
        {
            transformer.transformFromFile(file);
        }
    }

    private void consoleInputHandler()
    {
        Console console = System.console();
        if (console == null)
        {
            System.err.println("No console is available to accept input.");
            System.exit(1);
        }
        boolean keepWorking = true;
        while (keepWorking)
        {
            String content = console.readLine(String.format("Enter a phone number to transform, or '%s' to quit: ", EXIT_COMMAND));
            if (EXIT_COMMAND.equals(content))
            {
                keepWorking = false;
            }
            else
            {
                transformer.transformNumber(content);
            }
        }
    }
}
