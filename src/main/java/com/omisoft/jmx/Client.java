package com.omisoft.jmx;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {

  public static void main(String[] args) throws IOException {

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
