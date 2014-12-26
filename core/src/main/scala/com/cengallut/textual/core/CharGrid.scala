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

object CharGrid extends GridFactory {

  implicit def prototypeFromGrid(g: CharGrid): PrototypeWrapper =
    new PrototypeWrapper(g)

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

  private[core] class BasicGrid(xDim: Int, yDim: Int) extends CharGrid {

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

  private[core] class GridView(
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
