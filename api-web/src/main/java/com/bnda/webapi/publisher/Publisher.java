package com.bnda.webapi.publisher;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name="api_publisher")
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="publisher_name")
    private String publisherName;

    @Column(name="publisher_org")
    private String organization;

    @Column(name="base_uri")
    private String baseUri;

    @JsonIgnore
    @OneToMany(mappedBy = "publisher", orphanRemoval = true)
    List<ApiEndPoint> endPoints;
}
