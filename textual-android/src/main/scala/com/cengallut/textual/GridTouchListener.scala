package com.cengallut.textual

import android.util.Log
import android.view.View.OnTouchListener
import android.view.{MotionEvent, View}
import com.cengallut.textual.core.{CharGrid, GridAgent}

class Action(private val grid: CharGrid) {

  def touch(agent: GridAgent): OnTouchListener = new OnTouchListener {
    override def onTouch(v: View, event: MotionEvent) = v match {
      case textual: TextualView =>

        //Log.d("TouchListener", s"Received event on TextualView: ${event.getActionMasked}")
        if (event.getAction == MotionEvent.ACTION_DOWN) {
          true
        } else if (event.getAction == MotionEvent.ACTION_UP) {
          //Log.d("TouchListener", s"Received Received up event at (${event.getX}, ${event.getY}})")
          val (xx, yy) = textual.filterMap
            .map(event.getX.toInt, event.getY.toInt)
          //Log.d("TouchListener", s"Touch coordinate on grid is ($xx, $yy)")
          if (grid.filterMap.filter(xx, yy)) {

            val (xxx, yyy) = grid.filterMap.map(xx, yy)
            //Log.d("TouchListener", s"Received touch on TextGrid at ($xxx, $yyy)")

            agent.onAction(xxx, yyy)
            true
          } else {
            false
          }

        } else false

      case _ => throw new IllegalStateException("")
    }

  }

  def touch(reactor: (Int, Int) => Unit): OnTouchListener = touch(GridAgent(reactor))

}

object Action {
  def apply(g: CharGrid) = new Action(g)
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