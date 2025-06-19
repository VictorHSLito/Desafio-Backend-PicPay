package com.victor.picpay.dtos;

import com.victor.picpay.entities.User;

import java.math.BigDecimal;

public record TrasactionDTO (
        User payer,
        User payee,
        BigDecimal value
) {}
