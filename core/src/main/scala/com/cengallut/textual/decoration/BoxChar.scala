package com.cengallut.textual.decoration

object BoxChar {

  trait Corner {
    def topLeft: Char
    def topRight: Char
    def bottomRight: Char
    def bottomLeft: Char
  }

  object Corner {

    val Single = new Corner {
      def topLeft     = '\u250c'
      def topRight    = '\u2510'
      def bottomRight = '\u2518'
      def bottomLeft  = '\u2514'
    }

    val Thick = new Corner {
      def topLeft     = '\u250f'
      def topRight    = '\u2513'
      def bottomRight = '\u251b'
      def bottomLeft  = '\u2517'
    }

    val Double = new Corner {
      def topLeft     = '\u2554'
      def topRight    = '\u2557'
      def bottomRight = '\u255d'
      def bottomLeft  = '\u255a'
    }

    val Rounded = new Corner {
      def topLeft     = '\u256d'
      def topRight    = '\u256e'
      def bottomRight = '\u256f'
      def bottomLeft  = '\u2570'
    }

  }

  trait Line {
    def horizontal: Char
    def vertical: Char
  }

  object Line {

    object Single extends Line {
      def horizontal = '\u2500'
      def vertical   = '\u2502'
    }

    object Thick extends Line {
      def horizontal = '\u2501'
      def vertical   = '\u2503'
    }

    object Double extends Line {
      def horizontal = '\u2550'
      def vertical   = '\u2551'
    }

    object Dash2 extends Line {
      def horizontal = '\u254c'
      def vertical   = '\u254e'
    }

    object Dash2Thick extends Line {
      def horizontal = '\u254d'
      def vertical   = '\u254f'
    }

    object Dash3 extends Line {
      def horizontal = '\u2504'
      def vertical   = '\u2506'
    }

    object Dash3Thick extends Line {
      def horizontal = '\u2505'
      def vertical   = '\u2507'
    }

    object Dash4 extends Line {
      def horizontal = '\u2508'
      def vertical   = '\u250a'
    }

    object Dash4Thick extends Line {
      def horizontal = '\u2509'
      def vertical   = '\u250b'
    }

  }

  object Shade {
    def light  = '\u2591'
    def medium = '\u2592'
    def heavy  = '\u2593'

    def forwardChecker = '\u259e'
    def reverseChecker = '\u259a'
  }

  class Box(val corner: Corner, val line: Line)
  
  object Box {
    def single = new Box(Corner.Single, Line.Single)
    def thick  = new Box(Corner.Thick,  Line.Thick)
    def twin = new Box(Corner.Double, Line.Double)

    def dash2 = new Box(Corner.Single, Line.Dash2)
    def dash3 = new Box(Corner.Single, Line.Dash3)
    def dash4 = new Box(Corner.Single, Line.Dash4)

    def dash2thick = new Box(Corner.Thick, Line.Dash2Thick)
    def dash3thick = new Box(Corner.Thick, Line.Dash3Thick)
    def dash4thick = new Box(Corner.Thick, Line.Dash4Thick)

    def rounded = new Box(Corner.Rounded, Line.Single)
  }

}