package com.picpay.repositories;

import com.picpay.domain.user.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class UserRepository implements PanacheRepository<UserEntity> {

    public long findByDocument(String document) {
        return find("document", document).count();
    }
}
