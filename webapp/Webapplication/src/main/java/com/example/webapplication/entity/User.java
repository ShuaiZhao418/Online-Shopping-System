package com.example.webapplication.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(value="id", access = JsonProperty.Access.READ_ONLY)
    int id;
    @JsonProperty("first_name")
    String first_name;
    @JsonProperty("last_name")
    String last_name;
    @JsonProperty(value="password", access = JsonProperty.Access.WRITE_ONLY)
    String password;
    @JsonProperty("username")
    String username;
    @JsonProperty(value = "account_created", access = JsonProperty.Access.READ_ONLY)
    String account_created;
    @JsonProperty(value = "account_updated", access = JsonProperty.Access.READ_ONLY)
    String account_updated;
}

