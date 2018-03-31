package com.lijinshan.jsonview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by lijinshan on 2018/3/31.
 */

public class JsonView extends LinearLayout {

    public static int KEY_COLOR = 0xFF912d8d;
    public static int TEXT_COLOR = 0xFF40b34f;
    public static int NUMBER_COLOR = 0xFF25AAE2;
    public static int URL_COLOR = 0xFF66D2D5;
    public static int NULL_COLOR = 0xFFef5a34;
    public static int BOOLEAN_COLOR = 0xFFf78382;

    //最顶层可能为Object 或者 Array 两种类型
    private JSONObject rootJSONObject;
    private JSONArray rootJSONArray;


    public JsonView(Context context) {
        this(context, null);
    }

    public JsonView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JsonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
    }

    public void setJsonData(String json) {
        checkJsonIsIllegal(json);
        initRootView();
    }

    private void initRootView() {
        handleRootJsonObject(rootJSONObject);
        handleRootJsonArray(rootJSONArray);
        requestLayout();
    }

    private void handleRootJsonObject(JSONObject jsonObject) {
        if (jsonObject != null && jsonObject.names() != null) {
            handleJsonObject(null, jsonObject, 0);
        }
    }

    private void handleRootJsonArray(JSONArray jsonArray) {
        if (jsonArray != null && jsonArray.length() > 0) {
            handleJsonArray(null, jsonArray, 0);
        }
    }

    private void handleJsonArray(String key, JSONArray value, int hierarchy) {
        createItemViewLeftQuotation(key, hierarchy, "[");
        for (int i = 0; i < value.length(); i++) {
            Object valueObject = value.opt(i);
            handleValue(hierarchy, null, valueObject);
        }
        createItemViewRightQuotation(hierarchy, "]");
    }

    private void createItemViewLeftQuotation(String key, int hierarchy, String quotation) {
        JsonItemView itemViewLeftQuotation = createItemView();
        SpannableStringBuilder keyBuilder = new SpannableStringBuilder();
        keyBuilder.append(getHierarchyStr(hierarchy) + (TextUtils.isEmpty(key) ? "" : key + ":"));
        keyBuilder.setSpan(new ForegroundColorSpan(KEY_COLOR), 0, keyBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        itemViewLeftQuotation.hideLeft();
        itemViewLeftQuotation.showLeft(keyBuilder);
        itemViewLeftQuotation.showRight(quotation);
    }

    private void createItemViewRightQuotation(int hierarchy, String quotation) {
        JsonItemView itemViewRightQuotation = createItemView();
        itemViewRightQuotation.hideLeft();
        itemViewRightQuotation.showRight(getHierarchyStr(hierarchy) + quotation);
    }

    private void handleJsonObject(String key, JSONObject value, int hierarchy) {
        createItemViewLeftQuotation(key, hierarchy, "{");
        for (int i = 0; i < value.names().length(); i++) {
            String keyValue = value.names().optString(i);
            Object valueObject = value.opt(keyValue);
            handleValue(hierarchy, keyValue, valueObject);
        }
        createItemViewRightQuotation(hierarchy, "}");
    }

    private void handleValue(int hierarchyCopy, String keyValue, Object valueObject) {
        SpannableStringBuilder valueBuilder = new SpannableStringBuilder();
        JsonItemView itemViewChild = createItemView();
        if (valueObject instanceof Number) {
            valueBuilder.append(valueObject.toString());
            valueBuilder.setSpan(new ForegroundColorSpan(NUMBER_COLOR), 0, valueBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (valueObject instanceof Boolean) {
            valueBuilder.append(valueObject.toString());
            valueBuilder.setSpan(new ForegroundColorSpan(BOOLEAN_COLOR), 0, valueBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (valueObject instanceof JSONObject) {
            itemViewChild.setVisibility(View.GONE);
            JSONObject jsonObject = (JSONObject) valueObject;
            handleJsonObject(keyValue, jsonObject, ++hierarchyCopy);
        } else if (valueObject instanceof JSONArray) {
            itemViewChild.setVisibility(View.GONE);
            JSONArray jsonArray = (JSONArray) valueObject;
            handleJsonArray(keyValue, jsonArray, ++hierarchyCopy);
        } else if (valueObject instanceof String) {
            valueBuilder.append("\"").append(valueObject.toString()).append("\"");
            valueBuilder.setSpan(new ForegroundColorSpan(TEXT_COLOR), 0, valueBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (valueObject == null) {
            valueBuilder.append("null");
            valueBuilder.setSpan(new ForegroundColorSpan(NULL_COLOR), 0, valueBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else { //JSONObject$1 内部类[null,etc]
            valueBuilder.append(valueObject.toString());
            valueBuilder.setSpan(new ForegroundColorSpan(NULL_COLOR), 0, valueBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        itemViewChild.showRight(valueBuilder);
        SpannableStringBuilder keyBuilder = new SpannableStringBuilder();
        keyBuilder.append(getHierarchyStr(++hierarchyCopy) + (TextUtils.isEmpty(keyValue) ? "" : keyValue + ":"));
        keyBuilder.setSpan(new ForegroundColorSpan(KEY_COLOR), 0, keyBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        itemViewChild.showLeft(keyBuilder);
    }

    private JsonItemView createItemView() {
        JsonItemView itemView = new JsonItemView(getContext());
        addViewNoInvalidate(itemView);
        return itemView;
    }

    public void addViewNoInvalidate(View child) {
        ViewGroup.LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = generateDefaultLayoutParams();
            if (params == null) {
                throw new IllegalArgumentException("generateDefaultLayoutParams() cannot return null");
            }
        }
        addViewInLayout(child, -1, params);
    }

    private void checkJsonIsIllegal(String json) {
        Object object = null;
        try {
            object = new JSONTokener(json).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (object != null && object instanceof JSONObject) {
            rootJSONObject = (JSONObject) object;
        } else if (object != null && object instanceof JSONArray) {
            rootJSONArray = (JSONArray) object;
        } else {
            throw new IllegalArgumentException("jsonStr is illegal.");
        }
    }

    public static String getHierarchyStr(int hierarchy) {
        StringBuilder levelStr = new StringBuilder();
        for (int levelI = 0; levelI < hierarchy; levelI++) {
            levelStr.append("      ");
        }
        return levelStr.toString();
    }
}
