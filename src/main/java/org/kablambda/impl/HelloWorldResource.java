package org.kablambda.impl;

import com.atlassian.plugins.rest.common.security.AnonymousAllowed;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.MultiPart;
import org.apache.commons.io.IOUtils;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

@Path("/")
public class HelloWorldResource {

    @GET
    @Path("test")
    @Produces("text/plain")
    @AnonymousAllowed
    public Response uploadFileToOtherUrl() {
        final ClientConfig config = new DefaultClientConfig();
        final Client client = Client.create(config);

        final WebResource resource = client.resource("http://localhost:2990/jira/rest/helloworld/1.0/send");
        final FormDataMultiPart request = new FormDataMultiPart();
        request.bodyPart(new BodyPart(getClass().getResourceAsStream("/foo.txt"), MediaType.TEXT_PLAIN_TYPE));
        final String response = resource
                .entity(request, "multipart/form-data")
                .accept("text/plain")
                .post(String.class);
        return Response.ok(response).build();
    }

    @POST
    @Path("send")
    @Consumes("multipart/form-data")
    @Produces("text/plain")
    @AnonymousAllowed
    public Response receiveUpload(MultiPart form) throws IOException {
        return Response.ok(IOUtils.toString(form.getBodyParts().get(0).getEntityAs(InputStream.class))).build();
    }
}

