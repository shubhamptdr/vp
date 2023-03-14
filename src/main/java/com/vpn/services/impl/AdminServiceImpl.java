package com.vpn.services.impl;


import com.vpn.model.Admin;
import com.vpn.model.Country;
import com.vpn.model.CountryName;
import com.vpn.model.ServiceProvider;
import com.vpn.repository.AdminRepository;
import com.vpn.repository.CountryRepository;
import com.vpn.repository.ServiceProviderRepository;
import com.vpn.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminRepository adminRepository1;

    @Autowired
    ServiceProviderRepository serviceProviderRepository1;

    @Autowired
    CountryRepository countryRepository1;

    @Override
    public Admin register(String username, String password) {
        // create admin entity
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(password);

        // save admin
        adminRepository1.save(admin);
        return admin;
    }

    @Override
    public Admin addServiceProvider(int adminId, String providerName) {
        // fetch admin
        Admin admin = adminRepository1.findById(adminId).get();
        // crete serviceProvider entity
        ServiceProvider serviceProvider = new ServiceProvider();

        serviceProvider.setAdmin(admin);
        serviceProvider.setName(providerName);
        admin.getServiceProviders().add(serviceProvider);

        // save parent ie admin wrt serviceProvider;
        adminRepository1.save(admin);
        return admin;
    }

    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception {

        if (!CountryName.isValid(countryName)) {
            throw new Exception("Country not found");
        }

        // fetch
        ServiceProvider serviceProvider = serviceProviderRepository1.findById(serviceProviderId).get();
        // create county entity
        Country country = new Country();

        // set attr.
        CountryName name = CountryName.valueOf(countryName.toUpperCase());
        country.setCountryName(name);
        country.setCode(name.toCode());
        country.setServiceProvider(serviceProvider);
        serviceProvider.getCountryList().add(country);
        // save parent
        serviceProviderRepository1.save(serviceProvider);

        return serviceProvider;

    }
}
