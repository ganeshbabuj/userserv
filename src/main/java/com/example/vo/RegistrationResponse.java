package com.example.vo;

import com.example.adserv.vo.MarketingAccount;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistrationResponse {
    String token;
    MarketingAccount marketingAccount;

    public RegistrationResponse(String token, MarketingAccount marketingAccount) {
        this.token = token;
        this.marketingAccount = marketingAccount;
    }
}
