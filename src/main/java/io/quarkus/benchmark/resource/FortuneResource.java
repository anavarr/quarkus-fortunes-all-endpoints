package io.quarkus.benchmark.resource;

import io.quarkus.benchmark.model.Fortune;
import io.quarkus.benchmark.repository.FortuneRepository;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.common.annotation.RunOnVirtualThread;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Comparator;
import java.util.List;

@Path("/fortunes")
@Blocking
public class FortuneResource  {

    @Inject
    FortuneRepository repository;
    private Comparator<Fortune> fortuneComparator;


    public FortuneResource() {
        fortuneComparator = Comparator.comparing(fortune -> fortune.getMessage());
    }

    @GET
    @Path("create_db")
    public void createDb(){
        System.out.println("creating db");
        repository.prepareDB();
    }

    public JsonArray list2jsonArray(List<Fortune> fortunes){
//        System.out.println(Thread.currentThread());
        var ja = new JsonArray();
        for(Fortune f  : fortunes){
            var jo = new JsonObject();
            jo.put("id", f.getId());
            jo.put("message", f.getMessage());
            ja.add(jo);
        }
        return ja;
    }

    @GET
    @NonBlocking
    @Path("/nb")
    public Uni<JsonArray> fortunesNb() {
        return repository.findAll()
                .map(fortunes -> {
                    fortunes.add(new Fortune(0, "Additional fortune added at request time."));
                    fortunes.sort(fortuneComparator);
                    return list2jsonArray(fortunes);
                });
    }

    @GET
    @Path("/bl/jdbc")
    public JsonArray fortunesBlJdbc() {
        var result = repository.findAllJdbc();
        result.add(new Fortune(0, "Additional fortune added at request time."));
        result.sort(fortuneComparator);
        return list2jsonArray(result);
    }

    @GET
    @Path("/bl/reac")
    public JsonArray fortunesBlReac() {
        return repository.findAll()
                .map(fortunes -> {
                    fortunes.add(new Fortune(0, "Additional fortune added at request time."));
                    fortunes.sort(fortuneComparator);
                    return list2jsonArray(fortunes);
                }).await().indefinitely();
    }

    @GET
    @Path("/bl/reacBis")
    public JsonArray fortunesBlReacBis() {
        var fortunes= repository.findAll().await().indefinitely();
        fortunes.add(new Fortune(0, "Additional fortune added at request time."));
        fortunes.sort(fortuneComparator);
        return list2jsonArray(fortunes);
    }


    @GET
    @RunOnVirtualThread
    @Path("/vt/jdbc")
    public JsonArray fortunesVtJdbc() {
        var result = repository.findAllJdbc();
        result.add(new Fortune(0, "Additional fortune added at request time."));
        result.sort(fortuneComparator);
        return list2jsonArray(result);
    }

    @GET
    @RunOnVirtualThread
    @Path("/vt/reac")
    public JsonArray fortunesVtReac() {
        return repository.findAll()
                .map(fortunes -> {
                    fortunes.add(new Fortune(0, "Additional fortune added at request time."));
                    fortunes.sort(fortuneComparator);
                    return list2jsonArray(fortunes);
                }).await().indefinitely();
    }

    @GET
    @RunOnVirtualThread
    @Path("/vt/reacBis")
    public JsonArray fortunesVtReacBis() {
        var fortunes= repository.findAll().await().indefinitely();
        fortunes.add(new Fortune(0, "Additional fortune added at request time."));
        fortunes.sort(fortuneComparator);
        return list2jsonArray(fortunes);
    }

}
