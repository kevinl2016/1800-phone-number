package com.oracle.common;

import java.io.InputStream;
import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class DictionaryUnitTest
{
    @Test
    void testLoadingDictFailsWhenStreamIsNull()
    {
        Dictionary dictionary = new Dictionary();
        try
        {
            dictionary.loadDictionary((InputStream) null);
            fail("should have thrown an IOException if the file is missing");
        }
        catch (IllegalArgumentException e)
        {
            assertTrue(e.getMessage().equals("Stream cannot be null"));
        }
    }

    @Test
    void testLoadingDictFromFile()
    {
        Dictionary dictionary = new Dictionary();
        dictionary.loadDictionary(this.getClass().getClassLoader().getResourceAsStream("defaultDict"));
        assertEquals(10, dictionary.getWordsCount(), "There should be 10 entries in the map");
    }

    @Test
    public void testHasEmptyWordsMap()
    {
        Dictionary dictionary = new Dictionary();
        assertEquals(0, dictionary.getWordsCount(), "Expected the initialised word map to have no values");
    }

    @Test
    public void testLoadToMapIgnoreRedundant()
    {
        Dictionary dictionary = new Dictionary();
        String testWords = "go .kev,ha";
        assertEquals(0, dictionary.getWordsCount(), String.format("Adding the text \"%s\" to the map.", testWords));
    }

    @Test
    public void testLoadVeryLongWordsNotAddToMap()
    {
        Dictionary dictionary = new Dictionary();
        String testString = "This is a really long word that is going to be ignored";
        dictionary.addWordToDictionary(testString);
        assertEquals(0, dictionary.getWordsCount(),
            String.format("Shouldn't have been able to add \"%s\" to the index as it was too long.", testString));
    }

    @Test
    public void testFindValuesForNumber()
    {
        Dictionary dictionary = new Dictionary();
        dictionary.addWordToDictionary("cat");
        Set<String> stringValues = dictionary.findMatchWords(228);
        assertTrue(dictionary.getWordsCount() == 1);
        assertTrue(stringValues != null && stringValues.contains("CAT"));
    }
}
