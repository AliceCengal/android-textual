package com.cengallut.textualjavademo;

import com.cengallut.textual.TextualActivity;
import com.cengallut.textual.core.CharGrid;
import com.cengallut.textual.core.Grids;
import com.cengallut.textual.decoration.Border;
import com.cengallut.textual.decoration.Fill;

import static com.cengallut.textual.decoration.BoxChars.Boxes;
import static com.cengallut.textual.decoration.BoxChars.Shades;

/** Demonstration of decoration for multiple sub-grids. */
public class Demo2 extends TextualActivity {
    @Override
    public void onBufferReady(CharGrid buffer) {

        CharGrid top = Grids.factory.topBisect(buffer);
        CharGrid bottom = Grids.factory.bottomBisect(buffer);

        CharGrid topLeft = Grids.factory.leftBisect(top);
        CharGrid topRight = Grids.factory.rightBisect(top);

        CharGrid botLeft = Grids.factory.leftBisect(bottom);
        CharGrid botRight = Grids.factory.rightBisect(bottom);

        Border.box(Boxes.dash2()).decorate(topLeft);
        Border.box(Boxes.twin()).decorate(topRight);
        Border.box(Boxes.rounded()).decorate(botLeft);
        Border.box(Boxes.dash3()).decorate(botRight);

        Fill.using(Shades.light()).decorate(Grids.factory.shrink(topLeft, 1));
        Fill.using(Shades.medium()).decorate(Grids.factory.shrink(topRight, 1));
        Fill.using(Shades.heavy()).decorate(Grids.factory.shrink(botLeft, 1));
        Fill.using(Shades.forwardChecker()).decorate(Grids.factory.shrink(botRight, 1));

        buffer.notifyChanged();
    }
}
