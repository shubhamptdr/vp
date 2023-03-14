package com.vpn.services;


import com.vpn.model.Admin;
import com.vpn.model.ServiceProvider;

public interface AdminService {
    public Admin register(String username, String password);
    public Admin addServiceProvider(int adminId, String providerName) ;
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception;

}