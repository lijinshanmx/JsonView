package com.lijinshan.jsonview;

import org.json.JSONObject;

/**
 * Created by lijinshan on 2018/4/9.
 */

public interface IJsonOperation {

    void deleteJsonItem(JsonItemBean jsonItemBean);

    void appendJsonValue(JsonItemBean jsonItemBean, String key, Object value);

    void appendJsonObject(JsonItemBean jsonItemBean, String key, JSONObject value);

    void changeJsonItem(JsonItemBean jsonItemBean, Object valueObject);

    void collapseAllJsonItems();

    void expandAllJsonItems();

    String generateJson();
}
