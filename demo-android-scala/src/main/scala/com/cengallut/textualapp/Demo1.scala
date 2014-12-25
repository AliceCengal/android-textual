package com.cengallut.textualapp

import android.app.Activity
import android.os.Bundle
import com.cengallut.textual.core.CharGrid
import com.cengallut.textual.decoration.{Fill, Border}
import com.cengallut.textual.{TextualActivity, Action, TextualView}

/** Demonstration of decoration and touch event handling for a single Grid. */
class Demo1 extends TextualActivity {

  val decos = Seq(
    Fill.using('O'),
    Border.simple('+'))

  override def onBufferReady(buffer: CharGrid): Unit = {
    decos.foreach { _.decorate(buffer) }
    buffer.notifyChanged()

    val touchListener = Action(buffer).touch { (x, y) =>
      buffer.charAt(x, y) match {
        case 'O' => buffer.setChar(x, y, 'X')
        case _   => buffer.setChar(x, y, 'O')
      }
      buffer.notifyChanged()
    }

    view.setOnTouchListener(touchListener)

  }

}