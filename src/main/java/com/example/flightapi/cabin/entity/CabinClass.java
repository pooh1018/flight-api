package com.example.flightapi.cabin.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import java.math.BigDecimal;

@Data
@Document(collection = "cabin_class")
@CompoundIndexes({
    @CompoundIndex(name = "flight_class_idx",
                  def = "{'flight_id': 1, 'class_id': 1}",
                  unique = true)
})
public class CabinClass {
    @Id
    @Schema(description = "Unique identifier of the cabin class", example = "cc123")
    private String id;

    @Field("flight_id")
    @Schema(description = "ID of the flight this cabin class belongs to", example = "fl123")
    private String flightId;

    @Field("class_type")
    @Schema(description = "Type of cabin class (ECONOMY, BUSINESS, FIRST)", example = "BUSINESS")
    private int classType;

    @Field("name")
    @Schema(description = "name of cabin class (ECONOMY, BUSINESS, FIRST)", example = "BUSINESS")
    private String name;

    @Field("price")
    @Schema(description = "Price for this cabin class", example = "499.99")
    private Double price;

    @Field("price_factor")
    @Schema(description = "Price factor for this cabin class", example = "1.5")
    private Double priceFactor;

    @Field("total_seats")
    @Schema(description = "Total number of seats available in this cabin class", example = "50")
    private Integer totalSeats;

    @Field("available_seats")
    @Schema(description = "Number of seats currently available for booking", example = "42")
    private Integer availableSeats;

    /**
     * 预订指定数量的座位
     * @param numberOfSeats 要预订的座位数量
     * @return 是否预订成功
     */
    public boolean bookSeats(int numberOfSeats) {
        if (availableSeats >= numberOfSeats) {
            availableSeats -= numberOfSeats;
            return true;
        }
        return false;
    }

    /**
     * 释放指定数量的座位
     * @param numberOfSeats 要释放的座位数量
     * @return 是否释放成功
     */
    public boolean releaseSeats(int numberOfSeats) {
        if (availableSeats + numberOfSeats <= totalSeats) {
            availableSeats += numberOfSeats;
            return true;
        }
        return false;
    }

    /**
     * 检查是否有足够的可用座位
     * @param numberOfSeats 需要的座位数量
     * @return 是否有足够的座位
     */
    public boolean hasEnoughSeats(int numberOfSeats) {
        return availableSeats >= numberOfSeats;
    }

    /**
     * 重置可用座位数为总座位数
     */
    public void resetAvailableSeats() {
        this.availableSeats = this.totalSeats;
    }
}
