package com.cengallut.textualapp

import android.util.Log
import android.widget.Toast
import com.cengallut.textual.TextGrid.BufferStateListener
import com.cengallut.textual.decoration.{Fill, Border}

import scala.util.Random
import android.app.Activity
import android.os.Bundle
import com.cengallut.textual.core.WritableBuffer
import com.cengallut.textual.{TextGrid, GridTouchListener}

class Minesweeper extends Activity with GridTouchListener with BufferStateListener {

  var buffer    = WritableBuffer.zero
  var minefield = WritableBuffer.zero

  var mines     = Set.empty[(Int,Int)]
  var visited   = Set.empty[(Int,Int)]

  lazy val grid = TextGrid.create(this)

  override def onBufferReady(b: WritableBuffer): Unit = {
    buffer = b
    minefield = b.shrink(1, 1, 1, 1)
    new Border.Simple('#').decorate(buffer)
    new Fill('.').decorate(minefield)
    mines = generateMines(minefield.gridWidth, minefield.gridHeight, 40)
  }

  override def onGridTouch(x: Int, y: Int): Unit = {
    if (mines(x, y)) {
      Toast.makeText(this, "You're dead", Toast.LENGTH_SHORT).show()
      this.finish()
    } else {
      visitAll(x, y)
      minefield.bufferChanged()
    }
  }

  def visitAll(x: Int, y: Int): Unit = {
    var stack = List((x, y))
    while (stack.nonEmpty) {
      val (xx, yy) = stack.head
      visited += (xx -> yy)
      if (isVisitable(xx, yy)) {
        val (c, pts) = process(xx, yy)

      }
      stack = stack.tail ++ visit(xx, yy)
    }
  }

  def visit(x: Int, y: Int): Seq[(Int,Int)] = {
    if (!visited(x, y) && !mines(x, y) && minefield.isInRange(x, y)) {

      mineCount(x, y) match {
        case 0 =>
          minefield.setChar(x, y, ' ')
          neighborsFour(x, y)
        case n =>
          minefield.setChar(x, y, (48 + n).toChar)
          List()
      }
    } else {
      List()
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