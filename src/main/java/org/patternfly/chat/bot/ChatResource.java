package org.patternfly.chat.bot;

import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class ChatResource {

    @Inject
    MyChatService chatService;


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@QueryParam("q") String query) {

        String result = chatService.patternFlyInfo(query != null && !query.isEmpty() ? query: "PatternFly");

        return "Here is your info about " + query + ":\n" + result;
    }

}
