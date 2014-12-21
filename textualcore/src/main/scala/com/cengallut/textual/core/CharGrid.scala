package com.cengallut.textual.core

/** The central abstraction of this library is a 2D array of chars. A CharGrid
  * has a fixed dimension, given by the number of chars on its sides. The grid of chars can
  * be indexed into.
  *
  * There are two implementations of this interface. The first is a concrete implementation
  * that actually contains an underlying representation of some number of chars somewhere
  * in memory. This is the implementation returned by the factory methods defined in the
  * companion object.
  *
  * The second implementation is a "view", or a window into another Grid. This is the
  * implementation returned by the subviewing methods defined in the Prototype object.
  * The view allows a subset of the parent Grid to be treated as if it is a true Grid.
  * Border drawing and other decorations should work on a sub-grid just as it works on
  * a true Grid.
  *
  * Since the sub-grid would have its own coordinate system and its own bounds, the
  * FilterMap object is used to help delegate touch and click event from the original
  * UI element to all child Grids.
  */
trait CharGrid {

  /** Notifies listeners that the content of this buffer has changed. This method
    * should be called after all batch of modifications to the Grid. Renderers of
    * this Grid should set itself as a listener so that the Grid can be redrawn. */
  def notifyChanged(): Unit

  /** Sets listener to receive an update when the Grid is modified. */
  def setUpdateListener(listener: CharGrid.UpdateListener): Unit

  def width: Int

  def height: Int

  def setChar(x: Int, y: Int, c: Char): Unit

  def charAt(x: Int, y: Int): Char

  def isInRange(x: Int, y: Int) =
    (x >= 0 && x < width) && (y >= 0 && y < height)

  def filterMap: FilterMap

}

object CharGrid {

  /** Returns a Grid of dimension 0x0. */
  val zero: CharGrid = CharGrid.ofDim(0, 0)

  /** Returns a Grid of the specified. */
  def ofDim(xDim: Int, yDim: Int): CharGrid =
    new BasicGrid(xDim, yDim)

  trait UpdateListener {
    def onGridUpdated(): Unit
  }

  object UpdateListener {
    def apply(listener: ()=>Unit): UpdateListener = new UpdateListener {
      override def onGridUpdated(): Unit = listener()
    }

    def apply(): UpdateListener = new UpdateListener {
      override def onGridUpdated(): Unit = ()
    }
  }

