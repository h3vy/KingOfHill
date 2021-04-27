package KingOfHill.client;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

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
                while (participantTable.contains(ConnectNigor.myIP)){ // Мы играем пока наш IP в списке участников
                    String hostPlayer = participantTable.get(countPlayer); // Получаем ip игрока из списка
                    if (hostPlayer.equals(ConnectNigor.myIP)) { // Если ip совпадает с нашим
                        countPlayer += 1; // увеличиваем счетик на 1
                        hostPlayer = participantTable.get(countPlayer); // и берём другой ip
                    }
                    clientSocket = new Socket(hostPlayer,3333); // Запрашиваем доступ на сервер
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                    System.out.println("Подключен");
                    sendNumber = binary_search(serverWord); // Отправляемое число
                    out.write(sendNumber); // Кидаем сообщение на сервер
                    out.flush();
                    serverWord = in.readLine();
                    while (serverWord.equals("")) { // Отсылаем сообщение на сервер, если он не отправляет нам ответку
                        out.write(sendNumber);
                        out.flush();
                    }
                    System.out.println(serverWord);
                    if (serverWord.equals("D")){
                        enemyIP = clientSocket.getInetAddress(); // Получаем Ip игрока перед его смертью
                        mainServer.processingRequest("kill");
                    }
                    mainServer.processingRequest("table");
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

}
