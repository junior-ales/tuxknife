package br.com.tuxknife.main;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class Main {

    public static final int TIMEOUT_IN_SECONDS = 5000;
    public static final int PORT = 8083;

    public static void main(String[] args) throws Exception {
        Server server = new Server(PORT);
        server.setHandler(new MainHandler());
        server.start();
        server.join();
    }

    private static class MainHandler extends AbstractHandler {
        public static final String REGEX_SERVER = "^/servers/(.*)$";

        @Override
        public void handle(String url, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

            StringBuilder result = new StringBuilder();

            if (url.matches(REGEX_SERVER)) {
                String server = url.replaceAll(REGEX_SERVER, "$1");
                System.out.println("Request to server " + server);

                String username = request.getParameter("username");
                String password = request.getParameter("password");

                response.setStatus(HttpServletResponse.SC_OK);
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setContentType("application/json;charset=utf-8");
                baseRequest.setHandled(true);

                String commandResponse = "";
                String commandError = "";
                try {
                    commandResponse = executeCommand(server, username, password, "ls -ls");
                } catch (JSchException e) {
                    commandError = "Err: " + e.getMessage();
                }

                result.append("{\"server\": \"").append(server).append("\",")
                        .append("\"username\":\"").append(username).append("\",")
                        .append("\"password\":\"").append(password).append("\",")
                        .append("\"commandError\":\"").append(commandError).append("\",")
                        .append("\"commandResponse\":\"").append(commandResponse).append("\"}");
                System.out.println(result);
            }
            response.getWriter().print(result);
        }

    }
    
    private static String executeCommand(String server, String username, String password, String command) throws JSchException, IOException {
        Session session = getSession(server, username, password);
        Channel channel = getChannel(command, session);

        String tmp;
        final StringBuilder buffer = new StringBuilder();
        final BufferedReader fromServer = new BufferedReader(new InputStreamReader(channel.getInputStream()));

        while (!channel.isClosed()) {
            while ((tmp = fromServer.readLine()) != null) {
                buffer.append(tmp).append("\n");
            }
        }
        System.out.println("exit-status: " + channel.getExitStatus());
        channel.disconnect();
        session.disconnect();

        return normalizeReturn(buffer);
    }

    private static String normalizeReturn(StringBuilder buffer) {
        return buffer.toString().replaceAll("\\n","");
    }

    private static Session getSession(String server, String username, String password) throws JSchException {
        Session session = new JSch().getSession(username, server);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(TIMEOUT_IN_SECONDS);
        return session;
    }

    private static Channel getChannel(String command, Session session) throws JSchException {
        Channel channel=  session.openChannel("exec");
        ((ChannelExec)channel).setCommand(command);
        channel.setInputStream(null);
        ((ChannelExec)channel).setErrStream(System.err);
        channel.connect();
        return channel;
    }
}
