package com.cengallut.textualapp

import com.cengallut.textual.{TextualActivity, TouchComposite, Action}
import com.cengallut.textual.core.CharGrid

/** Demonstration of event handling for multiple sub-grids. */
class Demo3 extends TextualActivity {

  override def onBufferReady(buffer: CharGrid): Unit = {

    val (top, bottom) = buffer.horizontalBisect

    val topTouch = Action(top).touch { (x, y) =>
      top.charAt(x, y) match {
        case 'o' => top.setChar(x, y, 'T')
        case _   => top.setChar(x, y, 'o')
      }
      top.notifyChanged()
    }

    val botTouch = Action(bottom).touch { (x, y) =>
      bottom.charAt(x, y) match {
        case 'o' => bottom.setChar(x, y, 'B')
        case _   => bottom.setChar(x, y, 'o')
      }
      bottom.notifyChanged()
    }

    val compositeTouch = TouchComposite.empty + topTouch + botTouch

    view.setOnTouchListener(compositeTouch)

  }

}