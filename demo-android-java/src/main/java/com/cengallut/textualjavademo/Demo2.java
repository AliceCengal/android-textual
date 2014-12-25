package com.cengallut.textualjavademo;

import com.cengallut.textual.TextualActivity;
import com.cengallut.textual.core.CharGrid;
import com.cengallut.textual.decoration.Border;
import com.cengallut.textual.decoration.Fill;

import static com.cengallut.textual.core.CharGrid.Prototype;
import static com.cengallut.textual.decoration.BoxChars.Boxes;
import static com.cengallut.textual.decoration.BoxChars.Shades;

/** Demonstration of decoration for multiple sub-grids. */
public class Demo2 extends TextualActivity {
    @Override
    public void onBufferReady(CharGrid buffer) {

        Prototype p = new Prototype(buffer);
        CharGrid top = p.topBisect();
        CharGrid bottom = p.bottomBisect();

        Prototype pTop = new Prototype(top);
        CharGrid topLeft = pTop.leftBisect();
        CharGrid topRight = pTop.rightBisect();

        Prototype pBot = new Prototype(bottom);
        CharGrid botLeft = pBot.leftBisect();
        CharGrid botRight = pBot.rightBisect();

        Border.box(Boxes.dash2()).decorate(topLeft);
        Border.box(Boxes.twin()).decorate(topRight);
        Border.box(Boxes.rounded()).decorate(botLeft);
        Border.box(Boxes.dash3()).decorate(botRight);

        Fill.using(Shades.light()).decorate(new Prototype(topLeft).shrink(1));
        Fill.using(Shades.medium()).decorate(new Prototype(topRight).shrink(1));
        Fill.using(Shades.heavy()).decorate(new Prototype(botLeft).shrink(1));
        Fill.using(Shades.forwardChecker()).decorate(new Prototype(botRight).shrink(1));

        buffer.notifyChanged();
    }
}
