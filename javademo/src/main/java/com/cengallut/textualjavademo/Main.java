package com.cengallut.textualjavademo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.cengallut.textual.GridTouchListener;
import com.cengallut.textual.TextGrid;

public class Main extends Activity {

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

        grid.setOnTouchListener(new GridTouchListener() {
            @Override
            public void onGridTouch(int x, int y) {
                grid.buffer().setChar(x, y, 'x');
                grid.buffer().bufferChanged();
            }
        });

        setContentView(grid);
    }

}
