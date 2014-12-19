package com.cengallut.textual.decoration

import com.cengallut.textual.core.WritableBuffer

class Fill(c: Char) extends Decoration {
  override def decorate(buffer: WritableBuffer): Unit = {
    for {
      x <- 0 until buffer.gridWidth
      y <- 0 until buffer.gridHeight
    } buffer.setChar(x, y, c)
  }
}