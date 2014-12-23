package com.cengallut.textual

import android.os.Bundle

abstract class TextualActivity extends android.app.Activity
    with TextualView.BufferStateListener {

  lazy val view = TextualView.create(this)

  override def onCreate(saved: Bundle): Unit = {
    super.onCreate(saved)
    setContentView(view)
  }

}