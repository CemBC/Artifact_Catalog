package com.example.artifact_catalog;

import java.util.Objects;

public class Dimensions {
    private double width;
    private double length;
    private double height;

    public Dimensions(double width, double length, double height) {
        this.width = width;
        this.length = length;
        this.height = height;
    }

    public Dimensions() {}

    public double getWidth() { return width; }
    public void setWidth(double width) { this.width = width; }

    public double getLength() { return length; }
    public void setLength(double length) { this.length = length; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dimensions that = (Dimensions) o;
        return Double.compare(that.width, width) == 0 &&
                Double.compare(that.length, length) == 0 &&
                Double.compare(that.height, height) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, length, height);
    }

    @Override
    public String toString() {
        return "com.example.artifact_catalog.Dimensions{" +
                "width=" + width +
                ", length=" + length +
                ", height=" + height +
                '}';
    }
}