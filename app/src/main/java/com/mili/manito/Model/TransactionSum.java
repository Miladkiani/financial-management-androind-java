package com.mili.manito.Model;

public class TransactionSum {

    public TransactionSum(Integer tran_type, Long tran_cost) {
        this.tran_type = tran_type;
        this.tran_cost = tran_cost;
    }

    public Integer getTran_type() {
        return tran_type;
    }

    public Long getTran_cost() {
        return tran_cost;
    }

    private Integer tran_type;

    private Long tran_cost;
}
