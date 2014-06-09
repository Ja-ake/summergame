package collisions;

import engine.Room;

public abstract class Collisions {

    static boolean getLowestRoot(double a, double b, double c, double maxR, DoubleWrapper root) {
        // Check if a solution exists
        double determinant = b * b - 4 * a * c;
        // If determinant is negative it means no solutions.
        if (determinant < 0) {
            return false;
        }
        // calculate the two roots: (if determinant == 0 then
        // x1==x2 but let’s disregard that slight optimization)
        double sqrtD = Math.sqrt(determinant);
        double r1 = (-b - sqrtD) / (2 * a);
        double r2 = (-b + sqrtD) / (2 * a);
        // Sort so x1 <= x2
        if (r1 > r2) {
            double temp = r2;
            r2 = r1;
            r1 = temp;
        }
        // Get lowest root:
        if (r1 > 0 && r1 < maxR) {
            root.val = r1;
            return true;
        }
        return false;
    }

    static boolean checkPointInTriangle(Vector point, Vector pa, Vector pb, Vector pc) {
        Vector e10 = pb.subtract(pa);
        Vector e20 = pc.subtract(pa);
        double a = e10.dot(e10);
        double b = e10.dot(e20);
        double c = e20.dot(e20);
        double ac_bb = (a * c) - (b * b);
        Vector vp = new Vector(point.x - pa.x, point.y - pa.y, point.z - pa.z);
        double d = vp.dot(e10);
        double e = vp.dot(e20);
        double x = (d * c) - (e * b);
        double y = (e * a) - (d * b);
        double z = x + y - ac_bb;
        return (((int) z & ~((int) x | (int) y)) & 0x80000000) != 0;
    }

