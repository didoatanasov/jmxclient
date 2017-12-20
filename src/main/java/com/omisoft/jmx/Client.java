package com.omisoft.jmx;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Simple jmx client Args host port
 */
public class Client {

  public static void main(String[] args) throws IOException {
    if (args.length != 2) {

      System.out.println(
          "Please enter host and port Ex. java -jar jmxclient-1.0-SNAPSHOT-all.jar localhost 9000");
      return;
    }

    ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[1]), 10,
        InetAddress.getByName(args[0]));
    while (true) {
      Socket s = serverSocket.accept();
      SocketThread st = new SocketThread(s);
      Thread t = new Thread(st);
      t.start();
    }
  }

}
