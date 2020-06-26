package com.project.money.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CacheType {

    THROW_MONEY_TOKEN("throwMoneyToken", 10, 3);  // 10 min. token size 3.

    private String cacheName;
    private int expireAfterWrite;
    private int maximumSize;

}
