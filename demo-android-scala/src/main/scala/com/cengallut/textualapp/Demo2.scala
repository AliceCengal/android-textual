package com.cengallut.textualapp

import android.app.Activity
import android.os.Bundle
import com.cengallut.textual.TextualView
import com.cengallut.textual.core.CharGrid
import com.cengallut.textual.decoration.{BoxChar, Fill, Border}
import com.cengallut.textual.decoration.BoxChar.Box

/** Demonstration of decoration for multiple sub-grids. */
class Demo2 extends Activity with TextualView.BufferStateListener {

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

  override def onCreate(saved: Bundle) = {
    super.onCreate(saved)
    setContentView(TextualView.create(this))
  }

}