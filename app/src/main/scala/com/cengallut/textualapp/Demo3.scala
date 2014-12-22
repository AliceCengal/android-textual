package com.cengallut.textualapp

import android.app.Activity
import android.os.Bundle
import com.cengallut.textual.{TouchComposite, Action, TextualView}
import com.cengallut.textual.core.CharGrid

class Demo3 extends Activity with TextualView.BufferStateListener {

  var buffer = CharGrid.zero

  lazy val textGrid = TextualView.create(this)


  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(textGrid)
  }

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

    textGrid.setOnTouchListener(compositeTouch)

  }

}