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
        JsonItemBean jsonItemBean = createJsonItemBean(parent, hierarchy, true);
        String itemKeyLeftSpace = getHierarchyStr(hierarchy);
        String itemKey = itemKeyLeftSpace + (TextUtils.isEmpty(key) ? "" : "\"" + key + "\"");
        jsonItemBean.isObjectOrArray = isJsonObject;
        jsonItemBean.parentJsonObject = jsonValue;
        jsonItemBean.curJsonItemKey = jsonIndex;
        jsonItemBean.key = itemKey;
        jsonItemBean.hasComma = appendComma;
        jsonItemBean.isLeftBoundary = true;
        jsonItemBean.collapsedNodeText = itemKey + (TextUtils.isEmpty(key) ? "" : ":") + (isJsonObject ? "Object{...}" : "Array[" + arraySize + "]");
        jsonItemBean.collapsedNodeKeyIndex = itemKey.length();
        return jsonItemBean;

    }

    private JsonItemBean createItemViewRightQuotation(JsonItemBean parent, int hierarchy, boolean isJsonObject, boolean appendComma) {
        JsonItemBean jsonItemBean = createJsonItemBean(parent, hierarchy);
        jsonItemBean.isObjectOrArray = isJsonObject;
        jsonItemBean.key = getHierarchyStr(hierarchy) + (isJsonObject ? "}" : "]");
        jsonItemBean.hasComma = appendComma;
        jsonItemBean.isRightBoundary = true;
        return jsonItemBean;
    }

    private SpannableStringBuilder createItemViewRightQuotationSpannableStringBuilder(JsonItemBean jsonItemBean) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if (jsonItemBean.isNode) {
            if (jsonItemBean.isFolded) {
                spannableStringBuilder.append(jsonItemBean.collapsedNodeText + (jsonItemBean.hasComma ? "," : ""));
                spannableStringBuilder.setSpan(new ForegroundColorSpan(KEY_COLOR), 0, jsonItemBean.collapsedNodeKeyIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(BRACES_COLOR), jsonItemBean.collapsedNodeKeyIndex, spannableStringBuilder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            } else {
                String mark = jsonItemBean.isObjectOrArray ? "{" : "[";
                spannableStringBuilder.append(jsonItemBean.key + (TextUtils.isEmpty(jsonItemBean.key.trim()) ? "" : ":") + mark);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(KEY_COLOR), 0, jsonItemBean.key.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(BRACES_COLOR), jsonItemBean.key.length(), spannableStringBuilder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        } else {
            if (jsonItemBean.isRightBoundary) {
                spannableStringBuilder.append(jsonItemBean.key + (jsonItemBean.hasComma ? "," : ""));
                spannableStringBuilder.setSpan(new ForegroundColorSpan(BRACES_COLOR), 0, spannableStringBuilder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            } else {
                spannableStringBuilder.append(jsonItemBean.key + ":");
                spannableStringBuilder.setSpan(new ForegroundColorSpan(KEY_COLOR), 0, jsonItemBean.key.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(BRACES_COLOR), jsonItemBean.key.length(), spannableStringBuilder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableStringBuilder;
    }

    private void handleJsonArray(JsonItemBean parentItem, String key, Object jsonIndex, JSONArray value, int hierarchy, boolean appendComma) {
        if (value == null) return;
        JsonItemBean parent = createItemViewLeftQuotation(parentItem, value, jsonIndex, key, hierarchy, false, value.length(), appendComma);
        for (int i = 0; i < value.length(); i++) {
            Object valueObject = value.opt(i);
            handleValue(parent, value, i, hierarchy, null, valueObject, i < value.length() - 1);
        }
        parent.rightBoundaryItem = createItemViewRightQuotation(parent, hierarchy, false, appendComma);
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
        parent.rightBoundaryItem = createItemViewRightQuotation(parent, hierarchy, true, appendComma);
    }

    private void handleValue(JsonItemBean parent, Object jsonValue, Object jsonIndex, int hierarchy, String keyValue, Object valueObject, boolean appendComma) {
        if (valueObject instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) valueObject;
            handleJsonObject(parent, keyValue, jsonIndex, jsonObject, ++hierarchy, appendComma);
        } else if (valueObject instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) valueObject;
            handleJsonArray(parent, keyValue, jsonIndex, jsonArray, ++hierarchy, appendComma);
        } else {
            JsonItemBean jsonItemBean = createJsonItemBean(parent, hierarchy);
            jsonItemBean.hasComma = appendComma;
            jsonItemBean.value = valueObject;
            jsonItemBean.parentJsonObject = jsonValue;
            jsonItemBean.curJsonItemKey = jsonIndex;
            jsonItemBean.key = getHierarchyStr(++hierarchy) + (TextUtils.isEmpty(keyValue) ? "" : "\"" + keyValue + "\"");
        }
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
        holder.tvRight.setVisibility(jsonItemBean.value == null && (jsonItemBean.isNode || jsonItemBean.isRightBoundary) ? View.GONE : View.VISIBLE);
        holder.tvLeft.setOnClickListener(null);
        holder.tvRight.setOnClickListener(null);
        holder.tvLeft.setText(createItemViewRightQuotationSpannableStringBuilder(jsonItemBean));
        holder.tvRight.setText(createValueSpannableStringBuilder(jsonItemBean));
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (jsonItemBean.isNode && jsonItemBean.hierarchy > 0) {//不折叠root节点
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
        holder.tvLeft.setOnLongClickListener(onLongClickListener);
        holder.tvRight.setOnLongClickListener(onLongClickListener);
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
                removeViewJsonItemComma(jsonItemBean);
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

    private void removeViewJsonItemComma(JsonItemBean jsonItemBean) {
        //1.删除折叠的","
        int viewLeftNodeIndex = viewJsonItemBeans.indexOf(jsonItemBean) - 1;
        if (viewLeftNodeIndex >= 0) {
            JsonItemBean viewLeftNode = viewJsonItemBeans.get(viewLeftNodeIndex);
            if (viewLeftNode != null) {
                if (!viewLeftNode.isNode) {
                    if (!jsonItemBean.hasComma) {
                        viewLeftNode.hasComma = false;
                        if (viewLeftNode.isRightBoundary) {
                            viewLeftNode.parent.hasComma = false;
                        }
                    }
                } else {
                    if (viewLeftNode.isFolded) {
                        if (!jsonItemBean.hasComma) {
                            viewLeftNode.hasComma = false;
                            viewLeftNode.rightBoundaryItem.hasComma = false;
                        }
                    }
                }
            }
        }
    }

    //change async
    public void changeJsonItem(final JsonItemBean jsonItemBean, final Object valueObject) {
        new Thread() {
            @Override
            public void run() {
                if (changeJsonValue(jsonItemBean, valueObject)) {
                    jsonItemBean.value = valueObject;
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
                if (jsonItemBean.parentJsonObject instanceof JSONObject) {
                    ((JSONObject) jsonItemBean.parentJsonObject).remove((String) jsonItemBean.curJsonItemKey);
                } else {
                    remove((int) jsonItemBean.curJsonItemKey, (JSONArray) jsonItemBean.parentJsonObject);
                }
            }
            jsonItemBean.deleteFlag = true;
        } else {
            if (jsonItemBean.parent != null) {//avoid delete root "{"
                Object parentObject = jsonItemBean.parent.parentJsonObject;
                if (parentObject instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) parentObject;
                    (jsonObject).remove((String) jsonItemBean.curJsonItemKey);
                } else {
                    remove((int) jsonItemBean.curJsonItemKey, (JSONArray) parentObject);
                }
                jsonItemBean.deleteFlag = true;
                for (JsonItemBean itemBean : jsonItemBeans) {
                    if (itemBean.parent == jsonItemBean) {
                        deleteJsonItems(itemBean);
                    }
                }
            }
        }
    }

    private boolean changeJsonValue(JsonItemBean jsonItemBean, Object valueObject) {
        boolean isSyntaxCorrect = true;
        if (jsonItemBean.parentJsonObject instanceof JSONObject) {
            try {
                ((JSONObject) jsonItemBean.parentJsonObject).put((String) jsonItemBean.curJsonItemKey, valueObject);
            } catch (JSONException e) {
                isSyntaxCorrect = false;
            }
        } else if (jsonItemBean.parentJsonObject instanceof JSONArray) {
            try {
                ((JSONArray) jsonItemBean.parentJsonObject).put((int) jsonItemBean.curJsonItemKey, valueObject);
            } catch (JSONException e) {
                isSyntaxCorrect = false;
            }
        }
        return isSyntaxCorrect;
    }

    @NonNull
    private SpannableStringBuilder createValueSpannableStringBuilder(JsonItemBean jsonItemBean) {
        SpannableStringBuilder valueBuilder = new SpannableStringBuilder();
        if (jsonItemBean.value instanceof Number) {
            valueBuilder.append(jsonItemBean.value.toString());
            valueBuilder.setSpan(new ForegroundColorSpan(NUMBER_COLOR), 0, valueBuilder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } else if (jsonItemBean.value instanceof Boolean) {
            valueBuilder.append(jsonItemBean.value.toString());
            valueBuilder.setSpan(new ForegroundColorSpan(BOOLEAN_COLOR), 0, valueBuilder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } else if (jsonItemBean.value instanceof String) {
            valueBuilder.append("\"").append(jsonItemBean.value.toString()).append("\"");
            valueBuilder.setSpan(new ForegroundColorSpan(TEXT_COLOR), 0, valueBuilder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } else if (jsonItemBean.value == null) {
            valueBuilder.append("null");
            valueBuilder.setSpan(new ForegroundColorSpan(NULL_COLOR), 0, valueBuilder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } else { //JSONObject$1 内部类[null,etc]
            valueBuilder.append(jsonItemBean.value.toString());
            valueBuilder.setSpan(new ForegroundColorSpan(NULL_COLOR), 0, valueBuilder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
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
                jsonItemBean.collapse = false;
                viewJsonItemBeans.add(jsonItemBean);
            } else {
                //reset state.
                jsonItemBean.collapse = true;
            }
            if (jsonItemBean.isNode && jsonItemBean.hierarchy > 0) {
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

    //string, boolean, string, [not null]
    public void appendJsonValue(JsonItemBean jsonItemBean, String key, Object value) {
        if (jsonItemBean.isNode && jsonItemBean.parentJsonObject != null && jsonItemBean.parentJsonObject instanceof JSONObject && value != null) {
            JsonItemBean appJsonItemBean = new JsonItemBean(false, jsonItemBean, jsonItemBean.hierarchy);
            appJsonItemBean.key = getHierarchyStr(appJsonItemBean.hierarchy + 1) + "\"" + key + "\"";
            appJsonItemBean.value = value;
            appJsonItemBean.curJsonItemKey = key;
            appJsonItemBean.isFolded = false;
            appJsonItemBean.parentJsonObject = jsonItemBean.parentJsonObject;
            try {
                ((JSONObject) appJsonItemBean.parentJsonObject).putOpt(key, value);
            } catch (JSONException e) {
                System.out.println("append error!");
                return;
            }
            List<JsonItemBean> jsonObjectChildRen = findJsonObjectChildren(jsonItemBean);
            if (checkAppendJsonValueIsIllegal(jsonObjectChildRen, key)) {
                if (jsonObjectChildRen.size() == 0) {
                    int index = jsonItemBeans.indexOf(jsonItemBean);
                    appJsonItemBean.hasComma = false;
                    jsonItemBeans.add(index + 1, appJsonItemBean);
                } else {
                    for (JsonItemBean child : jsonObjectChildRen) {
                        child.collapse = false;
                    }
                    appJsonItemBean.collapse = false;
                    if (jsonItemBean.isFolded) {
                        jsonItemBean.isFolded = false;
                    }
                    JsonItemBean jsonObjectLastChild = jsonObjectChildRen.get(0);
                    jsonObjectLastChild.hasComma = true;
                    appJsonItemBean.hasComma = false;
                    int index;
                    if (jsonObjectLastChild.isNode) {
                        jsonObjectLastChild.rightBoundaryItem.hasComma = true;
                        index = jsonItemBeans.indexOf(jsonObjectLastChild.rightBoundaryItem);
                    } else {
                        index = jsonItemBeans.indexOf(jsonObjectLastChild);
                    }
                    jsonItemBeans.add(index + 1, appJsonItemBean);
                }
                notifyDataSetChanged();
            } else {
                System.out.println("key 重复 =================");
            }
        }
    }

    private boolean checkAppendJsonValueIsIllegal(List<JsonItemBean> jsonObjectChildRen, String key) {
        for (JsonItemBean jsonItemBean : jsonObjectChildRen) {
            if (jsonItemBean.curJsonItemKey.equals(key)) {
                return false;
            }
        }
        return true;
    }

    private List<JsonItemBean> findJsonObjectChildren(JsonItemBean jsonItemBean) {
        List<JsonItemBean> children = new ArrayList<>();
        for (int i = jsonItemBeans.size() - 1; i >= 0; i--) {
            JsonItemBean itemBean = jsonItemBeans.get(i);
            if (itemBean.parent == jsonItemBean && !itemBean.isRightBoundary) {
                children.add(itemBean);
            }
        }
        return children;
    }

    @Override
    public int getItemCount() {
        return calcViewJsonItemBeanSize();
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
                    if (jsonItemBean.hierarchy == 0) {
                        jsonItemBean.isFolded = false;
                    }
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
