package com.cengallut.textual.core

class GenCursor[C <: GenCursor[C]](val grid: CharGrid) {

  private var pos = (0,0)

  final def move(dx: Int = 0, dy: Int = 0): C = {
    pos = (pos._1 + dx, pos._2 + dy)
    this.asInstanceOf[C]
  }

  final def moveTo(x: Int = pos._1, y: Int = pos._2): C = {
    pos = (x, y)
    this.asInstanceOf[C]
  }

  final def current: Char = grid.charAt(pos._1, pos._2)

  final def set(c: Char): C = {
    grid.setChar(pos._1, pos._2, c)
    this.asInstanceOf[C]
  }

  final def isInRange = grid.isInRange(pos._1, pos._2)

  final def xPos = pos._1

  final def yPos = pos._2
}

trait RawCursor extends GenCursor[RawCursor]

trait TextCursor extends GenCursor[TextCursor] {

  def write(c: Char): TextCursor =
    set(c).linearMove(1)

  def write(s: String): TextCursor =
    s.foldLeft(this) { case (cursor, c) => write(c) }

  def writeLn(c: Char): TextCursor =
    write(c).newLine()

  def writeLn(s: String): TextCursor =
    write(s).newLine()

  def linearMove(steps: Int): TextCursor = {
    move(dx = steps)

    while (xPos > grid.width) {
      move(dx = -grid.width, dy = 1)
    }

    while (xPos < 0) {
      move(dx = grid.width, dy = -1)
    }

    this
  }

  def advance() =
    linearMove(1)

  def retreat() =
    linearMove(-1)

  def newLine(): TextCursor =
    moveTo(x = 0, y = yPos + 1)

  def backspace() =
    retreat().set(' ')

  def toLeftMargin =
    moveTo(x = 0)

  def toRightMargin =
    moveTo(x = grid.width - 1)
   
}

trait GridCursor extends GenCursor[GridCursor] {

  def write(src: CharGrid): GridCursor = {
    for {
      dx <- 0 until src.width
      dy <- 0 until src.height
    } grid.setChar(xPos + dx, yPos + dy, src.charAt(dx, dy))
    this
  }

}

object Cursor {

  def raw(base: CharGrid): RawCursor = new GenCursor[RawCursor](base) with RawCursor

  def text(base: CharGrid): TextCursor = new GenCursor[TextCursor](base) with TextCursor

  def grid(base: CharGrid): GridCursor = new GenCursor[GridCursor](base) with GridCursor

}