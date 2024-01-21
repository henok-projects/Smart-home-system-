package com.galsie.gcs.serviceutilslibrary;

import org.junit.jupiter.api.Test;

import static com.galsie.lib.utils.StringUtils.compareVersions;


public class CompareVersionsTests {

    @Test
    public void testSameLength(){
        // TESTS
        assert compareVersions("0.0.1", "1.0.0") < 0;
        assert compareVersions("0.1.0", "1.0.0") < 0;
        assert compareVersions("1.0.0", "1.0.0") == 0;
        assert compareVersions("1.0.0", "1.11.0") < 0;
    }

    @Test
    public void testDifferentLength(){
        assert compareVersions("0.0.0.1", "0.0.0") > 0;
        assert  compareVersions("0.0.0.1", "0.0.1") < 0;
    }

    @Test
    public void testWithVersionDescription(){
        assert compareVersions("0.0.0.1-A", "0.0.1-Z") < 0;
        assert  compareVersions("0.0.2-Z", "0.0.2-A") > 0;
    }
}
