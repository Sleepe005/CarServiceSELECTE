package com.example.CarAPI_1.adapter.web.DTO;

import com.example.CarAPI_1.core.service.ElectreEngine;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CarRankingDTO {
    private Long id;
    private String brand;
    private String model;
    private Long price;
    private Integer rank;

    public CarRankingDTO(ElectreEngine.CarRanking ranking) {
        this.id = ranking.getCar().getId();
        this.brand = ranking.getCar().getBrand();
        this.model = ranking.getCar().getModel();
        this.price = ranking.getCar().getPrice();
        this.rank = ranking.getRank();
    }
}