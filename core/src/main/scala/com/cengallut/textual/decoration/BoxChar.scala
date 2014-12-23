package com.cengallut.textual.decoration

object BoxChar {

  trait Corner {
    def topLeft: Char
    def topRight: Char
    def bottomRight: Char
    def bottomLeft: Char
  }

  object Corner {

    object Single extends Corner {
      val topLeft     = '\u250c'
      val topRight    = '\u2510'
      val bottomRight = '\u2518'
      val bottomLeft  = '\u2514'
    }

    object Thick extends Corner {
      val topLeft     = '\u250f'
      val topRight    = '\u2513'
      val bottomRight = '\u251b'
      val bottomLeft  = '\u2517'
    }

    object Double extends Corner {
      val topLeft     = '\u2554'
      val topRight    = '\u2557'
      val bottomRight = '\u255d'
      val bottomLeft  = '\u255a'
    }

    object Rounded extends Corner {
      val topLeft     = '\u256d'
      val topRight    = '\u256e'
      val bottomRight = '\u256f'
      val bottomLeft  = '\u2570'
    }

  }

  trait Line {
    def horizontal: Char
    def vertical: Char
  }

  object Line {

    object Single extends Line {
      val horizontal = '\u2500'
      val vertical   = '\u2502'
    }

    object Thick extends Line {
      val horizontal = '\u2501'
      val vertical   = '\u2503'
    }

    object Double extends Line {
      val horizontal = '\u2550'
      val vertical   = '\u2551'
    }

    object Dash2 extends Line {
      val horizontal = '\u254c'
      val vertical   = '\u254e'
    }

    object Dash2Thick extends Line {
      val horizontal = '\u254d'
      val vertical   = '\u254f'
    }

    object Dash3 extends Line {
      val horizontal = '\u2504'
      val vertical   = '\u2506'
    }

    object Dash3Thick extends Line {
      val horizontal = '\u2505'
      val vertical   = '\u2507'
    }

    object Dash4 extends Line {
      val horizontal = '\u2508'
      val vertical   = '\u250a'
    }

    object Dash4Thick extends Line {
      val horizontal = '\u2509'
      val vertical   = '\u250b'
    }

  }

  object Shade {
    val light  = '\u2591'
    val medium = '\u2592'
    val heavy  = '\u2593'

    val forwardChecker = '\u259e'
    val reverseChecker = '\u259a'
  }

  class Box(val corner: Corner, val line: Line)
  
  object Box {
    val single = new Box(Corner.Single, Line.Single)
    val thick  = new Box(Corner.Thick,  Line.Thick)
    val double = new Box(Corner.Double, Line.Double)

    val dash2 = new Box(Corner.Single, Line.Dash2)
    val dash3 = new Box(Corner.Single, Line.Dash3)
    val dash4 = new Box(Corner.Single, Line.Dash4)

    val dash2thick = new Box(Corner.Thick, Line.Dash2Thick)
    val dash3thick = new Box(Corner.Thick, Line.Dash3Thick)
    val dash4thick = new Box(Corner.Thick, Line.Dash4Thick)

    val rounded = new Box(Corner.Rounded, Line.Single)
  }

}