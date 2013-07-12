package br.com.tuxknife;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

@Resource
public class IndexController {

    private static final int TIMEOUT_IN_MILLISECONDS = 5000;
    private static final JSch JSCH = new JSch();
    private final Result result;
    private LoggedUser loggedUser;

    public IndexController(Result result, LoggedUser loggedUser) {
		this.result = result;
        this.loggedUser = loggedUser;
        this.result.use(Results.status()).header("Access-Control-Allow-Origin", "*");
    }

    @Get
	@Path("/")
	public void index() {
        result.nothing();
	}

    @Post
    @Path("/servers/{server}")
    public void login(String username, String password, String server) throws IOException {
        if (loggedUser.isLogged()) result.redirectTo(this).commandPage();

        String response = "";
        String error = "";
        try {
            executeCommand(server, username, password, "ls -l");
            response = "test";
//            loggedUser.setSshSession(getSession(server, username, password));
//            response = "OK";
        } catch (JSchException e) {
            error = "Err: " + e.getMessage();
        }
        CommandResponse responseJSON = new CommandResponse(response, error);

        result.use(Results.json()).withoutRoot().from(responseJSON).serialize();
    }

    @Get
    @Path("/commandpage")
    public void commandPage() {
        result.use(Results.json()).withoutRoot().from("commandpage").serialize();
    }

    private String executeCommand(String server, String username, String password, String command) throws JSchException, IOException {
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

    private String normalizeReturn(StringBuilder buffer) {
        return buffer.toString().replaceAll("\\n","");
    }

    // if everything the credentials are valid:
    //  - create a code to identify the combination username,password,server
    //  - store this identification and the session
    //  - use the same session
    //  - dont close the session until the user log out
    private Session getSession(String server, String username, String password) throws JSchException {
        Session session = JSCH.getSession(username, server);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(TIMEOUT_IN_MILLISECONDS);
        return session;
    }

    private Channel getChannel(String command, Session session) throws JSchException {
        Channel channel=  session.openChannel("exec");
        ((ChannelExec)channel).setCommand(command);
        channel.setInputStream(null);
        ((ChannelExec)channel).setErrStream(System.err);
        channel.connect();
        return channel;
    }
}

