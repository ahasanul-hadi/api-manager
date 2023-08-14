package com.bnda.webapi.repository;

import com.bnda.webapi.consumer.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerRepository extends JpaRepository<Consumer, Long> {
}
