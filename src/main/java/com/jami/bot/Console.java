package com.jami.bot;

import java.util.Scanner;

import com.jami.BotAdmin.Admin;

public class Console {
  private static Scanner scanner = new Scanner(System.in);
  
  public static void startScanning() {
    while (scanner.hasNext()) {
      String command = scanner.nextLine();
      Admin.adminCommands(null, command);
    }
  }

  public static void shutdown() {
    scanner.close();
  }
}
