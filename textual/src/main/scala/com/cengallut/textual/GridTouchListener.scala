package com.cengallut.textual

import android.view.{MotionEvent, View}

/** An adapter to translate a touch event in the View coordinate space
  * into the WritableBuffer coordinate space. An implementation of this
  * trait should be passed into the `setOnTouchListener` of a TextGrid.
  *
  * It seems that this trait is only usable from Scala, not from Java.
  * An alternate Listener-Adapter system is provided in the companion
  * object. */
trait GridTouchListener extends View.OnTouchListener {

  override final def onTouch(v: View, event: MotionEvent): Boolean = {
    v match {
      case grid: TextualView =>
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

  /** A pure interface for a TextGrid's touch event handler. An
    * implementation of this interface should be wrapped in
    * `GridTouchListener.Adapter` before being passed into
    * the `setOnTouchListener` of a TextGrid. */
  trait Interface {
    def onGridTouch(x: Int, y: Int): Unit
  }

  /** An adapter to transform touch events from the coordinate
    * space of the View to the WritableBuffer's coordinate space. */
  class Adapter(listener: Interface) extends View.OnTouchListener {
    override final def onTouch(v: View, event: MotionEvent): Boolean = {
      v match {
        case grid: TextualView =>
          val x = event.getX / grid.getWidth * grid.buffer.gridWidth
          val y = event.getY / grid.getHeight * grid.buffer.gridHeight
          listener.onGridTouch(x.toInt, y.toInt)
        case _ =>
      }
      true
    }
  }

  /** Creates a new GridTouchListener from a binary function. */
  def apply(f: (Int,Int)=>Unit) = new GridTouchListener {
    override def onGridTouch(x: Int, y: Int): Unit = f(x, y)
  }

}