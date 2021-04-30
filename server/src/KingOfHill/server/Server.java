package KingOfHill.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Scanner;


public class Server {
    private static Socket clientSocket; // Сокет общения
    private static ServerSocket serverSocket; // Сокет сервера
    private static Boolean run = true;
    static int myNum = (int) (Math.random() * 2147483647);

    public static void main(String[] args) throws IOException {
        try{
            try {
                serverSocket = new ServerSocket(2222); // Слушаем порт
                System.out.println("Сервер запущен");
                clientSocket = serverSocket.accept(); // Ждем подключения
                try {
                    PrintWriter output = new PrintWriter(clientSocket.getOutputStream());
                    Scanner input = new Scanner(clientSocket.getInputStream());
                    while (run){
                        byte[] arr = new byte[4];
                        clientSocket.getInputStream().read(arr);
                        for(int i = 0;i <arr.length/2; i++){
                            byte temp = arr[i];
                            arr[i] = arr[arr.length - i - 1];
                            arr[arr.length - i - 1] = temp;
                        }
                        ByteBuffer wrapped = ByteBuffer.wrap(arr);
                        int number = wrapped.getInt();
                        System.out.println(number);
                        ByteBuffer dbuf = ByteBuffer.allocate(4);
                        String answer = answer(number);
                        System.out.println(answer);
                        Thread.sleep(1000);
                        dbuf.put((byte)(int)answer.charAt(0));
                        clientSocket.getOutputStream().write(dbuf.array());
                        System.out.println("MyNum:  " + myNum);
                        if (answer.equals("D")){
                            run = false;
                        }
                    }
                    input.close();
                    output.close();
                } finally {
                    clientSocket.close();
                    System.exit(0);
                }

            } finally {
                System.out.println("Сервер закрыт!");
                serverSocket.close();
            }
        } catch (IOException | InterruptedException e){
            System.err.println(e);
        }
    }

    public static String answer(int n){
        int num = (int) (Math.random() * 101);
        if (num < 105){
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
