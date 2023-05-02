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
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(value = "image_id", access = JsonProperty.Access.READ_ONLY)
    Integer image_id;
    @JsonProperty(value = "product_id", access = JsonProperty.Access.READ_ONLY)
    Integer product_id;
    @JsonProperty(value = "file_name", access = JsonProperty.Access.READ_ONLY)
    String file_name;
    @JsonProperty(value = "date_created", access = JsonProperty.Access.READ_ONLY)
    String date_created;
    @JsonProperty(value = "s3_bucket_path", access = JsonProperty.Access.READ_ONLY)
    String s3_bucket_path;
}
