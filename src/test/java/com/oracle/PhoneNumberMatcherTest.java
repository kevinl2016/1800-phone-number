package com.oracle;

import java.util.Set;

import com.oracle.common.Dictionary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class PhoneNumberMatcherTest
{
    private Dictionary dictionary;

    @BeforeEach
    void setUp()
    {
        dictionary = new Dictionary();
        dictionary.loadDictionary(this.getClass().getClassLoader().getResourceAsStream("defaultDict"));
    }

    @Test
    public void testWithoutDictionary()
    {
        try
        {
            new PhoneNumberMatcher(null, "1800.12-3922");
            fail("An exception should have been thrown at construction as there wasn't a dictionary provided.");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Word dictionary cannot be null", e.getMessage(), "Invalid IllegalArgumentException message");
        }
    }

    @Test
    void testWithEmptyNumberString()
    {
        try
        {
            new PhoneNumberMatcher(dictionary, "");
            fail("An exception should have been thrown at construction as number string was empty");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Number String cannot be empty", e.getMessage(), "Invalid IllegalArgumentException message");
        }
    }

    @Test
    void testGetMatchingValueRemoveRedundant()
    {
        PhoneNumberMatcher phoneNumberMatcher = new PhoneNumberMatcher(dictionary, "123-234,233");
        assertEquals(Integer.valueOf(123234233), phoneNumberMatcher.getMatchingValue());
    }

    @Test
    void testGetMatchingValueRemovePrefix()
    {
        PhoneNumberMatcher phoneNumberMatcher = new PhoneNumberMatcher(dictionary, "1800-234,233");
        assertEquals(Integer.valueOf(234233), phoneNumberMatcher.getMatchingValue());
    }


    @Test
    void findMatchesFullWordMatch()
    {
        PhoneNumberMatcher phoneNumberMatcher = new PhoneNumberMatcher(dictionary, "1800-264,357");
        Set<String> matches = phoneNumberMatcher.findMatchWords();
        assertNotNull(matches, "Matches should not be null");
        assertEquals(1, matches.size(), "There should be 1 match for the provided number");
        assertTrue(matches.contains("1-800-ANGELS"), "Invalid matching number");
    }

    @Test
    void findMatchesWithWordWordMatch()
    {
        PhoneNumberMatcher phoneNumberMatcher = new PhoneNumberMatcher(dictionary, "1800-264,357");
        Set<String> matches = phoneNumberMatcher.findMatchWords();
        assertNotNull(matches, "Matches should not be null");
        assertEquals(1, matches.size(), "There should be 1 match for the provided number");
        assertTrue(matches.contains("1-800-ANGELS"), "Invalid matching number");
    }

    @Test
    void findMatchesWithWordDigitWordMatch()
    {
        PhoneNumberMatcher phoneNumberMatcher = new PhoneNumberMatcher(dictionary, "1800-264,357");
        Set<String> matches = phoneNumberMatcher.findMatchWords();
        assertNotNull(matches, "Matches should not be null");
        assertEquals(1, matches.size(), "There should be 1 match for the provided number");
        assertTrue(matches.contains("1-800-ANGELS"), "Invalid matching number");
    }

    @Test
    void findMatchesWithDigitWordMatch()
    {
        PhoneNumberMatcher phoneNumberMatcher = new PhoneNumberMatcher(dictionary, "1800-264,357");
        Set<String> matches = phoneNumberMatcher.findMatchWords();
        assertNotNull(matches, "Matches should not be null");
        assertEquals(1, matches.size(), "There should be 1 match for the provided number");
        assertTrue(matches.contains("1-800-ANGELS"), "Invalid matching number");
    }

    @Test
    void findMatchesWithWordDigitMatch()
    {
        PhoneNumberMatcher phoneNumberMatcher = new PhoneNumberMatcher(dictionary, "1800-264,357");
        Set<String> matches = phoneNumberMatcher.findMatchWords();
        assertNotNull(matches, "Matches should not be null");
        assertEquals(1, matches.size(), "There should be 1 match for the provided number");
        assertTrue(matches.contains("1-800-ANGELS"), "Invalid matching number");
    }
}
