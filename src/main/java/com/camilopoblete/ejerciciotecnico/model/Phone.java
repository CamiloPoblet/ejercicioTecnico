package com.camilopoblete.ejerciciotecnico.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Phone {

    private Long number;
    private int citycode;
    private String contrycode;

}
