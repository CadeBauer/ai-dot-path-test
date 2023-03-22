package component;

import java.util.concurrent.ThreadLocalRandom;
import java.util.List;
import objects.Dot;

public class Data {
	public double[][][] current;
	public double[][][] nextGen;
	private int gen = 1;
	private int dotSaveIndex;
	private int dotCount;
	private int steps;
	
	public Data(int DOTCOUNT, int STEPS) {
		current = new double[DOTCOUNT][STEPS][2];
		nextGen = new double[DOTCOUNT][STEPS][2];
		dotCount = DOTCOUNT;
		steps = STEPS;
		for (int i = 0; i < dotCount; i++) {
			for (int j = 0; j < steps; j++) {
				current[i][j][0] = ThreadLocalRandom.current().nextDouble(0, 360);
				current[i][j][1] = ThreadLocalRandom.current().nextDouble(0, 2);
			}
		}
	}
	
	public boolean nextGen(double fitnessTotal, List<Dot> dots, Dot highestFitness) {
		if (dotSaveIndex == 0) {
			for (int j = 0; j < steps; j++) {
				nextGen[dotSaveIndex][j][0] = current[highestFitness.getID()][j][0];
				nextGen[dotSaveIndex][j][1] = current[highestFitness.getID()][j][1];
			}
			dotSaveIndex += 1;
		}
		for (int i = 0; i < dotCount; i++) {
			if (dotSaveIndex < dotCount && dotSaveIndex > 0 && ThreadLocalRandom.current().nextDouble(0, fitnessTotal) < dots.get(i).getFitness() * 3) {
				for (int j = 0; j < steps; j++) {
					nextGen[dotSaveIndex][j][0] = current[i][j][0];
					nextGen[dotSaveIndex][j][1] = current[i][j][1];
					if (ThreadLocalRandom.current().nextInt(0, 100) == 0) {
						nextGen[dotSaveIndex][j][0] = ThreadLocalRandom.current().nextDouble(0, 360);
						nextGen[dotSaveIndex][j][1] = ThreadLocalRandom.current().nextDouble(0, 2);
					}
				}
				dotSaveIndex += 1;
			}
		}
		if (dotSaveIndex == dotCount) {
			dotSaveIndex = 0;
			gen += 1;
			current = nextGen.clone();
			return true;
		}
		return false;
	}

	public int getGen() {
		return gen;
	}
}
