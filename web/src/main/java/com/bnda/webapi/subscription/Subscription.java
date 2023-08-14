package com.bnda.webapi.subscription;

import com.bnda.webapi.api.ApiEndPoint;
import com.bnda.webapi.consumer.Consumer;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="api_subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "consumer_id")
    private Consumer consumer;

    @ManyToOne
    @JoinColumn(name = "api_endpoint_id")
    private ApiEndPoint apiEndPoint;

    @Column(name = "rpm_limit")
    private int requestPerMinLimit;

    @Column(name = "quota_limit")
    private int quotaLimit;


}
