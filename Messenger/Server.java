package com.javarush.task.task30.task3008;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    private static class Handler extends Thread {
        private Socket socket;

        public Handler(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            ConsoleHelper.writeMessage("Установлено новое соединение с адресом: " + socket.getRemoteSocketAddress());
            try (Connection connection = new Connection(socket)){
                String userName = serverHandshake(connection);
                sendBroadcastMessage(new Message(MessageType.USER_ADDED, userName));
                notifyUsers(connection, userName);
                serverMainLoop(connection, userName);
                connectionMap.remove(userName);
                sendBroadcastMessage(new Message(MessageType.USER_REMOVED, userName));
            } catch (IOException | ClassNotFoundException e) {
                ConsoleHelper.writeMessage("Ошибка при обмене данными с " + socket.getRemoteSocketAddress());
            }
            ConsoleHelper.writeMessage("Соединение с " + socket.getRemoteSocketAddress() + " закрыто.");
        }

        // Рукопожатие с клиентом при соединении
        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException {

            while (true) {
                connection.send(new Message(MessageType.NAME_REQUEST, "Введите свое имя: "));
                Message message = connection.receive();

                if (message.getType() != MessageType.USER_NAME) {
                    ConsoleHelper.writeMessage("Некоррректный тип сообщения !");
                    continue;
                }
                String userName = message.getData();
                if (userName.isEmpty()){
                    ConsoleHelper.writeMessage("Переданное имя пустое!");
                    continue;
                }
                if (connectionMap.containsKey(userName)){
                    ConsoleHelper.writeMessage("Такое имя уже существует!");
                    continue;
                }
                connectionMap.put(userName, connection);
                connection.send(new Message(MessageType.NAME_ACCEPTED, "Имя принято!"));
                return userName;
            }
        }

        // Оповещение пользователей
        private void notifyUsers(Connection connection, String userName) throws IOException {
            for (String name: connectionMap.keySet()){
                if (!name.equals(userName)){
                    connection.send(new Message(MessageType.USER_ADDED, name));
                }
            }
        }

        // Основной метод отправки полученного сообщения
        private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException {
            while (true) {
                Message message = connection.receive();
                if (message.getType() == MessageType.TEXT){
                    sendBroadcastMessage(new Message(MessageType.TEXT, userName+": "+message.getData()));
                } else{
                    ConsoleHelper.writeMessage("Принятое сообщение не является текстом");
                }
            }
        }
    }

    // Широковещательное оповещение пользователей
    public static void sendBroadcastMessage(Message message) {
        for (Connection connection : connectionMap.values()){
            try {
                connection.send(message);
            } catch (IOException e) {
                ConsoleHelper.writeMessage("Не смогли отправить сообщение");
            }
        }
    }


    public static void main(String[] args) throws IOException {
        
        int port = ConsoleHelper.readInt();

        try (ServerSocket ss = new ServerSocket(port)) {
            ConsoleHelper.writeMessage("Сервер запущен!");
            while (true){
                Socket socket = ss.accept();
                new Handler(socket).start();
            }
        } catch (Exception e) {
            ConsoleHelper.writeMessage(e.getMessage());
        }
    }
}
