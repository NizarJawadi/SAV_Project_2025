package com.sav.authentification.services;

import com.sav.authentification.model.ResponsableSAV;

import java.util.List;


public interface ResponsableSavServices {

    public void addResponsableSav(ResponsableSAV ResponsableSav);

    public ResponsableSAV getResponsableSavById(Long id);

    public List<ResponsableSAV> getAllResponsables();

    public void deleteResponsableSav(Long id);

    public ResponsableSAV updateResponsableSav(Long id, ResponsableSAV updatedResponsable);


}
