package com.bnda.webapi.publisher;

import com.bnda.webapi.subscription.Subscription;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="api_endpoints")
public class ApiEndPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="path",unique = true, nullable = false)
    private String path;

    @Enumerated(EnumType.STRING)
    private MethodType method;

    private String parameter;

    @ManyToOne
    @JoinColumn(name="publisher_id")
    public Publisher publisher;

    @JsonIgnore
    @OneToMany(mappedBy = "apiEndPoint", orphanRemoval = true)
    List<Subscription> listOfSubscriptions;

}
