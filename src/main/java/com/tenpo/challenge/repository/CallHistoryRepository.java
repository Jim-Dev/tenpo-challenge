package com.tenpo.challenge.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tenpo.challenge.repository.entity.CallHistory;

/**
 * Repository for managing call history entries.
 */
public interface CallHistoryRepository extends
    JpaRepository<CallHistory, Long> {

    /**
     * Finds all call history entries ordered by timestamp descending.
     *
     * @param pageable pagination information
     * @return page of call history entries
     */
    Page<CallHistory> findAllByOrderByTimestampDesc(Pageable pageable);

}
