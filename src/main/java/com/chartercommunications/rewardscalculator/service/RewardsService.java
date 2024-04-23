package com.chartercommunications.rewardscalculator.service;

import com.chartercommunications.rewardscalculator.dao.Rewards;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.*;

@Service
public class RewardsService {

    private static final Logger logger = LoggerFactory.getLogger(RewardsService.class);

    private final Map<Long, List<Rewards>> rewardMap = new HashMap<>();

    public void recordTransaction(Rewards rewards) {
        rewardMap.computeIfAbsent(rewards.getCustomerId(), k -> new ArrayList<>()).add(rewards);
        logger.debug("Recording transaction for customer {}: amount {}", rewards.getCustomerId(), rewards.getAmount());
    }

    public int calculateRewards(double amount) {
        logger.debug("Calculating rewards for amount: {}", amount);
        int points = 0;
        if(amount > 100) {
            points += (int) ((amount - 100) * 2);
            logger.debug("Calculated {} points for spending over $100", points);
            amount = 100;
        }
        if(amount > 50) {
            points += (int) (amount - 50);
        }
        logger.info("Total points calculated: {}", points);
        return points;
    }

    public Map<LocalDate, Integer> getMonthlyPoints(Long customerId, Year year)  {
        Map<LocalDate, Integer> monthlyPoints = new TreeMap<>();
        List<Rewards> rewards = rewardMap.getOrDefault(customerId, Collections.emptyList());
        rewards.stream()
                .filter(t -> t.getDate().getYear() == year.getValue())
                .collect(Collectors.groupingBy(t -> t.getDate().withDayOfMonth(1)))
                .forEach((month, transactionList) -> {
                    int totalPoints = transactionList.stream()
                            .mapToInt(t -> calculateRewards(t.getAmount()))
                            .sum();

                    monthlyPoints.put(month, totalPoints);
                });
        return monthlyPoints;
    }

    public Rewards updateTransaction(Long rewardsId, Rewards updatedRewards) {
        List<Rewards> customerTransactions = rewardMap.getOrDefault(updatedRewards.getCustomerId(), new ArrayList<>());
        for(int i = 0; i < customerTransactions.size(); i++) {
            Rewards existingTransaction = customerTransactions.get(i);
            if(existingTransaction.getId() == rewardsId) {
                existingTransaction.setAmount(updatedRewards.getAmount());
                existingTransaction.setDate(updatedRewards.getDate());
                return existingTransaction;
            }
        }
        return null;
    }
}
