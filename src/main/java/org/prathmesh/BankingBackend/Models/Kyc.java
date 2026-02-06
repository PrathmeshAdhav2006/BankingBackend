package org.prathmesh.BankingBackend.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.prathmesh.BankingBackend.Enums.KycStatus;

import java.time.LocalDateTime;
@Entity
@Table(name = "kyc")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Kyc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // Aadhaar
    @Column(nullable = false, unique = true, length = 12)
    private String aadhaarNumber;

    // PAN
    @Column(nullable = false, unique = true, length = 10)
    private String panNumber;

    private String aadhaarImageUrl;

    private String panImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private KycStatus status;

    private String rejectionReason;

    @Column(updatable = false)
    private LocalDateTime submittedAt;

    private LocalDateTime verifiedAt;

    @PrePersist
    public void onCreate() {
        this.submittedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = KycStatus.PENDING;
        }
    }
}
