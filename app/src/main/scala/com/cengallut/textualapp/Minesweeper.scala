package com.cengallut.textualapp

import android.widget.Toast
import com.cengallut.textual.TextGrid.BufferStateListener

import scala.util.Random
import android.app.Activity
import android.os.Bundle
import com.cengallut.textual.basic.WritableBuffer
import com.cengallut.textual.{TextGrid, GridTouchListener}

class Minesweeper extends Activity with GridTouchListener with BufferStateListener {

  var minefield = WritableBuffer.zero

  var mines     = Set.empty[(Int,Int)]

  var visited   = Set.empty[(Int,Int)]

  lazy val grid = TextGrid.create(this)

  override def onBufferReady(buffer: WritableBuffer): Unit = {
    //grid.setOnTouchListener(this)

  }

  override def onGridTouch(x: Int, y: Int): Unit = {
    if (mines(x, y)) {
      Toast.makeText(this, "You're dead", Toast.LENGTH_LONG).show()
      this.finish()
    } else {
      visit(x, y)
    }
  }



  def visit(x: Int, y: Int): Unit = {
    if (!visited(x, y)) {
      visited += (x -> y)
      val mineCount = neighbors(x, y)
        .foldLeft(0) { case (count, (xn, yn)) => if (mines(xn, yn)) count + 1 else count}
      if (mineCount > 0) minefield.setChar(x, y, mineCount.toChar)

      neighbors(x, y).foreach { case (xn, yn) => visit(xn, yn)}
    }
  }

  def neighbors(x: Int, y: Int) = Seq(
    (x - 1, y - 1), (x, y - 1), (x + 1, y - 1),
    (x - 1, y),                 (x + 1, y),
    (x - 1, y + 1), (x, y + 1), (x + 1, y + 1))

  def randomPosition(xLimit: Int, yLimit: Int) =
    (Random.nextInt(xLimit), Random.nextInt(yLimit))

  override def onCreate(saved: Bundle): Unit = {
    super.onCreate(saved)
    setContentView(grid)
  }


}