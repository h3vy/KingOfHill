package KingOfHill.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {
    private static Socket clientSocket;
    private static BufferedReader in; //Чтение сокета
    private static BufferedWriter out;
    private static String serverWord = "N";
    public static InetAddress enemyIP;
    static int low = 0;
    static int high = 127;
    static int mid;

    public static void main(String[] args) throws IOException {
        ConnectNigor mainServer = new ConnectNigor();
        ConnectNigor.initSocket();
        int sendNumber = 0;
        int countPlayer = 0;
        try {
            try {
                mainServer.processingRequest("registr");
                ArrayList<String> participantTable = ConnectNigor.participantTable;
                // Мы играем пока наш IP в списке участников
                while (participantTable.contains(ConnectNigor.myIP)) { // Мы играем пока наш IP в списке участников
                    String hostPlayer = participantTable.get(countPlayer); // Получаем ip игрока из списка
                    if (hostPlayer.equals(ConnectNigor.myIP)) { // Если ip совпадает с нашим
                        countPlayer += 1; // увеличиваем счетик на 1
                        hostPlayer = participantTable.get(countPlayer); // и берём другой ip
                    }
                    clientSocket = new Socket(hostPlayer, 2222); // Запрашиваем доступ на сервер
                    PrintWriter output = new PrintWriter(clientSocket.getOutputStream());
                    Scanner input = new Scanner(clientSocket.getInputStream());
                    System.out.println(hostPlayer);
                    System.out.println("Подключен");
                    sendNumber = binary_search(serverWord); // Отправляемое число
                    output.println(sendNumber); // Кидаем сообщение на сервер
                    output.flush();
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
                    input.close();
                    output.close();
                }
            } catch(Exception e){
                System.out.println("Ошибка");
            }
            finally{
                clientSocket.close();
            }

        } catch (IOException e) {
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

}
