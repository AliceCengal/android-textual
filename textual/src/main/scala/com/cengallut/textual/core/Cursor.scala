package com.cengallut.textual.core

import com.cengallut.textual.aside.Sugar._

trait Cursor {

  def write(c: Char): Cursor

  def write(s: String): Cursor

  def linearMove(steps: Int): Cursor

  def advance() = linearMove(1)

  def retreat() = linearMove(-1)

  def current: Char

}

trait RawCursor[C <: RawCursor[C]] {

  def set(c: Char): C

  def move(dx: Int, dy: Int): C

  def current: Char

}

trait TextCursor extends RawCursor[TextCursor] {

  def write(c: Char): TextCursor

  def write(s: String): TextCursor

  def writeLn(c: Char): TextCursor =
    write(c).newLine()

  def writeLn(s: String): TextCursor =
    write(s).newLine()

  def linearMove(steps: Int): TextCursor

  def advance() = linearMove(1)

  def retreat() = linearMove(-1)

  def newLine(): TextCursor

  def backspace()

}

object Cursor {

  def base(grid: WritableBuffer): Cursor = new BaseCursor(grid)

  private class BaseCursor(val grid: WritableBuffer) extends Cursor {

    private var position = (0,0)

    override def write(c: Char): Cursor = {
      grid.setChar(position.x, position.y, c)
      grid.bufferChanged()
      advance()
      this
    }

    override def write(s: String): Cursor = {
      s.foreach { c =>
        grid.setChar(position.x, position.y, c)
        advance()
      }
      grid.bufferChanged()
      this
    }

    override def linearMove(steps: Int): Cursor = {
      var xNew = (position.x + steps) % grid.gridWidth
      var yNew = position.y + ((position.x + steps) / grid.gridWidth)


      position = (xNew, yNew)
      this
    }

    override def current: Char =
      grid.charAt(position.x, position.y)

  }

}