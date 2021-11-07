package hr.fer.ilj.workshop.server;

public class Main {

  public static void main(String[] args) {
    String currentWorkingDirectory = System.getProperty("user.dir");
    Server server = new Server();
    server.start(currentWorkingDirectory);
  }

}
