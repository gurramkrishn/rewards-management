package com.chartercommunications.rewardscalculator.controller;

import com.chartercommunications.rewardscalculator.dao.Rewards;
import com.chartercommunications.rewardscalculator.service.RewardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Year;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RewardsController {

    @Autowired
    private RewardsService rewardsService;

    @PostMapping("/transactions")
    public ResponseEntity<String> recordTransaction(@RequestBody Rewards rewards) {
        rewardsService.recordTransaction(rewards);
        return new ResponseEntity<>("Transaction made recorded", HttpStatus.CREATED);
    }

    @GetMapping("/points/{customerId}/{year}")
    public ResponseEntity<Map<LocalDate, Integer>> getPointsByMonth(@PathVariable Long customerId, @PathVariable int year) {
        Map<LocalDate, Integer> points = rewardsService.getMonthlyPoints(customerId, Year.of(year));
        return new ResponseEntity<>(points, HttpStatus.OK);
    }

    @PutMapping("/transactions/{transactionId}")
    public ResponseEntity<Rewards> updateTransaction(@PathVariable long transactionId, @RequestBody Rewards rewards) {
        Rewards updatedRewards = rewardsService.updateTransaction(transactionId, rewards);
        if(updatedRewards == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(updatedRewards, HttpStatus.OK);
    }

}
