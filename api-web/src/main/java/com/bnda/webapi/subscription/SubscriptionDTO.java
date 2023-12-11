package com.bnda.webapi.subscription;

import com.bnda.webapi.consumer.Consumer;
import com.bnda.webapi.publisher.ApiEndPoint;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class SubscriptionDTO {
    private long id;

    private Long consumerID;

    private Long publisherID;

    private Long endpointID;

    private int requestPerSecLimit;

    private int quotaLimit;
}
