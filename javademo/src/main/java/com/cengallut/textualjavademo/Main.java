package com.cengallut.textualjavademo;

import android.app.Activity;
import android.os.Bundle;

import com.cengallut.textual.GridTouchListener;
import com.cengallut.textual.TextualView;
import com.cengallut.textual.core.CharGrid;
import com.cengallut.textual.decoration.Border;
import com.cengallut.textual.decoration.Decoration;

public class Main extends Activity
        implements GridTouchListener.Interface,
        TextualView.BufferStateListener {

    final Decoration border = new Border.Simple('H');

    CharGrid buffer = CharGrid.Factory.zero();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TextualView grid = new TextualView(this) {};

        grid.setOnTouchListener(new GridTouchListener.Adapter(this));

        setContentView(grid);
    }

    @Override
    public void onGridTouch(int x, int y) {
        buffer.setChar(x, y, 'O');
        buffer.notifyChanged();
    }

    @Override
    public void onBufferReady(CharGrid b) {
        buffer = b;
        border.decorate(buffer);
        buffer.notifyChanged();
    }
}
