package com.vpn.model;

import javax.persistence.*;

@Entity
@Table(name = "connections")
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    // child wrt to ServiceProvider
    @ManyToOne
    @JoinColumn
    private ServiceProvider serviceProvider;

    // child wrt to user
    @ManyToOne
    @JoinColumn
    private User user;

    public Connection() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
