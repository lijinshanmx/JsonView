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
    private boolean hasJsonItemsChanged = false;

    private List<JsonItemBean> jsonItemBeans;
    private List<JsonItemBean> viewJsonItemBeans;


    public JsonAdapter() {
        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                hasJsonItemsChanged = true;
            }
        });
    }

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
            handleJsonObject(null, null, jsonObject, 0);
        }
    }

    private void handleRootJsonArray(JSONArray jsonArray) {
        if (jsonArray != null && jsonArray.length() > 0) {
            handleJsonArray(null, null, jsonArray, 0);
        }
    }

    private JsonItemBean createItemViewLeftQuotation(JsonItemBean parent, String key, int hierarchy, String quotation) {
        JsonItemBean jsonItemBean = new JsonItemBean();
        jsonItemBean.hierarchy = hierarchy;
        jsonItemBean.isNode = true;
        jsonItemBean.parent = parent;
        jsonItemBeans.add(jsonItemBean);
        SpannableStringBuilder keyBuilder = new SpannableStringBuilder();
        keyBuilder.append(getHierarchyStr(hierarchy) + (TextUtils.isEmpty(key) ? "" : "\"" + key + "\"" + ":") + quotation);
        keyBuilder.setSpan(new ForegroundColorSpan(KEY_COLOR), 0, keyBuilder.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        jsonItemBean.key = keyBuilder;
        return jsonItemBean;

    }

    private void createItemViewRightQuotation(JsonItemBean parent, int hierarchy, String quotation) {
        JsonItemBean jsonItemBean = new JsonItemBean();
        jsonItemBean.hierarchy = hierarchy;
        jsonItemBean.parent = parent;
        jsonItemBeans.add(jsonItemBean);
        jsonItemBean.key = getHierarchyStr(hierarchy) + quotation;
    }

    private void handleJsonArray(JsonItemBean parentItem, String key, JSONArray value, int hierarchy) {
        JsonItemBean parent = createItemViewLeftQuotation(parentItem, key, hierarchy, "[");
        for (int i = 0; i < value.length(); i++) {
            Object valueObject = value.opt(i);
            handleValue(parent, hierarchy, null, valueObject, i < value.length() - 1);
        }
        createItemViewRightQuotation(parent, hierarchy, "]" + (hierarchy == 0 ? "" : ","));
    }

    private void handleJsonObject(JsonItemBean parentItem, String key, JSONObject value, int hierarchy) {
        JsonItemBean parent = createItemViewLeftQuotation(parentItem, key, hierarchy, "{");
        for (int i = 0; i < value.names().length(); i++) {
            String keyValue = value.names().optString(i);
            Object valueObject = value.opt(keyValue);
            handleValue(parent, hierarchy, keyValue, valueObject, i < value.names().length() - 1);
        }
        createItemViewRightQuotation(parent, hierarchy, "}" + (hierarchy == 0 ? "" : ","));
    }

    private void handleValue(JsonItemBean parent, int hierarchyCopy, String keyValue, Object valueObject, boolean appendComma) {
        SpannableStringBuilder valueBuilder = new SpannableStringBuilder();
        JsonItemBean jsonItemBean = createItemView();
        jsonItemBean.parent = parent;
        jsonItemBean.hierarchy = hierarchyCopy;
        if (valueObject instanceof Number) {
            valueBuilder.append(valueObject.toString());
            valueBuilder.setSpan(new ForegroundColorSpan(NUMBER_COLOR), 0, valueBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (valueObject instanceof Boolean) {
            valueBuilder.append(valueObject.toString());
            valueBuilder.setSpan(new ForegroundColorSpan(BOOLEAN_COLOR), 0, valueBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (valueObject instanceof JSONObject) {
            jsonItemBeans.remove(jsonItemBean);
            JSONObject jsonObject = (JSONObject) valueObject;
            handleJsonObject(parent, keyValue, jsonObject, ++hierarchyCopy);
        } else if (valueObject instanceof JSONArray) {
            jsonItemBeans.remove(jsonItemBean);
            JSONArray jsonArray = (JSONArray) valueObject;
            handleJsonArray(parent, keyValue, jsonArray, ++hierarchyCopy);
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
        final JsonItemBean jsonItemBean = viewJsonItemBeans.get(position);
        holder.tvLeft.setVisibility(TextUtils.isEmpty(jsonItemBean.key) ? View.GONE : View.VISIBLE);
        holder.tvRight.setVisibility(TextUtils.isEmpty(jsonItemBean.value) ? View.GONE : View.VISIBLE);
        holder.tvLeft.setOnClickListener(null);
        holder.tvRight.setOnClickListener(null);
        if (jsonItemBean.key != null) {
            holder.tvLeft.setText(jsonItemBean.key);
            holder.tvLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (jsonItemBean.isNode) {
                        jsonItemBean.isFolded = !jsonItemBean.isFolded;
                        System.out.println(jsonItemBean.key.toString().trim());
                        findParent(jsonItemBean.isFolded, jsonItemBean);
                    }
                }
            });
        }
        if (jsonItemBean.value != null) {
            holder.tvRight.setText(jsonItemBean.value);
            holder.tvRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(jsonItemBean.value.toString().trim());
                }
            });
        }
    }

    private void findParent(boolean collapse, JsonItemBean jsonItemBean) {
        for (JsonItemBean itemBean : jsonItemBeans) {
            if (itemBean.parent == jsonItemBean) {
                itemBean.collapse = collapse;
                if (itemBean.isNode) {
                    findParent(collapse, itemBean);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return calcViewJsonItemBeanSize();
    }

    private int calcViewJsonItemBeanSize() {
        if (viewJsonItemBeans == null) {
            viewJsonItemBeans = new ArrayList<>();
            viewJsonItemBeans.addAll(jsonItemBeans);
        }
        if (hasJsonItemsChanged) {
            hasJsonItemsChanged = false;
            viewJsonItemBeans.clear();
            for (JsonItemBean jsonItemBean : jsonItemBeans) {
                if (!jsonItemBean.collapse) {
                    viewJsonItemBeans.add(jsonItemBean);
                }
            }
        }

        return viewJsonItemBeans.size();
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
