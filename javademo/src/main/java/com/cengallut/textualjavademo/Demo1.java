package com.cengallut.textualjavademo;

import android.app.Activity;
import android.os.Bundle;

import com.cengallut.textual.Action;
import com.cengallut.textual.TextualView;
import com.cengallut.textual.core.CharGrid;
import com.cengallut.textual.core.GridAgent;
import com.cengallut.textual.decoration.Border;
import com.cengallut.textual.decoration.Decoration;
import com.cengallut.textual.decoration.Fill;

import java.util.Arrays;
import java.util.List;

/** Demonstration of decoration and touch event handling for a single Grid. */
public class Demo1 extends Activity
        implements TextualView.BufferStateListener, GridAgent {

    final List<Decoration> decos = Arrays.asList(
            new Fill('O'),
            new Border.Simple('#'));

    CharGrid buffer = null;

    TextualView textual = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textual = TextualView.create(this);
        setContentView(textual);
    }

    @Override
    public void onBufferReady(CharGrid b) {
        buffer = b;

        for (Decoration d : decos) {
            d.decorate(b);
        }

        textual.setOnTouchListener(new Action(b).touch(this));

        buffer.notifyChanged();

    }

    @Override
    public void onAction(int x, int y) {
        if (buffer.charAt(x, y) == 'O') {
            buffer.setChar(x, y, 'X');
        } else {
            buffer.setChar(x, y, 'O');
        }
        buffer.notifyChanged();
    }
}
