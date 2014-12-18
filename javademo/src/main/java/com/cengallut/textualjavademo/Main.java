package com.cengallut.textualjavademo;

import android.app.Activity;
import android.os.Bundle;

import com.cengallut.textual.GridTouchListener;
import com.cengallut.textual.TextGrid;
import com.cengallut.textual.basic.WritableBuffer;
import com.cengallut.textual.widget.Border;
import com.cengallut.textual.widget.Decoration;

public class Main extends Activity
        implements GridTouchListener.Interface, TextGrid.BufferStateListener {

    final Decoration border = new Border.Simple('H');

    WritableBuffer buffer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TextGrid grid = new TextGrid(this) {};

        grid.setOnTouchListener(new GridTouchListener.Adapter(this));

        setContentView(grid);
    }

    @Override
    public void onGridTouch(int x, int y) {
        buffer.setChar(x, y, 'O');
        buffer.bufferChanged();
    }

    @Override
    public void onBufferReady(WritableBuffer b) {
        buffer = b;
        border.decorate(buffer);
        buffer.bufferChanged();
    }
}
