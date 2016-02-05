package mcjty.aquamunda.chunkdata;

public class DataChunkCoordinate {
    private final int dx;
    private final int dy;
    private final int dz;

    public DataChunkCoordinate(int dx, int dy, int dz) {
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public int getDz() {
        return dz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataChunkCoordinate that = (DataChunkCoordinate) o;

        if (dx != that.dx) return false;
        if (dy != that.dy) return false;
        return dz == that.dz;

    }

    @Override
    public int hashCode() {
        int result = dx;
        result = 31 * result + dy;
        result = 31 * result + dz;
        return result;
    }
}
