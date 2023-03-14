package com.vpn.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;

    private String password;

    private String originalIp;

    private String maskedIp;

    private Boolean connected;

    //parent wrt to country'=
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Country originalCountry;

    //parent wrt to connection
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Connection> connectionList = new ArrayList<>();

    @ManyToMany
    @JoinColumn
    private List<ServiceProvider> serviceProviderList = new ArrayList<>();


}
