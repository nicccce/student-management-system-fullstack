package org.fatmansoft.teach.stream;

import java.io.*;

class Account implements Serializable{
    private String num;
    private Double money;
    public Account(String num, Double money){
        this.num = num;
        this.money = money;
    }
}
public class ObjectDemo {
    public static void main(String[] args) throws Exception {
        Account [] as= {new Account("123",35.0),new Account("li",80.0)};
        ObjectOutputStream outputStream =
                new ObjectOutputStream(new FileOutputStream("accounts.dat"));
        outputStream.writeObject(as);
        outputStream.close();
        ObjectInputStream inputStream =
                new ObjectInputStream(new FileInputStream("accounts.dat"));
        Account[] as1 = (Account[])inputStream.readObject();
        inputStream.close();
    }
}
