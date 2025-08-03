package net.sparkly.api.position;

public class Vector {
    
    private double x;
    private double y;
    private double z;
    
    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector add(Vector other) {
        return new Vector(x + other.x, y + other.y, z + other.z);
    }
    
    public Vector add(double value) {
        return new Vector(x + value, y + value, z + value);
    }
    
    public Vector subtract(Vector other) {
        return new Vector(x - other.x, y - other.y, z - other.z);
    }
    
    public Vector subtract(double value) {
        return new Vector(x - value, y - value, z - value);
    }
    
    public Vector multiply(Vector other) {
        return new Vector(x * other.x, y * other.y, z * other.z);
    }
    
    public Vector multiply(double value) {
        return new Vector(x * value, y * value, z * value);
    }
    
    public Vector divide(Vector other) {
        return new Vector(x / other.x, y / other.y, z / other.z);
    }
    
    public Vector divide(double value) {
        return new Vector(x / value, y / value, z / value);
    }
    
    public double x() {
        return x;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public double y() {
        return y;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public double z() {
        return z;
    }
    
    public void setZ(double z) {
        this.z = z;
    }
    
    public int blockX() {
        return (int) x;
    }
    
    public int blockY() {
        return (int) y;
    }
    
    public int blockZ() {
        return (int) z;
    }
}
