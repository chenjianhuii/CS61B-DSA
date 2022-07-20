/**
 * Planet
 */
public class Planet {

    public double xxPos, yyPos, xxVel, yyVel, mass;
    public String imgFileName;
    private static final double G = 6.67e-11;

    public Planet(double xP, double yP, double xV, double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }

    public Planet(Planet p) {
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }

    /**
     * Calculate the distance between this planet and p
     * @param p
     * @return distance
     */
    public double calcDistance(Planet p) {
        double dX = xxPos - p.xxPos;
        double dY = yyPos - p.yyPos;
        return Math.sqrt(dX*dX+dY*dY);
    }
    
    /**
     * Calculate the force exerted by other planet
     * @param p
     * @return force
     */
    public double calcForceExertedBy(Planet p) {
        double d = calcDistance(p);
        return G * mass * p.mass / (d * d);
    }
    
    /**
     * Calculate the force exerted in X direction by other planet
     * @param p
     * @return The force in X direction
     */
    public double calcForceExertedByX(Planet p) {
        double d = calcDistance(p);
        double f = G * mass * p.mass / (d * d);
        return f * (p.xxPos - xxPos) / d; 
    }

    /**
     * Calculate the force exerted in Y direction by other planet
     * @param p
     * @return The force in Y direction
     */
    public double calcForceExertedByY(Planet p) {
        double d = calcDistance(p);
        double f = G * mass * p.mass / (d * d);
        return f * (p.yyPos - yyPos) / d; 
    }

    /**
     * Calculate the net force exerted in X direction by a list of planets
     * @param ps
     * @return The net force in X direction
     */
    public double calcNetForceExertedByX(Planet[] ps) {
        double netf = 0;
        for (Planet planet : ps) {
            if (planet.equals(this)) {
                continue;
            }
            netf += calcForceExertedByX(planet);
        }
        return netf;
    }

    /**
     * Calculate the net force exerted in Y direction by a list of planets
     * @param ps
     * @return The net force in Y direction
     */
    public double calcNetForceExertedByY(Planet[] ps) {
        double netf = 0;
        for (Planet planet : ps) {
            if (planet.equals(this)) {
                continue;
            }
            netf += calcForceExertedByY(planet);
        }
        return netf;
    }

    /**
     * Update the position and velocity of planet given time and force
     * @param dt The duration of focre
     * @param fX The force in X direction
     * @param fY The force in Y direction
     */
    public void update(double dt, double fX, double fY) {
        double aX = fX / mass, aY = fY / mass;
        xxVel += dt * aX;
        yyVel += dt * aY;
        xxPos += dt * xxVel;
        yyPos += dt * yyVel;
    }
    
    public void draw() {
        StdDraw.picture(xxPos, yyPos, "images/"+imgFileName);
    }
}