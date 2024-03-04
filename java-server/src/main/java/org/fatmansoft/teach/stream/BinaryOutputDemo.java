package org.fatmansoft.teach.stream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class BinaryOutputDemo
{
   public static void main(String[] args)
   {
      try
      {
          Scanner s = new Scanner(System.in);
    	  ObjectOutputStream outputStream =
             new ObjectOutputStream(new FileOutputStream("numbers.dat"));
         int n;

         System.out.println("Enter nonnegative integers, one per line.");
         System.out.println("Place a negative number at the end.");

         do
         {
            
        	 n = s.nextInt();
            outputStream.writeInt(n);
         }while (n >= 0);
        
         System.out.println("Numbers and sentinel value");
         System.out.println("written to the file numbers.dat.");
         outputStream.close( );
      }
      catch(IOException e)
      {
         System.out.println("Problem with output to file numbers.dat.");
      }
   }
}
