package br.com.tuxknife;

import java.io.IOException;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import com.jcraft.jsch.JSchException;

import static br.com.caelum.vraptor.view.Results.json;
import static br.com.caelum.vraptor.view.Results.status;

@Resource
@Path("/api")
public class IndexController {

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
        if (!loggedUser.isLoggedOut()) result.forwardTo(this).commandPage();

        CommandResponse response = new CommandResponse().withResource("login");
        result.use(json()).withoutRoot().from(response).serialize();
	}

    @Post
    @Path("/servers/{server}/{port}")
    public void signin(String username, String password, String server, String port) throws IOException {
        if (!loggedUser.isLoggedOut()) result.forwardTo(this).commandPage();

        try {
            loggedUser.setSshSession(SSH.getSession(username, password, server, port));
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
            response.addResponseData("hostname", SSH.executeCommand(loggedUser.getSshSession(), "hostname"));
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
}

