package br.com.tuxknife;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.util.test.MockResult;
import br.com.caelum.vraptor.util.test.MockSerializationResult;
import br.com.tuxknife.helper.Should;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IndexControllerTest {

    private LoggedUser loggedUser;
    private MockSerializationResult serializationResult;
    private final boolean withoutJsonRootElement = true;
    private IndexController controller;

    @Before
    public void setUp() throws Exception {
        loggedUser = mock(LoggedUser.class);
        serializationResult = new MockSerializationResult();
        controller = new IndexController(serializationResult, loggedUser);
    }

    @Test
    public void shouldForwardToLoginPage() throws Exception {
        controller.index();

        CommandResponse cResponse = new CommandResponse().withResource("login");
        String expectedResult = Should.getJsonOf(cResponse, withoutJsonRootElement);

        assertThat(serializationResult.serializedResult(), equalTo(expectedResult));
    }

    @Test
    public void shouldSendLogOutMessageSuccessfully() throws Exception {
        controller.closeSession();

        verify(loggedUser).closeSession();

        CommandResponse cResponse = new CommandResponse().withResource("login");
        cResponse.addResponseData("userMessage", "Signed out successfully");
        String expectedResult = Should.getJsonOf(cResponse, withoutJsonRootElement);

        assertThat(serializationResult.serializedResult(), equalTo(expectedResult));
    }

    @Test
    @Ignore(value = "WIP")
    public void shouldSendLoginErrorWhenCredentialAreInvalid() throws Exception {
        IndexController mockedController = spy(controller);
        when(loggedUser.isLoggedOut()).thenReturn(true);

        Session sshSession = mock(Session.class);
//        when(IndexController.getSession("username", "password", "server")).thenReturn(sshSession);

//        doThrow(new JSchException("asdjga")).when(loggedUser).setSshSession(sshSession);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                throw new JSchException("");
            }
        }).when(loggedUser).setSshSession(sshSession);

        mockedController.signin("username", "password", "server");

        CommandResponse cResponse = new CommandResponse().withError("");
        String expectedResult = Should.getJsonOf(cResponse, withoutJsonRootElement);

        assertThat(serializationResult.serializedResult(), equalTo(expectedResult));
    }

    @Test
    @Ignore(value = "Looking for ways to implement it")
    public void shouldForwardToCommandPageWhenLoggedIn() {
        when(loggedUser.isLoggedOut()).thenReturn(false);
        Result result = spy(new MockResult());
        IndexController controller = spy(new IndexController(result, loggedUser));
        controller.index();

        when(result.forwardTo(controller)).thenReturn(controller);
        verify(controller).commandPage();
    }
}
