package com.example.assessment.repository;

import com.example.assessment.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long>, JpaSpecificationExecutor<Record> {
}
