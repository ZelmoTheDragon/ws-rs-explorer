package com.github.ws.rs.explorer.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import jakarta.json.JsonObject;
import jakarta.security.enterprise.SecurityContext;


import com.github.ws.rs.explorer.Action;
import com.github.ws.rs.explorer.DynamicEntry;
import com.github.ws.rs.explorer.Jsons;
import com.github.ws.rs.explorer.persistence.Queries;
import com.github.ws.rs.explorer.persistence.ExplorerDAO;
import com.github.ws.rs.explorer.ExplorerManager;
import com.github.ws.rs.explorer.EntityMapper;
import com.github.ws.rs.explorer.security.SecurityManager;


public abstract class AbstractExplorerService implements ExplorerService {

    protected final SecurityContext securityContext;
    protected final ExplorerManager explorerManager;
    protected final ExplorerDAO dao;

    protected AbstractExplorerService(
            final SecurityContext securityContext,
            final ExplorerManager explorerManager,
            final ExplorerDAO dao) {

        this.securityContext = securityContext;
        this.explorerManager = explorerManager;
        this.dao = dao;
    }

    @Override
    public <E, D, M extends EntityMapper<E, D>> PaginationData<D> filter(
            final String name,
            final Map<String, List<String>> parameters) {

        var entry = this.explorerManager.<E, D, M, AbstractExplorerService>resolve(name);
        checkAuthorization(entry, Action.FILTER);

        var entityClass = entry.getEntityClass();
        var mapper = this.explorerManager.invokeMapper(entry);
        var queries = Queries.convertQueries(parameters);

        var entities = this.dao.find(entityClass, queries, this::onFilter);
        var size = this.dao.size(entityClass, queries, this::onFilter);
        var pageSize = Queries.getPageSize(queries);
        var pageNumber = Queries.getPageNumber(queries);
        var pageCount = Queries.getPageCount(queries, size);

        var data = mapper.fromEntity(entities);

        var paginationData = new PaginationData<D>();
        paginationData.setData(data);
        paginationData.setSize(size);
        paginationData.setPageSize(pageSize);
        paginationData.setPageNumber(pageNumber);
        paginationData.setPageCount(pageCount);
        return paginationData;

    }

    @Override
    public <E, D, M extends EntityMapper<E, D>> Optional<D> find(
            final String name,
            final String id) {

        var entry = this.explorerManager.<E, D, M, AbstractExplorerService>resolve(name);
        checkAuthorization(entry, Action.FIND);

        var entityClass = entry.getEntityClass();
        var mapper = this.explorerManager.invokeMapper(entry);
        var uuid = mapper.mapId(id);

        return this.dao
                .find(entityClass, uuid)
                .map(mapper::fromEntity)
                .map(this::onFind);
    }

    @Override
    public <E, D, M extends EntityMapper<E, D>, K> K create(
            final String name,
            final JsonObject document) {

        var entry = this.explorerManager.<E, D, M, AbstractExplorerService>resolve(name);
        checkAuthorization(entry, Action.CREATE);

        var dataClass = entry.getDataClass();
        var mapper = this.explorerManager.invokeMapper(entry);

        var data = Jsons.parse(dataClass, document);
        var entity = mapper.toEntity(data);

        if (this.dao.contains(entity)) {
            throw new ServiceExplorerException("Entity already exist !");
        }

        entity = this.onCreate(entity);
        entity = this.dao.save(entity);
        return this.dao.getPrimaryKey(entity);

    }

    @Override
    public <E, D, M extends EntityMapper<E, D>> void update(
            final String name,
            final JsonObject document,
            final String id) {

        var entry = this.explorerManager.<E, D, M, AbstractExplorerService>resolve(name);
        checkAuthorization(entry, Action.UPDATE);

        var entityClass = entry.getEntityClass();
        var dataClass = entry.getDataClass();
        var mapper = this.explorerManager.invokeMapper(entry);
        var uuid = mapper.mapId(id);

        var data = Jsons.parse(dataClass, document);
        var entity = this.dao
                .find(entityClass, uuid)
                .orElseThrow(() -> new ServiceExplorerException("Entity not exist !"));

        mapper.updateEntity(data, entity);
        this.onUpdate(entity);
        //this.dao.save(entity);
    }

    @Override
    public <E, D, M extends EntityMapper<E, D>> void delete(
            final String name,
            final String id) {

        var entry = this.explorerManager.<E, D, M, AbstractExplorerService>resolve(name);
        checkAuthorization(entry, Action.DELETE);

        var mapper = this.explorerManager.invokeMapper(entry);
        var entityClass = entry.getEntityClass();
        var uuid = mapper.mapId(id);
        var entity = this.dao
                .find(entityClass, uuid)
                .orElseThrow(() -> new ServiceExplorerException("Entity not exist !"));

        this.onRemove(entity);
        this.dao.remove(entity);
    }

    protected void checkAuthorization(
            final DynamicEntry<?, ?, ?, ?> entry,
            final Action action) {

        if (entry.getActions().containsKey(action)) {
            var role = entry.getActions().get(action);
            if (!Objects.equals(SecurityManager.PERMIT_ALL, role)) {
                var principal = this.securityContext.getCallerPrincipal();
                if (Objects.isNull(principal)) {
                    throw new ActionDeniedException(action, "User not authenticate");
                } else if (!this.securityContext.isCallerInRole(role)) {
                    throw new ActionDeniedException(action, "Insufficient authorization");
                }
                // NO-OP: OK
            }
        } else {
            throw new ActionDeniedException(action);
        }
    }
}
