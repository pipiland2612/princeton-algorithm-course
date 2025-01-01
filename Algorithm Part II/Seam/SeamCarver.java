/* *****************************************************************************
 *  Name: Tran Dang Minh Nguyen
 *  Date: 31/12/2006
 *  Description: My solution for SeamCarver coding assignment
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private static final double MAX_ENERGY = 195075.0;

    private final Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return this.picture;
    }

    // width of current picture
    public int width() {
        return this.picture.width();
    }

    // height of current picture
    public int height() {
        return this.picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || y < 0 || x > picture.width() - 1 || y > picture.height() - 1) {
            throw new IllegalArgumentException();
        }

        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
            return MAX_ENERGY;
        }

        double xDiff = gradient(picture.get(x - 1, y), picture.get(x + 1, y));
        double yDiff = gradient(picture.get(x, y - 1), picture.get(x, y + 1));
        return xDiff + yDiff;
    }

    private double gradient(Color a, Color b) {
        int red = a.getRed() - b.getRed();
        int green = a.getGreen() - b.getGreen();
        int blue = a.getBlue() - b.getBlue();
        return red * red + green * green + blue * blue;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return new int[] { };
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] energyTo = new double[width()][height()];
        int[][] edgeTo = new int[width()][height()];

        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                // Fill in the table, with src is the first row
                energyTo[i][j] = j == 0 ? energy(i, j) : Double.POSITIVE_INFINITY;
            }
        }

        // Relax from top to bottom (Topological order)
        for (int y = 0; y < height() - 1; y++) {
            for (int x = 0; x < width(); x++) {
                if (x > 0) relax(x, y, x - 1, y + 1, energyTo, edgeTo, true);
                relax(x, y, x, y + 1, energyTo, edgeTo, true);
                if (x < width() - 1) relax(x, y, x + 1, y + 1, energyTo, edgeTo, true);
            }
        }

        double minEnergy = Double.POSITIVE_INFINITY;
        int minIndex = 0;
        for (int x = 0; x < width(); x++) {
            if (energyTo[x][height() - 1] < minEnergy) {
                minEnergy = energyTo[x][height() - 1];
                minIndex = x;
            }
        }

        // Reconstruct the seam
        int[] seam = new int[height()];
        for (int y = height() - 1; y >= 0; y--) {
            seam[y] = minIndex;
            minIndex = edgeTo[minIndex][y];
        }

        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        checkValidity(seam);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        checkValidity(seam);
    }

    private void checkValidity(int[] seam) {
        if (width() <= 1 || height() <= 1) {
            throw new IllegalArgumentException(
                    "The width and height of the picture must be greatern than 1");
        }
        if (seam.length <= 1) {
            throw new IllegalArgumentException("The seam size must be greater than 1.");
        }

        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException();
            }
        }
    }

    private void relax(int fromX, int fromY, int toX, int toY, double[][] energyTo,
                       int[][] edgeTo, boolean isVertical) {
        double energy = energy(fromX, fromY);
        if (energyTo[toX][toY] > energyTo[fromX][fromY] + energy) {
            energyTo[toX][toY] = energyTo[fromX][fromY] + energy;
            edgeTo[toX][toY] = isVertical ? fromX : fromY;
        }
    }

    //  unit testing (optional)
    public static void main(String[] args) {
    }

}