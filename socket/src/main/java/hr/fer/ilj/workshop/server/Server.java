package hr.fer.ilj.workshop.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hr.fer.ilj.workshop.files.FileLoader;
import hr.fer.ilj.workshop.files.HtmlGenerator;

public class Server {
  private final Logger LOG = LoggerFactory.getLogger(this.getClass());


  private static final int LISTEN_PORT = 4444;
  private static final int MAX_PARALLEL_CLIENT = 2;

  public void start(String rootPath) {
    FileLoader loader = new FileLoader(Path.of(rootPath));
    HtmlGenerator generator = new HtmlGenerator();
    Protocol protocol = new Protocol(loader, generator );

    try (ServerSocket ssc = new ServerSocket(LISTEN_PORT);) {
      ExecutorService pool = Executors.newFixedThreadPool(MAX_PARALLEL_CLIENT);

      LOG.info("Start listening at port {}", LISTEN_PORT);
      while (true) {
        Socket scClient = ssc.accept();
        ConnectionHandler st = new ConnectionHandler(scClient, protocol);
        pool.execute(st);
      }
    } catch (IOException e) {
      LOG.error("Error starting server.", e);
    }
  }

}
