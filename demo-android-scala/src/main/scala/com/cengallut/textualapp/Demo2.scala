package com.cengallut.textualapp

import com.cengallut.textual.TextualActivity
import com.cengallut.textual.core.CharGrid
import com.cengallut.textual.decoration.{BoxChar, Fill, Border}
import com.cengallut.textual.decoration.BoxChar.Box

/** Demonstration of decoration for multiple sub-grids. */
class Demo2 extends TextualActivity {

  override def onBufferReady(buffer: CharGrid): Unit = {

    val (top, bottom) = buffer.horizontalBisect

    val (topLeft, topRight) = top.verticalBisect
    val (botLeft, botRight) = bottom.verticalBisect

    Border.box(Box.single).decorate(topLeft)
    Border.box(Box.double).decorate(topRight)
    Border.box(Box.rounded).decorate(botLeft)
    Border.box(Box.dash3).decorate(botRight)

    Fill.using(BoxChar.Shade.light).decorate(topLeft.shrink(1))
    Fill.using(BoxChar.Shade.medium).decorate(topRight.shrink(1))
    Fill.using(BoxChar.Shade.heavy).decorate(botLeft.shrink(1))
    Fill.using(BoxChar.Shade.forwardChecker)
      .decorate(botRight.shrink(1))

    buffer.notifyChanged()
  }

}