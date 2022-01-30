package com.oracle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Set;

import com.oracle.common.Dictionary;
import com.oracle.common.StringUtils;

public class PhoneNumberTransformer
{
    private static final String DICTIONARY_PATH_OVERRIDE = "dict.file";
    private Dictionary dictionary;

    public PhoneNumberTransformer()
    {
        this(new Dictionary());
    }

    public PhoneNumberTransformer(Dictionary dictionary)
    {
        this.dictionary = (dictionary != null) ? dictionary : new Dictionary();
        init();
    }

    /**
     * Initialise the dictionary from a provided file or default file
     */
    private void init()
    {
        String dictionaryFileLocation = System.getProperty(DICTIONARY_PATH_OVERRIDE);
        if (!StringUtils.isEmpty(dictionaryFileLocation))
        {
            try
            {
                InputStream inputStream = new FileInputStream(dictionaryFileLocation);
                dictionary.loadDictionary(inputStream);
            }
            catch (FileNotFoundException e)
            {
                throw new IllegalArgumentException("Dictionary file cannot be loaded.", e);
            }
        }
        else
        {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("defaultDict");
            dictionary.loadDictionary(inputStream);
        }

    }

    /**
     * Read phone number from a file
     * @param filePath
     */
    public void transformFromFile(String filePath)
    {
        System.out.println(String.format("Processing file: %s", filePath));
        try
        {
            try (Scanner scanner = new Scanner(new FileInputStream(filePath)))
            {
                while (scanner.hasNext())
                {
                    transformNumber(scanner.nextLine());
                }
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println(String.format("File \"%s\" is skipped as the file not found", filePath));
        }
    }

    /**
     * Transform a phone number
     * @param phoneNumber
     */
    public void transformNumber(String phoneNumber)
    {
        System.out.println(String.format("Processing phone number %s.", phoneNumber));
        final PhoneNumberMatcher phoneNumberMatcher = new PhoneNumberMatcher(dictionary, phoneNumber);
        final Set<String> matches = phoneNumberMatcher.findMatchWords();

        if (!matches.isEmpty())
        {
            System.out.println(String.format("%d options found: ", matches.size()));
            for (String match : matches)
            {
                System.out.println(match);
            }
        }
        else
        {
            System.out.println("No options found.");
        }
    }
}
