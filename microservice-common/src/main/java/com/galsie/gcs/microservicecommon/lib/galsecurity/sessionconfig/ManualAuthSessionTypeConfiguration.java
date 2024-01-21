package com.galsie.gcs.microservicecommon.lib.galsecurity.sessionconfig;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This class defines a list of paths that are to be authenticated by a provided authenticator in a given {@link AuthSessionTypeConfiguration}
 */
public class ManualAuthSessionTypeConfiguration {

    List<Pattern> pathsToAuthenticate;

    public ManualAuthSessionTypeConfiguration(List<String> patterns){
       this.pathsToAuthenticate = patterns.stream().map(Pattern::compile ).toList();
    }

    public ManualAuthSessionTypeConfiguration(String... patterns){
        this.pathsToAuthenticate = Arrays.stream(patterns).map(Pattern::compile ).toList();
    }


    /**
     * given a uri, this method will return true if the uri matches any of the patterns in the list of paths to authenticate
     */
    public boolean doesUriRequireAuth(String uri) {
        return this.pathsToAuthenticate.stream().anyMatch((p) -> p.matcher(uri).find());
    }

}
