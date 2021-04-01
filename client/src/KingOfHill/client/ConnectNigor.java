package KingOfHill.client;

import java.io.*;
import java.net.Socket;

public class ConnectNigor {

    public static void main(String[] args) {
        byte[] request = "1234567".getBytes();
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        out.write(request);
        System.out.println(request.toString());
    }

}
