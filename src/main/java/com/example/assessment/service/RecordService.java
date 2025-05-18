package com.example.assessment.service;

import com.example.assessment.entity.Record;
import com.example.assessment.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.persistence.OptimisticLockException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final RecordRepository recordRepository;

    public Page<Record> searchRecords(String customerId, String accountNumber, String description, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        return recordRepository.findAll((root, query, cb) -> {
            var predicates = cb.conjunction();
            if (customerId != null && !customerId.isBlank())
                predicates = cb.and(predicates, cb.equal(root.get("customerId"), customerId));
            if (accountNumber != null && !accountNumber.isBlank())
                predicates = cb.and(predicates, cb.equal(root.get("accountNumber"), accountNumber));
            if (description != null && !description.isBlank())
                predicates = cb.and(predicates, cb.like(root.get("description"), "%" + description + "%"));
            return predicates;
        }, pageable);
    }

    public Optional<Record> findById(Long id) {
        return recordRepository.findById(id);
    }

    public Record updateDescription(Long id, String description, Long version) {
        Record record = recordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found"));

        if (!record.getVersion().equals(version)) {
            throw new OptimisticLockException("Record was updated by someone else");
        }

        record.setDescription(description);
        return recordRepository.save(record);
    }
}
