package controller;

import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner = new Scanner(System.in);

    public String readString(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    public Long readLong(String message) {
        try {
            return Long.parseLong(readString(message));
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return readLong(message);
        }
    }

    public Integer readInt(String message) {
        try {
            return Integer.parseInt(readString(message));
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return readInt(message);
        }
    }
}
