package com.cengallut.textual.decoration

import com.cengallut.textual.core.CharGrid

object Border {

  def simple(c: Char): Decoration = new Simple(c)

  def box(box: BoxChar.Box): Decoration = new Box(box)

  private class Simple(border: Char) extends Decoration {
    override def decorate(buffer: CharGrid): Unit = {
      for (x <- 0 until buffer.width) {
        buffer.setChar(x, 0, border)
        buffer.setChar(x, buffer.height - 1, border)
      }
      for (y <- 1 until buffer.height - 1) {
        buffer.setChar(0, y, border)
        buffer.setChar(buffer.width - 1, y, border)
      }
    }
  }

  private class Box(box: BoxChar.Box) extends Decoration {
    override def decorate(buffer: CharGrid): Unit = {
      buffer.setChar(0, 0, box.corner.topLeft)
      buffer.setChar(buffer.width - 1, 0, box.corner.topRight)
      buffer.setChar(buffer.width - 1, buffer.height - 1, box.corner.bottomRight)
      buffer.setChar(0, buffer.height - 1, box.corner.bottomLeft)

      for (x <- 1 until buffer.width - 1) {
        buffer.setChar(x, 0, box.line.horizontal)
        buffer.setChar(x, buffer.height - 1, box.line.horizontal)
      }
      for (y <- 1 until buffer.height - 1) {
        buffer.setChar(0, y, box.line.vertical)
        buffer.setChar(buffer.width - 1, y, box.line.vertical)
      }
    }
  }

  val lightShade = simple(BoxChar.Shade.light)

  val mediumShade = simple(BoxChar.Shade.medium)

  val heavyShade = simple(BoxChar.Shade.heavy)

}

