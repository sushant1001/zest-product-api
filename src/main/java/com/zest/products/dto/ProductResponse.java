package com.zest.products.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductResponse {

    private Integer id;
    private String productName;
    private String createdBy;
    private LocalDateTime createdOn;
    private String modifiedBy;
    private LocalDateTime modifiedOn;
}
