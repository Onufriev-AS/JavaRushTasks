package com.javarush.task.task30.task3008;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleHelper {

    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    // Собщение в консоль
    public static void writeMessage(String message) {
        System.out.println(message);
    }

    // Чтение строки из консоли
    public static String readString() {
        String result = null;
        while (true){
            try {
                result = reader.readLine();
                break;
            } catch (IOException e) {
                writeMessage("Произошла ошибка при попытке ввода текста. Попробуйте еще раз.");
            }
        }
        return result;
    }

    // Чтение числа из консоли
    public static int readInt() {
        int result = 0;
        while (true) {
            try {
                result = Integer.parseInt(readString());
                break;
            } catch (NumberFormatException e) {
                writeMessage("Произошла ошибка при попытке ввода числа. Попробуйте еще раз.");
            }
        }
        return result;
    }
}