package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCodes) {
        List<InventoryResponse> inventoryResponses = inventoryRepository
                .findBySkuCodeIn(skuCodes)
                .stream()
                .map(inventory ->
                        InventoryResponse
                                .builder()
                                .isInStock(inventory.getQuantity() > 0)
                                .skuCode(inventory.getSkuCode())
                                .build()
                ).toList();
        return inventoryResponses;
    }
}
