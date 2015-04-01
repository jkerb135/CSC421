/**
 * Created by Josh on 2/11/2015.
 */
// File: InputReader.java
// Methods for Reading char and int
// Package implemented. java directory in classpath. Package is named
//	according to subdirectories of that directory.

package Input;

import java.io.*;

public class InputReader {

    public static char readChar() throws java.io.IOException
    { BufferedReader DataIn=new BufferedReader(new InputStreamReader(System.in));
        String Line;
        do
            Line=DataIn.readLine().trim();
        while (Line.length()==0);
        return((Line.toUpperCase().toCharArray())[0]);
    }

    public static char getChoice(String OK)  throws java.io.IOException
    {char Reply;
        do {
            Reply=readChar();
        } while (OK.indexOf(Reply,0)==-1);
        return(Reply);
    }

    public static int readInt() throws java.io.IOException
    {BufferedReader DataIn = new BufferedReader(new InputStreamReader(System.in));
        String Input=DataIn.readLine();
        return (Integer.valueOf(Input).intValue());
    }

} // end of ReadInput Class
