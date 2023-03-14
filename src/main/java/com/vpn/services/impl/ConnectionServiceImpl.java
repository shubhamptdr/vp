package com.vpn.services.impl;


import com.vpn.enums.CountryName;
import com.vpn.model.*;
import com.vpn.repository.ConnectionRepository;
import com.vpn.repository.ServiceProviderRepository;
import com.vpn.repository.UserRepository;
import com.vpn.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired
    UserRepository userRepository2;
    @Autowired
    ServiceProviderRepository serviceProviderRepository2;

    @Autowired
    ConnectionRepository connectionRepository2;

    @Override
    public User connect(int userId, String countryName) throws Exception{
        // fetch user
        User user = userRepository2.findById(userId).get();

        // check validation for user isConnected or not
        if(user.getConnected()){
            throw new Exception("Already connected");
        }

        // check validation for countryName corresponds to the original country of the user
        if(countryName.equalsIgnoreCase(user.getOriginalCountry().getCountryName().toString())){
            return user;
        }

        // check validation for subscribed user
        if (user.getServiceProviderList()==null){
            throw new Exception("Unable to connect");
        }


        ServiceProvider serviceProvider = null;
        Country country = null;
        int max = Integer.MAX_VALUE;

        for(ServiceProvider serviceProvider1:user.getServiceProviderList()){

            for (Country country1: serviceProvider1.getCountryList()){

                if(countryName.equalsIgnoreCase(country1.getCountryName().toString()) && max > serviceProvider1.getId() ){
                    max=serviceProvider1.getId();
                    serviceProvider=serviceProvider1;
                    country=country1;
                }
            }
        }
        // create connection and set maskedIp is "updatedCountryCode.serviceProviderId.userId"
        if (serviceProvider!=null){

            String countryC = country.getCode();
            int givenId = serviceProvider.getId();
            String mask = countryC+"."+givenId+"."+userId;

            Connection connection = Connection.builder()
                    .user(user)
                    .serviceProvider(serviceProvider)
                    .build();

            user.setConnected(true);
            user.setMaskedIp(mask);
            user.getConnectionList().add(connection);

            serviceProvider.getConnectionList().add(connection);

            userRepository2.save(user);
            serviceProviderRepository2.save(serviceProvider);

        }

        return user;
    }
    @Override
    public User disconnect(int userId) throws Exception {
        // fetch user
        User user = userRepository2.findById(userId).get();

        // check for user isConnected or not
        if(!user.getConnected()){
            throw new Exception("Already disconnected");
        }

        // set attr
        user.setConnected(false);
        user.setMaskedIp(null);
        //save user
        userRepository2.save(user);
        return user;
    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception {
        User user = userRepository2.findById(senderId).get();
        User user1 = userRepository2.findById(receiverId).get();

        // if receiver is connected
        if(user1.getConnected()){
            String str = user1.getMaskedIp();
            String countryCode = str.substring(0,3);

            // check for sender's original country matches receiver's current country
            if(countryCode.equals(user.getOriginalCountry().getCode())){
                return user;
            }


            String countryName =  CountryName.getCountryName(countryCode);
            User user2 = connect(senderId,countryName);

            // If communication can not be established due to any reason
            if (!user2.getConnected()){
                throw new Exception("Cannot establish communication");
            }
            return user2;

        }

        if(user1.getOriginalCountry().equals(user.getOriginalCountry())){
            return user;
        }

        String countryName = user1.getOriginalCountry().getCountryName().toString();

        User user2 =  connect(senderId,countryName);
        if (!user2.getConnected()){
            throw new Exception("Cannot establish communication");
        }
        return user2;


    }
}