    public static void checkTriangle(CollisionPacket colPackage, Vector p1, Vector p2, Vector p3) {
        // Make the plane containing this triangle.
        Plane trianglePlane = new Plane(p1, p2, p3);
        // Is triangle front-facing to the velocity vector?
        // We only check front-facing triangles
        // (your choice of course)
        //if (trianglePlane.isFrontFacingTo(colPackage.normalizedVelocity)) {
        // Get interval of plane intersection:
        double t0, t1;
        boolean embeddedInPlane = false;
        // Calculate the signed distance from sphere
        // position to triangle plane
        double signedDistToTrianglePlane = trianglePlane.signedDistanceTo(colPackage.basePoint);
        // cache this as we’re going to use it a few times below:
        double normalDotVelocity = trianglePlane.normal.dot(colPackage.velocity);
        // if sphere is travelling parrallel to the plane:
        if (normalDotVelocity == 0) {
            if (Math.abs(signedDistToTrianglePlane) >= 1) {
                // Sphere is not embedded in plane.
                // No collision possible:
                return;
            } else {
                // sphere is embedded in plane.
                // It intersects in the whole range [0..1]
                embeddedInPlane = true;
                t0 = 0;
                t1 = 1;
            }
        } else {
            // N dot D is not 0. Calculate intersection interval:
            t0 = (-1 - signedDistToTrianglePlane) / normalDotVelocity;
            t1 = (1 - signedDistToTrianglePlane) / normalDotVelocity;
            // Swap so t0 < t1
            if (t0 > t1) {
                double temp = t1;
                t1 = t0;
                t0 = temp;
            }
            // Check that at least one result is within range:
            if (t0 > 1 || t1 < 0) {
                // Both t values are outside values [0,1]
                // No collision possible:
                return;
            }
            // Clamp to [0,1]
            if (t0 < 0) {
                t0 = 0;
            }
            if (t1 < 0) {
                t1 = 0;
            }
            if (t0 > 1) {
                t0 = 1;
            }
            if (t1 > 1) {
                t1 = 1;
            }
        }
        // OK, at this point we have two time values t0 and t1
        // between which the swept sphere intersects with the
        // triangle plane. If any collision is to occur it must
        // happen within this interval.
        Vector collisionPoint = null;
        boolean foundCollison = false;
        double t = 1;
        // First we check for the easy case - collision inside
        // the triangle. If this happens it must be at time t0
        // as this is when the sphere rests on the front side
        // of the triangle plane. Note, this can only happen if
        // the sphere is not embedded in the triangle plane.
        if (!embeddedInPlane) {
            Vector planeIntersectionPoint = (colPackage.basePoint.subtract(trianglePlane.normal)).add(colPackage.velocity.multiply(t0));
            if (checkPointInTriangle(planeIntersectionPoint, p1, p2, p3)) {
                foundCollison = true;
                t = t0;
                collisionPoint = planeIntersectionPoint;
            }
        }
        // if we haven’t found a collision already we’ll have to
        // sweep sphere against points and edges of the triangle.
        // Note: A collision inside the triangle (the check above)
        // will always happen before a vertex or edge collision!
        // This is why we can skip the swept test if the above
        // gives a collision!
        if (foundCollison == false) {
            //System.out.println("?");
            // some commonly used terms:
            Vector velocity = colPackage.velocity;
            Vector base = colPackage.basePoint;
            double velocitySquaredLength = velocity.squaredLength();
            double a, b, c; // Params for equation
            DoubleWrapper newT = new DoubleWrapper(0);
            // For each vertex or edge a quadratic equation have to
            // be solved. We parameterize this equation as
            // a*t^2 + b*t + c = 0 and below we calculate the
            // parameters a,b and c for each test.
            // Check against points:
            a = velocitySquaredLength;
            // P1
            b = 2 * (velocity.dot(base.subtract(p1)));
            c = (p1.subtract(base)).squaredLength() - 1;
            if (getLowestRoot(a, b, c, t, newT)) {
                t = newT.val;
                foundCollison = true;
                collisionPoint = p1;
            }
            // P2
            b = 2 * (velocity.dot(base.subtract(p2)));
            c = (p2.subtract(base)).squaredLength() - 1;
            if (getLowestRoot(a, b, c, t, newT)) {
                t = newT.val;
                foundCollison = true;
                collisionPoint = p2;
            }
            // P3
            b = 2 * (velocity.dot(base.subtract(p3)));
            c = (p3.subtract(base)).squaredLength() - 1;
            if (getLowestRoot(a, b, c, t, newT)) {
                t = newT.val;
                foundCollison = true;
                collisionPoint = p3;
            }
            // Check agains edges:
            // p1 . p2:
            Vector edge = p2.subtract(p1);
            Vector baseToVertex = p1.subtract(base);
            double edgeSquaredLength = edge.squaredLength();
            double edgeDotVelocity = edge.dot(velocity);
            double edgeDotBaseToVertex = edge.dot(baseToVertex);
            // Calculate parameters for equation
            a = edgeSquaredLength * -velocitySquaredLength + edgeDotVelocity * edgeDotVelocity;
            b = edgeSquaredLength * (2 * velocity.dot(baseToVertex)) - 2 * edgeDotVelocity * edgeDotBaseToVertex;
            c = edgeSquaredLength * (1 - baseToVertex.squaredLength()) + edgeDotBaseToVertex * edgeDotBaseToVertex;
            // Does the swept sphere collide against infinite edge?
            if (getLowestRoot(a, b, c, t, newT)) {
                // Check if intersection is within line segment:
                double f = (edgeDotVelocity * newT.val - edgeDotBaseToVertex) / edgeSquaredLength;
                if (f >= 0 && f <= 1) {
                    // intersection took place within segment.
                    t = newT.val;
                    foundCollison = true;
                    collisionPoint = p1.add(edge.multiply(f));
                }
            }
            // p2 . p3:
            edge = p3.subtract(p2);
            baseToVertex = p2.subtract(base);
            edgeSquaredLength = edge.squaredLength();
            edgeDotVelocity = edge.dot(velocity);
            edgeDotBaseToVertex = edge.dot(baseToVertex);
            a = edgeSquaredLength * -velocitySquaredLength + edgeDotVelocity * edgeDotVelocity;
            b = edgeSquaredLength * (2 * velocity.dot(baseToVertex)) - 2 * edgeDotVelocity * edgeDotBaseToVertex;
            c = edgeSquaredLength * (1 - baseToVertex.squaredLength()) + edgeDotBaseToVertex * edgeDotBaseToVertex;
            if (getLowestRoot(a, b, c, t, newT)) {
                double f = (edgeDotVelocity * newT.val - edgeDotBaseToVertex) / edgeSquaredLength;
                if (f >= 0 && f <= 1) {
                    t = newT.val;
                    foundCollison = true;
                    collisionPoint = p2.add(edge.multiply(f));
                }
            }
            // p3 . p1:
            edge = p1.subtract(p3);
            baseToVertex = p3.subtract(base);
            edgeSquaredLength = edge.squaredLength();
            edgeDotVelocity = edge.dot(velocity);
            edgeDotBaseToVertex = edge.dot(baseToVertex);
            a = edgeSquaredLength * -velocitySquaredLength + edgeDotVelocity * edgeDotVelocity;
            b = edgeSquaredLength * (2 * velocity.dot(baseToVertex)) - 2 * edgeDotVelocity * edgeDotBaseToVertex;
            c = edgeSquaredLength * (1 - baseToVertex.squaredLength()) + edgeDotBaseToVertex * edgeDotBaseToVertex;
            if (getLowestRoot(a, b, c, t, newT)) {
                double f = (edgeDotVelocity * newT.val - edgeDotBaseToVertex) / edgeSquaredLength;
                if (f >= 0 && f <= 1) {
                    t = newT.val;
                    foundCollison = true;
                    collisionPoint = p3.add(edge.multiply(f));
                }
            }
        }
        // Set result:
        if (foundCollison == true) {
            // distance to collision: ’t’ is time of collision
            double distToCollision = t * colPackage.velocity.length();
            // Does this triangle qualify for the closest hit?
            // it does if it’s the first hit or the closest
            if (colPackage.foundCollision == false || distToCollision < colPackage.nearestDistance) {
                // Collision information nessesary for sliding
                colPackage.nearestDistance = distToCollision;
                colPackage.intersectionPoint = collisionPoint;
                colPackage.intersectionNormal = trianglePlane.normal;
                colPackage.foundCollision = true;
            }
        }
        //} // if not backface
    }

