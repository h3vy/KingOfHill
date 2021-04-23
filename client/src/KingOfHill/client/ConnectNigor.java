package KingOfHill.client;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;


public class ConnectNigor {
    public final static int SERVER_PORT = 3333;
    public final static int RECEIVE_PORT = 4444;
    public static ArrayList<String> participantTable;


    public void selectRequest(String nameRequest) throws IOException{
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


        try{
            DatagramSocket clientSocket = new DatagramSocket(RECEIVE_PORT);
            InetAddress IPAddress = InetAddress.getByName("25.80.244.184");
            clientSocket.connect(IPAddress,SERVER_PORT);
            // Создайте соответствующие буферы
            byte[] receivingDataBuffer = new byte[255];
            DatagramPacket sendingPacket = new DatagramPacket(message,message.length,IPAddress, SERVER_PORT); // Формирование пакета отправки
            DatagramPacket receivingPacket = new DatagramPacket(receivingDataBuffer,receivingDataBuffer.length); // Формирования пакета приёма
            System.out.println(clientSocket.getLocalPort());
            clientSocket.setSoTimeout(1000);
            boolean continueSending = true;
            int counter = 0;
            while (continueSending && counter < 10) {
                clientSocket.send(sendingPacket);
                counter++;
                try {
                    clientSocket.receive(receivingPacket);
                    continueSending = false; // Пакет получен, останавливаем отправка запроса
                }
                catch (SocketTimeoutException e) {
                    clientSocket.send(sendingPacket); // Ответ не получен спустя 1 секунду, повторяем отправку
                }
            }
//            while (receivingPacket == null){
//                clientSocket.send(sendingPacket);
//                clientSocket.receive(receivingPacket);
//            }
            if (nameRequest.equals("registr")){
                byte[] buf = receivingPacket.getData();
                participantTable = fillTable(buf); // Заполняем таблицу IP адресов
                receivingPacket = new DatagramPacket(receivingDataBuffer,receivingDataBuffer.length); // Заново прослушиваем порт
                clientSocket.receive(receivingPacket); // Получаем сообщение
                buf = receivingPacket.getData();
                if (buf[0] == 0){ // Если код запроса сервера = 0
                    selectRequest("ready");
                }
            } else if (nameRequest.equals("table")){
                byte[] buf = receivingPacket.getData();
                participantTable = fillTable(buf); // Заполняем таблицу IP адресов
            }

            clientSocket.close();
        }
        catch(SocketException e) {
            e.printStackTrace();
        }


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

        for(int i = 1; i < ipList.length; i += 4){
            participantTable.add((0xff & (int)ipList[i]) + "." +
                                (0xff & (int)ipList[i+1]) + "." +
                                (0xff & (int)ipList[i+2]) + "." +
                                (0xff & (int)ipList[i+3]));
        }
        return participantTable;
    }


}
