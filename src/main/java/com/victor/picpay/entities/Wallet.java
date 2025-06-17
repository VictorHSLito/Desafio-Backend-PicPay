package com.victor.picpay.entities;

import com.victor.picpay.enums.WalletType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_wallet")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "balance")
    private BigDecimal balance = BigDecimal.ZERO;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
