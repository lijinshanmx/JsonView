package com.lijinshan.jsonview;

/**
 * Created by lijinshan on 2018/3/31.
 */

public class JsonItemBean {
    public String key;
    public Object value;
    public Object parentJsonObject;
    public Object curJsonItemKey;
    public boolean isNode;
    public JsonItemBean parent;
    public JsonItemBean rightBoundaryItem;
    public int hierarchy;
    public boolean collapse = true;
    public boolean isFolded = true;
    public String collapsedNodeText;
    public int collapsedNodeKeyIndex = 0;
    public boolean hasComma;
    public boolean isObjectOrArray;
    public boolean isRightBoundary;
    public boolean isLeftBoundary;
    public boolean deleteFlag;

    public boolean canModify() {
        return (!isNode && !isRightBoundary);
    }

    public boolean canDelete() {
        return !isRightBoundary;
    }

    public JsonItemBean(boolean isNode, JsonItemBean parent, int hierarchy) {
        this.isNode = isNode;
        this.parent = parent;
        this.hierarchy = hierarchy;
    }

    @Override
    public String toString() {
        return "JsonItemBean{" +
                "key='" + key + '\'' +
                ", value=" + value +
                ", isNode=" + isNode +
                ", hierarchy=" + hierarchy +
                ", collapse=" + collapse +
                ", isFolded=" + isFolded +
                ", collapsedNodeText='" + collapsedNodeText + '\'' +
                ", collapsedNodeKeyIndex=" + collapsedNodeKeyIndex +
                ", hasComma=" + hasComma +
                ", isObjectOrArray=" + isObjectOrArray +
                ", isRightBoundary=" + isRightBoundary +
                ", isLeftBoundary=" + isLeftBoundary +
                ", deleteFlag=" + deleteFlag +
                '}';
    }
}
