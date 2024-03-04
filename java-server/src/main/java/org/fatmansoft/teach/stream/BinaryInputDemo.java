package org.fatmansoft.teach.stream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class BinaryInputDemo
{
  public static void main(String[] args)
  {
     try
     {
         ObjectInputStream inputStream =
           new ObjectInputStream(new FileInputStream("numbers.dat"));
         int n;
         System.out.println("Reading the nonnegative integers");
         System.out.println("in the file numbers.dat.");
         n = inputStream.readInt( );
         while (n >= 0)
         {
             System.out.println(n);
             n = inputStream.readInt( );
         }

         System.out.println("End of reading from file.");
         inputStream.close( );
     }
     catch(FileNotFoundException e)
     {
         System.out.println("Cannot find file numbers.dat.");
     }
     catch(IOException e)
     {
         System.out.println("Problem with input from file numbers.dat.");
     }
  }
}
