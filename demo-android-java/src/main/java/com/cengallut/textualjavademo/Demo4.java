package com.cengallut.textualjavademo;

import android.view.View;

import com.cengallut.textual.Action;
import com.cengallut.textual.TextualActivity;
import com.cengallut.textual.core.CharGrid;
import com.cengallut.textual.core.Cursor;
import com.cengallut.textual.core.GridAgent;
import com.cengallut.textual.core.TextCursor;
import com.cengallut.textual.decoration.Border;
import com.cengallut.textual.decoration.BoxChars;

/** Demonsrates the use of cursors. */
public class Demo4 extends TextualActivity {
    @Override
    public void onBufferReady(final CharGrid buffer) {

        Border.box(BoxChars.Boxes.single()).decorate(buffer);
        final TextCursor cursor = Cursor.text(new CharGrid.Prototype(buffer).shrink(2));

        View.OnTouchListener touch = new Action(buffer).touch(new GridAgent() {
            @Override public void onAction(int x, int y) {
                cursor.write("Hello ");
                buffer.notifyChanged();
            }
        });

        view().setOnTouchListener(touch);

    }
}
