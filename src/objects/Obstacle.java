package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class Obstacle {
	private double x;
	private double y;
	private double width;
	private double height;
	private final Shape shape;
	private Color color = new Color(100, 200, 200);
	
	public Obstacle(double width, double height, double x, double y) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		shape = new Rectangle2D.Double(0, 0, width, height);
	}
	
	public void draw(Graphics2D g2) {
		AffineTransform oldTransform = g2.getTransform();
		g2.setColor(color);
		g2.translate(x, y);
		g2.fill(shape);
		Shape shape = getShape();
		g2.setTransform(oldTransform);
		g2.draw(shape.getBounds2D());
	}
	
	public Area getShape() {
		return new Area(new Rectangle2D.Double(x, y, width, height));
	}
	
	public void setColor(Color color) {
		this.color = color;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

}
