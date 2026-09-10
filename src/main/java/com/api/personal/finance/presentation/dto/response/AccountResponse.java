package com.api.personal.finance.presentation.dto.response;

import com.api.personal.finance.domain.entity.Account;
import com.api.personal.finance.domain.entity.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {

    private Long id;
    private String name;
    private AccountType type;
    private BigDecimal balance;
    private Long userId;
    private Instant createdAt;
    private Instant updatedAt;

    public static AccountResponse fromDomain(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .name(account.getName())
                .type(account.getType())
                .balance(account.getBalance())
                .userId(account.getUser().getId())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }
}
