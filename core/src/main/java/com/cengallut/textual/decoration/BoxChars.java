package com.cengallut.textual.decoration;

/** Java convenient access. */
public class BoxChars {

    public static class Corners {
        public static final BoxChar.Corner Single  = BoxChar.Corner$.MODULE$.Single();
        public static final BoxChar.Corner Thick   = BoxChar.Corner$.MODULE$.Thick();
        public static final BoxChar.Corner Double  = BoxChar.Corner$.MODULE$.Double();
        public static final BoxChar.Corner Rounded = BoxChar.Corner$.MODULE$.Rounded();
    }

    public static final BoxChar.Line$ Lines = BoxChar.Line$.MODULE$;

    public static final BoxChar.Box$ Boxes = BoxChar.Box$.MODULE$;

    public static final BoxChar.Shade$ Shades = BoxChar.Shade$.MODULE$;

}
