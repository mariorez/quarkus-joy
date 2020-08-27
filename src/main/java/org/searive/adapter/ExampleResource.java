package org.searive.adapter;

import org.searive.application.domain.Person;
import org.searive.application.service.PersonService;

import javax.inject.Inject;
import javax.transaction.Transactional;
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
import java.util.List;

import static javax.ws.rs.core.Response.Status.CREATED;

@Path("/hello")
@Transactional
public class ExampleResource {

    @Inject
    private PersonService personService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/persons")
    public Response create(@Valid PersonInput input) {

        personService.create(input.name, input.age);

        return Response.status(CREATED).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/persons")
    public List<Person> list() {

        return personService.getAll();
    }

    static class PersonInput {
        @NotBlank
        public String name;
        @Positive
        public Integer age;
    }
}
