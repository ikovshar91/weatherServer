package main;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import servlets.WeatherServlet;

public class Main {
    public static void main(String[] args) {

        ServletContextHandler contextHandler = new ServletContextHandler(1);
        QueuedThreadPool pool = new QueuedThreadPool(100);
        contextHandler.addServlet(new ServletHolder(new WeatherServlet(pool)), "/*");

        Server server = new Server(pool);
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.setConnectors(new Connector[]{connector});

        server.setHandler(contextHandler);

        try {
            server.start();
            server.join();
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
