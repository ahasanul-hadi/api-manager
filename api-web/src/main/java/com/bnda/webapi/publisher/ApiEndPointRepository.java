package com.bnda.webapi.publisher;

import com.bnda.webapi.publisher.ApiEndPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiEndPointRepository extends JpaRepository<ApiEndPoint, Long> {
}
