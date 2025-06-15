package com.victor.picpay.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class WalletDataAlreadyExists extends PicPayException{

    private final String detail;

    public WalletDataAlreadyExists(String detail) {
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pb = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        pb.setTitle("Wallet already exists on database!");
        pb.setDetail(detail);

        return pb;
    }
}
