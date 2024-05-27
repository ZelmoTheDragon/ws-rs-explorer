package com.github.happiexplorer.example.gender;

import java.util.Optional;

import com.github.happiexplorer.example.persistence.GenericDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import com.github.happiexplorer.EntityMapper;

@ApplicationScoped
public class GenderMapper implements EntityMapper<GenderEntity, GenderDTO> {

    @Inject
    private GenericDAO dao;

    public GenderMapper() {
        // NO-OP
    }

    @Override
    public GenderEntity toEntity(final GenderDTO data) {
        var entity = Optional
                .ofNullable(data.getId())
                .flatMap(e -> this.dao.find(GenderEntity.class, e))
                .orElseGet(GenderEntity::new);

        this.updateEntity(data, entity);
        return entity;
    }

    @Override
    public GenderDTO fromEntity(final GenderEntity entity) {
        var data = new GenderDTO();
        data.setId(entity.getId());
        data.setName(entity.getName());
        data.setCode(entity.getCode());
        data.setDescription(entity.getDescription());
        return data;
    }

    @Override
    public void updateEntity(final GenderDTO source, final GenderEntity target) {
        target.setName(source.getName());
        target.setCode(source.getCode());
        target.setDescription(source.getDescription());
    }
}
