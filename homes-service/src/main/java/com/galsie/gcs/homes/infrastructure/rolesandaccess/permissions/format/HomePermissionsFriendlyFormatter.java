package com.galsie.gcs.homes.infrastructure.rolesandaccess.permissions.format;

import java.util.HashMap;
/**
 * This method can take a regex permission and ensures its friendly formatted
 *
 * The regex permission looks as such:
 * - Permission\.sub1\.sub2
 * - Permission\.sub1\..*
 * - Permission\.su.*b1\.sub2
 * - Permission\.sub(1|2|3|4)\.something\..*
 * - Permission\.sub(1|2|3|4)*\.*
 * - Permission\.sub(1.3.5*)
 *
 * It will have to be returned back to its original state, as such:
 * - Permission.sub1.sub2
 * - Permission.sub1.*
 * - Permission.su*b1.sub2
 * - Permission.sub(1|2|3|4).something.*
 * - Permission.sub(1|2|3|4)*.*
 * - Permission.sub(1.3.5*)
 *
 * The rules are:
 * - Regex in () or [] remains untouched
 * - If a '\' followed by a dot, it is removed
 * - If a dot is NOT preceeded by a backslash, and followed by a '*', it is removed
 *
 * NOTE:
 * - Calling this method on a NON-regex fixed permission may not yield the same permission, because some things are ambiguous
 *  - eg: liwaa.permi*ssion.* becomes liwaa.permi*ssion*
 */
public class HomePermissionsFriendlyFormatter extends HomePermissionsFormatter {

    HomePermissionsFriendlyFormatter(){ // package private

    }
    @Override
    public String processCharacterOutsideContainer(String chars, int charIndex, boolean justAfterContainer, HashMap<String, String> arguments) {
        var isPrevCharBackslash = arguments.remove("isPrevCharBackslash") != null;


        char character = chars.charAt(charIndex);
        if (character == '\\') {
            // if the '\' is the last character, it's shouldn't be there
            if (charIndex == chars.length() - 1) {
                return "";
            }
            var nextChar = chars.charAt(charIndex + 1);
            // If the '\' isn't followed by a dot, it shouldn't be there
            if (nextChar != '.'){
                return "";
            }
            // it is followed by a dot, it should be removed. Add it to the map for context though
            arguments.put("isPrevCharBackslash", "");
            return "";
        }

        if (character == '.') {
            if (charIndex == 0 || charIndex == chars.length() - 1) {
                return "";
            }

            // if the prev char was a backslash, keep it
            if (isPrevCharBackslash){
                return ".";
            }
            // if the prev char is not a backslash, and it is followed by a '*', remove the dot
            if (chars.charAt(charIndex+1) == '*'){
                return "";
            }

            // if the previous character is a dot, and this dot is not followed by a '*', its a duplicate dot
            if (chars.charAt(charIndex-1) == '.'){
                return "";
            }
            // if its not followed by '*', keep the dot
            return ".";
        }
        return String.valueOf(character);
    }
}
