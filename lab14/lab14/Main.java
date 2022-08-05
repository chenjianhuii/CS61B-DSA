package lab14;

import java.util.ArrayList;
import lab14lib.*;

public class Main {
	public static void main(String[] args) {
		Generator g = new StrangeBitwiseGenerator(1024);
		// Generator g = new AcceleratingSawToothGenerator(400, 1.1);
		GeneratorDrawer gd = new GeneratorDrawer(g);
		gd.draw(12800);
		// GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(g);
		// gav.drawAndPlay(12800, 1000000);
	}
} 