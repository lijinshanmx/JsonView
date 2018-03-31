package com.lijinshan.jsonview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by lijinshan on 2018/3/31.
 */

public class JsonAdapter {
    public static int KEY_COLOR = 0xFF922799;
    public static int TEXT_COLOR = 0xFF3AB54A;
    public static int NUMBER_COLOR = 0xFF25AAE2;
    public static int URL_COLOR = 0xFF66D2D5;
    public static int NULL_COLOR = 0xFFEF5935;
    public static int BRACES_COLOR = 0xFF4A555F;

    //最顶层可能为Object 或者 Array 两种类型
    private JSONObject rootJSONObject;

    private JSONArray rootJSONArray;

    public JsonAdapter(String json) {
        Object object = checkJsonIsIllegal(json);
        if (object != null && object instanceof JSONObject) {
            rootJSONObject = (JSONObject) object;
        } else if (object != null && object instanceof JSONArray) {
            rootJSONArray = (JSONArray) object;
        } else {
            throw new IllegalArgumentException("jsonStr is illegal.");
        }
    }

    private Object checkJsonIsIllegal(String json) {
        Object object = null;
        try {
            object = new JSONTokener(json).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
}
