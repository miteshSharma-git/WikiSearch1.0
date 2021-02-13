package com.Google;
//How to fetch data from Api in our Programme
/*
Requrements
 json-simple-1.1.1.jar
 jsoup-1.13.1.jar
 */

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.TreeMap;
import java.util.function.*;

//I will try to Use Functional Interface and Lambda Expressaion
interface Wiki<String> {
    InputStreamReader Search(String Search);
}

interface Parser<InputStream> {
    JSONArray parser(InputStreamReader ie);
}

interface Process<JSONArray> {
    TreeMap process(JSONArray o);
}


public class Main {
    public static void main(String[] args) {

        Wiki<String> w = i -> {
            HttpURLConnection c = null;
            URL u;
            try {
                u = new URL("https://en.wikipedia.org/w/api.php?origin=*&action=opensearch&search=" + i);
                c = (HttpURLConnection) u.openConnection();
                if (c.getResponseCode() > -1) {
                    InputStream inputStream = c.getInputStream();
                    InputStreamReader input = new InputStreamReader(inputStream);
                    return input;
                }
            } catch (MalformedURLException m) {
                m.printStackTrace();
            } catch (IOException n) {
                n.printStackTrace();
            }
            return null;
        };
        Parser<InputStreamReader> parser = ir -> {
            try {
                return (JSONArray) new JSONParser().parse(ir);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        };
        Process<JSONArray> processing = jsonArray -> {
            Predicate<Object> p = i -> i.equals(i.toString()) && !i.toString().equals("");
            var i = 0;
            TreeMap<Integer, Object> treeMap = new TreeMap<Integer, Object>();
            for (Object object : jsonArray) {
                if (p.test(object)) {
                    treeMap.put(i, object);
                    i++;
                } else {
                    JSONArray insideObject = (JSONArray) object;
                    for (Object inObject : insideObject) {
                        if (p.test(inObject)) {
                            treeMap.put(i, object);
                            i++;
                        }

                    }
                }


            }
            if (treeMap.isEmpty()) return null;
            else return treeMap;
        };


        InputStreamReader i = w.Search("salman khan");
        JSONArray o = parser.parser(i);
        TreeMap t = processing.process(o);
        // System.out.println(t);


    }

}

