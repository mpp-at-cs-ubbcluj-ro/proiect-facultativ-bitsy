package com.helpinghands.rest_services.data;

import com.helpinghands.domain.Voluntar;

public class AddVoluntarRequestData {
    private int idVoluntar;
    private String role;

    public int getIdVoluntar() {
        return idVoluntar;
    }

    public void setIdVoluntar(int idVoluntar) {
        this.idVoluntar = idVoluntar;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String rol) {
        this.role = rol;
    }
}
