

import edu.princeton.cs.algs4.Picture;;

public class SeamCarver {

    private Picture p;
    
    public SeamCarver(Picture picture) {
        p = picture;
    }

    /**current picture */
    public Picture picture() {
        return p;
    }

    /**width of current picture */
    public int width() {
        return p.width();
    }

    /**height of current picture */
    public int height() {
        return p.height();
    }

    private boolean inBound(int x, int y) {
        return 0 <= x && x < width() && 0 <= y && y < height();
    }

    /**energy of pixel at column x and row y */
    public double energy(int x, int y) {
        if (!inBound(x, y)) {
            throw new IndexOutOfBoundsException();
        }
        int W = width(), H = height();
        int rgbL = p.getRGB((x - 1 + W) % W, y);
        int rgbR = p.getRGB((x + 1) % W, y);
        int rgbU = p.getRGB(x, (y - 1 + H) % H);
        int rgbD = p.getRGB(x, (y + 1) % H);
        return delta(rgbL, rgbR) + delta(rgbU, rgbD);
    }

    private int getR(int rgb) {
        return (rgb >> 16) & 0xFF;
    }

    private int getG(int rgb) {
        return (rgb >> 8) & 0xFF;
    }

    private int getB(int rgb) {
        return rgb & 0xFF;
    }

    private double delta(int rgb1, int rgb2) {
        int r1 = getR(rgb1), r2 = getR(rgb2);
        int g1 = getG(rgb1), g2 = getG(rgb2);
        int b1 = getB(rgb1), b2 = getB(rgb2);
        int dR = r1 > r2 ? r1 - r2 : r2 - r1;
        int dG = g1 > g2 ? g1 - g2 : g2 - g1;
        int dB = b1 > b2 ? b1 - b2 : b2 - b1;
        return dR * dR + dG * dG + dB * dB;
    }
    
    /**sequence of indices for vertical seam */
    public int[] findVerticalSeam() {
        int W = width(), H = height();
        double[][] e = energyGrid();
        double[][] M = new double[W][H];
        /**Dynamic programming */
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                if (i == 0) {
                    M[j][i] = e[j][i];
                } else {
                    M[j][i] = e[j][i] + Math.min(Math.min(getItem(M, j - 1, i - 1), getItem(M, j, i - 1)), getItem(M, j + 1, i - 1));
                }
            }
        }
        /**Find the minimum path */
        int[] seam = new int[H];
        int place = H - 1;
        int minCol = 0;
        for (int i = 1; i < W; i++) {
            if (M[i][place] < M[minCol][place]) {
                minCol = i;
            }
        }
        seam[place] = minCol;
        while (place > 0) {
            if (M[minCol][place] == e[minCol][place] + getItem(M, minCol - 1, place - 1)) {
                seam[--place] = --minCol;
            } else if (M[minCol][place] == e[minCol][place] + getItem(M, minCol, place - 1)) {
                seam[--place] = minCol;
            } else {
                seam[--place] = ++minCol;
            }
        }
        return seam;
    }

    public int[] findHorizontalSeam() {
        Picture oldPicture = p;
        p = transpose();
        int[] seam = findVerticalSeam();
        p = oldPicture;
        return seam;
    }

    private double[][] energyGrid() {
        int W = width(), H = height();
        double[][] grid = new double[W][H];
        for (int i = 0; i < W; i++) {
            for (int j = 0; j < H; j++) {
                grid[i][j] = energy(i, j);
            }
        }
        return grid;
    }

    private double getItem(double[][] M, int x, int y) {
        if (!inBound(x, y)) {
            return Double.MAX_VALUE;
        }
        return M[x][y];
    }

    private Picture transpose() {
        int W = width(), H = height();
        Picture newPicture = new Picture(H, W);
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                newPicture.setRGB(i, j, p.getRGB(j, i));
            }
        }
        return newPicture;
    }

    /**remove horizontal seam from picture */
    public void removeHorizontalSeam(int[] seam) {
        if (seam.length != width() || ! isValidSeam(seam)) {
            throw new IllegalArgumentException();
        }
        p = SeamRemover.removeHorizontalSeam(p, seam);
    }
    /**remove vertical seam from picture */
    public void removeVerticalSeam(int[] seam) {
        if (seam.length != height() || ! isValidSeam(seam)) {
            throw new IllegalArgumentException();
        }
        p = SeamRemover.removeVerticalSeam(p, seam);
    }

    private boolean isValidSeam(int[] seam) {
        for (int i = 0; i < seam.length - 1; i++) {
            int diff = seam[i] - seam[i + 1];
            if (!(-1 <= diff && diff <= 1)) {
                return false;
            }
        }
        return true;
    }
}
