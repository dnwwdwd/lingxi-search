package com.hjj.lingxisearch.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Picture implements Serializable {
    private String title;
    private String url;
}
