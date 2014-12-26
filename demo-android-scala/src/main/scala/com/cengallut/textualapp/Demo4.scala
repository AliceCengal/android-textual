package com.cengallut.textualapp

import com.cengallut.textual.{Action, TextualActivity}
import com.cengallut.textual.core.{Cursor, CharGrid}
import com.cengallut.textual.decoration.{BoxChar, Border}

class Demo4 extends TextualActivity {
  override def onBufferReady(buffer: CharGrid): Unit = {

    Border.box(BoxChar.Box.single).decorate(buffer)
    val cursor = Cursor.text(buffer.shrink(2))

    val touch = Action(buffer).touch { (x, y) =>
      cursor.write("Hello ")
      buffer.notifyChanged()
    }

    view.setOnTouchListener(touch)

  }
}