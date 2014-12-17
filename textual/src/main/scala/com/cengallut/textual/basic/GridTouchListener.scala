package com.cengallut.textual.basic

import android.view.{MotionEvent, View}

trait GridTouchListener extends View.OnTouchListener {

  override final def onTouch(v: View, event: MotionEvent): Boolean = {
    v match {
      case grid: View with WritableBuffer =>
        val x = event.getX / grid.getWidth * grid.gridWidth
        val y = event.getY / grid.getHeight * grid.gridHeight
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