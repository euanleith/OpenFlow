public class Route {//TODO name
    int port, dist, neighbour;

    Route(int port, int dist, int neighbour) {
        this.port = port;
        this.dist = dist;
        this.neighbour = neighbour;//TODO name
    }

    @Override
    public String toString() {
        return "port: " + port + ", dist: " + dist + ", neighbour: " + neighbour;
    }
}
