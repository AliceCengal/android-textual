package com.cengallut.textualapp

import scala.util.Random

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import com.cengallut.textual.TextualView.BufferStateListener
import com.cengallut.textual.decoration.{BoxChar, Fill, Border}
import com.cengallut.textual.core.{GridAgent, CharGrid}
import com.cengallut.textual.{Action, TextualView}

class Minesweeper extends Activity
    with BufferStateListener with GridAgent {

  var buffer    = CharGrid.zero
  var minefield = CharGrid.zero

  var mines     = Set.empty[(Int,Int)]
  var visited   = Set.empty[(Int,Int)]

  lazy val grid = TextualView.create(this)

  override def onBufferReady(b: CharGrid): Unit = {
    buffer = b
    minefield = b.shrink(1)

    new Border.Box(BoxChar.Box.double).decorate(buffer)
    new Fill('.').decorate(minefield)
    mines = generateMines(minefield.width, minefield.height, 40)

    grid.setOnTouchListener(Action(minefield).touch(this))
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
    mineCount(x, y) match {
      case 0 => (' ', neighborsFour(x, y))
      case n => ((48 + n).toChar, List())
    }
  }

  def isVisitable(x: Int, y: Int) =
    !visited(x, y) && !mines(x, y) && minefield.isInRange(x, y)

  def neighborsEight(x: Int, y: Int) = for {
    dx <- -1 to 1
    dy <- -1 to 1
    if dx != 0 || dy != 0
  } yield (x + dx, y + dy)

  def neighborsFour(x: Int, y: Int) = for {
    dx <- -1 to 1
    dy <- -1 to 1
    if math.abs(dx + dy) == 1
  } yield (x + dx, y + dy)

  def generateMines(xLimit: Int, yLimit: Int, n: Int) = {
    val allpoints = for {
      xc <- 0 until xLimit
      yc <- 0 until yLimit
    } yield (xc, yc)
    Random.shuffle(allpoints).take(n).toSet
  }

  def mineCount(x: Int, y: Int) =
    neighborsEight(x, y).foldLeft(0) {
      case (count, (xn, yn)) =>
        if (mines(xn, yn)) count + 1 else count
    }

  override def onCreate(saved: Bundle): Unit = {
    super.onCreate(saved)
    setContentView(grid)
  }

}