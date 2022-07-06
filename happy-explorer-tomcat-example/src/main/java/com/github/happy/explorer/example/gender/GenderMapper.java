package com.github.happy.explorer.example.gender;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import com.github.happy.explorer.EntityMapper;
import com.github.happy.explorer.example.persistence.GenericDAO;

@ApplicationScoped
public class GenderMapper implements EntityMapper<GenderEntity, GenderDTO> {

    @Inject
    private GenericDAO dao;

    public GenderMapper() {
    }

    @Override
    public GenderEntity toEntity(final GenderDTO data) {
        var entity = this.dao
                .find(GenderEntity.class, data.getId())
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
