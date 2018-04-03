package com.lijinshan.jsonview;

import android.os.Handler;
import android.support.annotation.NonNull;
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

    public static int KEY_COLOR = 0xFF922799;
    public static int TEXT_COLOR = 0xFF3AB54A;
    public static int NUMBER_COLOR = 0xFF25AAE2;
    public static int URL_COLOR = 0xFF66D2D5;
    public static int NULL_COLOR = 0xFFEF5935;
    public static int BOOLEAN_COLOR = 0xFFf78382;
    public static int BRACES_COLOR = 0xFF4A555F;//"{"，"}","[","]",":" Color
    private Handler handler = new Handler();

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
        handleJsonObject(null, null, null, jsonObject, 0, false);
    }

    private void handleRootJsonArray(JSONArray jsonArray) {
        handleJsonArray(null, null, null, jsonArray, 0, false);
    }

    private JsonItemBean createItemViewLeftQuotation(JsonItemBean parent, Object jsonValue, Object jsonIndex, String key, int hierarchy, boolean isJsonObject, int arraySize, boolean appendComma) {
        String quotation = isJsonObject ? "{" : "[";
        String itemKeyLeftSpace = getHierarchyStr(hierarchy);
        String itemKeyLeft = itemKeyLeftSpace + (TextUtils.isEmpty(key) ? "" : "\"" + key + "\"");
        String itemKey = itemKeyLeft + (TextUtils.isEmpty(key) ? "" : ":") + quotation;
        JsonItemBean jsonItemBean = createJsonItemBean(parent, hierarchy, true);
        jsonItemBean.isObjectOrArray = isJsonObject;
        jsonItemBean.jsonValue = jsonValue;
        jsonItemBean.jsonIndex = jsonIndex;
        SpannableStringBuilder keyBuilder = new SpannableStringBuilder();
        keyBuilder.append(itemKey);
        if (keyBuilder.length() > 0) {
            if (itemKeyLeft.length() > 0) {
                keyBuilder.setSpan(new ForegroundColorSpan(KEY_COLOR), 0, itemKeyLeft.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                keyBuilder.setSpan(new ForegroundColorSpan(BRACES_COLOR), itemKeyLeft.length(), keyBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                keyBuilder.setSpan(new ForegroundColorSpan(BRACES_COLOR), 0, keyBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        jsonItemBean.key = keyBuilder;
        if (hierarchy == 0) {
            jsonItemBean.collapsedNodeText = keyBuilder;
        } else {
            SpannableStringBuilder collapseTextBuilder = new SpannableStringBuilder(itemKeyLeft + (TextUtils.isEmpty(key) ? "" : ":") + (isJsonObject ? "Object{...}" : "Array[" + arraySize + "]") + (appendComma ? "," : ""));
            if (collapseTextBuilder.length() > 0) {
                if (itemKeyLeft.length() > 0) {
                    collapseTextBuilder.setSpan(new ForegroundColorSpan(KEY_COLOR), 0, itemKeyLeft.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    collapseTextBuilder.setSpan(new ForegroundColorSpan(BRACES_COLOR), itemKeyLeft.length(), collapseTextBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    collapseTextBuilder.setSpan(new ForegroundColorSpan(BRACES_COLOR), 0, collapseTextBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            jsonItemBean.collapsedNodeText = collapseTextBuilder;
        }
        return jsonItemBean;

    }

    private void createItemViewRightQuotation(JsonItemBean parent, int hierarchy, boolean isJsonObject, boolean appendComma) {
        String quotation = (isJsonObject ? "}" : "]") + (appendComma ? "," : "");
        JsonItemBean jsonItemBean = createJsonItemBean(parent, hierarchy);
        jsonItemBean.isObjectOrArray = isJsonObject;
        SpannableStringBuilder keyBuilder = new SpannableStringBuilder(getHierarchyStr(hierarchy) + quotation);
        if (keyBuilder.length() > 0) {
            keyBuilder.setSpan(new ForegroundColorSpan(BRACES_COLOR), 0, keyBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        jsonItemBean.key = keyBuilder;
        jsonItemBean.isRightBoundary = true;
    }

    private void handleJsonArray(JsonItemBean parentItem, String key, Object jsonIndex, JSONArray value, int hierarchy, boolean appendComma) {
        if (value == null) return;
        JsonItemBean parent = createItemViewLeftQuotation(parentItem, value, jsonIndex, key, hierarchy, false, value.length(), appendComma);
        for (int i = 0; i < value.length(); i++) {
            Object valueObject = value.opt(i);
            handleValue(parent, value, i, hierarchy, null, valueObject, i < value.length() - 1);
        }
        createItemViewRightQuotation(parent, hierarchy, false, appendComma);
    }

    private void handleJsonObject(JsonItemBean parentItem, String key, Object jsonIndex, JSONObject value, int hierarchy, boolean appendComma) {
        if (value == null) return;
        JsonItemBean parent = createItemViewLeftQuotation(parentItem, value, jsonIndex, key, hierarchy, true, 0, appendComma);
        if (value.names() != null) {
            for (int i = 0; i < value.names().length(); i++) {
                String keyValue = value.names().optString(i);
                Object valueObject = value.opt(keyValue);
                handleValue(parent, value, keyValue, hierarchy, keyValue, valueObject, i < value.names().length() - 1);
            }
        }
        createItemViewRightQuotation(parent, hierarchy, true, appendComma);
    }

    private void handleValue(JsonItemBean parent, Object jsonValue, Object jsonIndex, int hierarchy, String keyValue, Object valueObject, boolean appendComma) {
        JsonItemBean jsonItemBean = createJsonItemBean(parent, hierarchy);
        SpannableStringBuilder valueBuilder = new SpannableStringBuilder();
        if (valueObject instanceof Number) {
            valueBuilder.append(valueObject.toString());
            valueBuilder.setSpan(new ForegroundColorSpan(NUMBER_COLOR), 0, valueBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (valueObject instanceof Boolean) {
            valueBuilder.append(valueObject.toString());
            valueBuilder.setSpan(new ForegroundColorSpan(BOOLEAN_COLOR), 0, valueBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (valueObject instanceof JSONObject) {
            jsonItemBeans.remove(jsonItemBean);
            JSONObject jsonObject = (JSONObject) valueObject;
            handleJsonObject(parent, keyValue, jsonIndex, jsonObject, ++hierarchy, appendComma);
        } else if (valueObject instanceof JSONArray) {
            jsonItemBeans.remove(jsonItemBean);
            JSONArray jsonArray = (JSONArray) valueObject;
            handleJsonArray(parent, keyValue, jsonIndex, jsonArray, ++hierarchy, appendComma);
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
        jsonItemBean.hasComma = appendComma;
        jsonItemBean.value = valueBuilder;
        jsonItemBean.jsonValue = jsonValue;
        jsonItemBean.jsonIndex = jsonIndex;
        SpannableStringBuilder keyBuilder = new SpannableStringBuilder();
        keyBuilder.append(getHierarchyStr(++hierarchy) + (TextUtils.isEmpty(keyValue) ? "" : "\"" + keyValue + "\"" + ":"));
        keyBuilder.setSpan(new ForegroundColorSpan(KEY_COLOR), 0, keyBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        jsonItemBean.key = keyBuilder;
    }

    private JsonItemBean createJsonItemBean(JsonItemBean parent, int hierarchy, boolean isNode) {
        JsonItemBean jsonItemBean = new JsonItemBean(isNode, parent, hierarchy);
        jsonItemBeans.add(jsonItemBean);
        return jsonItemBean;
    }

    private JsonItemBean createJsonItemBean(JsonItemBean parent, int hierarchy) {
        return createJsonItemBean(parent, hierarchy, false);
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
            if (jsonItemBean.isNode && jsonItemBean.isFolded) {
                holder.tvLeft.setText(jsonItemBean.collapsedNodeText);
            } else {
                holder.tvLeft.setText(jsonItemBean.key);
            }
        }
        if (jsonItemBean.value != null) {
            holder.tvRight.setText(jsonItemBean.value);
        }
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jsonItemBean.isNode) {
                    jsonItemBean.isFolded = !jsonItemBean.isFolded;
                    expandOrCollapseJsonItem(jsonItemBean.isFolded, jsonItemBean);
                }
            }
        };
        View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (jsonModifyCallback != null) {
                    jsonModifyCallback.modify(jsonItemBean);
                }
                return true;
            }
        };
        holder.tvLeft.setOnClickListener(onClickListener);
        holder.tvRight.setOnClickListener(onClickListener);
        holder.itemView.setOnClickListener(onClickListener);
        holder.tvLeft.setOnLongClickListener(onLongClickListener);
        holder.tvRight.setOnLongClickListener(onLongClickListener);
        holder.itemView.setOnLongClickListener(onLongClickListener);
    }

    public static JSONArray remove(final int idx, final JSONArray from) {
        final List<JSONObject> objs = asList(from);
        objs.remove(idx);

        final JSONArray ja = new JSONArray();
        for (final JSONObject obj : objs) {
            ja.put(obj);
        }

        return ja;
    }

    public static List<JSONObject> asList(final JSONArray ja) {
        final int len = ja.length();
        final ArrayList<JSONObject> result = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final JSONObject obj = ja.optJSONObject(i);
            if (obj != null) {
                result.add(obj);
            }
        }
        return result;
    }

    //delete async
    public void deleteJsonItem(final JsonItemBean jsonItemBean) {
        new Thread() {
            @Override
            public void run() {
                deleteJsonItems(jsonItemBean);
                for (int i = jsonItemBeans.size() - 1; i >= 0; i--) {
                    JsonItemBean itemBean = jsonItemBeans.get(i);
                    if (itemBean.deleteFlag) {
                        jsonItemBeans.remove(itemBean);
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });

            }
        }.start();

    }

    //change async
    public void changeJsonItem(final JsonItemBean jsonItemBean, final Object valueObject) {
        new Thread() {
            @Override
            public void run() {
                if (changeJsonValue(jsonItemBean, valueObject)) {
                    jsonItemBean.value = createValueSpannableStringBuilder(jsonItemBean, valueObject);
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        }.start();
    }

    private void deleteJsonItems(JsonItemBean jsonItemBean) {
        if (!jsonItemBean.isNode) {
            if (!jsonItemBean.isRightBoundary) {
                if (jsonItemBean.jsonValue instanceof JSONObject) {
                    ((JSONObject) jsonItemBean.jsonValue).remove((String) jsonItemBean.jsonIndex);
                } else {
                    remove((int) jsonItemBean.jsonIndex, (JSONArray) jsonItemBean.jsonValue);
                }
            }
            jsonItemBean.deleteFlag = true;
        } else {
            Object parentObject = jsonItemBean.parent.jsonValue;
            if (parentObject instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) parentObject;
                (jsonObject).remove((String) jsonItemBean.jsonIndex);
            } else {
                remove((int) jsonItemBean.jsonIndex, (JSONArray) parentObject);
            }
            jsonItemBean.deleteFlag = true;
            for (JsonItemBean itemBean : jsonItemBeans) {
                if (itemBean.parent == jsonItemBean) {
                    deleteJsonItems(itemBean);
                }
            }
        }
    }

    private boolean changeJsonValue(JsonItemBean jsonItemBean, Object valueObject) {
        boolean isSyntaxCorrect = true;
        if (jsonItemBean.jsonValue instanceof JSONObject) {
            try {
                ((JSONObject) jsonItemBean.jsonValue).put((String) jsonItemBean.jsonIndex, valueObject);
            } catch (JSONException e) {
                isSyntaxCorrect = false;
            }
        } else if (jsonItemBean.jsonValue instanceof JSONArray) {
            try {
                ((JSONArray) jsonItemBean.jsonValue).put((int) jsonItemBean.jsonIndex, valueObject);
            } catch (JSONException e) {
                isSyntaxCorrect = false;
            }
        }
        return isSyntaxCorrect;
    }

    @NonNull
    private SpannableStringBuilder createValueSpannableStringBuilder(JsonItemBean jsonItemBean, Object valueObject) {
        SpannableStringBuilder valueBuilder = new SpannableStringBuilder();
        if (valueObject instanceof Number) {
            valueBuilder.append(valueObject.toString());
            valueBuilder.setSpan(new ForegroundColorSpan(NUMBER_COLOR), 0, valueBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (valueObject instanceof Boolean) {
            valueBuilder.append(valueObject.toString());
            valueBuilder.setSpan(new ForegroundColorSpan(BOOLEAN_COLOR), 0, valueBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
        if (jsonItemBean.hasComma) {
            valueBuilder.append(",");
        }
        return valueBuilder;
    }

    private JsonModifyCallback jsonModifyCallback = null;

    public void setJsonModifyCallback(JsonModifyCallback jsonModifyCallback) {
        this.jsonModifyCallback = jsonModifyCallback;
    }

    public interface JsonModifyCallback {
        void modify(JsonItemBean jsonItemBean);
    }


    private void expandOrCollapseJsonItem(boolean collapse, JsonItemBean jsonItemBean) {
        for (JsonItemBean itemBean : jsonItemBeans) {
            if (!collapse) {
                if (itemBean.parent == jsonItemBean) {
                    itemBean.collapse = false;
                    if (itemBean.isNode) {
                        itemBean.isFolded = true;
                    }
                }
            } else if (!itemBean.collapse) {
                if (itemBean.parent == jsonItemBean) {
                    itemBean.collapse = true;
                    if (itemBean.isNode) {
                        itemBean.isFolded = false;
                        expandOrCollapseJsonItem(true, itemBean);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public void expandAllJsonItems() {
        expandOrCollapseJsonItems(false, jsonItemBeans.get(0));
    }

    public void collapseAllJsonItems() {
        for (JsonItemBean jsonItemBean : jsonItemBeans) {
            if (jsonItemBean.hierarchy == 0 || (jsonItemBean.hierarchy == 1 && jsonItemBean.isNode)) {
                //保持第一层和第二层的Node的显示
                jsonItemBean.collapse = false;
                viewJsonItemBeans.add(jsonItemBean);
            } else {
                //reset state.
                jsonItemBean.collapse = true;
            }
            if (jsonItemBean.isNode) {
                jsonItemBean.isFolded = true;
            }
        }
        notifyDataSetChanged();
    }

    private void expandOrCollapseJsonItems(boolean collapse, JsonItemBean jsonItemBean) {
        for (JsonItemBean itemBean : jsonItemBeans) {
            if (itemBean.parent == jsonItemBean) {
                itemBean.collapse = collapse;
                if (itemBean.isNode) {
                    itemBean.isFolded = false;
                    expandOrCollapseJsonItems(collapse, itemBean);
                }
            }
        }
        notifyDataSetChanged();
    }

    public String generateJson() {
        if (rootJSONArray != null) {
            return rootJSONArray.toString();
        } else if (rootJSONObject != null) {
            return rootJSONObject.toString();
        }
        return "";
    }


    @Override
    public int getItemCount() {
        return calcViewJsonItemBeanSize();
    }

    private int calcViewJsonItemBeanSize() {
        initViewJsonItems();
        changeViewJsonItems();
        return viewJsonItemBeans.size();
    }

    private void changeViewJsonItems() {
        if (hasJsonItemsChanged) {
            hasJsonItemsChanged = false;
            viewJsonItemBeans.clear();
            for (JsonItemBean jsonItemBean : jsonItemBeans) {
                if (!jsonItemBean.collapse) {
                    viewJsonItemBeans.add(jsonItemBean);
                }
            }
        }
    }

    //First show 显示第一层和第二层的Node.*/
    //for example:*/
    //{                         ==> hierarchy = 0*/
    //  "code":200              ==> hierarchy = 0*/
    //  "msg":"获取数据成功"      ==> hierarchy = 0*/
    //  "data":Object{...}      ==> hierarchy = 1 && isNode节点*/
    //}                         ==> hierarchy = 0*/
    private void initViewJsonItems() {
        if (viewJsonItemBeans == null) {
            viewJsonItemBeans = new ArrayList<>();
            for (JsonItemBean jsonItemBean : jsonItemBeans) {
                if (jsonItemBean.hierarchy == 0 || (jsonItemBean.hierarchy == 1 && jsonItemBean.isNode)) {
                    jsonItemBean.collapse = false;
                    viewJsonItemBeans.add(jsonItemBean);
                }
            }
        }
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
