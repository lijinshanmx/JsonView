package com.lijinshan.jsonview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

/**
 * Created by lijinshan on 2018/4/9.
 */

public class JsonAdapter extends RecyclerView.Adapter<JsonAdapter.ViewHolder> implements IJsonOperation {

    private JsonOperationCallback callback;
    private JsonProcessor jsonProcessor;

    public JsonAdapter(String json) {
        jsonProcessor = new JsonProcessor(JsonAdapter.this);
        jsonProcessor.process(json);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final JsonItemBean jsonItemBean = jsonProcessor.getViewJsonItem(position);
        holder.tvRight.setVisibility(jsonItemBean.value == null && (jsonItemBean.isNode || jsonItemBean.isRightBoundary) ? View.GONE : View.VISIBLE);
        holder.tvLeft.setOnClickListener(null);
        holder.tvRight.setOnClickListener(null);
        holder.tvLeft.setText(jsonProcessor.createItemViewJsonKeySpannableStringBuilder(jsonItemBean));
        holder.tvRight.setText(jsonProcessor.createItemViewJsonValueSpannableStringBuilder(jsonItemBean));

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonProcessor.expandOrCollapseJsonItem(jsonItemBean);
            }
        };
        View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (callback != null) {
                    callback.operate(jsonItemBean);
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
        return jsonProcessor.calcViewJsonItemBeanSize();
    }

    @Override
    public void deleteJsonItem(JsonItemBean jsonItemBean) {
        jsonProcessor.deleteJsonItem(jsonItemBean);
    }

    @Override
    public void appendJsonValue(JsonItemBean jsonItemBean, String key, Object value) {
        jsonProcessor.appendJsonValue(jsonItemBean, key, value);
    }

    @Override
    public void appendJsonObject(JsonItemBean jsonItemBean, String key, JSONObject value) {
        jsonProcessor.appendJsonObject(jsonItemBean, key, value);
    }

    @Override
    public void changeJsonItem(JsonItemBean jsonItemBean, Object valueObject) {
        jsonProcessor.changeJsonItem(jsonItemBean, valueObject);
    }

    @Override
    public void collapseAllJsonItems() {
        jsonProcessor.collapseAllJsonItems();
    }

    @Override
    public void expandAllJsonItems() {
        jsonProcessor.expandAllJsonItems();
    }

    @Override
    public String generateJson() {
        return jsonProcessor.generateJson();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvLeft, tvRight;

        ViewHolder(View itemView) {
            super(itemView);
            tvLeft = itemView.findViewById(R.id.tv_left);
            tvRight = itemView.findViewById(R.id.tv_right);
        }
    }

    public void setCallback(JsonOperationCallback callback) {
        this.callback = callback;
    }
}
