package com.cengallut.textual.basic

import android.view.View
import com.cengallut.textual.aside.Sugar._

trait Cursor {

  def write(c: Char): Cursor

  def write(s: String): Cursor

  def linearMove(steps: Int): Cursor

  def advance() = linearMove(1)

  def retreat() = linearMove(-1)

  def current: Char

}

object Cursor {

  def base(grid: View with WritableBuffer): Cursor = new BaseCursor(grid)

  private class BaseCursor(val grid: View with WritableBuffer) extends Cursor {

    private var position = (0,0)

    override def write(c: Char): Cursor = {
      grid.setChar(position.x, position.y, c)
      grid.invalidate()
      advance()
      this
    }

    override def write(s: String): Cursor = {
      s.foreach { c =>
        grid.setChar(position.x, position.y, c)
        advance()
      }
      grid.invalidate()
      this
    }

    override def linearMove(steps: Int): Cursor = {
      val xNew = (position.x + steps) % grid.gridWidth
      val yNew = position.y + ((position.x + steps) / grid.gridWidth)
      position = (xNew, yNew)
      this
    }

    override def current: Char =
      grid.charAt(position.x, position.y)

  }

}