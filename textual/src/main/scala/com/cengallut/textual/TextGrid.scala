package com.cengallut.textual

import android.content.Context
import android.graphics._
import com.cengallut.textual.aside.Sugar._
import com.cengallut.textual.basic.{Cursor, WritableBuffer}

/** Displays an array of characters as a grid throughout its rectangle. */
abstract class TextGrid(context: Context)
    extends android.view.View(context)
    with WritableBuffer {

  def textSize: Float

  def textColor: Int

  def background: Int

  lazy val cursor: Cursor = Cursor.base(this)

  /** The array of characters that will fill the entire View rectangle. */
  protected var buffer = Array.empty[Char]

  /** The dimension of the grid of characters that make up the window.
    * dimension.x * dimension.y == buffer.length */
  protected var dimension = (0,0)

  /** Holds the information needed to draw characters onto the canvas. This object is used to
    * calculate the individual sizes of each character, and so determines how big of a buffer is
    * needed to fill a screen. */
  private val paint = {
    val p = new Paint
    p.setTextSize(textSize)
    p.setColor(textColor)
    p.setTypeface(Typeface.MONOSPACE)
    p
  }

  /** Calculate the maximum bounding box for a character when it is drawn on a canvas, using
    * the paint object created previously. This dimension includes ascenders and descenders. */
  private val charDimension = {
    val rect = new Rect
    paint.getTextBounds(Array('j', 'k'), 0, 2, rect)
    val charHeight = rect.height()
    paint.getTextBounds(Array('m'), 0, 1, rect)
    val charWidth = rect.width()
    (charWidth, charHeight)
  }

  override /* android.view.View */
  protected def onSizeChanged(w: Int, h: Int, ow: Int, oh: Int): Unit = {
    dimension = (w / charDimension.x, h / charDimension.y)
    buffer = Array.ofDim(dimension.x * dimension.y)
  }

  override /* android.view.View */
  protected def onDraw(c: Canvas): Unit = {
    c.drawColor(background)

    val charBuffer = Array('c')
    var (x, y) = dimension
    val xDelta = getWidth.toFloat / x
    val yDelta = getHeight.toFloat / y

    while (y > 0) {
      y -= 1
      x = dimension.x
      while (x > 0) {
        x -= 1

        charBuffer(0) = charAt(x, y)
        c.drawText(charBuffer, 0, 1, xDelta*x, yDelta*(y+1), paint)
      }
    }
  }

}




