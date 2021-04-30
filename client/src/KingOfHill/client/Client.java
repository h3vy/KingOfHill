package KingOfHill.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    private static Socket clientSocket;
    private static PrintWriter output;
    private static Scanner input;
    private static String serverWord = "N";
    public static InetAddress enemyIP;
    static int low = 0;
    static int high = 2147483647;
    static int mid = (high + low)/2;


    public static void main(String[] args) throws IOException {
        ConnectNigor mainServer = new ConnectNigor();
        ConnectNigor.initSocket();
        int sendNumber;
        int countPlayer = 0;
        try {
            try {
                mainServer.processingRequest("registr");
                ArrayList<String> participantTable = ConnectNigor.participantTable;
                String hostPlayer = participantTable.get(countPlayer);
                if (hostPlayer.equals(ConnectNigor.myIP)) { // Если ip совпадает с нашим
                    countPlayer += 1; // увеличиваем счетик на 1
                    hostPlayer = participantTable.get(countPlayer); // и берём другой ip
                }
                clientSocket = new Socket(hostPlayer, 2222); // Запрашиваем доступ на сервер
                output = new PrintWriter(clientSocket.getOutputStream());
                input = new Scanner(clientSocket.getInputStream());
                while (participantTable.contains(ConnectNigor.myIP)) { // Мы играем пока наш IP в списке участников
                    enemyIP = clientSocket.getInetAddress();
                    System.out.println("Подключен");
                    System.out.println(hostPlayer);
                    sendNumber = binary_search(serverWord); // Отправляемое число
                    ByteBuffer dbuf = ByteBuffer.allocate(4);
                    dbuf.putInt(sendNumber);
                    byte[] output_array = dbuf.array();
                    for(int i = 0;i < output_array.length/2; i++){
                        byte temp = output_array[i];
                        output_array[i] = output_array[output_array.length - i - 1];
                        output_array[output_array.length - i - 1] = temp;
                    }
                    Thread.sleep(1000);
                    clientSocket.getOutputStream().write(output_array);
                    System.out.println("send "+sendNumber);
                    String answer = "";
                    /*if (input.hasNext())*/ {
                        //answer = input.nextLine();
                        byte[] bytes_input = new byte[1];
                        clientSocket.getInputStream().read(bytes_input);

                        answer = String.valueOf((char)bytes_input[0]);
                        System.out.println("Ответ получен сразу");
                    }
                    while (answer == null || answer.equals("")) {
                        /*if (input.hasNext())*/ {
                            answer = input.nextLine();
                            System.out.println("Ответ получен сразу с ожиданием");
                        }
                    }
                    serverWord = answer;

                    while (serverWord.equals("")) { // Отсылаем сообщение на сервер, если он не отправляет нам ответку
                        clientSocket.getOutputStream().write(dbuf.array());
                    }
                    System.out.println(serverWord);
                    if (serverWord.equals("D")) {
                        enemyIP = clientSocket.getInetAddress(); // Получаем Ip игрока перед его смертью
                        mainServer.processingRequest("kill");
                    }
                    mainServer.processingRequest("table");
                }
            }
            finally{
                clientSocket.close();
                input.close();
                output.close();
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Error");
        }
    }

    /* Обработка значений */
    public static int binary_search(String input) {
        while (low <= high) {
            switch (input) {
                case "L":
                    high = mid-1;
                    mid = (low + high) / 2;
                    return (mid);
                case "R":
                    low = mid+1;
                    mid = (low + high) / 2;
                    return (mid);
                case "D":
                    break;
                case "N":
                    return mid;
            }
        }
        return mid;
    }
}