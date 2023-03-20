package com.example.orderservice.dto;

import com.example.orderservice.model.OrderLineItem;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    List<OrderLineItemDto> orderLineItemDtos;
}
