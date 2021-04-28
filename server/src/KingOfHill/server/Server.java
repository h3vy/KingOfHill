package KingOfHill.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class Server {
    private static Socket clientSocket; // Сокет общения
    private static ServerSocket serverSocket; // Сокет сервера
    private static BufferedReader in; // Чтение из сокета
    private static BufferedWriter out; // Запись в сокет
    private static Boolean run = true;
    static int myNum = (int) (Math.random() * 127);

    public static void main(String[] args) {
        try{
            try {
                serverSocket = new ServerSocket(2222); // Слушаем порт
                System.out.println("Сервер запущен");
                clientSocket = serverSocket.accept(); // Ждем подключения
                try {
                    PrintWriter output = new PrintWriter(clientSocket.getOutputStream());
                    Scanner input = new Scanner(clientSocket.getInputStream());
                    while (run){
                        String word = "";
                        if(input.hasNext()) {
                            word = input.nextLine();
                        }
                        while (word == null || word.equals("")) {
                            if(input.hasNext()) {
                                word = input.nextLine();
                            }
                        }
                        System.out.println(word);
                        int number = Integer.parseInt(word);
                        String answer = answer(number);
                        System.out.println(answer);
                        output.println(answer);
                        output.flush();
                        if (answer.equals("D")){
                            run = false;
                        }
                    }
                } finally {
                    clientSocket.close();
                    in.close();
                    out.close();
                }

            } finally {
                System.out.println("Сервер закрыт!");
                serverSocket.close();
            }
        } catch (IOException e){
            System.err.println(e);
        }
    }

    public static String answer(int n){
        int num = (int) (Math.random() * 101);
        if (num < 20){
            if (myNum < n){
                return "L";
            }
            else if (myNum > n) {
                return "R";
            }
            else {
                return "D";
            }
        }
        return "N";
    }
}
