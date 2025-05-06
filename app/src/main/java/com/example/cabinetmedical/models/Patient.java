package com.example.cabinetmedical.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Entity(tableName = "patients")
@Getter
@Setter
@AllArgsConstructor
public class Patient {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String fullname;

    private String phone;

    private String email;

}