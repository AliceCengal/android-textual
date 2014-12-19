package com.cengallut.textualapp

import android.app.Activity
import android.os.Bundle
import com.cengallut.textual.TextGrid.BufferStateListener
import com.cengallut.textual.core.WritableBuffer
import com.cengallut.textual.decoration.Border
import com.cengallut.textual.{GridTouchListener, TextGrid}

class Main extends Activity with GridTouchListener with BufferStateListener {

  val border = new Border.Simple('#')

  var buffer = WritableBuffer.zero

  lazy val textGrid = TextGrid.create(this)

  override def onCreate(saved: Bundle): Unit = {
    super.onCreate(saved)
    setContentView(textGrid)
  }

  override def onGridTouch(x: Int, y: Int): Unit = {
    buffer.setChar(x, y, 'O')
    buffer.bufferChanged()
  }

  override def onBufferReady(b: WritableBuffer): Unit = {
    buffer = b
    border.decorate(buffer)
    buffer.bufferChanged()
  }

}