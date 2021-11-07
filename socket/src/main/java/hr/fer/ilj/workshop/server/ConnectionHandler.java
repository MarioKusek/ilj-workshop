package hr.fer.ilj.workshop.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionHandler implements Runnable {
  private final Logger LOG = LoggerFactory.getLogger(this.getClass());

  private Socket client;

  private Protocol protocol;

  public ConnectionHandler(Socket client, Protocol protocol) {
    this.client = client;
    this.protocol = protocol;
  }

  @Override
  public void run() {
    try (PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));) {


      String requestline = in.readLine();
      String response = protocol.handleRequest(requestline);
      out.println(response);
    } catch (IOException ioe) {
      LOG.error("Error handling client.", ioe);
    } finally {
      try {
        client.close();
      } catch (Exception e) {
      }
    }
  }

}
