package com.chartercommunications.rewardscalculator.dao;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Rewards {

    private long id;
    private Long customerId;
    private double amount;
    private LocalDate date;

    public Rewards(Long customerId, double amount, LocalDate date) {
        this.customerId = customerId;
        this.amount = amount;
        this.date = date;
    }
}
