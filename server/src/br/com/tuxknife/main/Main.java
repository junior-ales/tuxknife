package br.com.tuxknife.main;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: jsantos
 * Date: May/23/13
 * Time: 22:14
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) throws Exception {

        Server server = new Server(8083);

        server.setHandler( new MainHandler() );

        server.start();
        server.join();

    }

    private static class MainHandler extends AbstractHandler {


        public static final String REGEX_SERVER = "^\\/servers\\/(.*)$";

        @Override
        public void handle(String s, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

            StringBuilder result = new StringBuilder();

            if ( s.matches(REGEX_SERVER)  ) {

                System.out.println("Request to server");

                String server = "localhost";
                String username = request.getParameter("username");
                String password = request.getParameter("password");

                response.setStatus(HttpServletResponse.SC_OK);
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setContentType("application/json;charset=utf-8");
                baseRequest.setHandled(true);

                result.append("{")
                        .append("\"host\": \"").append(server).append("\", ")
                        .append("\"username\":\"").append(username).append("\", ")
                        .append("\"password\":\"").append(password).append("\"")
                        .append("}");
                System.out.println(result);

            }

            response.getWriter().print(result);

        }
    }
}
