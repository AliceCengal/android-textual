package com.cengallut.textual

import android.content.Context
import android.graphics._
import com.cengallut.textual.aside.Sugar._
import com.cengallut.textual.basic.WritableBuffer.UpdateListener
import com.cengallut.textual.basic.{Cursor, WritableBuffer}

/** Displays an array of characters as a grid throughout its rectangle. */
abstract class TextGrid(context: Context)
    extends android.view.View(context)
    with WritableBuffer.UpdateListener {

  def textSize: Float

  def textColor: Int

  def background: Int

  private[textual] var buffer = WritableBuffer.zero

  def getBuffer: WritableBuffer = buffer

  def setBuffer(b: WritableBuffer): Unit = {
    if (buffer.gridHeight == b.gridHeight && buffer.gridWidth == b.gridWidth) {
      buffer.setUpdateListener(UpdateListener())
      buffer = b
      buffer.setUpdateListener(this)
    } else
      throw new IllegalArgumentException("Must input a buffer with the same dimensions")
  }

  lazy val cursor: Cursor = Cursor.base(buffer)

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


  override /* WritableBuffer.UpdateListener */
  def onBufferUpdated(): Unit = invalidate()

  override /* android.view.View */
  protected def onSizeChanged(w: Int, h: Int, ow: Int, oh: Int): Unit = {
    buffer = WritableBuffer.ofDim(w / charDimension.x, h / charDimension.y)
    buffer.setUpdateListener(this)
  }

  override /* android.view.View */
  protected def onDraw(c: Canvas): Unit = {
    c.drawColor(background)

    val charBuffer = Array('c')
    var x = buffer.gridWidth
    var y = buffer.gridHeight
    val xDelta = getWidth.toFloat / x
    val yDelta = getHeight.toFloat / y

    while (y > 0) {
      y -= 1
      x = buffer.gridWidth
      while (x > 0) {
        x -= 1

        charBuffer(0) = buffer.charAt(x, y)
        c.drawText(charBuffer, 0, 1, xDelta*x, yDelta*(y+1), paint)
      }
    }
  }

}




