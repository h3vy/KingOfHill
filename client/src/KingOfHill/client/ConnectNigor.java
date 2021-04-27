package KingOfHill.client;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;


public class ConnectNigor {
    public final static int SERVER_PORT = 3333;
    public final static int RECEIVE_PORT = 4444;
    public static ArrayList<String> participantTable;
    private static DatagramSocket clientSocket;
    private static DatagramSocket serverSocket;
    private static DatagramPacket receivingPacket;
    private static DatagramPacket activityPacket;
    private static InetAddress IPAddress;
    public static String myIP;
    private final byte[] receivingDataBuffer = new byte[255];
    private final byte[] activityDataBuffer = new byte[255];


    public static void initSocket() throws IOException{ // Инициализация потоков для UDP
        clientSocket = new DatagramSocket();
        serverSocket = new DatagramSocket(RECEIVE_PORT);
        IPAddress = InetAddress.getByName("127.0.0.1");
        clientSocket.connect(IPAddress,SERVER_PORT);
    }

    public void processingRequest(String nameRequest) throws IOException { // Обмен пакетами
        try{
            activityDataBuffer[0] = (byte) 5; // Закидываем 5-ку в пакет "активности"
            byte[] message = selectRequest(nameRequest); // Получаем массив байтов
            DatagramPacket sendingPacket = new DatagramPacket(message,message.length,IPAddress, SERVER_PORT); // Формирование пакета отправки
            receivingPacket = new DatagramPacket(receivingDataBuffer,receivingDataBuffer.length); // Формирования пакета приёма
            activityPacket = new DatagramPacket(activityDataBuffer,activityDataBuffer.length); // Формирование пакета "активности"
            clientSocket.setSoTimeout(1000);
            boolean continueSending = true;
            int counter = 0;
            while (continueSending && counter < 10) {
                clientSocket.send(activityPacket);
                clientSocket.send(sendingPacket);
                counter++;
                try {
                    serverSocket.receive(receivingPacket);
                    continueSending = false; // Пакет получен, останавливаем отправка запроса
                }
                catch (SocketTimeoutException e) {
                    clientSocket.send(sendingPacket); // Ответ не получен спустя 1 секунду, повторяем отправку
                }
            }
            serverOperation(receivingPacket.getData());
        }
        catch(SocketException e) {
            e.printStackTrace();
        }
    }

    private void serverOperation(byte[] serverResponse) throws IOException {
        switch (serverResponse[0]) {
            case 0: { // Пропуск всех нулей
                while (serverResponse[0] == 0){
                    serverSocket.receive(receivingPacket);
                }
                break;
            }
            case 1: { // Запрашиваем таблицу
                processingRequest("table");
                break;
            }
            case 2: {
                participantTable = fillTable(serverResponse); // Заполняем таблицу IP адресов
                break;
            }
            case 3: {
                serverSocket.receive(receivingPacket);
                break;
            }
            case 4: { // Запрос, который следует после регистрации
                byte[] buf;
                // Получаем наш IP
                myIP = ((0xff & (int)serverResponse[1]) + "." + (0xff & (int) serverResponse[2]) + "." + (0xff & (int) serverResponse[3]) + "." + (0xff & (int) serverResponse[4]));
                receivingPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length); // Заново прослушиваем порт
                serverSocket.receive(receivingPacket); // Получаем сообщение
                buf = receivingPacket.getData();
                if (buf[0] == 0) { // Если код запроса сервера = 0
                    processingRequest("ready");
                }
                break;
            }
            case 5:{
                while (serverResponse[0] == 5){
                    clientSocket.send(activityPacket); // Если нет необходимости - можно убрать
                    serverSocket.receive(receivingPacket);
                }
                break;
            }
        }
    }


    private byte[] selectRequest(String nameRequest) throws IOException{
        byte[] message = new byte[255];
        switch (nameRequest){
            case ("registr"): message = reqistrRequest(); break;
            case ("ready"): message = readyRequest(); break;
            case ("delete"): message = deleteRequest(); break;
            case ("table"): message = acceptTable(); break;
            case ("kill"):
                InetAddress ipEnemy = Client.enemyIP;
                message = killRequest(ipEnemy);
                break;
        }
        return message;
    }

    private byte[] reqistrRequest(){
        byte[] message = new byte[255];
        ArrayList<Byte> fillMessage = new ArrayList<Byte>();
        String password = "valid cd";
        String username = "stroenevsd";

        fillMessage.add((byte) 0); // Код запроса для регистрации
        for(int i = 0; i < password.length(); i++){
            fillMessage.add((byte)password.charAt(i));
        }
        byte b1 = (byte) (255 & RECEIVE_PORT); // 1 байт пароля сервера
        byte b2 = (byte) (((255 << 8) & RECEIVE_PORT) >> 8); // 2 байт пароля сервера
        fillMessage.add(b1);
        fillMessage.add(b2);
        for(int i = 0; i < username.length(); i++){
            fillMessage.add((byte)username.charAt(i));
        }
        int j=0;
        // Byte[] to byte[]
        for(Byte b: fillMessage)
            message[j++] = b.byteValue();

        return message;
    }

    private byte[] readyRequest(){
        byte[] message = new byte[255];
        message[0] = (byte) 2;
        return message;
    }

    private byte[] deleteRequest(){
        byte[] message = new byte[255];
        message[0] = (byte) 1;
        return message;
    }

    private static byte[] killRequest(InetAddress ia){
        byte[] message = new byte[255];
        byte[] ip;
        ip = ia.getAddress();
        message[0] = (byte) 3;
        System.arraycopy(ip, 0, message, 1, ip.length);
        return message;
    }

    private static byte[] acceptTable(){
        byte[] message = new byte[255];
        message[0] = (byte) 4;
        return message;
    }

    public ArrayList<String> fillTable(byte[] ipList){
        ArrayList<String> participantTable = new ArrayList<>();
        //String[] participantTable = new String[ipList[1]];
        for(int i = 2; i < ipList.length - 3; i += 4){
            participantTable.add((0xff & (int)ipList[i]) + "." +
                                (0xff & (int)ipList[i+1]) + "." +
                                (0xff & (int)ipList[i+2]) + "." +
                                (0xff & (int)ipList[i+3]));
        }
        System.out.println(participantTable);
        return participantTable;
    }


}
