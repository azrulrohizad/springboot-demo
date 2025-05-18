package com.example.assessment.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerId;

    private String accountNumber;

    private String description;

    @Version
    private Long version;
}
