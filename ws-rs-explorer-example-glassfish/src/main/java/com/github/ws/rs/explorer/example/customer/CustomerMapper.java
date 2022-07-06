package com.github.ws.rs.explorer.example.customer;

import java.util.UUID;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import com.github.ws.rs.explorer.EntityMapper;
import com.github.ws.rs.explorer.example.persistence.GenericDAO;


@Singleton
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
