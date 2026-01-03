package org.prathmesh.BankingBackend.Models;

import jakarta.persistence.*;
import lombok.*;
import org.prathmesh.BankingBackend.Enums.AccountStatus;
import org.prathmesh.BankingBackend.Enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false,unique = true,updatable = false)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @Column(nullable = false)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus accountStatus;

    @ManyToOne
    private User user;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        if(this.balance==null){
            this.balance = BigDecimal.ZERO;
        }
        if(this.accountStatus == null){
            this.accountStatus = AccountStatus.ACTIVE;
        }
    }

}
