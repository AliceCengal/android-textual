package com.cengallut.textualapp

import android.app.Activity
import android.os.Bundle
import com.cengallut.textual.core.CharGrid
import com.cengallut.textual.decoration.Border
import com.cengallut.textual.TextualView

class Main extends Activity with TextualView.BufferStateListener {

  val border = new Border.Simple('#')

  var buffer = CharGrid.zero

  lazy val textGrid = TextualView.create(this)

  override def onCreate(saved: Bundle): Unit = {
    super.onCreate(saved)
    setContentView(textGrid)
  }

  def onGridTouch(x: Int, y: Int): Unit = {
    buffer.charAt(x, y) match {
      case 'O' => buffer.setChar(x, y, 'X')
      case _   => buffer.setChar(x, y, 'O')
    }
    buffer.notifyChanged()
  }

  override def onBufferReady(b: CharGrid): Unit = {
    buffer = b
    border.decorate(buffer)
    buffer.notifyChanged()
  }

}