package com.cengallut.textualapp

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.cengallut.textual.{GridTouchListener, TextGrid}

class Main extends Activity {

  override def onCreate(saved: Bundle): Unit = {
    super.onCreate(saved)

    val textGrid = new TextGrid(this) {

      override def textColor: Int = Color.WHITE

      override def background: Int = Color.BLACK

      override def textSize: Float = 30f
    }

    textGrid.setOnTouchListener(GridTouchListener {
      (x, y) =>
        textGrid.cursor.write('G')
    })

    setContentView(textGrid)
  }

}