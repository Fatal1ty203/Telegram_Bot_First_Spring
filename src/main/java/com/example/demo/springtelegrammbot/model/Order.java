package com.example.demo.springtelegrammbot.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity(name = "orders")
public class Order {

    @Id
    private long id;

    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private Date data1;
}
