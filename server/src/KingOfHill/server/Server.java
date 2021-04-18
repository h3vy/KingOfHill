package KingOfHill.server;

import java.net.*;
import java.io.*;


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
                serverSocket = new ServerSocket(4004); // Слушаем порт
                System.out.println("Сервер запущен");
                clientSocket = serverSocket.accept(); // Ждем подключения
                try {
                    while (run){
                        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                        String word = in.readLine(); // Ждем сообщение от клиента
                        System.out.println(word);
                        int number = Integer.parseInt(word); // Строка -> число
                        String answer = answer(number);
                        out.write(answer);
                        out.flush();
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
