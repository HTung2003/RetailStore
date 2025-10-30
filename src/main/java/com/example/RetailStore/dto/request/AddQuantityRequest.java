package com.example.RetailStore.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddQuantityRequest {
    private String cartIemId;

    private int quantity;
}
