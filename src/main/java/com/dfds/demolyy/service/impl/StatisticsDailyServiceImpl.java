package com.dfds.demolyy.service.impl;

import com.dfds.demolyy.service.StatisticsDailyService;
import org.springframework.stereotype.Service;

@Service
public class StatisticsDailyServiceImpl implements StatisticsDailyService {
    @Override
    public void test(String day) {
        System.out.println(day);
    }
}
