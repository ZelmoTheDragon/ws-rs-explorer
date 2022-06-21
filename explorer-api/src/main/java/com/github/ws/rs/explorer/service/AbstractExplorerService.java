package com.github.ws.rs.explorer.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.json.JsonObject;


import com.github.ws.rs.explorer.Action;
import com.github.ws.rs.explorer.DynamicEntry;
import com.github.ws.rs.explorer.Jsons;
import com.github.ws.rs.explorer.persistence.Queries;
import com.github.ws.rs.explorer.persistence.ExplorerDAO;
import com.github.ws.rs.explorer.ExplorerManager;
import com.github.ws.rs.explorer.EntityMapper;


public abstract class AbstractExplorerService implements ExplorerService {

    protected final ExplorerManager explorerManager;
    protected final ExplorerDAO dao;

    protected AbstractExplorerService(final ExplorerManager explorerManager, final ExplorerDAO dao) {
        this.explorerManager = explorerManager;
        this.dao = dao;
    }

    @Override
    public <E, D, M extends EntityMapper<E, D>> PaginationData<D> onFilter(final String name, final Map<String, List<String>> parameters) {

        var entry = this.explorerManager.<E, D, M, AbstractExplorerService>resolve(name);
        checkAction(entry, Action.FILTER);

        var entityClass = entry.getEntityClass();
        var mapper = this.explorerManager.invokeMapper(entry);
        var queries = Queries.extractQueries(parameters);

        var entities = this.dao.find(entityClass, queries);
        var size = this.dao.size(entityClass, queries);
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
    public <E, D, M extends EntityMapper<E, D>> Optional<D> onFind(final String name, final String id) {

        var entry = this.explorerManager.<E, D, M, AbstractExplorerService>resolve(name);
        checkAction(entry, Action.FIND);

        var entityClass = entry.getEntityClass();
        var mapper = this.explorerManager.invokeMapper(entry);
        var uuid = mapper.mapId(id);

        return this.dao
                .find(entityClass, uuid)
                .map(mapper::fromEntity);
    }

    @Override
    public <E, D, M extends EntityMapper<E, D>, K> K onCreate(final String name, final JsonObject document) {

        var entry = this.explorerManager.<E, D, M, AbstractExplorerService>resolve(name);
        checkAction(entry, Action.CREATE);

        var dataClass = entry.getDataClass();
        var mapper = this.explorerManager.invokeMapper(entry);

        var data = Jsons.parse(dataClass, document);
        var entity = mapper.toEntity(data);

        if (this.dao.contains(entity)) {
            throw new ServiceExplorerException("Entity already exist !");
        }

        entity = this.dao.save(entity);
        return this.dao.getPrimaryKey(entity);

    }

    @Override
    public <E, D, M extends EntityMapper<E, D>> void onUpdate(final String name, final JsonObject document, final String id) {

        var entry = this.explorerManager.<E, D, M, AbstractExplorerService>resolve(name);
        checkAction(entry, Action.UPDATE);

        var entityClass = entry.getEntityClass();
        var dataClass = entry.getDataClass();
        var mapper = this.explorerManager.invokeMapper(entry);
        var uuid = mapper.mapId(id);

        var data = Jsons.parse(dataClass, document);
        var entity = this.dao
                .find(entityClass, uuid)
                .orElseThrow(() -> new ServiceExplorerException("Entity not exist !"));

        mapper.updateEntity(data, entity);
        //this.dao.save(entity);
    }

    @Override
    public <E, D, M extends EntityMapper<E, D>> void onDelete(final String name, final String id) {

        var entry = this.explorerManager.<E, D, M, AbstractExplorerService>resolve(name);
        checkAction(entry, Action.DELETE);

        var mapper = this.explorerManager.invokeMapper(entry);
        var uuid = mapper.mapId(id);

        var entityClass = entry.getEntityClass();
        this.dao.remove(entityClass, uuid);
    }

    protected static void checkAction(final DynamicEntry<?, ?, ?, ?> entry, final Action action) {
        if (!entry.getActions().contains(action)) {
            throw new ActionDeniedException(action);
        }
    }
}
