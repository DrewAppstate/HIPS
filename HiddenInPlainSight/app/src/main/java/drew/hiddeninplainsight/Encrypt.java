package drew.hiddeninplainsight;

/**
 * Created by drew.
 */

import android.support.v7.app.AppCompatActivity;

public class Encrypt extends AppCompatActivity {
    /**
     *
     * @param x is the string to be converted
     * @return string converted into pig latin
     */

    public String convertAlg(String x)
    {
        // Rules:
        //  First character is vowel add -yay at the end
        //  Take first set of constants and move to the end and add ay
        String y = "";
        String vowels = "aeiou";
        int firstV = 0;
        boolean flag = false;
        String a = x.toLowerCase();
        // For loop to go through given string and get to first vowel
        for (int i = 0; i < x.length(); i++)
        {
            char b = a.charAt(i);
            for (int j = 0; j < vowels.length(); j++)
            {
                if (b == vowels.charAt(j) && i == 0)
                {
                    firstV = i;
                    flag = true;
                }
                else if (b == vowels.charAt(j) && !flag)
                {
                    firstV = i;
                    flag = true;
                }
            }
        }

        if (firstV == 0)
        {
            y = x + "-ay";
        }
        else
        {
            y = x.substring(firstV) + "-" + x.substring(0, firstV) + "ay";
        }

        return y;
    }

    /**
     * Takes a given string and then converts it into a pig latin string
     * by using the convert alg
     * @param s given string to be converted
     * @return converted string
     */
    public String convert(String s)
    {
        String[] strArr = s.split(" ");
        String output = "";
        for(int i = 0; i < strArr.length; i++)
        {
            output = output + convertAlg(strArr[i]) + " ";
        }
        return output;

    }

}