package KingOfHill.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    private static Socket clientSocket;
    private static PrintWriter output;
    private static Scanner input;
    private static String serverWord = "N";
    public static InetAddress enemyIP;
    static long low = 0;
    static long high = 2147483647;
    static long mid = (high + low)/2;

    public static void main(String[] args) throws IOException {
        ConnectNigor mainServer = new ConnectNigor();
        ConnectNigor.initSocket();
        long sendNumber;
        int countPlayer = 0;
        try {
            try {
                mainServer.processingRequest("registr");
                ArrayList<String> participantTable = ConnectNigor.participantTable;
                String hostPlayer = participantTable.get(countPlayer);
                clientSocket = new Socket(hostPlayer, 2222); // Запрашиваем доступ на сервер
                output = new PrintWriter(clientSocket.getOutputStream());
                input = new Scanner(clientSocket.getInputStream());
                while (participantTable.contains(ConnectNigor.myIP)) { // Мы играем пока наш IP в списке участников
                    if (hostPlayer.equals(ConnectNigor.myIP)) { // Если ip совпадает с нашим
                        countPlayer += 1; // увеличиваем счетик на 1
                        hostPlayer = participantTable.get(countPlayer); // и берём другой ip
                    }
                    enemyIP = clientSocket.getInetAddress();
                    System.out.println("Подключен");
                    sendNumber = binary_search(serverWord); // Отправляемое число
                    output.println(sendNumber);// Кидаем сообщение на сервер
                    output.flush();
                    System.out.println("send"+sendNumber);
                    String answer = "";
                    if (input.hasNext()) {
                        answer = input.nextLine();
                    }
                    while (answer == null || answer.equals("")) {
                        if (input.hasNext()) {
                            answer = input.nextLine();
                        }
                    }
                    serverWord = answer;
                    while (serverWord.equals("")) { // Отсылаем сообщение на сервер, если он не отправляет нам ответку
                        output.println(sendNumber); // Кидаем сообщение на сервер
                        output.flush();
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

        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    /* Обработка значений */
    public static long binary_search(String input) {
        while (low <= high) {
            switch (input) {
                case "L":
                    high = mid - 1;
                    mid = (low + high) / 2;
                    return (mid);
                case "R":
                    low = mid + 1;
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
