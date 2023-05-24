package com.zx.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: mybatisTest
 * @description:
 * @author: zhou  xun
 * @create: 2023-05-15 12:41
 */
@AllArgsConstructor
@Data
public class User implements Serializable {
    private int id;
    private String name;
    private int age;

    // Getter and Setter methods...
}