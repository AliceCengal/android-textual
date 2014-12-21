package com.cengallut.textual.decoration

import com.cengallut.textual.core.CharGrid

class Fill(c: Char) extends Decoration {
  override def decorate(buffer: CharGrid): Unit = {
    for {
      x <- 0 until buffer.width
      y <- 0 until buffer.height
    } buffer.setChar(x, y, c)
  }
}