package com.cengallut.textualjavademo;

import android.view.View;

import com.cengallut.textual.Action;
import com.cengallut.textual.TextualActivity;
import com.cengallut.textual.TouchComposite;
import com.cengallut.textual.core.CharGrid;
import com.cengallut.textual.core.GridAgent;

/** Demonstration of event handling for multiple sub-grids. */
public class Demo3 extends TextualActivity {
    @Override
    public void onBufferReady(CharGrid buffer) {

        CharGrid.Prototype p = new CharGrid.Prototype(buffer);
        final CharGrid top = p.topBisect();
        final CharGrid bottom = p.bottomBisect();

        View.OnTouchListener topTouch = new Action(top).touch(new GridAgent() {
            @Override public void onAction(int x, int y) {
                if (top.charAt(x, y) == 'o') {
                    top.setChar(x, y, 'T');
                } else {
                    top.setChar(x, y, 'o');
                }
                top.notifyChanged();
            }
        });

        View.OnTouchListener botTouch = new Action(bottom).touch(new GridAgent() {
            @Override public void onAction(int x, int y) {
                if (bottom.charAt(x, y) == 'o') {
                    bottom.setChar(x, y, 'B');
                } else {
                    bottom.setChar(x, y, 'o');
                }
                bottom.notifyChanged();
            }
        });

        TouchComposite comp = TouchComposite.empty().add(topTouch).add(botTouch);

        view().setOnTouchListener(comp);

    }
}
