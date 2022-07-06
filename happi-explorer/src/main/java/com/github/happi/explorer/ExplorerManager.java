package com.github.happi.explorer;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.CDI;

import com.github.happi.explorer.service.ExplorerService;

/**
 * The manager for all entry point.
 * This class should register dynamics entries when the initializing phase is call.
 */
@ApplicationScoped
public class ExplorerManager {

    /**
     * Dynamics entries.
     */
    private final Set<DynamicEntry<?, ?, ?, ?>> entries;

    /**
     * Default constructor.
     * This class is injectable, don't call this constructor explicitly.
     */
    public ExplorerManager() {
        this.entries = new HashSet<>();
    }

    /**
     * Register a new entry point in application.
     *
     * @param entry New entry point
     * @param <E>   Type of persistence entity
     * @param <D>   Type of data transfer object
     * @param <M>   Type of mapper
     * @param <S>   Type of service
     * @throws IllegalArgumentException If the entry already exist
     */
    public <E, D, M extends EntityMapper<E, D>, S extends ExplorerService> void register(final DynamicEntry<E, D, M, S> entry) {
        if (this.entries.contains(entry)) {
            throw new ExplorerException("Entry already registered : " + entry);
        } else {
            this.entries.add(entry);
        }
    }

    /**
     * Retrieve the entry point form the unique path name.
     *
     * @param name Unique path name
     * @param <E>  Type of persistence entity
     * @param <D>  Type of data transfer object
     * @param <M>  Type of mapper
     * @param <S>  Type of service
     * @return The entry point
     * @throws IllegalArgumentException If no entry found with the unique path name
     */
    public <E, D, M extends EntityMapper<E, D>, S extends ExplorerService> DynamicEntry<E, D, M, S> resolve(final String name) {
        return (DynamicEntry<E, D, M, S>) this.entries
                .stream()
                .filter(d -> Objects.equals(d.getPath(), name))
                .findFirst()
                .orElseThrow(() -> new ExplorerException("No entry registered for name : " + name));
    }

    /**
     * Invoke the service instance using <i>CDI</i>.
     *
     * @param name Unique path name
     * @param <E>  Type of persistence entity
     * @param <D>  Type of data transfer object
     * @param <M>  Type of mapper
     * @param <S>  Type of service
     * @return Instance of service
     */
    public <E, D, M extends EntityMapper<E, D>, S extends ExplorerService> S invokeService(final String name) {
        var entry = this.<E, D, M, S>resolve(name);
        var service = entry.getServiceClass();
        return CDI.current().select(service).get();
    }

    /**
     * Invoke the mapper instance using <i>CDI</i>.
     *
     * @param entry Dynamic entry
     * @param <E>   Type of persistence entity
     * @param <D>   Type of data transfer object
     * @param <M>   Type of mapper
     * @param <S>   Type of service
     * @return Instance of mapper
     */
    public <E, D, M extends EntityMapper<E, D>, S extends ExplorerService> M invokeMapper(final DynamicEntry<E, D, M, S> entry) {
        var mapper = entry.getMapperClass();
        return CDI.current().select(mapper).get();
    }

    /**
     * Retrieve all registered entry point.
     *
     * @return All registered entry point
     */
    public Set<DynamicEntry<?, ?, ?, ?>> entries() {
        return Set.copyOf(entries);
    }

}
