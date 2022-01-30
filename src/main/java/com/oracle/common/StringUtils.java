package com.oracle.common;


public final class StringUtils
{

    /**
     * Removes all whitespace and punctuation from the provided text.
     * @param value
     * @return
     */
    public static String removeRedundantCharacters(String value) {
        if (value != null) {
            String replacedValue = value.replaceAll("\\W", "");
            return (replacedValue.isEmpty()) ? null : replacedValue;
        }
        return null;
    }

    /**
     * Formant phone number with the provided elements using prefix of "1-800"
     * @param elements
     * @return
     */
    public static String formatPhoneNumber(String... elements) {
        final StringBuilder builder = new StringBuilder("1-800");

        for (String element : elements) {
            builder.append("-").append(element);
        }
        return builder.toString();
    }

    /**
     *
     * @param value
     * @return
     */
    public static boolean isEmpty(String value) {
        return (value == null || value.isEmpty() || value.trim().isEmpty());
    }
}
