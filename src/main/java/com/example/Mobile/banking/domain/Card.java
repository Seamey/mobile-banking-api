package com.example.Mobile.banking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Locale;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true,nullable = false)
    private String number;
    @Column(nullable = false)
    private String cvv;
    private String issuedAt;
    private LocalDate expiredAt;
    private Boolean isDeleted;

    // if manyTomany only join Table and other can join column kr ban join Table kr ban
    //relationship between card and cardType
    @ManyToOne
    @JoinColumn(name="type_id") // use for customize single column when join column
    private  CardType cardType;

    // relationship between card and account
    @OneToOne(mappedBy = "card")
    @JoinTable(name = "user_roles")
    private Account account;
}
