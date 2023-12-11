package com.bnda.webapi.consumer;
import com.bnda.webapi.subscription.Subscription;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Data
@Entity
@Table(name="api_consumer")
public class Consumer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "token_id", unique = true, nullable = false)
    private String tokenId;

    @Column(name="consumer_name")
    private String consumerName;

    @Column(name="consumer_org")
    private String consumerOrganization;

    @Column(name = "is_active")
    private boolean isActive;

    @JsonIgnore
    @OneToMany(mappedBy = "consumer")
    List<Subscription> apiSubscriptions;


}
