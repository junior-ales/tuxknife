package br.com.tuxknife;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import static br.com.caelum.vraptor.view.Results.json;
import static br.com.caelum.vraptor.view.Results.status;

@Resource
@Path("/api")
public class IndexController {

    private static final int TIMEOUT_IN_MILLISECONDS = 5000;
    private static final JSch JSCH = new JSch();
    private final Result result;
    private LoggedUser loggedUser;

    public IndexController(Result result, LoggedUser loggedUser) {
		this.result = result;
        this.loggedUser = loggedUser;
        this.result.use(status()).header("Access-Control-Allow-Origin", "*");
    }

    @Get
	@Path("/")
	public void index() {
        CommandResponse response = new CommandResponse().withResource("login");
        result.use(json()).withoutRoot().from(response).serialize();
	}

    @Post
    @Path("/servers/{server}")
    public void signin(String username, String password, String server) throws IOException {
        if (!loggedUser.isLoggedOut()) result.forwardTo(this).commandPage();

        try {
            loggedUser.setSshSession(getSession(server, username, password));
            result.forwardTo(this).commandPage();
        } catch (JSchException e) {
            String error = "Error: " + e.getMessage();
            CommandResponse response = new CommandResponse().withError(error);
            result.use(json()).withoutRoot().from(response).serialize();
        }
    }

    @Get
    @Path("/commandpage")
    public void commandPage() {
        if (loggedUser.isLoggedOut()) result.forwardTo(this).index();

        CommandResponse response = new CommandResponse();
        try {
            response.addResponseData("hostname", executeCommand(loggedUser.getSshSession(), "hostname"));
            response.setResource("commandpage");
            result.use(json()).withoutRoot().from(response).include("responseData").serialize();
        } catch (JSchException | IOException e) {
            String error = "Error: " + e.getMessage();
            response = new CommandResponse().withError(error);
            result.use(json()).withoutRoot().from(response).serialize();
        }
    }

    @Get
    @Path("/signout")
    public void closeSession() {
        loggedUser.closeSession();
        CommandResponse response = new CommandResponse().withResource("login");
        response.addResponseData("userMessage", "Signed out successfully");
        result.use(json()).withoutRoot().from(response).include("responseData").serialize();
    }

    private String executeCommand(Session session, String command) throws JSchException, IOException {
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

        return normalizeReturn(buffer);
    }

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

    private String normalizeReturn(StringBuilder buffer) {
        return buffer.toString().replaceAll("\\n","");
    }
}

