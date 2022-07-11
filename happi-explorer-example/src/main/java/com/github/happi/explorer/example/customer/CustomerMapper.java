package com.github.happi.explorer.example.customer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import com.github.happi.explorer.example.persistence.GenericDAO;
import com.github.happi.explorer.EntityMapper;


@ApplicationScoped
public class CustomerMapper implements EntityMapper<CustomerEntity, CustomerDTO> {

    @Inject
    private GenericDAO dao;

    public CustomerMapper() {
    }

    @Override
    public CustomerEntity toEntity(final CustomerDTO data) {
        var entity = this.dao
                .find(CustomerEntity.class, data.getId())
                .orElseGet(CustomerEntity::new);

        this.updateEntity(data, entity);

        return entity;
    }

    @Override
    public CustomerDTO fromEntity(final CustomerEntity entity) {
        var data = new CustomerDTO();
        data.setGivenName(entity.getGivenName());
        data.setFamilyName(entity.getFamilyName());
        data.setEmail(entity.getEmail());
        data.setPhoneNumber(entity.getPhoneNumber());
        return data;
    }

    @Override
    public void updateEntity(final CustomerDTO source, final CustomerEntity target) {
        target.setGivenName(source.getGivenName());
        target.setFamilyName(source.getFamilyName());
        target.setEmail(source.getEmail());
        target.setPhoneNumber(source.getPhoneNumber());
    }
}
