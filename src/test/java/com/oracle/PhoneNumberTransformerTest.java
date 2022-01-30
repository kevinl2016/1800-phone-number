package com.oracle;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.PrintStream;

import com.oracle.common.Dictionary;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PhoneNumberTransformerTest
{
    private static final String DICTIONARY_PATH_OVERRIDE = "dict.file";
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void testProcessFileWithAnInvalidFileLocation() {
        PhoneNumberTransformer transformer = new PhoneNumberTransformer(mock(Dictionary.class));
        String testFilePath = "randomLocation";
        transformer.transformFromFile(testFilePath);

        String outMsg = outputStreamCaptor.toString();
        assertNotNull(outMsg, "There should be some message output.");
        String[] outputLines = outMsg.split(System.lineSeparator());
        assertEquals(outputLines.length, 2, "Should be 2 lines of output for invalid file locarion");

        assertEquals(String.format("Processing file: %s", testFilePath), outputLines[0], "Unexpected first line of output.");
        assertEquals(String.format("File \"%s\" is skipped as the file not found", testFilePath), outputLines[1], "Unexpected second line of output.");
    }

    @Test
    void testTransformFromFile() {
        PhoneNumberTransformer transformer = new PhoneNumberTransformer(mock(Dictionary.class));
        String testFilePath = this.getClass().getClassLoader().getResource("testNumber").getPath();
        transformer.transformFromFile(testFilePath);

        String outMsg = outputStreamCaptor.toString();
        assertNotNull(outMsg, "There should be some message output.");
        String[] outputLines = outMsg.split(System.lineSeparator());
        assertEquals(outputLines.length, 3, "Should be 3 lines of output");

        assertEquals(String.format("Processing file: %s", testFilePath), outputLines[0], "Unexpected first line of output.");
        assertEquals(String.format("Processing phone number %s.", "1800-234,233"), outputLines[1], "Unexpected second line of output.");
    }

    @Test
    void testOverriedDictionaryFile()
    {
        String testFilePath = this.getClass().getClassLoader().getResource("testNumber").getPath();
        System.setProperty(DICTIONARY_PATH_OVERRIDE, testFilePath);
        Dictionary dictionary = mock(Dictionary.class);
        PhoneNumberTransformer transformer = new PhoneNumberTransformer(dictionary);
        verify(dictionary).loadDictionary(isA(FileInputStream.class));
    }

    @Test
    void testDefaultDictionaryFile()
    {
        String testFilePath = System.getProperty(DICTIONARY_PATH_OVERRIDE);
        assertNull(testFilePath, "System property should be null");
        Dictionary dictionary = mock(Dictionary.class);
        PhoneNumberTransformer transformer = new PhoneNumberTransformer(dictionary);
        verify(dictionary).loadDictionary(isA(BufferedInputStream.class));
    }

    @Test
    void testTransformNumber()
    {
        PhoneNumberTransformer transformer = new PhoneNumberTransformer();
        transformer.transformNumber("1800-264,357");
        String outMsg = outputStreamCaptor.toString();
        assertNotNull(outMsg, "There should be some message output.");
        String[] outputLines = outMsg.split(System.lineSeparator());
        assertEquals(outputLines.length, 3, "Should be 3 lines of output");
        assertEquals(outputLines[1], "1 options found: ", "Should have 1 option");
        assertTrue(outMsg.contains("1-800-ANGELS"), "Output message should contained: 1-800-ANGELS");
    }
}
