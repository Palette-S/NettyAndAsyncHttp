package com.tuming.socket.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author Administrator;
 * @since 2018/1/15;
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Base64ImageEntity {
    private String imageData;
    private String imageType; // .jpeg,.png
}
