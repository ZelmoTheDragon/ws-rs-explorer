package com.github.ws.rs.explorer.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.json.JsonObject;

import com.github.ws.rs.explorer.EntityMapper;

public interface ExplorerService {

    <E, D, M extends EntityMapper<E, D>> PaginationData<D> onFilter(String name, Map<String, List<String>> parameters);

    <E, D, M extends EntityMapper<E, D>> Optional<D> onFind(String name, String id);

    <E, D, M extends EntityMapper<E, D>, K> K onCreate(String name, JsonObject document);

    <E, D, M extends EntityMapper<E, D>> void onUpdate(String name, JsonObject document, String id);

    <E, D, M extends EntityMapper<E, D>> void onDelete(String name, String id);

}
