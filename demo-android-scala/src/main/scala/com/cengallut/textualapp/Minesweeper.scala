package com.cengallut.textualapp

import scala.util.Random
import android.widget.Toast

import com.cengallut.textual.decoration.{BoxChar, Fill, Border}
import com.cengallut.textual.core.{GridAgent, CharGrid}
import com.cengallut.textual.{TextualActivity, Action}

class Minesweeper extends TextualActivity with GridAgent {

  var minefield = CharGrid.zero

  var mines     = Set.empty[(Int,Int)]
  var visited   = Set.empty[(Int,Int)]

  override def onBufferReady(b: CharGrid): Unit = {
    minefield = b.shrink(1)

    Border.box(BoxChar.Box.twin).decorate(b)
    Fill.using(BoxChar.Shade.light).decorate(minefield)
    mines = generateMines(minefield.width, minefield.height, 0.5)

    view.setOnTouchListener(Action(minefield).touch(this))
  }

  override def onAction(x: Int, y: Int): Unit = {
    if (mines(x, y)) {
      Toast.makeText(this, "You're dead", Toast.LENGTH_SHORT).show()
      this.finish()
    } else {
      visitAll(x, y)
      minefield.notifyChanged()
    }
  }

  def visitAll(start: (Int,Int)): Unit = {
    var stack = List(start)
    while (stack.nonEmpty) {
      val (xx, yy) = stack.head
      if (isVisitable(xx, yy)) {
        val (c, pts) = process(xx, yy)
        minefield.setChar(xx, yy, c)
        stack = stack.tail ++ pts
      } else {
        stack = stack.tail
      }
      visited += (xx -> yy)
    }
  }

  def process(x: Int, y: Int): (Char, Seq[(Int, Int)]) = {
    val nn = neighbors(x, y)
    mineCount(nn) match {
      case 0 => (' ', nn)
      case n => ((48 + n).toChar, List())
    }
  }

  def isVisitable(x: Int, y: Int) =
    !visited(x, y) && !mines(x, y) && minefield.isInRange(x, y)

  def neighbors(x: Int, y: Int) = for {
    dx <- -1 to 1
    dy <- -1 to 1
    if dx != 0 || dy != 0
  } yield (x + dx, y + dy)

  def generateMines(xLimit: Int, yLimit: Int, n: Double) = {
    val allpoints = for {
      xc <- 0 until xLimit
      yc <- 0 until yLimit
    } yield (xc, yc)
    Random.shuffle(allpoints)
      .take((xLimit * yLimit * n).toInt).toSet
  }

  def mineCount(spots: Seq[(Int,Int)]) =
    spots.foldLeft(0) {
      case (count, (xn, yn)) => if (mines(xn, yn)) count + 1 else count
    }

}