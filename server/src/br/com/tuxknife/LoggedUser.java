package br.com.tuxknife;

import javax.annotation.PreDestroy;
import javax.servlet.http.HttpSession;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;
import com.jcraft.jsch.Session;

@Component
@SessionScoped
public class LoggedUser {

    private Session sshSession;
    private HttpSession httpSession;

    public LoggedUser(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    public Session getSshSession() {
        return sshSession;
    }

    public void setSshSession(Session sshSession) {
        this.sshSession = sshSession;
    }

    public boolean isLoggedOut() {
        return sshSession == null;
    }

    @PreDestroy
    public void closeSession() {
        if (!isLoggedOut()) {
            sshSession.disconnect();
            System.out.println("SSH session closed");
        }
        httpSession.invalidate();
    }
}
