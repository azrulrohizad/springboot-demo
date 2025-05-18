package com.example.assessment.controller;

import com.example.assessment.dto.UpdateDescriptionRequest;
import com.example.assessment.entity.Record;
import com.example.assessment.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.persistence.OptimisticLockException;
import java.util.Map;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @GetMapping
    public Page<Record> getRecords(
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return recordService.searchRecords(customerId, accountNumber, description, page, size);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDescription(
            @PathVariable Long id,
            @RequestBody UpdateDescriptionRequest request
    ) {
        try {
            String newDesc = request.getDescription();
            Long version = request.getVersion();

            Record updated = recordService.updateDescription(id, newDesc, version);
            return ResponseEntity.ok(updated);
        } catch (OptimisticLockException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Record was updated by someone else.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