    public static CollisionPacket collideAndSlide(Room world, Vector position, Vector eRadius, Vector vel, Vector gravity) {
        collisionPackage = new CollisionPacket();
        // Do collision detection:
        collisionPackage.eRadius = eRadius;
        collisionPackage.R3Position = position;
        collisionPackage.R3Velocity = vel;
        // calculate position and velocity in eSpace
        Vector eSpacePosition = collisionPackage.R3Position.divide(collisionPackage.eRadius);
        Vector eSpaceVelocity = collisionPackage.R3Velocity.divide(collisionPackage.eRadius);
        // Iterate until we have our final position.
        collisionRecursionDepth = 0;
        Vector finalPosition = collideWithWorld(world, eSpacePosition, eSpaceVelocity);
        // Add gravity pull:
        // To remove gravity uncomment from here .....
        // Set the new R3 position (convert back from eSpace to R3
        collisionPackage.R3Position = finalPosition.multiply(collisionPackage.eRadius);
        collisionPackage.R3Velocity = gravity;
        eSpaceVelocity = gravity.divide(collisionPackage.eRadius);
        collisionRecursionDepth = 0;
        finalPosition = collideWithWorld(world, finalPosition, eSpaceVelocity);
        // ... to here
        // Convert final result back to R3:
        finalPosition = finalPosition.multiply(collisionPackage.eRadius);
        // Move the entity (application specific function)
        collisionPackage.finalPoint = finalPosition;
        return collisionPackage;
    }

    static double collisionRecursionDepth;
    static CollisionPacket collisionPackage;

    public static Vector collideWithWorld(Room world, Vector pos, Vector vel) {
        // All hard-coded distances in this function is
        // scaled to fit the setting above..
        double veryCloseDistance = 0.01;
        // do we need to worry?
        if (collisionRecursionDepth > 5) {
            return pos;
        }
        // Ok, we need to worry:
        collisionPackage.velocity = vel;
        collisionPackage.normalizedVelocity = vel.normalize();
        collisionPackage.basePoint = pos;
        collisionPackage.foundCollision = false;
        // Check for collision (calls the collision routines)
        // Application specific!!
        world.checkCollision(collisionPackage);
        // If no collision we just move along the velocity
        if (collisionPackage.foundCollision == false) {
            return pos.add(vel);
        }
        // *** Collision occured ***
        // The original destination point
        Vector destinationPoint = pos.add(vel);
        Vector newBasePoint = pos;
        // only update if we are not already very close
        // and if so we only move very close to intersection..not
        // to the exact spot.
        if (collisionPackage.nearestDistance >= veryCloseDistance) {
            Vector V = vel;
            V = V.setLength(collisionPackage.nearestDistance - veryCloseDistance);
            newBasePoint = collisionPackage.basePoint.add(V);
            // Adjust polygon intersection point (so sliding
            // plane will be unaffected by the fact that we
            // move slightly less than collision tells us)
            V = V.normalize();
            collisionPackage.intersectionPoint = collisionPackage.intersectionPoint.subtract(V.multiply(veryCloseDistance));
        }
        // Determine the sliding plane
        Vector slidePlaneOrigin = collisionPackage.intersectionPoint;
        Vector slidePlaneNormal = newBasePoint.subtract(collisionPackage.intersectionPoint);
        slidePlaneNormal = slidePlaneNormal.normalize();
        Plane slidingPlane = new Plane(slidePlaneOrigin, slidePlaneNormal);
        // Again, sorry about formatting.. but look carefully ;)
        Vector newDestinationPoint = destinationPoint.subtract(slidePlaneNormal.multiply(slidingPlane.signedDistanceTo(destinationPoint)));
        // Generate the slide vector, which will become our new
        // velocity vector for the next iteration
        Vector newVelocityVector = newDestinationPoint.subtract(collisionPackage.intersectionPoint);
        // Recurse:
        // dont recurse if the new velocity is very small
        if (newVelocityVector.length() < veryCloseDistance) {
            return newBasePoint;
        }
        collisionRecursionDepth++;
        return collideWithWorld(world, newBasePoint, newVelocityVector);
    }
}
