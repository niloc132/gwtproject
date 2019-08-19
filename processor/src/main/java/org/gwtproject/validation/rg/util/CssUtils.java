package org.gwtproject.validation.rg.util;

import com.google.common.base.Strings;

/**
 * @author Dmitrii Tikhomirov <chani@me.com>
 * Created by treblereel on 10/28/18.
 */
public class CssUtils {

    private static final int MAX_SIXTEEN_BIT_NUMBER_STRING_LENGTH = 5;

    /**
     * Escapes string content to be a valid string literal.
     *
     * @return an escaped version of <code>unescaped</code>, suitable for being enclosed in double
     *         quotes in Java source
     */
    public static String escape(String unescaped) {
        int extra = 0;
        for (int in = 0, n = unescaped.length(); in < n; ++in) {
            switch (unescaped.charAt(in)) {
                case '\0':
                case '\n':
                case '\r':
                case '\"':
                case '\\':
                    ++extra;
                    break;
            }
        }

        if (extra == 0) {
            return unescaped;
        }

        char[] oldChars = unescaped.toCharArray();
        char[] newChars = new char[oldChars.length + extra];
        for (int in = 0, out = 0, n = oldChars.length; in < n; ++in, ++out) {
            char c = oldChars[in];
            switch (c) {
                case '\0':
                    newChars[out++] = '\\';
                    c = '0';
                    break;
                case '\n':
                    newChars[out++] = '\\';
                    c = 'n';
                    break;
                case '\r':
                    newChars[out++] = '\\';
                    c = 'r';
                    break;
                case '\"':
                    newChars[out++] = '\\';
                    c = '"';
                    break;
                case '\\':
                    newChars[out++] = '\\';
                    c = '\\';
                    break;
            }
            newChars[out] = c;
        }

        return String.valueOf(newChars);
    }

    /**
     * Returns an escaped version of a String that is valid as a Java class name.<br />
     *
     * Illegal characters become "_" + the character integer padded to 5 digits like "_01234". The
     * padding prevents collisions like the following "_" + "123" + "4" = "_" + "1234". The "_" escape
     * character is escaped to "__".
     */
    public static String escapeClassName(String unescapedString) {
        char[] unescapedCharacters = unescapedString.toCharArray();
        StringBuilder escapedCharacters = new StringBuilder();

        boolean firstCharacter = true;
        for (char unescapedCharacter : unescapedCharacters) {
            if (firstCharacter && !Character.isJavaIdentifierStart(unescapedCharacter)) {
                // Escape characters that can't be the first in a class name.
                escapeAndAppendCharacter(escapedCharacters, unescapedCharacter);
            } else if (!Character.isJavaIdentifierPart(unescapedCharacter)) {
                // Escape characters that can't be in a class name.
                escapeAndAppendCharacter(escapedCharacters, unescapedCharacter);
            } else if (unescapedCharacter == '_') {
                // Escape the escape character.
                escapedCharacters.append("__");
            } else {
                // Leave valid characters alone.
                escapedCharacters.append(unescapedCharacter);
            }

            firstCharacter = false;
        }

        return escapedCharacters.toString();
    }

    private static void escapeAndAppendCharacter(
            StringBuilder escapedCharacters, char unescapedCharacter) {
        String numberString = Integer.toString(unescapedCharacter);
        numberString = Strings.padStart(numberString, MAX_SIXTEEN_BIT_NUMBER_STRING_LENGTH, '0');
        escapedCharacters.append("_" + numberString);
    }

}
