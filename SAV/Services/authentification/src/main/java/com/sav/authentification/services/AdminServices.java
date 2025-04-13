package com.sav.authentification.services;

import com.sav.authentification.model.Admin;
import com.sav.authentification.model.User;

import java.util.List;

public interface AdminServices {


    public void addAdmin(Admin admin);

    public Admin getAdminById(Long id);

    public List<Admin> getAllAdmins();

    public void deleteAdmin(Long id);


}
