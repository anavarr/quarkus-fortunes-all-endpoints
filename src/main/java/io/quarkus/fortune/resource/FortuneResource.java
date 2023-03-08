package io.quarkus.fortune.resource;

import io.quarkus.fortune.model.Fortune;
import io.quarkus.fortune.repository.FortuneRepository;
import io.smallrye.common.annotation.NonBlocking;
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

    @GET
    @RunOnVirtualThread
    @NonBlocking
    @Path("/get_virtual")
    public List<Fortune> getVirtual() {
        System.out.println("in a virtual-thread"+Thread.currentThread());
        return repository.findAllDeadLockPrint().await().indefinitely();
    }

    @GET
    @RunOnVirtualThread
    @NonBlocking
    @Path("/get_eventloop")
    public Uni<List<Fortune>> getReactive() {
        System.out.println("in an event-loop"+Thread.currentThread());
        return repository.findAllDeadLockPrint();
    }

    @GET
    @RunOnVirtualThread
    @NonBlocking
    @Path("/print-inner")
    public List<Fortune> getAllInner() {
        var list = repository.findAllDeadLockPrint();
        return list.await().indefinitely();
    }

    @GET
    @RunOnVirtualThread
    @NonBlocking
    @Path("/print-outer")
    public List<Fortune> getAllOuter() {
        System.out.println("outer - "+Thread.currentThread());
        var list = repository.findAllDeadLock();
        return list.await().indefinitely();
    }

    @GET
    @RunOnVirtualThread
    @NonBlocking
    @Path("/print-both")
    public List<Fortune> getAll() {
        System.out.println("outer - "+Thread.currentThread());
        var list = repository.findAllDeadLockPrint();
        return list.await().indefinitely();
    }

    private Fortune pickOne(List<Fortune> list) {
        int idx = random.nextInt(list.size());
        return list.get(idx);
    }

}
