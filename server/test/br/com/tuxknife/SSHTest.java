package br.com.tuxknife;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SSHTest {

    private Session sshSession = mock(Session.class);
    private ChannelExec channel = mock(ChannelExec.class);
    private String username = "user";
    private String server = "server";
    private String port = "22";
    private String command = "command";

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void shouldGetSession() throws JSchException {
        SSH.Jsch = mock(JSch.class);
        when(SSH.Jsch.getSession(username, server, Integer.valueOf(port))).thenReturn(sshSession);

        String password = "pass";
        Session expectedSshSession = SSH.getSession(username, password, server, port);

        verify(sshSession).setPassword(password);
        verify(sshSession).setConfig("StrictHostKeyChecking", "no");
        verify(sshSession).connect(SSH.TIMEOUT_IN_MILLISECONDS);

        assertThat(sshSession, is(equalTo(expectedSshSession)));
    }

    @Test
    @Ignore("Learn how to implement it")
    public void shouldThrowJschExceptionWhenPasswordIsWrong() throws JSchException {
        SSH.Jsch = mock(JSch.class);
        when(SSH.Jsch.getSession(username, server, Integer.valueOf(port))).thenReturn(sshSession);
        doThrow(JSchException.class).when(sshSession).connect();

        String wrongPassword = "just wrong";
        SSH.getSession(username, wrongPassword, server, port);
    }

    @Test
    public void shouldGetChannel() throws Exception {
        when(sshSession.openChannel("exec")).thenReturn(channel);

        Channel expectedChannel = SSH.getChannel(command, sshSession);

        verify(channel).setCommand(command);
        verify(channel).setInputStream(null);
        verify(channel).setErrStream(System.err);
        verify(channel).connect();

        assertThat((ChannelExec) expectedChannel, is(equalTo(channel)));
    }

    @Test
    public void shouldExecuteCommand() throws Exception {
        String actualResult = "command result";
        InputStream inputStream = new ByteArrayInputStream(actualResult.getBytes());

        when(sshSession.openChannel("exec")).thenReturn(channel);
        when(channel.getInputStream()).thenReturn(inputStream);
        when(channel.isClosed()).thenReturn(false).thenReturn(true);

        String commandResult = SSH.executeCommand(command, sshSession);

        assertThat(commandResult, is(equalTo(actualResult)));
    }
}
