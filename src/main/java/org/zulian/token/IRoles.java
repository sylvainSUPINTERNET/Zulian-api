package org.zulian.token;

public interface IRoles {
    public RoleImpl.Roles getUserRole();
    public RoleImpl.Roles getAdminRole();
    public RoleImpl.Roles getSuperAdminRole();
}
