package com.cengallut.textual.basic

trait WritableBuffer {

  protected def buffer: Array[Char]

  protected def dimension: (Int,Int)

  def gridWidth = dimension._1

  def gridHeight = dimension._2

  def setChar(x: Int, y: Int, c: Char): Unit = {
    if (isInRange(x, y))
      buffer(x + (y * gridWidth)) = c
  }

  def charAt(x: Int, y: Int) = {
    assert(isInRange(x, y), "index out of bound")
    buffer(x + (y * gridWidth))
  }

  def isInRange(x: Int, y: Int) =
    (x >= 0 && x < gridWidth) && (y >= 0 && y < gridHeight)

}