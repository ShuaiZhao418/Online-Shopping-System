package com.example.webapplication.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;


@Entity
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    Integer id;
    @JsonProperty("name")
    String name;
    @JsonProperty("description")
    String description;
    @JsonProperty("sku")
    String sku;
    @JsonProperty("manufacturer")
    String manufacturer;
    @JsonProperty("quantity")
    Integer quantity;
    @JsonProperty(value = "date_added", access = JsonProperty.Access.READ_ONLY)
    String date_added;
    @JsonProperty(value = "date_last_updated", access = JsonProperty.Access.READ_ONLY)
    String date_last_updated;
    @JsonProperty(value = "owner_user_id", access = JsonProperty.Access.READ_ONLY)
    Integer owner_user_id;
}
