package com.cengallut.textualapp

import android.app.Activity
import android.os.Bundle
import com.cengallut.textual.core.CharGrid
import com.cengallut.textual.decoration.{Fill, Border}
import com.cengallut.textual.{Action, TextualView}

class Main extends Activity with TextualView.BufferStateListener {

  val decos = Seq(
    new Fill('O'),
    new Border.Simple('+'))

  var buffer = CharGrid.zero

  lazy val textGrid = TextualView.create(this)

  override def onCreate(saved: Bundle): Unit = {
    super.onCreate(saved)
    setContentView(textGrid)
  }

  override def onBufferReady(b: CharGrid): Unit = {
    buffer = b
    decos.foreach { _.decorate(b) }

    textGrid.setOnTouchListener(Action(b).touch { (x, y) =>
      buffer.charAt(x, y) match {
        case 'O' => buffer.setChar(x, y, 'X')
        case _   => buffer.setChar(x, y, 'O')
      }
      buffer.notifyChanged()
    })

    buffer.notifyChanged()
  }

}