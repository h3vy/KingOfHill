package KingOfHill.client;

import java.io.*;
import java.net.*;

public class Client {
    private static Socket clientSocket;
    private static BufferedReader in; //Чтение сокета
    private static BufferedWriter out;
    private static String serverWord = "N";
    private static Boolean run = true;
    static int low = 0;
    static int high = 127;
    static int mid;

    public static void main(String[] args) {
        try {
            try {
                while (run){
                    clientSocket = new Socket("localhost",4004); // Запрашиваем доступ на сервер
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                    System.out.println("Подключен");
                    out.write(binary_search(serverWord)); // Кидаем сообщение на сервер
                    out.flush();
                    serverWord = in.readLine();
                    System.out.println(serverWord);
                    if (serverWord.equals("D")){
                        run = false;
                    }
                }
            } catch (Exception e){
                System.out.println("Ошибка");
            }
            finally {
                clientSocket.close();
                in.close();
                out.close();
            }

        } catch (IOException e){
            System.out.println(e);
        }
    }

    /* Обработка значений */
    public static int binary_search(String input) {
        if (input.equals("N")) {
            return (high -= 1);
        } else {
            while (low <= high) {
                mid = (low + high) / 2;
                switch (input) {
                    case "L":
                        high = mid;
                        return (high -= 1);
                    case "R":
                        low = mid;
                        return (low += 1);
                    case "D":
                        break;
                }
            }
        }
        return mid;
    }

    public class ConnectNigor {

    }
}
