package com.github.happiexplorer.example.customer;

import java.util.Optional;

import com.github.happiexplorer.example.persistence.GenericDAO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import com.github.happiexplorer.EntityMapper;
import com.github.happiexplorer.example.gender.GenderDTO;
import com.github.happiexplorer.example.gender.GenderEntity;
import com.github.happiexplorer.example.gender.GenderMapper;


@ApplicationScoped
public class CustomerMapper implements EntityMapper<CustomerEntity, CustomerDTO> {

    @Inject
    private GenericDAO dao;

    @Inject
    private GenderMapper genderMapper;

    public CustomerMapper() {
        // NO-OP
    }

    @Override
    public CustomerEntity toEntity(final CustomerDTO data) {
        var entity = Optional
                .ofNullable(data.getId())
                .flatMap(e -> this.dao.find(CustomerEntity.class, e))
                .orElseGet(CustomerEntity::new);

        this.updateEntity(data, entity);

        return entity;
    }

    @Override
    public CustomerDTO fromEntity(final CustomerEntity entity) {
        var data = new CustomerDTO();
        data.setId(entity.getId());
        data.setGivenName(entity.getGivenName());
        data.setFamilyName(entity.getFamilyName());
        data.setEmail(entity.getEmail());
        data.setPhoneNumber(entity.getPhoneNumber());

        Optional
                .ofNullable(entity.getGender())
                .map(this.genderMapper::fromEntity)
                .ifPresent(data::setGender);

        return data;
    }

    @Override
    public void updateEntity(final CustomerDTO source, final CustomerEntity target) {
        target.setGivenName(source.getGivenName());
        target.setFamilyName(source.getFamilyName());
        target.setEmail(source.getEmail());
        target.setPhoneNumber(source.getPhoneNumber());

        Optional
                .ofNullable(source.getGender())
                .map(GenderDTO::getId)
                .flatMap(e -> this.dao.find(GenderEntity.class, e))
                .ifPresent(target::setGender);
    }
}
