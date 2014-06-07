package collisions;

public class CollisionPacket {

    public Vector eRadius;     // Information about the move being requested: (in R3)
    public Vector R3Velocity;
    public Vector R3Position;
    // Information about the move being requested: (in eSpace)
    public Vector velocity;
    public Vector normalizedVelocity;
    public Vector basePoint;
    // Hit information
    public boolean foundCollision;
    public double nearestDistance;
    public Vector intersectionPoint;
    public Vector finalPoint;
};
