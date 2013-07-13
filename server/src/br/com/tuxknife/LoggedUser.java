package br.com.tuxknife;

import javax.annotation.PreDestroy;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.ioc.SessionScoped;
import com.jcraft.jsch.Session;

@Component
@SessionScoped
public class LoggedUser {

    private Session sshSession;

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
            System.out.println("Session closed");
        }
    }
}
