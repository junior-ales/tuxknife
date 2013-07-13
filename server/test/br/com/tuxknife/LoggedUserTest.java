package br.com.tuxknife;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LoggedUserTest {

    @Test
    public void shouldCheckWhenUserIsLogged() throws JSchException {
        LoggedUser user = new LoggedUser();
        assertThat(user.isLoggedOut(), is(true));

        Session session = mock(Session.class);
        user.setSshSession(session);

        assertThat(user.isLoggedOut(), is(false));
    }


    @Test
    public void shouldCloseUserSessionWhenItsValid() {
        LoggedUser user = new LoggedUser();

        Session session = mock(Session.class);
        user.setSshSession(session);

        user.closeSession();
        verify(session).disconnect();
    }
}
