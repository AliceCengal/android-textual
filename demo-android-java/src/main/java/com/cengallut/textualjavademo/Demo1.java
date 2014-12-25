package com.cengallut.textualjavademo;

import com.cengallut.textual.Action;
import com.cengallut.textual.TextualActivity;
import com.cengallut.textual.core.CharGrid;
import com.cengallut.textual.core.GridAgent;
import com.cengallut.textual.decoration.Border;
import com.cengallut.textual.decoration.Decoration;
import com.cengallut.textual.decoration.Fill;

import java.util.Arrays;
import java.util.List;

/** Demonstration of decoration and touch event handling for a single Grid. */
public class Demo1 extends TextualActivity
        implements GridAgent {

    final List<Decoration> decos = Arrays.asList(
            Fill.using('o'),
            Border.simple('#'));

    CharGrid buffer = null;

    @Override
    public void onBufferReady(CharGrid b) {
        buffer = b;

        for (Decoration d : decos) {
            d.decorate(b);
        }

        view().setOnTouchListener(new Action(b).touch(this));
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
