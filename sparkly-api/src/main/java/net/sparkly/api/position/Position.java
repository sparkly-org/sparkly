package net.sparkly.api.position;

public class Position {
    
    private double x;
    private double y;
    private double z;
    
    public Position(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Position add(Position other) {
        return new Position(x + other.x, y + other.y, z + other.z);
    }
    
    public Position add(double value) {
        return new Position(x + value, y + value, z + value);
    }
    
    public Position subtract(Position other) {
        return new Position(x - other.x, y - other.y, z - other.z);
    }
    
    public Position subtract(double value) {
        return new Position(x - value, y - value, z - value);
    }
    
    public Position multiply(Position other) {
        return new Position(x * other.x, y * other.y, z * other.z);
    }
    
    public Position multiply(double value) {
        return new Position(x * value, y * value, z * value);
    }
    
    public Position divide(Position other) {
        return new Position(x / other.x, y / other.y, z / other.z);
    }
    
    public Position divide(double value) {
        return new Position(x / value, y / value, z / value);
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
