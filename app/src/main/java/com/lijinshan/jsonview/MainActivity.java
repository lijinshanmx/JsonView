package com.lijinshan.jsonview;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rvJson;
    private JsonAdapter jsonAdapter;
    private Button btnExpand, btnCollapse, btnGenerateJson, btnShowDialog;
    private BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvJson = findViewById(R.id.rvJson);
        btnExpand = findViewById(R.id.btnExpand);
        btnCollapse = findViewById(R.id.btnCollapse);
        btnGenerateJson = findViewById(R.id.btnGenerateJson);
        btnShowDialog = findViewById(R.id.btnShowDialog);
        rvJson.setLayoutManager(new LinearLayoutManager(this));
        String json = "{\"name\":\"lijinshan\",\"age\":25,\"city\":\"beijing\",\"msg\":\"hello json!\"}";
        jsonAdapter = new JsonAdapter(json);
        rvJson.setAdapter(jsonAdapter);
        btnCollapse.setOnClickListener(this);
        btnExpand.setOnClickListener(this);
        btnGenerateJson.setOnClickListener(this);
        btnShowDialog.setOnClickListener(this);
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_modify_layout);
        jsonAdapter.setCallback(new JsonOperationCallback() {
            @Override
            public void operate(JsonItemBean jsonItemBean) {
                if (jsonItemBean.canModify()) {
                    jsonAdapter.changeJsonItem(jsonItemBean, "iam modified");
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnCollapse) {
            jsonAdapter.collapseAllJsonItems();
        } else if (v.getId() == R.id.btnExpand) {
            jsonAdapter.expandAllJsonItems();
        } else if (v.getId() == R.id.btnGenerateJson) {
            System.out.println(jsonAdapter.generateJson());
        } else if (v.getId() == R.id.btnShowDialog) {
            bottomSheetDialog.show();
        }
    }
}
