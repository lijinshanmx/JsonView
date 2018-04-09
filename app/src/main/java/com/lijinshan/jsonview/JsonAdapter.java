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

    private Handler handler = new Handler();
    //根节点 Object 或者 Array
    private JSONObject rootJSONObject;
    private JSONArray rootJSONArray;
    //所有的JsonItems
    private List<JsonItemBean> jsonItemBeans;

    //可见的JsonItems
    private List<JsonItemBean> viewJsonItemBeans;
    //json 操作回调接口
    private JsonOperationCallback jsonOperationCallback = null;
    //用于notify后标识数据发生改变
    private boolean hasJsonItemsChanged = false;

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
        handleJsonObject(null, null, null, null, jsonObject, 0, false);
    }

    private void handleRootJsonArray(JSONArray jsonArray) {
        handleJsonArray(null, null, null, null, jsonArray, 0, false);
    }

    private JsonItemBean createItemViewLeftQuotation(JsonItemBean parent, Object jsonValue, Object jsonIndex, String key, int hierarchy, boolean isJsonObject, int arraySize, boolean appendComma) {
        JsonItemBean jsonItemBean = createJsonItemBean(parent, hierarchy, true);
        String itemKeyLeftSpace = JsonHelper.getHierarchyStr(hierarchy);
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
        jsonItemBean.key = JsonHelper.getHierarchyStr(hierarchy) + (isJsonObject ? "}" : "]");
        jsonItemBean.hasComma = appendComma;
        jsonItemBean.isRightBoundary = true;
        return jsonItemBean;
    }

    private SpannableStringBuilder createItemViewJsonKeySpannableStringBuilder(JsonItemBean jsonItemBean) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if (jsonItemBean.isNode) {
            if (jsonItemBean.isFolded) {
                spannableStringBuilder.append(jsonItemBean.collapsedNodeText + (jsonItemBean.hasComma ? "," : ""));
                spannableStringBuilder.setSpan(new ForegroundColorSpan(JsonHelper.KEY_COLOR), 0, jsonItemBean.collapsedNodeKeyIndex, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(JsonHelper.BRACES_COLOR), jsonItemBean.collapsedNodeKeyIndex, spannableStringBuilder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            } else {
                String mark = jsonItemBean.isObjectOrArray ? "{" : "[";
                spannableStringBuilder.append(jsonItemBean.key + (TextUtils.isEmpty(jsonItemBean.key.trim()) ? "" : ":") + mark);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(JsonHelper.KEY_COLOR), 0, jsonItemBean.key.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(JsonHelper.BRACES_COLOR), jsonItemBean.key.length(), spannableStringBuilder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        } else {
            if (jsonItemBean.isRightBoundary) {
                spannableStringBuilder.append(jsonItemBean.key + (jsonItemBean.hasComma ? "," : ""));
                spannableStringBuilder.setSpan(new ForegroundColorSpan(JsonHelper.BRACES_COLOR), 0, spannableStringBuilder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            } else {
                spannableStringBuilder.append((TextUtils.isEmpty(jsonItemBean.key.trim())) ? jsonItemBean.key : jsonItemBean.key + ":");
                spannableStringBuilder.setSpan(new ForegroundColorSpan(JsonHelper.KEY_COLOR), 0, jsonItemBean.key.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.setSpan(new ForegroundColorSpan(JsonHelper.BRACES_COLOR), jsonItemBean.key.length(), spannableStringBuilder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
        if (JsonHelper.isDebug && (jsonItemBean.isNode || jsonItemBean.isRightBoundary)) {
            spannableStringBuilder.append(" ->" + jsonItemBean.hierarchy);
            spannableStringBuilder.setSpan(new ForegroundColorSpan(JsonHelper.DEBUG_COLOR), spannableStringBuilder.length() - 3, spannableStringBuilder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return spannableStringBuilder;
    }

    @NonNull
    private SpannableStringBuilder createItemViewJsonValueSpannableStringBuilder(JsonItemBean jsonItemBean) {
        SpannableStringBuilder valueBuilder = new SpannableStringBuilder();
        if (jsonItemBean.value instanceof Number) {
            valueBuilder.append(jsonItemBean.value.toString());
            valueBuilder.setSpan(new ForegroundColorSpan(JsonHelper.NUMBER_COLOR), 0, valueBuilder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } else if (jsonItemBean.value instanceof Boolean) {
            valueBuilder.append(jsonItemBean.value.toString());
            valueBuilder.setSpan(new ForegroundColorSpan(JsonHelper.BOOLEAN_COLOR), 0, valueBuilder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } else if (jsonItemBean.value instanceof String) {
            valueBuilder.append("\"").append(jsonItemBean.value.toString()).append("\"");
            valueBuilder.setSpan(new ForegroundColorSpan(JsonHelper.TEXT_COLOR), 0, valueBuilder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } else if (jsonItemBean.value == null) {
            valueBuilder.append("null");
            valueBuilder.setSpan(new ForegroundColorSpan(JsonHelper.NULL_COLOR), 0, valueBuilder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } else { //JSONObject$1 内部类[null,etc]
            valueBuilder.append(jsonItemBean.value.toString());
            valueBuilder.setSpan(new ForegroundColorSpan(JsonHelper.NULL_COLOR), 0, valueBuilder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        if (jsonItemBean.hasComma) {
            valueBuilder.append(",");
            valueBuilder.setSpan(new ForegroundColorSpan(JsonHelper.BRACES_COLOR), valueBuilder.length() - 1, valueBuilder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        if (JsonHelper.isDebug) {
            valueBuilder.append(" ->" + jsonItemBean.hierarchy);
            valueBuilder.setSpan(new ForegroundColorSpan(JsonHelper.DEBUG_COLOR), valueBuilder.length() - 3, valueBuilder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return valueBuilder;
    }

    private void handleJsonArray(JsonItemBean parentItem, String key, Object parentJsonObject, Object jsonIndex, JSONArray value, int hierarchy, boolean appendComma) {
        if (value == null) return;
        JsonItemBean parent = createItemViewLeftQuotation(parentItem, parentJsonObject, jsonIndex, key, hierarchy, false, value.length(), appendComma);
        for (int i = 0; i < value.length(); i++) {
            Object valueObject = value.opt(i);
            handleValue(parent, value, i, hierarchy, null, valueObject, i < value.length() - 1);
        }
        parent.rightBoundaryItem = createItemViewRightQuotation(parent, hierarchy, false, appendComma);
    }

    private void handleJsonObject(JsonItemBean parentItem, String key, Object parentJsonObject, Object jsonIndex, JSONObject value, int hierarchy, boolean appendComma) {
        if (value == null) return;
        JsonItemBean parent = createItemViewLeftQuotation(parentItem, parentJsonObject, jsonIndex, key, hierarchy, true, 0, appendComma);
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
            handleJsonObject(parent, keyValue, jsonValue, jsonIndex, jsonObject, ++hierarchy, appendComma);
        } else if (valueObject instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) valueObject;
            handleJsonArray(parent, keyValue, jsonValue, jsonIndex, jsonArray, ++hierarchy, appendComma);
        } else {
            JsonItemBean jsonItemBean = createJsonItemBean(parent, hierarchy);
            jsonItemBean.hasComma = appendComma;
            jsonItemBean.value = valueObject;
            jsonItemBean.parentJsonObject = jsonValue;
            jsonItemBean.curJsonItemKey = jsonIndex;
            jsonItemBean.key = JsonHelper.getHierarchyStr(++hierarchy) + (TextUtils.isEmpty(keyValue) ? "" : "\"" + keyValue + "\"");
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

    //更新Array索引
    private void updateJsonArrayIndexAndIndex(JsonItemBean jsonItemBean) {
        if (jsonItemBean.parent != null) {
            if (jsonItemBean.parent.isNode && !jsonItemBean.parent.isObjectOrArray) {
                List<JsonItemBean> itemBeans = findJsonObjectChildren(jsonItemBean.parent);
                for (int i = itemBeans.size() - 1; i >= 0; i--) {
                    JsonItemBean itemBean = itemBeans.get(i);
                    if (itemBean != null) {
                        itemBean.curJsonItemKey = itemBeans.size() - 1 - i;
                    }
                }
                int arraySize = ((JSONArray) jsonItemBean.parentJsonObject).length();
                jsonItemBean.parent.collapsedNodeText = jsonItemBean.parent.key + ":Array[" + arraySize + "]";
            }
        }
    }

    private void removeDeleteFlagJsonItems() {
        for (int i = jsonItemBeans.size() - 1; i >= 0; i--) {
            JsonItemBean itemBean = jsonItemBeans.get(i);
            if (itemBean.deleteFlag) {
                jsonItemBeans.remove(itemBean);
            }
        }
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

    //delete async
    public void deleteJsonItem(final JsonItemBean jsonItemBean) {
        new Thread() {
            @Override
            public void run() {
                //标记delete
                deleteJsonItems(jsonItemBean, true);
                //删除多余的逗号
                removeViewJsonItemComma(jsonItemBean);
                //删除标记
                removeDeleteFlagJsonItems();
                updateJsonArrayIndexAndIndex(jsonItemBean);
                //更新UI
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });

            }
        }.start();

    }

    private void deleteJsonItems(JsonItemBean jsonItemBean, boolean updateJsonData) {
        if (!jsonItemBean.isNode) {
            if (!jsonItemBean.isRightBoundary) {
                if (updateJsonData) { // 不是node节点才需要更新节点信息
                    if (jsonItemBean.parentJsonObject instanceof JSONObject) {
                        ((JSONObject) jsonItemBean.parentJsonObject).remove((String) jsonItemBean.curJsonItemKey);
                    } else {
                        //需要parent.parent因为自身是一个子节点
                        JSONArray jsonArray = JsonHelper.remove((int) jsonItemBean.curJsonItemKey, (JSONArray) ((JSONObject) jsonItemBean.parent.parent.parentJsonObject).opt((String) jsonItemBean.parent.curJsonItemKey));
                        ((JSONObject) jsonItemBean.parent.parent.parentJsonObject).remove((String) jsonItemBean.parent.curJsonItemKey);
                        try {
                            ((JSONObject) jsonItemBean.parent.parent.parentJsonObject).putOpt((String) jsonItemBean.parent.curJsonItemKey, jsonArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        List<JsonItemBean> itemBeans = findJsonObjectChildren(jsonItemBean.parent);
                        for (JsonItemBean itemBean : itemBeans) {
                            itemBean.parentJsonObject = jsonArray;
                        }
                    }
                }
            }
            jsonItemBean.deleteFlag = true;
        } else {
            if (jsonItemBean.parent != null) {
                Object parentObject = jsonItemBean.parentJsonObject;
                if (updateJsonData) {
                    if (parentObject instanceof JSONObject) {
                        JSONObject jsonObject = (JSONObject) parentObject;
                        jsonObject.remove((String) jsonItemBean.curJsonItemKey);
                    } else {
                        JSONArray jsonArray = JsonHelper.remove((int) jsonItemBean.curJsonItemKey, (JSONArray) parentObject);
                        //不需要parent.parent因为自身就是一个节点
                        ((JSONObject) jsonItemBean.parent.parentJsonObject).remove((String) jsonItemBean.parent.curJsonItemKey);
                        try {
                            ((JSONObject) jsonItemBean.parent.parentJsonObject).putOpt((String) jsonItemBean.parent.curJsonItemKey, jsonArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        List<JsonItemBean> itemBeans = findJsonObjectChildren(jsonItemBean.parent);
                        for (JsonItemBean itemBean : itemBeans) {
                            itemBean.parentJsonObject = jsonArray;
                        }
                    }

                }
                jsonItemBean.deleteFlag = true;
                for (JsonItemBean itemBean : jsonItemBeans) {
                    if (itemBean.parent == jsonItemBean) {
                        deleteJsonItems(itemBean, false);
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

    //param:collapse false = 展开 true = 折叠
    private void expandOrCollapseJsonItem(boolean collapse, JsonItemBean jsonItemBean) {
        for (JsonItemBean itemBean : jsonItemBeans) {
            if (!collapse) {
                if (itemBean.parent == jsonItemBean) {
                    itemBean.collapse = false;//不隐藏
                    if (itemBean.isNode) {
                        itemBean.isFolded = true;//子节点折叠
                    }
                }
            } else {
                if (itemBean.parent == jsonItemBean) {
                    itemBean.collapse = true;//非node节点 隐藏
                    if (itemBean.isNode) { // node节点，折叠且显示
                        itemBean.isFolded = true;
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

    public boolean appendJsonValue(JsonItemBean jsonItemBean, String key, Object value) {
        if (appendJsonValueInternal(jsonItemBean, key, value)) {
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    //string, boolean, string, [not null]
    private boolean appendJsonValueInternal(JsonItemBean jsonItemBean, String key, Object value) {
        if (jsonItemBean != null && jsonItemBean.isNode && jsonItemBean.parentJsonObject != null && value != null) {
            boolean isJsonObject = jsonItemBean.parentJsonObject instanceof JSONObject;
            JsonItemBean appJsonItemBean = new JsonItemBean(false, jsonItemBean, jsonItemBean.hierarchy);
            appJsonItemBean.key = JsonHelper.getHierarchyStr(appJsonItemBean.hierarchy + 1) + (isJsonObject ? "\"" + key + "\"" : "");
            appJsonItemBean.value = value;
            if (isJsonObject) {
                appJsonItemBean.curJsonItemKey = key;
            } else {
                appJsonItemBean.curJsonItemKey = ((JSONArray) jsonItemBean.parentJsonObject).length();
                int arraySize = ((JSONArray) jsonItemBean.parentJsonObject).length() + 1;
                jsonItemBean.collapsedNodeText = jsonItemBean.key + ":Array[" + arraySize + "]";
            }
            appJsonItemBean.collapse = false;
            appJsonItemBean.isFolded = false;
            appJsonItemBean.parentJsonObject = jsonItemBean.parentJsonObject;
            try {
                if (isJsonObject) {
                    ((JSONObject) appJsonItemBean.parentJsonObject).putOpt((String) appJsonItemBean.curJsonItemKey, value);
                } else {
                    ((JSONArray) appJsonItemBean.parentJsonObject).put((Integer) appJsonItemBean.curJsonItemKey, value);
                }
            } catch (JSONException e) {
                System.out.println("append error!");
                return false;
            }
            List<JsonItemBean> jsonObjectChildRen = findJsonObjectChildren(jsonItemBean);
            boolean isAppendIllegal = !isJsonObject || checkAppendJsonValueIsIllegal(jsonObjectChildRen, key);
            if (isAppendIllegal) {
                if (jsonObjectChildRen.size() == 0) {
                    int index = jsonItemBeans.indexOf(jsonItemBean);
                    appJsonItemBean.hasComma = false;
                    jsonItemBeans.add(index + 1, appJsonItemBean);
                } else {
                    if (jsonItemBean.isFolded) {
                        appJsonItemBean.collapse = true;
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
                return true;
            } else {
                System.out.println("key 重复 =================");
            }
        }
        return false;
    }

    public void appendJsonObject(JsonItemBean jsonItemBean, String key, JSONObject value) {
        //1. {
        JsonItemBean itemLeftKeyBean = createJsonObjectLeftKeyItem(jsonItemBean, key, value);
        if (itemLeftKeyBean == null) return;
        for (int i = 0; i < value.names().length(); i++) {
            String childKey = value.names().optString(i);
            Object valueChild = value.opt(childKey);
            appendJsonValueInternal(itemLeftKeyBean, childKey, valueChild);
        }
        //遍历 value
        itemLeftKeyBean.rightBoundaryItem = createJsonObjectRightKeyItem(itemLeftKeyBean);
        //2.}
        notifyDataSetChanged();
    }

    private JsonItemBean createJsonObjectRightKeyItem(JsonItemBean jsonItemBean) {
        JsonItemBean appJsonItemBean = new JsonItemBean(false, jsonItemBean, jsonItemBean.hierarchy);
        appJsonItemBean.isObjectOrArray = true;
        appJsonItemBean.key = JsonHelper.getHierarchyStr(jsonItemBean.hierarchy) + (true ? "}" : "]");
        appJsonItemBean.hasComma = false;
        appJsonItemBean.collapse = false;
        appJsonItemBean.isFolded = false;
        appJsonItemBean.isRightBoundary = true;
        List<JsonItemBean> jsonObjectChildRen = findJsonObjectChildren(jsonItemBean);
        if (jsonObjectChildRen.size() == 0) {
            int index = jsonItemBeans.indexOf(jsonItemBean);
            jsonItemBeans.add(index + 1, appJsonItemBean);
        } else {
            JsonItemBean jsonObjectLastChild = jsonObjectChildRen.get(0);
            int index = jsonItemBeans.indexOf(jsonObjectLastChild);
            jsonItemBeans.add(index + 1, appJsonItemBean);
        }
        return appJsonItemBean;
    }

    private JsonItemBean createJsonObjectLeftKeyItem(JsonItemBean jsonItemBean, String key, JSONObject value) {
        if (jsonItemBean != null && jsonItemBean.isNode && jsonItemBean.parentJsonObject != null && jsonItemBean.parentJsonObject instanceof JSONObject && value != null) {
            JsonItemBean appJsonItemBean = new JsonItemBean(true, jsonItemBean, jsonItemBean.hierarchy + 1);
            String itemKeyLeftSpace = JsonHelper.getHierarchyStr(appJsonItemBean.hierarchy);
            String itemKey = itemKeyLeftSpace + (TextUtils.isEmpty(key) ? "" : "\"" + key + "\"");
            appJsonItemBean.isObjectOrArray = true;
            JSONObject jsonObject = new JSONObject();
            try {
                ((JSONObject) jsonItemBean.parentJsonObject).putOpt(key, jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            appJsonItemBean.parentJsonObject = jsonObject;
            appJsonItemBean.curJsonItemKey = key;
            appJsonItemBean.key = itemKey;
            appJsonItemBean.hasComma = false;
            appJsonItemBean.isLeftBoundary = true;
            appJsonItemBean.isFolded = false;
            appJsonItemBean.collapsedNodeText = itemKey + (TextUtils.isEmpty(key) ? "" : ":") + "Object{...}";
            appJsonItemBean.collapsedNodeKeyIndex = itemKey.length();
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
                return appJsonItemBean;
            } else {
                System.out.println("key 重复 =================");
            }
        }
        return null;
    }

    private boolean checkAppendJsonValueIsIllegal(List<JsonItemBean> jsonObjectChildRen, String key) {
        for (JsonItemBean jsonItemBean : jsonObjectChildRen) {
            if (key.equals(jsonItemBean.curJsonItemKey)) {
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

    //==============================================================================================
    //                                  Normal Adapter Entry
    //==============================================================================================

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final JsonItemBean jsonItemBean = viewJsonItemBeans.get(position);
        holder.tvRight.setVisibility(jsonItemBean.value == null && (jsonItemBean.isNode || jsonItemBean.isRightBoundary) ? View.GONE : View.VISIBLE);
        holder.tvLeft.setOnClickListener(null);
        holder.tvRight.setOnClickListener(null);
        holder.tvLeft.setText(createItemViewJsonKeySpannableStringBuilder(jsonItemBean));
        holder.tvRight.setText(createItemViewJsonValueSpannableStringBuilder(jsonItemBean));
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
                if (jsonOperationCallback != null) {
                    jsonOperationCallback.modify(jsonItemBean);
                }
                return true;
            }
        };
        holder.tvLeft.setOnClickListener(onClickListener);
        holder.tvRight.setOnClickListener(onClickListener);
        holder.tvLeft.setOnLongClickListener(onLongClickListener);
        holder.tvRight.setOnLongClickListener(onLongClickListener);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_json_item_view, parent, false));
    }

    @Override
    public int getItemCount() {
        return calcViewJsonItemBeanSize();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvLeft, tvRight;

        public ViewHolder(View itemView) {
            super(itemView);
            tvLeft = itemView.findViewById(R.id.tv_left);
            tvRight = itemView.findViewById(R.id.tv_right);
        }
    }

    public interface JsonOperationCallback {
        void modify(JsonItemBean jsonItemBean);
    }

    public void setJsonOperationCallback(JsonOperationCallback jsonOperationCallback) {
        this.jsonOperationCallback = jsonOperationCallback;
    }
}
