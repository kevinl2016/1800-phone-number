package com.oracle.common;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringUtilsUnitTest
{
    @Test
    void testThatPunctuationAndWhitespaceAreRemovedWhenStrippingRedundantCharacters()
    {
        final String testValue = "this Is.Kev;,Test";
        final String expectedResult = "thisIsKevTest";

        assertEquals(expectedResult, StringUtils.removeRedundantCharacters(testValue), "Unexpected result for removing punctuation and whitespace.");
    }

    @Test
    void testNullIsReturnedForEmptyStringWhenStrippingRedundantCharacters()
    {
        assertEquals(null, StringUtils.removeRedundantCharacters(""), "Unexpected result for removing punctuation and whitespace.");
    }

    @Test
    void testNullIsReturnedForStringContainingAllPunctuationAndWhitespaceWhenStrippingRedundantCharacters()
    {
        assertEquals(null, StringUtils.removeRedundantCharacters(" ;, '' ..."), "Unexpected result for removing punctuation and whitespace.");
    }

    @Test
    void testNullIsReturnedForNullStringWhenStrippingRedundantCharacters()
    {
        assertEquals(null, StringUtils.removeRedundantCharacters(null), "Unexpected result for removing punctuation and whitespace.");
    }

    @Test
    void testFormatNumberJoinsWithDashesAndAddsPrefix()
    {
        assertEquals("1-800-GOAT-COW", StringUtils.formatPhoneNumber("GOAT", "COW"), "Unexpected value of 1800 number");
    }

    @Test
    void testIsEmptyReturnsTrueForEmptyStringValue()
    {
        assertTrue(StringUtils.isEmpty(""));
    }

    @Test
    void testIsEmptyReturnsTrueForAllSpacesStringValue()
    {
        assertTrue(StringUtils.isEmpty("   "));
    }

    @Test
    void testIsEmptyReturnsTrueForNullStringValue()
    {
        assertTrue(StringUtils.isEmpty(null));
    }
}
