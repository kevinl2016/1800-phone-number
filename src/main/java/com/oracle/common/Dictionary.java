package com.oracle.common;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Dictionary
{
    /**
     * Integers are immutable, and the hash code of an integer is itself
     * it's cheap to calculate as a key
     */
    private Map<Integer, Set<String>> wordsMap = new HashMap<>();

    public void loadDictionary(InputStream inputStream)
    {
        if (inputStream == null)
        {
            throw new IllegalArgumentException("Stream cannot be null");
        }

        try (Scanner scanner = new Scanner(inputStream))
        {
            while (scanner.hasNext())
            {
                addWordToDictionary(scanner.nextLine());
            }
        }
    }

    public void addWordToDictionary(String newWord)
    {
        StringBuilder builder = new StringBuilder();
        String cleanedWord = StringUtils.removeRedundantCharacters(newWord).toUpperCase();
        for (char currentChar : cleanedWord.toCharArray())
        {
            int charNumber = getNumberFromChar(currentChar);
            if (isValidCharacter(charNumber))
            {
                builder.append(charNumber);
            }
            else
            {
                System.out.println(
                    String.format("Not adding value %s to file as it contained %c which can't be mapped to a number.", newWord, currentChar));
                return;
            }
        }
        addToDictMap(cleanedWord, builder.toString());
    }


    private boolean isValidCharacter(int encodedChar)
    {
        return encodedChar > 0;
    }

    /**
     * Add the word to the map with the number as key
     *
     * @param cleanedWord
     * @param convertedNumbers
     */
    private void addToDictMap(String cleanedWord, String convertedNumbers)
    {
        try
        {
            Integer entryNumberValue = Integer.valueOf(convertedNumbers);
            getWordsFromMap(entryNumberValue).add(cleanedWord);
        }
        catch (NumberFormatException e)
        {
            System.out.println(
                String.format("Not adding word %s represents number %s to dictionary as it was too long for a phone number.", cleanedWord,
                    convertedNumbers));
        }
    }

    private Set<String> getWordsFromMap(Integer key)
    {
        Set<String> words = wordsMap.get(key);
        if (words == null)
        {
            words = new HashSet<>();
            wordsMap.put(key, words);
        }
        return words;
    }

    /**
     * Searches the map to find any values that match the provided number.
     *
     * @param number
     * @return
     */
    public Set<String> findMatchWords(int number)
    {
        return wordsMap.get(number);
    }

    private int getNumberFromChar(char c)
    {
        switch (c)
        {
            case 'A':
            case 'B':
            case 'C':
                return 2;
            case 'D':
            case 'E':
            case 'F':
                return 3;
            case 'G':
            case 'H':
            case 'I':
                return 4;
            case 'J':
            case 'K':
            case 'L':
                return 5;
            case 'M':
            case 'N':
            case 'O':
                return 6;
            case 'P':
            case 'Q':
            case 'R':
            case 'S':
                return 7;
            case 'T':
            case 'U':
            case 'V':
                return 8;
            case 'W':
            case 'X':
            case 'Y':
            case 'Z':
                return 9;
            default:
                return -1;
        }
    }

    /**
     * Get total words count from the map, just for unit test
     *
     * @return
     */
    public int getWordsCount()
    {
        int count = 0;
        for (Set<String> values : wordsMap.values())
        {
            count += values.size();
        }
        return count;
    }

}
