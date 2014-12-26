package com.cengallut.textualjavademo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Main extends Activity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1,
                new String[] {"Demo 1", "Demo 2", "Demo 3"});

        ListView list = new ListView(this);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        setContentView(list);
    }

    @Override
    public void onItemClick(
            AdapterView<?> parent,
            View           view,
            int            position,
            long           id) {

        switch (position) {
            case 0:
                startActivity(new Intent(this, Demo1.class));
                break;
            case 1:
                startActivity(new Intent(this, Demo2.class));
                break;
            case 2:
                startActivity(new Intent(this, Demo3.class));
                break;
        }

    }
}
