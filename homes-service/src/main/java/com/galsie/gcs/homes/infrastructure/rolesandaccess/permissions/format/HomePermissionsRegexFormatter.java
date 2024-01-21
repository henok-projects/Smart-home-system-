package com.galsie.gcs.homes.infrastructure.rolesandaccess.permissions.format;

import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * This method can take a regex or non-regex permission and ensures its properly formatted
 *
 * A non-regex permission may be in any of the formats:
 * - Permission.sub1.sub2
 * - Permission.sub1.*
 * - Permission.su*b1.sub2
 * - Permission.sub(1|2|3|4).something.*
 * - Permission.sub(1|2|3|4)*.*
 * - Permission.sub(1.3.5*)
 *
 * The rules are:
 * - Regex must be in () or in []
 * - Regex in () or [] can be followed by * or +
 *      - When this is done, the '*' isn't replaced with '.*'
 * - '*' is a special character that is equivalent to '.*'
 *      - the only exception is if it comes after the () or [] blocks
 * - '.' dots are for dot joining permissions
 *  - 2 dots following one another are considered duplicate, one is removed.
 *
 * The regex permission becomes:
 * - Permission\.sub1\.sub2
 *      - notice how the dots were escaped
 * - Permission\.sub1\..*
 *      - notice how the dots were escaped, and the '*' became '.*'
 * - Permission\.su.*b1\.sub2
 *      - Notice how the dots were escaped, and the '*' became '.*' even when in the middle
 * - Permission\.sub(1|2|3|4)\.something\..*
 *      - Notice how Regex in
 * - Permission\.sub(1|2|3|4)*\.*
 *      - notice how the * after the () didn't become .*, following Rule 2.
 * - Permission\.sub(1.3.5*)
 *      - notice how the regex in () wasn't affected
 */
@Component
public class HomePermissionsRegexFormatter extends HomePermissionsFormatter {

    HomePermissionsRegexFormatter(){ // package private

    }
    @Override
    protected String processCharacterOutsideContainer(String chars, int charIndex, boolean justAfterContainer, HashMap<String, String> arguments) {
        var character = chars.charAt(charIndex);

        if (character == '.') {
            return this.processDotOutsideContainer(chars, charIndex, justAfterContainer);
        }
        if (character == '*') {
            return this.processAstherithOutsideContainer(chars, charIndex, justAfterContainer);
        }

        return String.valueOf(character);
    }

    /**
     * A '.' is:
     * - Escaped if there's no '.' before it and it's not already escaped
     * - If there is a '.' before it, then this dot may be a duplicate or a dot for a '*'. So:
     *  - We check if there's a '*' after it, if there is, we keep the dot
     *  - Otherwise, we remove the dot since its duplicate
     * @param chars
     * @param charIndex
     * @param justAfterContainer
     * @return
     */
    private String processDotOutsideContainer(String chars, int charIndex, boolean justAfterContainer) {
        var character = chars.charAt(charIndex);
        if (character != '.') {
            return String.valueOf(character);
        }
        // if its the first character, or the character before it is a dot
        if (charIndex == 0 || chars.charAt(charIndex-1) == '.'){
            // get the next character
            var nextChar = charIndex < chars.length()-1 ? chars.charAt(charIndex + 1): null;

            // if the dot is at the end, or the character after it is not a '*', simply remove this dot, its an extra dot
            if (nextChar == null || nextChar != '*') {
                return "";
            }
            // In this case, its a dot with a '*' after it, keep it.
            return ".";
        }
        // In this case, its not the first character, and its not preceeded by a dot
        // So, If it was not escaped, we need escape it

        // Here, we check if it wasn't escape, then escape it
        if (chars.charAt(charIndex-1) != '\\') {
            return "\\.";
        }
        // in this case, this is a dot that is escaped, keep it
        return ".";
    }

    /**
     * A '*' is:
     * - Kept as is if it's after a regex container
     * - Otherwise, it needs ot have a non-escaped dot before it. So:
     *  - We check if there's a dot before it. Then:
     *      - Check if that dot was escaped.
     *      - If the preceeding dot was escaped, we add a dot before this '*'
     *      - If it wasn't escaped, we keep this '*' as is
     *  - If there's no dot before it, so we add it
     *
     */
    private String processAstherithOutsideContainer(String chars, int charIndex, boolean justAfterContainer) {
        var character = chars.charAt(charIndex);
        if (character != '*') {
            return String.valueOf(character);
        }
        if (justAfterContainer) { // if the '*' came just after a container, allow it as its for the regex container
            return "*";
        }
        // If its the first character, add a dot before it
        if (charIndex == 0) {
            return ".*";
        }
        // get the previous character
        var prevChar = chars.charAt(charIndex - 1);
        // If the previous character is NOT a dot, we should add it
        if (prevChar != '.') {
            return ".*";
        }
        // If it was a DOT, ensure the character before the dot does not escape that dot
        if (charIndex == 1) { // there can't be anything before the dot if this character is at index 1
            return "*"; // return the star, it already has a dot before it
        }
        var escapingChar = chars.charAt(charIndex - 2); // get the character that may be escaping the dot
        if (escapingChar == '\\') { // if the dot before the '*' is escaped, add a dot
            return ".*";
        }
        return "*"; // In this case, there is a dot before it that is not escaped
    }

}
