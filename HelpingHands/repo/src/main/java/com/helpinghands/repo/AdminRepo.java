package com.helpinghands.repo;

import com.helpinghands.domain.Admin;

public class AdminRepo extends AbstractUtilizatorRepo<Admin> implements IAdminRepo {
    public AdminRepo(Class<Admin> entityType) {
        super(entityType);
    }
}
