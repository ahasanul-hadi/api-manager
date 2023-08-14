package com.bnda.webapi.repository;

import com.bnda.webapi.api.ApiEndPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiEndPointRepository extends JpaRepository<ApiEndPoint, Long> {
}
