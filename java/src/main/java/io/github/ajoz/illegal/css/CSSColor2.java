package io.github.ajoz.illegal.css;

import io.github.ajoz.util.Try;

/*
  To make the example easier we will narrow the fields to only
  the RGB, RGBA, named possibilities

  First let's create types that will represent the values:
 */
final class RGBComponent {
    public final int value;

    private RGBComponent(final int value) {
        this.value = value;
    }

    public static Try<RGBComponent> tryParse(final int value) {
        if (value < 0 || value > 255) {
            return Try.failure(new IllegalArgumentException("RGBComponent can be created from value between 0 - 255 but was: " + value));
        }
        return Try.success(new RGBComponent(value));
    }
}

// type representing alpha component
final class Alpha {
    public final float value;

    private Alpha(final float value) {
        this.value = value;
    }

    public static Try<Alpha> tryParse(final float value) {
        if (value < 0.0 || value > 1.0) {
            return Try.failure(new IllegalArgumentException("Alpha can be created from value between 0.0 - 1.0 but was: " + value));
        }
        return Try.success(new Alpha(value));
    }
}

// type representing named color
final class ColorName {
    public final String value;

    ColorName(final String value) {
        this.value = value;
    }

    public static Try<ColorName> tryParse(final String name) {
        // if the name is not recognized
        // return Try.failure(new IllegalArgumentException("ColorName not recognized, was: " + name));
        // if it is recognized then:
        return Try.success(new ColorName(name));
    }
}

/*
  Thanks to the newly created types we narrowed the possible values
  Code is a bit more readable because the types do indicate what one
  can expect from them
 */
public class CSSColor2 {
    public ColorName name = null;
    public RGBComponent red = null;
    public RGBComponent green = null;
    public RGBComponent blue = null;
    public Alpha alpha = null;
}

/*
  Although we do not need the UNKNOWN constant anymore, we still
  did not eliminate the null. Nothing prevents to have a CSSColor
  with a ColorName and Alpha or red and green but missing blue.

  We need to group it further through types.
 */

