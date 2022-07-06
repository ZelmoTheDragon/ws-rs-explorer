package com.github.happi.explorer.example;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class InternalService {

    private static final Logger LOG = Logger.getLogger(InternalService.class.getName());

    @Inject
    private transient EntityManager em;

    public InternalService() {
    }

    public void pingDatabase() {
        var ping = em
                .createNativeQuery("SELECT 1 = 1;")
                .getSingleResult();
        LOG.info("Generated database with JPA, ping: " + ping);
    }

    public void populateDatabase() {
        try {
            LOG.info("Populating database...");

            var sqlScriptFile = this.getClass()
                    .getClassLoader()
                    .getResource("h2-init.sql")
                    .toURI();

            var sqlScriptPath = Path.of(sqlScriptFile);
            var queries = Files.readAllLines(sqlScriptPath, StandardCharsets.UTF_8);

            queries
                    .stream()
                    .map(em::createNativeQuery)
                    .forEach(Query::executeUpdate);

        } catch (URISyntaxException | IOException ex) {
            throw new IllegalStateException(ex);
        }
    }
}