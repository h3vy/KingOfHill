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

    public static void main(String[] args) throws UnknownHostException {
        ConnectNigor mainServer = new ConnectNigor();
        InetAddress addr = InetAddress.getLocalHost(); // Вычисляем свой ip для дальнейших действий
        String myIP = addr.getHostAddress(); // Внутренний IP
        int sendNumber = 0;
        int countPlayer = 0;
        try {
            try {
                mainServer.selectRequest("registr");
                ArrayList<String> participantTable = ConnectNigor.participantTable;
                while (participantTable.contains(myIP)){ // Мы играем пока наш IP в списке участников
                    String hostPlayer = participantTable.get(countPlayer); // Получаем ip игрока из списка
                    if (hostPlayer.equals(myIP)) { // Если ip совпадает с нашим
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
                        mainServer.selectRequest("kill");
                    }
                    mainServer.selectRequest("table");
                }
            } catch (Exception e){
                System.out.println("Ошибка");
            }
            finally {
                mainServer.selectRequest("delete"); // Отправляем запрос о нашей смерти
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
