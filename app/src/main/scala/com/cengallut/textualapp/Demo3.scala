package com.cengallut.textualapp

import android.app.Activity
import android.os.Bundle
import com.cengallut.textual.TextualView
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

    
  }

}