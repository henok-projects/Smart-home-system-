package com.galsie.gcs.homes.infrastructure.rolesandaccess.permissions.format;


import org.springframework.data.util.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public abstract class HomePermissionsFormatter {

    public static HomePermissionsFriendlyFormatter getFriendlyFormatterInstance(){
        return new HomePermissionsFriendlyFormatter();
    }

    public static HomePermissionsRegexFormatter getRegexFormatterInstance(){
        return new HomePermissionsRegexFormatter();
    }
    protected static List<Pair<Character, Character>> REGEX_CONTAINER_CHARS = Arrays.asList(Pair.of('(', ')'), Pair.of('[', ']'));

    /**
     * Formats the permission:
     * - Loops through the characters, if a character is outside a regex container:
     *   - calls {@link HomePermissionsFormatter#processCharacterOutsideContainer} on that character
     * - If the character is in a regex container, it is ignored
     * @param _permission The permission to be formatted
     * @return
     */
    public Optional<String> format(String _permission) {
        var permission = _permission;
        var index = 0;
        var wasContainer = false;
        var arguments = new HashMap<String, String>(); // arguments are used by internally by processCharacterOutsideContainer
        while (index < permission.length()) {
            // Try to process a container at this index
            int tryToProcessContainer = tryToProcessContainer(permission, index);
            if (tryToProcessContainer != -2) { // there IS a new opening container at this index
                if (tryToProcessContainer == -1 || tryToProcessContainer == -3) {
                    // if the container being processed did not reach an end, return an empty optional indicating a fail
                    // OR if a container was closed before being open
                    return Optional.empty();
                }
                // at this point, a container was processed and ended at tryToProcessContainer
                index = tryToProcessContainer + 1; // the current index would now be after the processing container
                wasContainer = true;
                continue;
            }
            // Now, we have a character that is outside a regex container
            // We process this character
            var oldString = permission;
            var replacementString = processCharacterOutsideContainer(permission, index, wasContainer, arguments);
            permission = permission.substring(0, index) + replacementString + permission.substring(Math.min(index+1, permission.length()), permission.length());
            wasContainer = false;

            var sizeChange = permission.length()-oldString.length();
            index += 1 + sizeChange; // if the size decreased, account for it
        }
        return Optional.of(permission);
    }

    /**
     * Processes a character that is not in a regex container
     * @param chars
     * @param charIndex
     * @param justAfterContainer
     * @param arguments A map that persist from function call to another, so you can carry information
     * @return A String that should replace this character
     */
    protected abstract String processCharacterOutsideContainer(String chars, int charIndex, boolean justAfterContainer, HashMap<String, String> arguments);

    /**
     * Processes a regex container whose opening character is at the start index
     *
     * @param chars       The character array which this container is part of
     * @param startIndex  The opening characters index
     * @param closingChar The closing character, which needs to be found to close the container
     * @return The index of the closing character, or -1 if not found
     */
    protected int processRegexContainer(String chars, int startIndex, char closingChar) {
        var index = startIndex + 1; // start at the index after the opening character
        while (index < chars.length()) {
            if (chars.charAt(index) == closingChar) { // if we reached the closing character, return the closing character index
                return index;
            }
            // Try to process a sub-container at this index
            int tryToProcessContainer = tryToProcessContainer(chars, index);
            if (tryToProcessContainer == -2) { // theres NO new opening container at this index
                index += 1;
                continue;
            }
            // if tryToProcessContainer is -1, means we tried to process a container but it failed, just return -1
            // If its -3 means a closing was found before an opening
            if (tryToProcessContainer == -1 || tryToProcessContainer == -3) {
                return -1;
            }
            index = tryToProcessContainer + 1; // a subcontainer was processed and ended at tryToProcessContainer
        }
        return -1;
    }

    /**
     * Tries to process a container starting at this character index
     * - If no opening character was found, returns -2
     * - If a closing container was found before an opening, returns -3
     * - If one was found:
     * - returns -1 if a closing character wasn't found
     * - returns the index of the closing cahracter if it was found
     */
    protected int tryToProcessContainer(String chars, int charIndex) {
        char character = chars.charAt(charIndex);
        for (var containerChars : REGEX_CONTAINER_CHARS) {
            if (character == containerChars.getSecond()) { // closing was found before opening
                return -3; // return error
            }
            // No opening char, continue
            if (character != containerChars.getFirst()) {
                continue;
            }
            // if opening char, process
            return processRegexContainer(chars, charIndex, containerChars.getSecond());
        }
        return -2;
    }

}
