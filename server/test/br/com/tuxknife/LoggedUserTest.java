package br.com.tuxknife;

import javax.servlet.http.HttpSession;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LoggedUserTest {

    private LoggedUser user;
    private Session sshSession;

    @Before
    public void setUp() throws Exception {
        user = new LoggedUser(mock(HttpSession.class));
        sshSession = mock(Session.class);
    }

    @Test
    public void shouldCheckWhenUserIsLogged() throws JSchException {
        assertThat(user.isLoggedOut(), is(true));
        user.setSshSession(sshSession);
        assertThat(user.isLoggedOut(), is(false));
    }


    @Test
    public void shouldCloseUserSessionWhenItsValid() {
        user.setSshSession(sshSession);

        user.closeSession();
        verify(sshSession).disconnect();
    }
}
