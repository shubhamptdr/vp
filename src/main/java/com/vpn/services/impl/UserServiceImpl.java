package com.vpn.services.impl;

import com.vpn.model.Country;
import com.vpn.enums.CountryName;
import com.vpn.model.ServiceProvider;
import com.vpn.model.User;
import com.vpn.repository.CountryRepository;
import com.vpn.repository.ServiceProviderRepository;
import com.vpn.repository.UserRepository;
import com.vpn.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;

    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception {
        //check validation
        if (!CountryName.isValid(countryName)) {
            throw new Exception("Country not found");
        }

        // create user Entity
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        // create Country entity
        //set attr. according to validation
        CountryName name = CountryName.valueOf(countryName.toUpperCase());
        Country country = Country.builder()
                .countryName(name)
                .code(name.toCode())
                .build();

//        Country country = countryRepository3.findByCountryName(countryName);

        user.setConnected(false);
        country.setUser(user);
        user.setOriginalCountry(country);


        User user1 = userRepository3.save(user);

        // set originalIp i.e "countryCode.userId"
        StringBuilder sb = new StringBuilder();
        sb.append(country.getCode()).append(".").append(user1.getId());
        user.setOriginalIp(sb.toString());

        //update repo
        userRepository3.save(user);

        return user;
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {
        // fetch user & serviceProvider
        User user = userRepository3.findById(userId).get();
        ServiceProvider serviceProvider = serviceProviderRepository3.findById(serviceProviderId).get();

        // update user & serviceProvider
        user.getServiceProviderList().add(serviceProvider);
        serviceProvider.getUsers().add(user);

        //save parent
        serviceProviderRepository3.save(serviceProvider);
        return user;
    }
}
