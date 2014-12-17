package com.cengallut.textualapp

import android.app.Activity
import android.graphics.Color
import android.os.{Handler, Bundle}
import android.util.Log
import com.cengallut.textual.basic.WritableBuffer
import com.cengallut.textual.widget.Border
import com.cengallut.textual.{GridTouchListener, TextGrid}

class Main extends Activity {

  val border = new Border.Simple('#')

  lazy val textGrid: TextGrid = {
    val textGrid = new TextGrid(this) {
      override def onBufferReady(buffer: WritableBuffer): Unit = {
        border.decorate(buffer)
        buffer.bufferChanged()
      }
    }

    textGrid.setOnTouchListener(GridTouchListener {
      (x, y) =>
        textGrid.getBuffer.setChar(x, y, 'O')
        textGrid.getBuffer.bufferChanged()
    })

    textGrid
  }

  override def onCreate(saved: Bundle): Unit = {
    super.onCreate(saved)
    setContentView(textGrid)
  }
/*
  override def onResume(): Unit = {
    super.onResume()
    val h = new Handler
    h.post(new Runnable {
      override def run(): Unit = {
        border.decorate(textGrid.getBuffer)
      }
    })
  }
*/
}