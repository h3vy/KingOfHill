package KingOfHill.client;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;


public class ConnectNigor {
    public final static int SERVER_PORT = 4444;
    public static ArrayList<String> participantTable;


    public void selectRequest(String nameRequest) throws IOException{
        byte[] message = new byte[255];
        switch (nameRequest){
            case ("registr"): message = reqistrRequest(); break;
            case ("ready"): message = readyRequest(); break;
            case ("delete"): message = deleteRequest(); break;
            case ("table"): message = acceptTable(); break;
            //case ("death"): message = deathRequest(); break;
        }

        try{
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName("localhost");
            // Создайте соответствующие буферы
            byte[] receivingDataBuffer = new byte[255];
            // Отправка пакета
            DatagramPacket sendingPacket = new DatagramPacket(message,message.length,IPAddress, SERVER_PORT);
            clientSocket.send(sendingPacket);
            // Получение пакета
            DatagramPacket receivingPacket = new DatagramPacket(receivingDataBuffer,receivingDataBuffer.length);
            clientSocket.receive(receivingPacket);
            if (nameRequest.equals("registr")){
                //String receivedData = new String(receivingPacket.getData());
                byte[] buf = receivingPacket.getData();
                participantTable = fillTable(buf); // Заполняем таблицу IP адресов
                receivingPacket = new DatagramPacket(receivingDataBuffer,receivingDataBuffer.length); // Заново прослушиваем порт
                clientSocket.receive(receivingPacket); // Получаем сообщение
                buf = receivingPacket.getData();
                if (buf[0] == 0){
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

    public byte[] reqistrRequest(){
        byte[] message = new byte[255];
        ArrayList<Byte> fillMessage = new ArrayList<Byte>();
        String password = "valid_pass";
        String username = "stroenevsd";

        fillMessage.add((byte) 0); // Код запроса для регистрации
        for(int i = 0; i < password.length(); i++){
            fillMessage.add((byte)password.charAt(i));
        }
        byte b1 = (byte) (255 & SERVER_PORT); // 1 байт пароля сервера
        byte b2 = (byte) (((255 << 8) & SERVER_PORT) >> 8); // 2 байт пароля сервера
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

    public byte[] readyRequest(){
        byte[] message = new byte[255];
        message[0] = (byte) 2;
        return message;
    }

    public byte[] deleteRequest(){
        byte[] message = new byte[255];
        message[0] = (byte) 1;
        return message;
    }

    public static byte[] deathRequest(InetAddress ia){
        byte[] message = new byte[255];
        byte[] ip;
        ip = ia.getAddress();
        message[0] = (byte) 3;
        System.arraycopy(ip, 0, message, 1, ip.length);
        return message;
    }

    public static byte[] acceptTable(){
        byte[] message = new byte[255];
        message[0] = (byte) 1;
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