package io.quarkus.fortune.resource;

import io.quarkus.fortune.model.Fortune;
import io.quarkus.fortune.repository.FortuneRepository;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.smallrye.mutiny.Uni;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;
import java.util.Random;

@Path("/")
public class FortuneResource  {

    private final FortuneRepository repository;
    private final Random random;

    public FortuneResource(FortuneRepository repository) {
        this.repository = repository;
        this.random = new Random();
    }

    @GET
    @Path("/blocking")
    public Fortune blocking() {
        var list = repository.findAllBlocking();
        return pickOne(list);
    }

    @GET
    @Path("/reactive")
    public Uni<Fortune> reactive() {
        return repository.findAllAsync()
                .map(this::pickOne);
    }

    @GET
    @Path("/loom-jdbc")
    @RunOnVirtualThread
    public Fortune loomWithJdbc() {
        var list = repository.findAllBlocking();
        return pickOne(list);
    }

    @GET
    @Path("/loom")
    @RunOnVirtualThread
    public Fortune loom() {
        var list = repository.findAllAsyncAndAwait();
        return pickOne(list);
    }

    private Fortune pickOne(List<Fortune> list) {
        int idx = random.nextInt(list.size());
        return list.get(idx);
    }

}
