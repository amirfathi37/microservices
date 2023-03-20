package com.example.orderservice.service;

import com.example.orderservice.dto.InventoryResponse;
import com.example.orderservice.dto.OrderLineItemDto;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderLineItem;
import com.example.orderservice.repository.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepo orderRepo;
    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest) {

        List<String> skuCodes = orderRequest
                .getOrderLineItemDtos()
                .stream()
                .map(OrderLineItemDto::getSkuCode)
                .toList();

        InventoryResponse[] inventoryResponseLst = webClientBuilder.build()
                .get()
                .uri("http://inventory-service/api/inventory/is-in-stock",
                        uriBuilder -> uriBuilder.queryParam("skuCodes", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();
        List<InventoryResponse> inventoryResponses = Arrays.asList(inventoryResponseLst);
        boolean allAreInStock = inventoryResponses.stream().allMatch(InventoryResponse::isInStock);

        if (allAreInStock == false) {
            throw new IllegalArgumentException("This product is not in stock,please try later!!!");
        }

        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItem> orderLineItems = orderRequest
                .getOrderLineItemDtos()
                .stream()
                .map(this::map2Entity)
                .toList();

        order.setOrderLineItemList(orderLineItems);

        orderRepo.save(order);

    }

    private OrderLineItem map2Entity(OrderLineItemDto orderLineItemDto) {
        return OrderLineItem.builder()
                .skuCode(orderLineItemDto.getSkuCode())
                .price(orderLineItemDto.getPrice())
                .quantity(orderLineItemDto.getQuantity())
                .build();
    }
}
