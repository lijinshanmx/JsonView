package com.lijinshan.jsonview;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lijinshan
 * @date 2018/3/31
 */

public class JsonAdapter extends RecyclerView.Adapter<JsonAdapter.ViewHolder> {

    public static int KEY_COLOR = 0xFF912d8d;
    public static int TEXT_COLOR = 0xFF40b34f;
    public static int NUMBER_COLOR = 0xFF25AAE2;
    public static int URL_COLOR = 0xFF66D2D5;
    public static int NULL_COLOR = 0xFFef5a34;
    public static int BOOLEAN_COLOR = 0xFFf78382;

    private JSONObject rootJSONObject;
    private JSONArray rootJSONArray;

    private List<JsonItemBean> jsonItemBeans;


    public void setJsonData(String json) {
        checkJsonIsIllegal(json);
        initJsonItemList();
        initRootView();
    }

    private void initJsonItemList() {
        if (jsonItemBeans == null) {
            jsonItemBeans = new ArrayList<>();
        }
        jsonItemBeans.clear();
    }

    private void initRootView() {
        handleRootJsonObject(rootJSONObject);
        handleRootJsonArray(rootJSONArray);
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

    private void createItemViewLeftQuotation(String key, int hierarchy, String quotation) {
        JsonItemBean jsonItemBean = new JsonItemBean();
        jsonItemBeans.add(jsonItemBean);
        SpannableStringBuilder keyBuilder = new SpannableStringBuilder();
        keyBuilder.append(getHierarchyStr(hierarchy) + (TextUtils.isEmpty(key) ? "" : "\"" + key + "\"" + ":"));
        keyBuilder.setSpan(new ForegroundColorSpan(KEY_COLOR), 0, keyBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        jsonItemBean.key = keyBuilder;
        jsonItemBean.value = quotation;
    }

    private void createItemViewRightQuotation(int hierarchy, String quotation) {
        JsonItemBean jsonItemBean = new JsonItemBean();
        jsonItemBeans.add(jsonItemBean);
        jsonItemBean.value = getHierarchyStr(hierarchy) + quotation;
    }

    private void handleJsonArray(String key, JSONArray value, int hierarchy) {
        createItemViewLeftQuotation(key, hierarchy, "[");
        for (int i = 0; i < value.length(); i++) {
            Object valueObject = value.opt(i);
            handleValue(hierarchy, null, valueObject, i < value.length() - 1);
        }
        createItemViewRightQuotation(hierarchy, "]" + (hierarchy == 0 ? "" : ","));
    }

    private void handleJsonObject(String key, JSONObject value, int hierarchy) {
        createItemViewLeftQuotation(key, hierarchy, "{");
        for (int i = 0; i < value.names().length(); i++) {
            String keyValue = value.names().optString(i);
            Object valueObject = value.opt(keyValue);
            handleValue(hierarchy, keyValue, valueObject, i < value.names().length() - 1);
        }
        createItemViewRightQuotation(hierarchy, "}" + (hierarchy == 0 ? "" : ","));
    }

    private void handleValue(int hierarchyCopy, String keyValue, Object valueObject, boolean appendComma) {
        SpannableStringBuilder valueBuilder = new SpannableStringBuilder();
        JsonItemBean jsonItemBean = createItemView();
        if (valueObject instanceof Number) {
            valueBuilder.append(valueObject.toString());
            valueBuilder.setSpan(new ForegroundColorSpan(NUMBER_COLOR), 0, valueBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (valueObject instanceof Boolean) {
            valueBuilder.append(valueObject.toString());
            valueBuilder.setSpan(new ForegroundColorSpan(BOOLEAN_COLOR), 0, valueBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (valueObject instanceof JSONObject) {
//            itemViewChild.setVisibility(View.GONE);
            jsonItemBeans.remove(jsonItemBean);
            JSONObject jsonObject = (JSONObject) valueObject;
            handleJsonObject(keyValue, jsonObject, ++hierarchyCopy);
        } else if (valueObject instanceof JSONArray) {
//            itemViewChild.setVisibility(View.GONE);
            jsonItemBeans.remove(jsonItemBean);
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
        if (appendComma) {
            valueBuilder.append(",");
        }
        jsonItemBean.value = valueBuilder;
        SpannableStringBuilder keyBuilder = new SpannableStringBuilder();
        keyBuilder.append(getHierarchyStr(++hierarchyCopy) + (TextUtils.isEmpty(keyValue) ? "" : "\"" + keyValue + "\"" + ":"));
        keyBuilder.setSpan(new ForegroundColorSpan(KEY_COLOR), 0, keyBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        itemViewChild.showLeft(keyBuilder);
        jsonItemBean.key = keyBuilder;
    }

    private JsonItemBean createItemView() {
        JsonItemBean jsonItemBean = new JsonItemBean();
        jsonItemBeans.add(jsonItemBean);
        return jsonItemBean;
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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_json_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        JsonItemBean jsonItemBean = jsonItemBeans.get(position);
        holder.tvLeft.setVisibility(TextUtils.isEmpty(jsonItemBean.key) ? View.GONE : View.VISIBLE);
        holder.tvRight.setVisibility(TextUtils.isEmpty(jsonItemBean.value) ? View.GONE : View.VISIBLE);
        if (jsonItemBean.key != null) {
            holder.tvLeft.setText(jsonItemBean.key);
        }
        if (jsonItemBean.value != null) {
            holder.tvRight.setText(jsonItemBean.value);
        }
    }

    @Override
    public int getItemCount() {
        return jsonItemBeans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvLeft, tvRight;

        public ViewHolder(View itemView) {
            super(itemView);
            tvLeft = itemView.findViewById(R.id.tv_left);
            tvRight = itemView.findViewById(R.id.tv_right);
        }
    }
}
