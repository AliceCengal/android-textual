package com.cengallut.textualjavademo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.cengallut.textual.GridTouchListener;
import com.cengallut.textual.TextGrid;

public class Main extends Activity implements GridTouchListener.Interface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TextGrid grid = new TextGrid(this) {
            @Override
            public int textColor() {
                return Color.WHITE;
            }

            @Override
            public int background() {
                return Color.BLACK;
            }

            @Override
            public float textSize() {
                return 30f;
            }
        };

        grid.setOnTouchListener(new GridTouchListener.Adapter(this));

        setContentView(grid);
    }

    @Override
    public void onGridTouch(int x, int y) {

    }
}
