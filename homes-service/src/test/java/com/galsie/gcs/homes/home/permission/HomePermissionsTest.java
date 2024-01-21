package com.galsie.gcs.homes.home.permission;


import com.galsie.gcs.homes.infrastructure.rolesandaccess.permissions.format.HomePermissionsFormatter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

//	new code
@SpringBootTest
public class HomePermissionsTest {

    static List<Pair<Character, Character>> REGEX_CONTAINER_CHARS = Arrays.asList(Pair.of('(', ')'), Pair.of('[', ']'));

    public static void main(String[] args){
        var regexFormatter = HomePermissionsFormatter.getRegexFormatterInstance();
        var friendlyFormatter = HomePermissionsFormatter.getFriendlyFormatterInstance();

        var millis = System.currentTimeMillis();
        String[] randomPers = {
                "liwaa.permission..*",
                "liwaa.permi*ssion.*",
                "liwaa.perm(123.*)asdf*.lk.*",
                "liwaa.perm(1+23.*).lk\\.*",
                "perm.*.*.*",
                "perm...*",
                "perm().[]",
                //"perm(",
                //"perm]",
                //"perm[.*",
                "liwaa\\.perm(1+23.*)\\.lk\\..*"
        };//generateRandomPermissionList(100);
        var timeTaken = System.currentTimeMillis() - millis;
        System.out.println("Toook to gen: " + timeTaken + "ms");

        millis = System.currentTimeMillis();
        for (String permission: randomPers){
            var formattedOpt = regexFormatter.format(permission);
            var friendly = friendlyFormatter.format(formattedOpt.get());
            System.out.println(permission + " | " + friendlyFormatter.format(permission).get() + "  ------>  " + formattedOpt.get() + "  ----->   " + friendly.get() + "    ===   " + (friendly.get().compareTo(friendlyFormatter.format(permission).get()) == 0));
        }
        timeTaken = System.currentTimeMillis() - millis;
        System.out.println("Toook " + timeTaken + "ms");

    }

    @Test
    public void testByHardcodedPermissions() {
        var regexFormatter = HomePermissionsFormatter.getRegexFormatterInstance();
        var friendlyFormatter = HomePermissionsFormatter.getFriendlyFormatterInstance();

        // Test some hardcoded permissions
        String[] hardcodedPermissions = {
                // Add the expected permissions here
        };

        for (String permission : hardcodedPermissions) {
            var formattedOpt = regexFormatter.format(permission);
            var friendly = friendlyFormatter.format(formattedOpt.get());
            assertTrue(formattedOpt.isPresent(), "Formatted permission should be present");
            assertTrue(friendly.isPresent(), "Friendly permission should be present");

        }
    }

    @Test
    public void testWithRandomPermissions() {
        var regexFormatter = HomePermissionsFormatter.getRegexFormatterInstance();
        var friendlyFormatter = HomePermissionsFormatter.getFriendlyFormatterInstance();

        List<String> randomPermissions = generateRandomPermissionList(100);
        for (String permission : randomPermissions) {
            try {
                var formattedOpt = regexFormatter.format(permission);
                var friendly = friendlyFormatter.format(permission);

                assertTrue(formattedOpt.isPresent(), "Formatting for permission " + permission + " should provide a result.");
                assertTrue(friendly.isPresent(), "Friendly format for permission " + permission + " should provide a result.");

                // You can add more validation here if required, e.g., check if the formatted result matches certain criteria or patterns.
            } catch (Exception e) {
                fail("Exception occurred when processing permission: " + permission + ". Message: " + e.getMessage());
            }
        }
    }


    @Test
    public void testPerformance() {
        var regexFormatter = HomePermissionsFormatter.getRegexFormatterInstance();
        var friendlyFormatter = HomePermissionsFormatter.getFriendlyFormatterInstance();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<String> permissions = generateRandomPermissionList(10000);
        for (String permission : permissions) {
            regexFormatter.format(permission);
            friendlyFormatter.format(permission);
        }

        stopWatch.stop();
        assertFalse(stopWatch.getTotalTimeSeconds() <= 3, "Formatting 10,000 permissions should not take more than 3 seconds");
    }

    public static List<String> generateRandomPermissionList(int count){
        List<String> listStr = new ArrayList<>();
        for (int i = 0; i < count; i++){
            listStr.add(generateRandomPermission());
        }
        return listStr;
    }

        public static String generateRandomPermission(){
        int dotCount = (int) (Math.random()*30);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < dotCount; i++){
//            builder.append(StringUtils.random((int) (Math.random()*70)));
//            if (Math.random() > 0.5){
//                builder.append(generateRandomRegexContainer());
//            }
//            builder.append(".");
        }
        return builder.toString();
    }

        public static String generateRandomRegexContainer(){
        var randomRegexShit = "|^-.*+";
        int randomIndex = (int) (Math.random()*2);
        var containerType = REGEX_CONTAINER_CHARS.get(randomIndex);
        StringBuilder builder = new StringBuilder();
        builder.append(containerType.getFirst());
        for (int i = 0; i < 20; i++){
//            if (Math.random() < 0.5){
//                builder.append(StringUtils.random(1));
//                continue;
//            }
            int random = (int) (Math.random()*randomRegexShit.length());
            builder.append(randomRegexShit.charAt(random));
        }
        builder.append(containerType.getSecond());
        return builder.toString();
    }

}
