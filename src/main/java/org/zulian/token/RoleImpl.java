package org.zulian.token;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RoleImpl implements IRoles {

    @Override
    public Roles getUserRole() {
        return Roles.ROLE_USER;
    }

    @Override
    public Roles getAdminRole() {
        return Roles.ROLE_ADMIN;
    }

    @Override
    public Roles getSuperAdminRole() {
        return Roles.ROLE_SUPER_ADMIN;
    }

    enum Roles {
        ROLE_USER ,
        ROLE_ADMIN,
        ROLE_SUPER_ADMIN
    }

}
