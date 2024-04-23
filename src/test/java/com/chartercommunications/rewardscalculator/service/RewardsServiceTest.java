package com.chartercommunications.rewardscalculator.service;

import com.chartercommunications.rewardscalculator.dao.Rewards;
import com.chartercommunications.rewardscalculator.service.RewardsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RewardsServiceTest {
    private RewardsService service;
    @BeforeEach
    void setUp() {
        service = new RewardsService();
    }
    @Test
    public void testCalculateRewards() {
        assertEquals(90, service.calculateRewards(120.0));
        assertEquals(0, service.calculateRewards(49.0));
        assertEquals(1, service.calculateRewards(51.0));
    }
    @Test
    public void testUpdateTransaction() {
        Rewards t1 = new Rewards(1L, 100.0, LocalDate.now());
        service.recordTransaction(t1);
        Rewards transaction = new Rewards(1L, 150.0, LocalDate.now());
        Rewards updatedTransaction = service.updateTransaction(t1.getId(), transaction);

        assertNotNull(updatedTransaction);
        assertEquals(150.0, updatedTransaction.getAmount());
    }
}
