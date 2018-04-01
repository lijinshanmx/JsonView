package com.lijinshan.jsonview;

/**
 * Created by lijinshan on 2018/3/31.
 */

public class JsonItemBean {
    public CharSequence key;
    public CharSequence value;
    public boolean isNode;
    public JsonItemBean parent;
    public int hierarchy;
    public boolean collapse;
    public boolean isFolded;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JsonItemBean itemBean = (JsonItemBean) o;

        if (isNode != itemBean.isNode) return false;
        if (hierarchy != itemBean.hierarchy) return false;
        if (key != null ? !key.equals(itemBean.key) : itemBean.key != null) return false;
        if (value != null ? !value.equals(itemBean.value) : itemBean.value != null) return false;
        return parent != null ? parent.equals(itemBean.parent) : itemBean.parent == null;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (isNode ? 1 : 0);
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + hierarchy;
        return result;
    }
}
