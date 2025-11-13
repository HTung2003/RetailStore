package com.example.RetailStore.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class UserInfo {
    private String userId;
    private String username;
    private String email;
    private String address;
    private String phone;
}
