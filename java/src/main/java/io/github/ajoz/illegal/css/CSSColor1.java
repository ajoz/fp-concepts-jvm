package io.github.ajoz.illegal.css;

/*
 Let's imagine we are developing some code that is related to
 how CSS is processed. We would like to have a representation
 of CSS types available for us.

 Just by going through page:
 https://developer.mozilla.org/en-US/docs/Web/CSS/color_value

 we can clearly see that the color can be represented in
 multiple ways:
 - named e.g. "white", "red", "green" etc
 - hexadecimal e.g. #ffffff, #ff0000, #00ff00
 - rgb e.g. "rgb(255, 255, 255)", "rgb(255, 0, 0)"
 - rgba e.g. "rgba(255, 255, 255, 0)
 - hsl e.g. "hsl(360, 100%, 50%)"
 - hsla e.g. "hsla(120, 100%, 50%, 0.3)"
 - currentcolor refers to the value of the color property of the current element

A type representing such diverse thing most probably would look like:
 */
public class CSSColor1 {
    // named
    public String name;
    // hexadecimal string
    public String hexColor;
    // rgb + rgba
    public int red;
    public int green;
    public int blue;
    public float alpha;
    // hsl + hsla
    public int hue;
    public int saturation;
    public int value;
    public float alpha2;
    // current color
    public boolean isCurrentColor;
}
/*
  There are obvious issues with this code:
  - the name attribute can be anything even an empty string
  - the hexadecimal representation of the string can also be anything or empty
  - it is possible to use a negative value for every component of rgb and hsl
  - it is possible to use a value outside of 0 - 255 range for rgb
  - it is possible to use a value outside of 0 - 360 range for hue
  - it is possible to use a value outside of 0 - 100% for saturation and value
  - it is possible to use a value outside of 0.0 - 1.0 range for alpha
  - nothing prevents for any numeric value to be negative
  - nothing prevents for the color to be marked as isCurrentColor

  How is this usually solved?
  - with a use of null, for example for the name and hexColor
  - with a use of a negative value that represents a non existing value
    most probably as a field int UNKNOWN = -1
 */

class CSSColor1B {
    public static final int UNKNOWN = -1;
    // named
    public String name = null;
    // hexadecimal string
    public String hexColor = null;
    // rgb + rgba
    public int red = UNKNOWN;
    public int green = UNKNOWN;
    public int blue = UNKNOWN;
    public float alpha = UNKNOWN;
    // hsl + hsla
    public int hue = UNKNOWN;
    public int saturation = UNKNOWN;
    public int value = UNKNOWN;
    public float alpha2 = UNKNOWN;
    // current color
    public boolean isCurrentColor = false;
}

/*
  Working with such thing is tedious, although its possible to check if
  the value is UNKNOWN, but nothing prevents the code to be partially
  instantiated. For example having red = 255, green = 0 and blue = UNKNOWN.

  There is no way to indicate that the fields should be updated together etc.

  It is advised to create types that would narrow the possible values.q
 */
