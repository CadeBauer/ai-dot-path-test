package objects;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Dot {
	private double x;
	private double y;
	private final Shape shape;
	private final Color color = new Color(255, 255, 255);
	private double size;
	private double velocityX = 0d;
	private double velocityY = 0d;
	private int steps = 0;
	private boolean stop = false;
	private boolean champion = false;
	private double fitness = 0d;
	private int id;
	private int maxSteps;
	
	public Dot(double x, double y, double size, int id, int maxSteps) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.id = id;
		this.maxSteps = maxSteps;
		shape = new Ellipse2D.Double(0, 0, size, size);
	}
	
	public void update(double accAngle, double accAmount) {
		if (!stop) {
			if (steps < maxSteps) {
				velocityX += accAmount * Math.cos(Math.toRadians(accAngle));
				velocityY += accAmount * Math.sin(-Math.toRadians(accAngle));
				accAngle = 0;
				accAmount = 0;
				steps += 1;
			}
			x += velocityX;
			y += velocityY;
		}
	}
	
	public void update() {
		if (!stop) {
			x += velocityX;
			y += velocityY;
		}
	}
	
	public boolean stopCheck(int width, int height) {
		if (x < size || y < size || x > width - size || y > height - size) {
			if (x < size)
				x = 0;
			if (y < size)
				y = 0;
			if (x > width - size)
				x = width - size;
			if (y > height - size)
				y = height - size;
			return true;
		} else {
			return false;
		}
	}
	
	public void draw(Graphics2D g2) {
		AffineTransform oldTransform = g2.getTransform();
		if (champion) {
			g2.setColor(new Color(0, 255, 0));
		} else {
			g2.setColor(color);
		}
		Composite originalComposite = g2.getComposite();
		AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f);
		g2.setComposite(alphaComposite);
		g2.translate(x, y);
		g2.fill(shape);
		g2.setTransform(oldTransform);
		//g2.setComposite(originalComposite);
	}
	
	public Shape getShape() {
		return new Area(new Ellipse2D.Double(x, y, size, size));
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getSize() {
		return size;
	}

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}
	
	public boolean getStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	public boolean isChampion() {
		return champion;
	}

	public void setChampion(boolean champion) {
		this.champion = champion;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	public int getID() {
		return id;
	}
}
