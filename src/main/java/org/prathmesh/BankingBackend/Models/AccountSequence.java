package org.prathmesh.BankingBackend.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "account_sequence")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountSequence {

        @Id
        private int year;

        @Column(nullable = false)
        private long lastNumber;

}
