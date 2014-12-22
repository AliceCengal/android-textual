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

    new Border.Box(Box.single).decorate(topLeft)
    new Border.Box(Box.double).decorate(topRight)
    new Border.Box(Box.rounded).decorate(botLeft)
    new Border.Box(Box.dash3).decorate(botRight)

    buffer.notifyChanged()
  }

  override def onCreate(saved: Bundle) = {
    super.onCreate(saved)
    setContentView(TextualView.create(this))
  }

}