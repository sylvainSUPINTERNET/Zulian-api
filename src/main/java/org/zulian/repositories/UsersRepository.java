package org.zulian.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.zulian.entities.Users;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsersRepository implements PanacheRepository<Users> {

    public Users findByEmail(String email){
        return find("email", email).firstResult();
    }
}
