package com.bnda.webapi.repository;

import com.bnda.webapi.api.ApiPublisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiPublisherRepository extends JpaRepository<ApiPublisher,Long> {
}
