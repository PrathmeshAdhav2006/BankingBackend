package org.prathmesh.BankingBackend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "kyc_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KycDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "kyc_id")
    private Kyc kyc;

    @Column(nullable = false)
    private String documentType;
    // AADHAAR, PAN, PASSPORT, PHOTO

    @Column(nullable = false)
    private String documentNumber;

    @Column(nullable = false)
    private String documentUrl;
    // stored in S3 / cloud / local

    private boolean verified;
}
