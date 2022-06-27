package com.github.ws.rs.explorer;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Singleton;

import com.github.ws.rs.explorer.service.ExplorerService;

@Singleton
public class ExplorerManager {

    private final Set<DynamicEntry<?, ?, ?, ?>> entries;

    public ExplorerManager() {
        this.entries = new HashSet<>();
    }

    public <E, D, M extends EntityMapper<E, D>, S extends ExplorerService> void register(final DynamicEntry<E, D, M, S> entry) {
        if (this.entries.contains(entry)) {
            throw new IllegalArgumentException("Entry already registered : " + entry);
        } else {
            this.entries.add(entry);
        }
    }

    public <E, D, M extends EntityMapper<E, D>, S extends ExplorerService> DynamicEntry<E, D, M, S> resolve(final String name) {
        return (DynamicEntry<E, D, M, S>) this.entries
                .stream()
                .filter(d -> Objects.equals(d.getName(), name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No entry registered for name : " + name));
    }

    public <E, D, M extends EntityMapper<E, D>, S extends ExplorerService> S invokeService(final String name) {
        var entry = this.<E, D, M, S>resolve(name);
        var service = entry.getServiceClass();
        return CDI.current().select(service).get();
    }

    public <E, D, M extends EntityMapper<E, D>, S extends ExplorerService> M invokeMapper(final DynamicEntry<E, D, M, S> entry) {
        var mapper = entry.getMapperClass();
        return CDI.current().select(mapper).get();
    }

    public Set<DynamicEntry<?, ?, ?, ?>> entries() {
        return Set.copyOf(entries);
    }

}
