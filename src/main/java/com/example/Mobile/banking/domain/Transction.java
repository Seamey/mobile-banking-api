package com.example.Mobile.banking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="transctions")
public class Transction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // relational is the unidirectional
    @ManyToOne
    private Account sender; //return type is the Account cuz transfer acc to acc
    @ManyToOne
    private Account receiver;
    private BigDecimal amount;
    private String remark;
    private Boolean isPayment;  // isPayment true is the normal payment
                                // isPayment is false is the transfer
    private LocalDateTime transactionAt;
    private Boolean isDeleted;

}
