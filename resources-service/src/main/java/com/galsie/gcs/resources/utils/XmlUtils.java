package com.galsie.gcs.resources.utils;

import org.jsoup.nodes.Element;

import java.util.HashMap;

public class XmlUtils {

    public static HashMap<String, String> readStringDataFromCssQuery(Element element, String... cssQueries) throws Exception{
        HashMap<String, String> queryResults = new HashMap<>();
        for (String query: cssQueries){
            Element el = element.select(query).first();
            if (el == null){
                throw new Exception("Couldn't readStringDataFromCssQuery '" + query + "': Element was not found");
            }
            queryResults.put(query, el.text());
        }
        return queryResults;
    }
}
