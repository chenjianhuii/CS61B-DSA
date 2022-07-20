/**
 * NBody
 */
public class NBody {

    /**
     * Read the radius in file
     * @param fileName
     * @return The radius
     */
    public static double readRadius(String fileName) {
        In in = new In(fileName);
        // get rid of the first value in file
        in.readInt();
        return in.readDouble();
    }

    public static Planet[] readPlanets(String fileName) {
        In in = new In(fileName);
        int N = in.readInt();
        Planet[] planets = new Planet[N];
        in.readDouble();
        for (int i = 0; i < N; i++) {
            planets[i] = new Planet(in.readDouble(),
                                    in.readDouble(),
                                    in.readDouble(),
                                    in.readDouble(),
                                    in.readDouble(),
                                    in.readString());
        }
        return planets;
    }

    public static void main(String[] args) {
        double T = Double.valueOf(args[0]), dt = Double.valueOf(args[1]);
        String filename = args[2];
        double radius = readRadius(filename);
        Planet[] planets = readPlanets(filename);
        // StdDraw.setCanvasSize(800, 800);
		StdDraw.enableDoubleBuffering();
        StdDraw.setScale(-radius*1.1, radius*1.1);
        double time = 0;
        double[] xForces = new double[planets.length], yForces = new double[planets.length];
        while (time < T) {
            for (int i = 0; i < planets.length; i++) {
                xForces[i] = planets[i].calcNetForceExertedByX(planets);
                yForces[i] = planets[i].calcNetForceExertedByY(planets);
            }
            for (int i = 0; i < yForces.length; i++) {
                planets[i].update(dt, xForces[i], yForces[i]);
            }
            StdDraw.picture(0, 0, "images/starfield.jpg");
            for (Planet planet : planets) {
                planet.draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
            time += dt;
        }
        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                        planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                        planets[i].yyVel, planets[i].mass, planets[i].imgFileName);   
        }
    }
}