package com.cengallut.textual

import android.view.View.OnTouchListener
import android.view.{MotionEvent, View}
import com.cengallut.textual.core.{CharGrid, GridAgent}

/** An adapter to translate a touch event in the View coordinate space
  * into the WritableBuffer coordinate space. An implementation of this
  * trait should be passed into the `setOnTouchListener` of a TextGrid.
  *
  * It seems that this trait is only usable from Scala, not from Java.
  * An alternate Listener-Adapter system is provided in the companion
  * object. */

/*
object GridTouchListener {

  /** An adapter to transform touch events from the coordinate
    * space of the View to the WritableBuffer's coordinate space. */
  class Adapter(listener: GridAgent) extends View.OnTouchListener {
    override final def onTouch(v: View, event: MotionEvent): Boolean = {
      v match {
        case grid: TextualView =>
          val x = event.getX / grid.getWidth * grid.buffer.width
          val y = event.getY / grid.getHeight * grid.buffer.height
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

*/

object Action {

  def apply(grid: CharGrid) = new ActionFactory(grid)

  class ActionFactory(private val grid: CharGrid) {

    def touch(agent: GridAgent) = new OnTouchListener {
      override def onTouch(v: View, event: MotionEvent) = v match {
        case textual: TextualView =>

          val (xx, yy) = textual.filterMap
            .map(event.getX.toInt, event.getY.toInt)

          if (event.getAction == MotionEvent.ACTION_UP &&
              grid.filterMap.filter(xx, yy)) {
            val (xxx, yyy) = grid.filterMap.map(xx, yy)
            agent.onAction(xxx, yyy)
            true
          } else {
            false
          }
        case _ => throw new IllegalStateException("")
      }

    }

  }

}

/** Looking at the source code of android.view.View, it seems that each View will only
  * hold one listener of a type. In order to have multiple listeners to different
  * parts of the TextualView, we would have to composite them together. This class
  * is fully immutable. All append methods return a new instance of this Composite.
  * The original Composite is still a valid object. */
class TouchComposite private (private val listeners: List[View.OnTouchListener])
    extends View.OnTouchListener {

  override def onTouch(v: View, event: MotionEvent) = {
    listeners.foreach{ _.onTouch(v, event) }
    true
  }

  def add(l: View.OnTouchListener) =
    new TouchComposite(l :: listeners)

  def + = add _

  def ++(ls: Seq[View.OnTouchListener]) =
    new TouchComposite(listeners ++ ls)

  def addAll(ls: java.util.Collection[View.OnTouchListener]) = {
    val it = ls.iterator()
    var appended = listeners

    // fucking hell
    while (it.hasNext) {
      appended ::= it.next()
    }

    new TouchComposite(appended)
  }

}

object TouchComposite {
  def empty: TouchComposite = new TouchComposite(List())
}