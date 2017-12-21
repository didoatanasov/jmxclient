package com.omisoft.jmx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class SocketThread implements Runnable {

  public static final String ERROR_STRING = "ERROR:";
  private final Socket s;
  private final BufferedReader reader;
  private final PrintWriter writer;

  public SocketThread(Socket s) throws IOException {
    this.s = s;
    reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
    writer = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
  }

  @Override
  public void run() {
    boolean runFlag = true;
    String connectionURL;
    JMXConnector jmxConnector = null;
    try {
      connectionURL = reader.readLine();

    String[] args = connectionURL.split(":");
    String host = args[0];  // or some A.B.C.D
    int port = Integer.parseInt(args[1]);
    String url = "service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/jmxrmi";
    writer.flush();
    JMXServiceURL serviceUrl = null;

      serviceUrl = new JMXServiceURL(url);

      jmxConnector = JMXConnectorFactory.connect(serviceUrl, null);
    } catch (Throwable e) {
      e.printStackTrace();
      writer.println("ERROR_STRING:" + e.getMessage());
      try {
        s.close();
        return;
      } catch (IOException e1) {
        e1.printStackTrace();
      }

    }
    while (runFlag) {
      try {
        String query = reader.readLine();
        if (query.equals("\\q")) {
          runFlag = false;
          continue;
        }
        String[] queryParams = query.split("@");
        MBeanServerConnection mbeanConn = jmxConnector.getMBeanServerConnection();
        ObjectName objectName = new ObjectName(queryParams[0]);
        String value = mbeanConn.getAttribute(objectName, queryParams[1]).toString();
        writer.println(value);
        writer.flush();

      } catch (Exception e) {
        e.printStackTrace();
        writer.println(ERROR_STRING + e.getMessage());
        writer.flush();
      }
    }
    try {
      s.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}

