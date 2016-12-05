package drew.hiddeninplainsight;

import android.graphics.Bitmap;
import android.graphics.Color;


/**
 * Created by drew.
 *
 * This class is used to implement steganography.
 * Steganography is inserting data into data. In this
 * case I am storing text inside of  an image. There
 * is a encode and decode method. There is also a
 * method that converts strings to binary format.
 *
 */
public class Steg {

    static String encodedKey = "/nDr3w!CaP$t0n3";
    static String stopKey = "~~~";

    /*
     * Converts a string into binary representation
     * @return string of binary representation
     */
    public String stringToBinary(String str)
    {


        String result = "";
        String tmpStr;
        int tmpInt;
        char[] messChar = str.toCharArray();
        int bits = 8;

        for (int i = 0; i < messChar.length; i++) {
            tmpStr = Integer.toBinaryString(messChar[i]);
            tmpInt = tmpStr.length();
            if(tmpInt != bits) {
                tmpInt = bits - tmpInt;
                if (tmpInt == bits) {
                    result += tmpStr;
                } else if (tmpInt > 0) {
                    for (int j = 0; j < tmpInt; j++) {
                        result += "0";
                    }
                    result += tmpStr;
                } else {
                    System.err.println("argument 'bits' is too small");
                }
            } else {
                result += tmpStr;
            }
        }

        return result;


    }

    /**
     * This method takes a picture in the form of a
     * bitmap and a string of text and inserts the
     * message inside the picture. It does this by
     * changing the last bit of each pixel to a bit
     * of the message.
     */
    public void encodePicture(Bitmap bp, String message) {
        message = message + stopKey;

        // Converts message into binary form.
        String strToBinary = stringToBinary(message);

        int red = 0, blue = 0, green = 0, index, p, height, width, counter = 0, max;

        // Get the pictures height and width
        // need to know for looping through pixels
        height = bp.getHeight();
        width = bp.getWidth();

        // Gets the length of the string in binary form.
        max = strToBinary.length();

        int[] pix = new int[width * height];
        bp.getPixels(pix, 0, width, 0, 0, width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                index = y * width + x;
                p = pix[index];
                // bitwise shifting
                red = (p & 0xff0000) >> 16;
                green = (p & 0xff00) >> 8;
                blue = p & 0xff;
                if (counter < (max - 1)) {
                    blue = blue >> 1;
                    blue = blue << 1;
                    blue = blue ^ Character.getNumericValue(strToBinary.charAt(counter));

                    // Just to demonstrate a change in the picture
                    //bp.setPixel(x, y, Color.BLUE);
                }
                if (counter > max)
                {
                    break;
                }

                //Sets the pixel to the changed pixel
                bp.setPixel(x, y, Color.rgb(red, green, blue));
                counter++;
            }
        }

        counter = 0;
        int size = height * width;
        String encodedBinaryKey = stringToBinary(encodedKey);
        int pixelStart = size - encodedBinaryKey.length();
        int row = pixelStart / width;
        int col = pixelStart % width;
        for (int r = row; r < height; r++)
        {
            for (int c = col; c < width; c++)
            {
                index = row * width + col;
                p = pix[index];
                blue = p & 0xff;
                blue = blue >> 1;
                blue = blue << 1;
                blue = blue ^ Character.getNumericValue(encodedBinaryKey.charAt(counter));

                bp.setPixel(col, row, Color.rgb(red, green, blue));
                counter++;
            }

        }
    }

    public String decodePicture(Bitmap bp) {

        if (hasMessage(bp))
        {
            String message = "", temp = "", output;
            String binaryEnd = stringToBinary(stopKey);
            int blue = 0, index, p, height, width, counter = 0, endPoint;

            height = bp.getHeight();
            width = bp.getWidth();
            endPoint = binaryEnd.length();

            int[] pix = new int[width * height];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {

                    index = y * width + x;
                    p = pix[index];
                    blue = p & 0xff;
                    message += blue & 0x01;

                    if(index == endPoint)
                    {
                        if (message == binaryEnd)
                        {
                            return "No Real Message";
                        }
                    }
                    else if (index > endPoint)
                    {
                        temp = message.substring(message.length() - endPoint, message.length());
                        if (temp == binaryEnd)
                        {
                            output = message.substring(0, message.length() - endPoint);

                            return int2str(output);
                        }
                    }
                }

            }

            return int2str(message);
        }
        else
        {
            return "No Hidden Message";
        }
    }

    public static String int2str( String s ) {
        String temp;
        String output = "";
        for (int i = 0; i < s.length(); i+=8)
        {
            temp = s.substring(i, i+8);
            int ascii = Integer.parseInt(temp,2);
            char out = (char)ascii;
            output += out;
        }
        return (s + " " + output);
    }
    public boolean hasMessage(Bitmap bp)
    {
        String encodedBinaryKey = stringToBinary(encodedKey);
        String str = "";
        int height = bp.getHeight();
        int width = bp.getWidth();
        int size = height * width;
        int pixelStart = size - encodedBinaryKey.length();
        int row = pixelStart / width;
        int col = pixelStart % width;
        int index, p, blue;

        int[] pix = new int[width * height];
        bp.getPixels(pix, 0, width, 0, 0, width, height);
        for (int r = row; r < height; r++)
        {
            for (int c = col; c < width; c++)
            {
                index = row * width + col;
                p = pix[index];
                blue = p & 0xff;
                str += blue & 0x01;
            }

        }
        return (str == encodedBinaryKey);
    }
}
