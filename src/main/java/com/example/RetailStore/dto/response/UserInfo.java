package com.example.RetailStore.dto.response;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private String userId;
    private String username;
    private String email;
    private String address;
    private String phone;
}
