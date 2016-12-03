package drew.hiddeninplainsight;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.io.File;

/**
 * Created by drew.
 */
public class Steg {

    /*
     * Converts a string into binary representation
     * @return string of binary representation
     */

    public String stringToBinary(String str)
    {
        char[] charArray = str.toCharArray();
        StringBuilder sb = new StringBuilder();

        for (char c:charArray)
        {
            String cBinaryString =  Integer.toBinaryString((int)c);
            sb.append(cBinaryString);
        }
        return sb.toString();
    }

    public void encodePicture(Bitmap bp, String message) {
        //Key to know if picture is encrypted
        ///nDr3w!CaP$t0n3 is 99 bits long
        message = message + "/nDr3w!CaP$t0n3";
        String strToBinary = stringToBinary(message);

        int red = 0, blue = 0, green = 0, index, p, height, width, counter = 0, max;
        height = bp.getHeight();
        width = bp.getWidth();
        max = strToBinary.length();

        int[] pix = new int[width * height];
        bp.getPixels(pix, 0, width, 0, 0, width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                index = y * width + x;
                p = pix[index];
                red = (p & 0xff0000) >> 16;    //bitwise shifting
                green = (p & 0xff00) >> 8;
                blue = p & 0xff;
                if (counter < (max - 1)) {
                    blue = blue >> 1;
                    blue = blue << 1;
                    blue = blue ^ Character.getNumericValue(strToBinary.charAt(counter));

                    // Just to demonstrate a change in the picture
                    bp.setPixel(x, y, Color.BLUE);
                }

                //Sets the pixel to the changed pixel
                //bp.setPixel(x, y, Color.rgb(red, green, blue));
                counter++;
            }
        }
    }

    public String decodePicture(Bitmap bp) {
        //Key to know if picture is encrypted
        //"/nDr3w!CaP$t0n3" is 99 bits long

        String key = stringToBinary("/nDr3w!CaP$t0n3");
        String message = "";

        int red = 0, blue = 0, green = 0, index, p, height, width, counter = 0, max;
        height = bp.getHeight();
        width = bp.getWidth();
        max = key.length();

        int[] pix = new int[width * height];
        bp.getPixels(pix, 0, width, 0, 0, width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                index = y * width + x;
                p = pix[index];
                red = (p & 0xff0000) >> 16;
                green = (p & 0xff00) >> 8;
                blue = p & 0xff;
                message += (blue << 31);
                if (index >= 99)
                {
                    if (index == 99)
                    {
                        message.compareTo(key);
                    }
                    else if (index > 99)
                    {
                        String a = message.substring(message.length() - 99);
                        //word.substring(word.length() - 3);
                        a.compareTo(key);
                    }
                }
            }
        }
        return key;
    }
    /*
        {
        String str = "/n Hello how are you";
        char[] charArray = str.toCharArray();
        StringBuilder sb = new StringBuilder();

        for (char c:charArray)
        {
            String cBinaryString =  Integer.toBinaryString((int)c);
            sb.append(cBinaryString);
        }
        String strToBinary = sb.toString();

        File f = new File("A.png");

        BufferedImage image = ImageIO.read(f);
        int height = image.getHeight();
        int width = image.getWidth();
        int red = 0;
        int blue = 0;
        int green = 0;
        int color = 0;
       //int[][] result = new int[height][width];

        System.out.println("Height:" + height + ", Width:" + width);

        Color changedColor;

        int max = strToBinary.length();
        int counter = 0;
        System.out.println(max);
        for (int row = 0; row < height; row++)
        {
            for (int col = 0; col < width; col++)
            {

                color = image.getRGB(col, row);
                blue = (color & 0xff);
                green = (color & 0xff00) >> 8;
                red = (color & 0xff0000) >> 16;
                if (counter != max)
                {
                    blue = blue >> 1;
                    blue = blue << 1;
                    blue = blue ^ Character.getNumericValue(strToBinary.charAt(counter));
                }
                changedColor = new Color(red, green, blue);

                image.setRGB(col, row, changedColor.getRGB());
            }
        }
            // retrieve image
        File outputfile = new File("saved.png");
        ImageIO.write(image, "png", outputfile);
    }
     */
}
