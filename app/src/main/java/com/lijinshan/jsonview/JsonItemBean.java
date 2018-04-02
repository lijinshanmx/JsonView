package com.lijinshan.jsonview;

import android.text.SpannableStringBuilder;

/**
 * Created by lijinshan on 2018/3/31.
 */

public class JsonItemBean {
    public SpannableStringBuilder key;
    public SpannableStringBuilder value;
    public boolean isNode;
    public JsonItemBean parent;
    public int hierarchy;
    public boolean collapse = true;
    public boolean isFolded = true;
    public boolean isObjectOrArray;
    public SpannableStringBuilder collapseText;

    public JsonItemBean() {
    }

    public JsonItemBean(JsonItemBean parent, int hierarchy) {
        this.parent = parent;
        this.hierarchy = hierarchy;
    }

    public JsonItemBean(boolean isNode, JsonItemBean parent, int hierarchy) {
        this.isNode = isNode;
        this.parent = parent;
        this.hierarchy = hierarchy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonItemBean that = (JsonItemBean) o;

        if (isNode != that.isNode) return false;
        if (hierarchy != that.hierarchy) return false;
        if (collapse != that.collapse) return false;
        if (isFolded != that.isFolded) return false;
        if (isObjectOrArray != that.isObjectOrArray) return false;
        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        return parent != null ? parent.equals(that.parent) : that.parent == null;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (isNode ? 1 : 0);
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + hierarchy;
        result = 31 * result + (collapse ? 1 : 0);
        result = 31 * result + (isFolded ? 1 : 0);
        result = 31 * result + (isObjectOrArray ? 1 : 0);
        return result;
    }
}
