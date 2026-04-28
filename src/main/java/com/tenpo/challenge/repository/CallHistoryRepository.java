package com.tenpo.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tenpo.challenge.repository.entity.CallHistory;

/**
 * Repository for managing call history entries.
 */
public interface CallHistoryRepository extends
    JpaRepository<CallHistory, Long> {

}
