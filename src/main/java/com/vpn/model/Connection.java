package com.vpn.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "connections")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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


}
