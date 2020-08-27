package org.searive.adapter;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.annotations.SseElementType;
import org.searive.application.domain.Person;
import org.searive.application.service.PersonService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/hello")
public class ExampleResource {

    @Inject
    private PersonService personService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/persons")
    public Uni<Response> create(@Valid PersonInput input) {

        return personService
                .create(input.name, input.age)
                .onItem().transform(id -> URI.create("/hello/persons/" + id))
                .onItem().transform(uri -> Response.created(uri).build());
    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.APPLICATION_JSON)
    @Path("/persons")
    public Multi<Person> list() {

        return personService.getAll();
    }

    static class PersonInput {
        @NotBlank
        public String name;
        @Positive
        public Integer age;
    }
}
