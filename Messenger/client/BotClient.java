package com.javarush.task.task30.task3008.client;

import com.javarush.task.task30.task3008.ConsoleHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BotClient extends Client{

    public class BotSocketThread extends SocketThread {

        @Override
        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            sendTextMessage("Привет чату! Я бот. Понимаю команды: дата, день, месяц, год, время, час, минуты, секунды.");
            super.clientMainLoop();
        }

        @Override
        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);
            if (!message.contains(": ")) return;
            String[] str = message.split(": ");
            // Смотрим, что запросил пользователь и формируем ответ
            SimpleDateFormat dateFormat;
            switch (str[1]){
                case "дата": dateFormat = new SimpleDateFormat("d.MM.yyyy"); break;
                case "день": dateFormat = new SimpleDateFormat("d"); break;
                case "месяц": dateFormat = new SimpleDateFormat("MMMM"); break;
                case "год": dateFormat = new SimpleDateFormat("yyyy"); break;
                case "время": dateFormat = new SimpleDateFormat("H:mm:ss"); break;
                case "час": dateFormat = new SimpleDateFormat("H"); break;
                case "минуты": dateFormat = new SimpleDateFormat("m"); break;
                case "секунды": dateFormat = new SimpleDateFormat("s"); break;
                default:
                    dateFormat = null;
            }
            // Сообщение для пользователя, котоорый запрашивал
            if (!(dateFormat == null)){
                BotClient.this.sendTextMessage("Информация для " + str[0] + ": " + dateFormat.format(Calendar.getInstance().getTime()));
            }
        }
    }

    @Override
    protected SocketThread getSocketThread() {
        return new BotSocketThread();
    }

    @Override
    protected boolean shouldSendTextFromConsole() {
        return false;
    }

    @Override
    protected String getUserName() {
        return "date_bot_" + (int) (Math.random() * 100);
    }

    public static void main(String[] args) {
        BotClient bot = new BotClient();
        bot.run();
    }
}