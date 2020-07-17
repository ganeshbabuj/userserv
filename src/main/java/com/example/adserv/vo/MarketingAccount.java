package com.example.adserv.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class MarketingAccount {
    Integer id;
    Integer userId;
    String status;

    public MarketingAccount(Integer userId) {
        this.userId = userId;
    }
}
