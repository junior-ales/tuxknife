package br.com.tuxknife;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSH {
    private static final int TIMEOUT_IN_MILLISECONDS = 5000;
    private static final JSch JSCH = new JSch();

    public static Session getSession(String username, String password, String server, String port) throws JSchException {
        Session session = JSCH.getSession(username, server, Integer.valueOf(port));
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(TIMEOUT_IN_MILLISECONDS);
        return session;
    }

    public static Channel getChannel(String command, Session session) throws JSchException {
        Channel channel=  session.openChannel("exec");
        ((ChannelExec)channel).setCommand(command);
        channel.setInputStream(null);
        ((ChannelExec)channel).setErrStream(System.err);
        channel.connect();
        return channel;
    }

    public static String executeCommand(Session session, String command) throws JSchException, IOException {
        Channel channel = SSH.getChannel(command, session);

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

        return normalizeReturn(buffer);
    }

    private static String normalizeReturn(StringBuilder buffer) {
        return buffer.toString().replaceAll("\\n","");
    }
}
