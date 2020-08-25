package org.searive.application.service;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;
import org.searive.application.domain.Person;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class PersonService {

    @Inject
    PgPool client;

    public Uni<Long> create(String name, Integer age) {

        return client.preparedQuery("INSERT INTO person (name, age) VALUES ($1, $2) RETURNING (id)")
                .execute(Tuple.of(name, age))
                .onItem().transform(pgRowSet -> pgRowSet.iterator().next().getLong("id"));
    }

    public Multi<Person> getAll() {

        return client
                .query("SELECT id, name, age FROM person ORDER BY name ASC")
                .execute()
                .onItem()
                .transformToMulti(set ->
                        Multi.createFrom().items(() -> StreamSupport.stream(set.spliterator(), false)))
                .onItem()
                .transform(this::from);
    }

    private Person from(Row row) {

        var person = new Person();
        person.id = row.getLong("id");
        person.name = row.getString("name");
        person.age = row.getInteger("age");

        return person;
    }
}
