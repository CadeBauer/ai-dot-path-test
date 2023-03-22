package component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import java.util.ArrayList;
import java.util.List;
import objects.Dot;
import objects.Obstacle;

public class PanelGame extends JComponent{
	
	private Graphics2D g2;
	private BufferedImage image;
	private int width;
	private int height;
	private Thread thread;
	private boolean start = true;
	
	private final int FPS = 60;
	private final int TARGET_TIME = 1000000000/FPS;
	private final int DOTCOUNT = 50;
	private final int STEPS = 400;
	
	private List<Dot> dots;
	private List<Obstacle> obstacles;
	private Obstacle goal;
	
	private Data data = new Data(DOTCOUNT, STEPS);
	private double fitnessTotal = 0;
	private int deathCount = 0;
	private double highestFitnessAmount = 0;
	private Dot highestFitness;
	
	public void start() {
		width = getWidth();
		height = getHeight();
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		g2 = image.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		thread = new Thread(new Runnable() {
			public void run() {
				while(start) {
					long startTime = System.nanoTime();
					drawBackground();
					drawGame();
					render();
					long time = System.nanoTime() - startTime;
					if (time < TARGET_TIME) {
						long sleepTime = (TARGET_TIME - time)/1000000;
						sleep(sleepTime);
						//System.out.println(sleepTime);
					}
				}
			}
		});
		initDots();
		initObstacles();
		nextGenCheck();
		thread.start();
	}
	
	private void initDots() {
		dots = new ArrayList<>();
		new Thread(new Runnable() {
			public void run() {
				while (start) {
					for (int i = 0; i < dots.size(); i++) {
						Dot dot = dots.get(i);
						if (dot != null) {
							if (dot.getSteps() < STEPS)
								dot.update(data.current[i][dot.getSteps()][0], data.current[i][dot.getSteps()][1]);
							if (dot.getSteps() >= STEPS)
								dot.update();
							checkObstacles(dot);
							if (dot.stopCheck(width, height) && !dot.getStop())
								dotStop(dot);
							if (dot.getStop() && dot.getFitness() > highestFitnessAmount) {
								highestFitnessAmount = dot.getFitness();
								highestFitness = dot;
								highestFitness.setChampion(true);
							}
							if (dot.getFitness() < highestFitnessAmount)
								dot.setChampion(false);
						}
					}
					sleep(1);
				}
			}
		}).start();
		createDots();
	}
	
	private void createDots() {
		for (int i = 0; i < DOTCOUNT; i++)
			dots.add(new Dot(50, 300, 8, i, STEPS));
	}
	
	private void checkObstacles(Dot dot) {
		for (int i = 0; i < obstacles.size(); i++) {
			Obstacle obstacle = obstacles.get(i);
			if (obstacle != null) {
				Area area = new Area(dot.getShape());
				area.intersect(obstacle.getShape());
				if (!area.isEmpty() && !dot.getStop())
					dotStop(dot);
			}
		}
		Area area = new Area(dot.getShape());
		area.intersect(goal.getShape());
		if (!area.isEmpty()) {
			dot.setFitness(Math.pow(1000/(Math.sqrt(Math.pow(dot.getX() - goal.getX(), 2) + Math.pow(dot.getY() - goal.getY(), 2)) * 2 * Math.sqrt(dot.getSteps())), 1.5));
		} else {
			dot.setFitness(Math.pow(1000/(Math.sqrt(Math.pow(dot.getX() - goal.getX(), 2) + Math.pow(dot.getY() - goal.getY(), 2)) * 2 * Math.sqrt(STEPS)), 1.5));
		}
	}
	
	private void dotStop(Dot dot) {
		dot.setStop(true);
		deathCount += 1;
		fitnessTotal += dot.getFitness();
	}
	
	private void initObstacles() {
		obstacles = new ArrayList<>();
		goal = new Obstacle(20, 20, 900, 290);
		goal.setColor(new Color(200, 100, 0));
		obstacles.add(0, goal);
		Obstacle o1 = new Obstacle(50, 400, 300, 0);
		Obstacle o2 = new Obstacle(50, 500, 600, 300);
		obstacles.add(o1);
		obstacles.add(o2);
	}
	
	private void nextGenCheck() {
		new Thread(new Runnable() {
			public void run() {
				while (start) {
					if (deathCount == DOTCOUNT) {
						if (data.nextGen(fitnessTotal, dots, highestFitness)) {
							fitnessTotal = 0;
							deathCount = 0;
							highestFitnessAmount = 0;
							highestFitness = null;
							dots = new ArrayList<>();
							createDots();
						}
					}
					sleep(20);
				}
			}
		}).start();
	}
	
	private void drawBackground() {
		g2.setColor(new Color(30, 30, 30));
		g2.fillRect(0, 0, width, height);
	}
	
	private void drawGame() {
		for (int i=0; i<dots.size(); i++) {
			Dot dot = dots.get(i);
			if (dot != null) {
				dot.draw(g2);
			}
		}
		for (int i=0; i<obstacles.size(); i++) {
			Obstacle obstacle = obstacles.get(i);
			if (obstacle != null) {
				obstacle.draw(g2);
			}
		}
		goal.draw(g2);
		g2.setColor(new Color(255, 255, 255));
		g2.drawString("Generation " + data.getGen(), 900, 550);
	}
	
	private void render() {
		Graphics g = getGraphics();
		g.drawImage(image,  0,  0, null);
		g.dispose();
	}
	
	private void sleep(long speed) {
		try {
			Thread.sleep(speed);
		} catch (InterruptedException e) {
			System.err.println(e);
		}
	}

}
