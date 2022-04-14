
FROM quay.io/arnavarr/jdk19-loom
WORKDIR /quarkus-jdbc

#COPY --from=maven /quarkus-loom/$MODULE/target//lib lib
COPY /target/ /target

EXPOSE 8080:8080
EXPOSE 5432:5432

CMD ["java","--add-opens","java.base/java.lang=ALL-UNNAMED", "-server","-Xmx256m", "-XX:+UseStringDeduplication", "-XX:+UseNUMA", "-XX:+UseParallelGC", "-Djava.lang.Integer.IntegerCache.high=10000","-Dvertx.disableHttpHeadersValidation=true", "-Dvertx.disableMetrics=true", "-Dvertx.disableH2c=true","-Dvertx.disableWebsockets=true", "-Dvertx.flashPolicyHandler=false", "-Dvertx.threadChecks=false","-Dvertx.disableContextTimings=true", "-Dvertx.disableTCCL=true", "-Dhibernate.allow_update_outside_transaction=true","-Djboss.threads.eqe.statistics=false", "-jar",  "/target/quarkus-app/quarkus-run.jar"]