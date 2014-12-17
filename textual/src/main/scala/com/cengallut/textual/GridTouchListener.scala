package com.cengallut.textual

import android.view.{MotionEvent, View}

abstract class GridTouchListener extends View.OnTouchListener {

  override final def onTouch(v: View, event: MotionEvent): Boolean = {
    v match {
      case grid: TextGrid =>
        val x = event.getX / grid.getWidth * grid.buffer.gridWidth
        val y = event.getY / grid.getHeight * grid.buffer.gridHeight
        onGridTouch(x.toInt, y.toInt)
      case _ =>
    }
    true
  }

  def onGridTouch(x: Int, y: Int): Unit

}

object GridTouchListener {

  def apply(f: (Int,Int)=>Unit) = new GridTouchListener {
    override def onGridTouch(x: Int, y: Int): Unit = f(x, y)
  }

}