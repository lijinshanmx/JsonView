package com.lijinshan.jsonview;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijinshan on 2018/4/9.
 */

public class JsonHelper {

    public static boolean isDebug = false;

    public static int KEY_COLOR = 0xFF922799;
    public static int TEXT_COLOR = 0xFF3AB54A;
    public static int NUMBER_COLOR = 0xFF25AAE2;
    public static int URL_COLOR = 0xFF66D2D5;
    public static int NULL_COLOR = 0xFFEF5935;
    public static int BOOLEAN_COLOR = 0xFFf78382;
    public static int BRACES_COLOR = 0xFF4A555F;//"{"，"}","[","]",":" Color
    public static int DEBUG_COLOR = 0xFFFF0000;

    public static String getHierarchyStr(int hierarchy) {
        StringBuilder levelStr = new StringBuilder();
        for (int levelI = 0; levelI < hierarchy; levelI++) {
            levelStr.append("      ");
        }
        return levelStr.toString();
    }

    //note: json array don't have delete function.
    public static JSONArray remove(final int idx, final JSONArray from) {
        final List<Object> objs = asList(from);
        objs.remove(idx);
        final JSONArray ja = new JSONArray();
        for (final Object obj : objs) {
            ja.put(obj);
        }
        return ja;
    }

    private static List<Object> asList(final JSONArray ja) {
        final int len = ja.length();
        final ArrayList<Object> result = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Object obj = ja.opt(i);
            if (obj != null) {
                result.add(obj);
            }
        }
        return result;
    }
}
