package com.example.demo.springtelegrammbot.model;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;

@Data
@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class User {

    @Id
    @NonNull private Long chatID;

    @NonNull private String firstName;

    private String lastName;

    private String userName;

    private Timestamp registration;


}
