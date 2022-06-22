/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.badpal.Model;

import java.sql.Blob;
import java.util.List;

/**
 *
 * @author garyl
 */
public class CourtImage {

    private List<Blob> images;

    public void setImages(List<Blob> images) {
        this.images = images;
    }

    public List<Blob> getImages() {
        return images;
    }
}
