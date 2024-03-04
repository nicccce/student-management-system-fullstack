package org.fatmansoft.teach.stream;

/**
 Demonstrates use of the class File with text files.
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class FileClassDemo
{
    public static void main(String[] args)
    {
        String name = null;

    	Scanner scanner = new Scanner(System.in);
        System.out.println("I will show you the first line");
        System.out.println("in a text file you name.");
        System.out.println("The file must contain one or more lines.");

        System.out.println("Enter file name:");
        name = scanner.nextLine();
        File fileObject = new File(name);
        while (( ! fileObject.exists( ))
                          || ( ! fileObject.canRead( )))
        {
           if ( ! fileObject.exists( ))
              System.out.println("No such file");
           else if ( ! fileObject.canRead( ))
              System.out.println("That file is not readable.");
           System.out.println("Enter file name again:");
           name = scanner.nextLine();
           fileObject = new File(name);
        }

        try
        {
           BufferedReader fileInput =
                  new BufferedReader(new FileReader(name));
           System.out.println("The first line in the file is:");
           while(true) {
               String firstLine = fileInput.readLine();
               System.out.println(firstLine);
               if (firstLine == null)
                   break;
           }
           fileInput.close( );
       }
       catch(IOException e)
       {
          System.out.println("Problem reading from file.");
       }
    }
}