  /** Wrapper class for adding extension methods for cloning and sub-viewing
    * a CharGrid. */
  implicit class Prototype(val b: CharGrid) extends AnyVal {

    /** Returns a view into the current Grid.
      *
      * offset_1 :: left
      * offset_2 :: top
      * offset_3 :: width
      * offset_4 :: height
      *
      * if b is a 6x5 buffer:
      *
      *     ######
      *     ######
      *     ######
      *     ######
      *     ######
      *
      * Then calling b.takeView(1, 2, 3, 2) return a view to
      * the region marked as 'O'
      *
      *     ######
      *     ######
      *     #OOO##
      *     #OOO##
      *     ######
      *
      * The returned Grid7 shares the underlying representation
      * with the original Grid. */
    def takeView(offset: (Int,Int,Int,Int)): CharGrid =
      new GridView(b, offset)

    /** Same as before. */
    def takeView(left: Int, top: Int, width: Int, height: Int): CharGrid =
      new GridView(b, (left, top, width, height))

    /** Returns a view into the current buffer with the given distance from
      * the edges, in the order (left, top, right, bottom)
      *
      * Calling b.shrink(1, 2, 1, 1) yields the following region marked as 'O'
      *
      *     ######
      *     ######
      *     #OOOO#
      *     #OOOO#
      *     ######
      *
      * The returned Buffer view shares the underlying representation
      * with the original Buffer. */
    def shrink(offset: (Int,Int,Int,Int)): CharGrid = {
      val width = b.width - offset._3 - offset._1
      val height = b.height - offset._4 - offset._2
      new GridView(b, offset.copy(_3 = width, _4 = height))
    }

    /** Returns two Buffers. */
    def verticalBisect: (CharGrid,CharGrid) = {
      val (rightOffset, leftOffset) = partitionLength(b.width, 0.5)
      val left = shrink(0, 0, leftOffset, 0)
      val right = shrink(rightOffset, 0, 0, 0)
      (left, right)
    }

    /** Returns two Buffers. */
    def horizontalBisect: (CharGrid,CharGrid) = {
      val (bottomOffset, topOffset) = partitionLength(b.height, 0.5)
      val top = shrink(0, topOffset, 0, 0)
      val bottom = shrink(0, 0, 0, bottomOffset)
      (top, bottom)
    }

    /** Returns two Buffers. */
    def verticalSplit(ratio: Double): (CharGrid,CharGrid) = {
      val (bottomOffset, topOffset) = partitionLength(b.height, ratio)
      val top = shrink(0, topOffset, 0, 0)
      val bottom = shrink(0, 0, 0, bottomOffset)
      (top, bottom)
    }

    /** Returns two Buffers. */
    def horizontalSplit(ratio: Double): (CharGrid,CharGrid) = {
      val (bottomOffset, topOffset) = partitionLength(b.height, ratio)
      val top = shrink(0, topOffset, 0, 0)
      val bottom = shrink(0, 0, 0, bottomOffset)
      (top, bottom)
    }

    /** Returns a pair (n1,n2) such that n1 + n2 == l  and n1/l == ratio */
    private def partitionLength(l: Int, ratio: Double): (Int,Int) = {
      val first = (l * ratio).ceil.toInt
      ( first , l - first )
    }

    /** Creates a new Buffer with a separate underlying representation
      * but the same content and same dimension. Any modification made to the new Buffer
      * does not affect the original, and vice versa. */
    def copy: CharGrid = {
      val bNew = new BasicGrid(b.width, b.height)
      for {
        x <- 0 until b.width
        y <- 0 until b.height
      } bNew.setChar(x, y, b.charAt(x, y))
      bNew
    }

  }

  private class BasicGrid(xDim: Int, yDim: Int) extends CharGrid {

    /** The most important thing in the whole library, yet so innocuous. */
    private val buffer = Array.fill[Char](xDim * yDim)(' ')

    private var updateListener = UpdateListener()

    override def notifyChanged(): Unit = updateListener.onGridUpdated()

    override def charAt(x: Int, y: Int) =
      if (isInRange(x, y))
        buffer(x + (y * width))
      else
        throw new IndexOutOfBoundsException("Point is not on the Grid.")

    override def setUpdateListener(listener: UpdateListener): Unit =
      updateListener = listener

    override def width: Int = xDim

    override def setChar(x: Int, y: Int, c: Char): Unit =
      if (isInRange(x, y)) buffer(x + (y * width)) = c

    override def height: Int = yDim

    override def filterMap: FilterMap = FilterMap.identity
  }

  private class GridView(
      private val basis: CharGrid,
      private val bound: (Int,Int,Int,Int)) extends CharGrid {

    override def notifyChanged(): Unit =
      basis.notifyChanged()

    override val width: Int = bound._3

    override val height: Int = bound._4

    override def setChar(x: Int, y: Int, c: Char): Unit =
      basis.setChar(x + bound._1, y + bound._2, c)

    override def charAt(x: Int, y: Int): Char =
      basis.charAt(x + bound._1, y + bound._2)

    override def setUpdateListener(listener: UpdateListener): Unit =
      basis.setUpdateListener(listener)

    override val filterMap: FilterMap = new FilterMap {
      override def filter(x: Int, y: Int): Boolean = {
        val (xt, yt) = map(x, y)
        isInRange(xt, yt)
      }

      override def map(x: Int, y: Int): (Int, Int) =
        (x - bound._1, y - bound._2)
    }
  }

}
