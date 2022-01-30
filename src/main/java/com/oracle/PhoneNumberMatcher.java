package com.oracle;

import java.util.HashSet;
import java.util.Set;

import com.oracle.common.Dictionary;
import com.oracle.common.StringUtils;

public class PhoneNumberMatcher
{
    private String numberAsString;
    private Dictionary dictionary;

    public PhoneNumberMatcher(Dictionary dictionary, String number)
    {
        validateArgs(dictionary, number);
        this.dictionary = dictionary;
        this.numberAsString = number;
    }

    public Integer getMatchingValue()
    {
        String matchingMumber = StringUtils.removeRedundantCharacters(numberAsString);

        Integer matchingValue = null;
        if (matchingMumber != null)
        {
            matchingMumber = matchingMumber.replaceFirst("1800", "");
            try
            {
                matchingValue = Integer.valueOf(matchingMumber);
            }
            catch (NumberFormatException e)
            {
                matchingValue = null;
            }
        }
        return matchingValue;
    }

    private void validateArgs(Dictionary dictionary, String number)
    {
        if (dictionary == null)
        {
            throw new IllegalArgumentException("Word dictionary cannot be null");
        }

        if (StringUtils.isEmpty(number))
        {
            throw new IllegalArgumentException("Number String cannot be empty");
        }
    }

    public Set<String> findMatchWords()
    {
        Set<String> matches = new HashSet<>();
        matches.addAll(findFullWordMatches());

        if (matches.isEmpty())
        {
            matches.addAll(findMultiWordMatches());
        }
        return matches;
    }

    private Set<String> findFullWordMatches()
    {
        if (getMatchingValue() != null)
        {
            return generateFinalWords(dictionary.findMatchWords(getMatchingValue()));
        }
        return new HashSet<>();
    }


    /**
     * Finds multi-words matching options for:
     * <ul>
     * <li>word-word</li>
     * <li>digit-word</li>
     * <li>word-digit-word</li>
     * <li>word-digit</li>
     * </ul>
     *
     * @return the set of words for output including the 1-800 prefix.
     */

    private Set<String> findMultiWordMatches()
    {
        Set<String> matches = new HashSet<>();
        if (getMatchingValue() == null)
        {
            return matches;
        }

        String stringMatchingValue = String.valueOf(getMatchingValue());

        for (int i = 1; i < stringMatchingValue.length(); i++)
        {
            String firstPart = stringMatchingValue.substring(0, i);
            Set<String> firstPartWords = findMatchWords(firstPart);
            if (!hasValues(firstPartWords))
            {
                continue;
            }
            //generate word-word option
            matches.addAll(generateFinalWords(firstPartWords, findMatchWords(stringMatchingValue.substring(i))));

            char currentDigit = stringMatchingValue.charAt(i);
            if (isLastCharacter(stringMatchingValue, i))
            {
                //generate for word-digit option
                matches.addAll(generateFinalWords(firstPartWords, currentDigit));
            }
            else
            {
                //generate for word-digit-word option
                matches.addAll(generateFinalWords(firstPartWords, currentDigit, findMatchWords(stringMatchingValue.substring(i + 1))));
            }
        }
        //generate for digit-word option
        matches.addAll(generateFinalWords(stringMatchingValue.charAt(0), findMatchWords(stringMatchingValue.substring(1))));
        return matches;
    }

    /**
     * generate full word
     *
     * @param words
     * @return
     */
    private Set<String> generateFinalWords(Set<String> words)
    {
        Set<String> output = new HashSet<>();
        if (hasValues(words))
        {
            for (String word : words)
            {
                output.add(StringUtils.formatPhoneNumber(word));
            }
        }
        return output;
    }

    /**
     * generate word-word format
     *
     * @param firstWords
     * @param secondWords
     * @return
     */
    private Set<String> generateFinalWords(Set<String> firstWords, Set<String> secondWords)
    {
        Set<String> output = new HashSet<>();
        if (hasValues(firstWords, secondWords))
        {
            for (String firstWord : firstWords)
            {
                for (String secondPartWord : secondWords)
                {
                    output.add(StringUtils.formatPhoneNumber(firstWord, secondPartWord));
                }
            }
        }
        return output;
    }

    /**
     * generate word-digit-word format
     *
     * @param firstWords
     * @param firstChar
     * @param secondWords
     * @return
     */
    private Set<String> generateFinalWords(Set<String> firstWords, char firstChar, Set<String> secondWords)
    {
        Set<String> output = new HashSet<>();
        if (hasValues(firstWords, secondWords))
        {
            for (String firstWord : firstWords)
            {
                for (String secondWord : secondWords)
                {
                    output.add(StringUtils.formatPhoneNumber(firstWord, String.valueOf(firstChar), secondWord));
                }
            }
        }
        return output;
    }

    /**
     * generate word-digit format
     *
     * @param words
     * @param number
     * @return
     */
    private Set<String> generateFinalWords(Set<String> words, char number)
    {
        Set<String> output = new HashSet<>();
        if (hasValues(words))
        {
            for (String firstWord : words)
            {
                output.add(StringUtils.formatPhoneNumber(firstWord, String.valueOf(number)));
            }
        }
        return output;
    }

    /**
     * generate digit-word format
     *
     * @param number
     * @param words
     * @return
     */
    private Set<String> generateFinalWords(char number, Set<String> words)
    {
        Set<String> output = new HashSet<>();
        if (hasValues(words))
        {
            for (String word : words)
            {
                output.add(StringUtils.formatPhoneNumber(String.valueOf(number), word));
            }
        }
        return output;
    }


    private Set<String> findMatchWords(String searchValue)
    {
        return dictionary.findMatchWords(Integer.parseInt(searchValue));
    }

    private boolean isLastCharacter(String stringMatchingValue, int i)
    {
        return i == stringMatchingValue.length() - 1;
    }

    private boolean hasValues(Set<String> words)
    {
        return words != null && !words.isEmpty();
    }

    private boolean hasValues(Set<String> words, Set<String> moreWords)
    {
        return hasValues(words) && hasValues(moreWords);
    }

}
