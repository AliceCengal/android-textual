package com.cengallut.textual.core

trait FilterMap {
  def filter(x: Int, y: Int): Boolean
  def map(x: Int, y: Int): (Int,Int)
}

object FilterMap {

  val identity: FilterMap = new FilterMap {
    override def filter(x: Int, y: Int): Boolean = true
    override def map(x: Int, y: Int): (Int, Int) = (x, y)
  }

}