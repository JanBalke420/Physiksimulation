/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulationtest;

/**
 *
 * @author Jan-Laptop
 */
public class SphereSphereCollision {
    Sphere objectA;
    Sphere objectB;
    
    public SphereSphereCollision(Sphere objectA, Sphere objectB){
        this.objectA = objectA;
        this.objectB = objectB;
    }
    
    public void collide(){
        float[] n = {this.objectB.getPositionX()-this.objectA.getPositionX(), this.objectB.getPositionY()-this.objectA.getPositionY()};
        float[] velocity_a = this.objectA.getVelocity();
        float[] velocity_b = this.objectB.getVelocity();
        float[] xAxis = {1.0f,0.0f};
        float alpha = getAngle(n, xAxis);
        
        float ra = 0.0f;
        float[] velocity_a_r = {0.0f, 0.0f};
        float[] velocity_b_r = {0.0f, 0.0f};

        if (n[1] >= 0.0){
            ra = 360-alpha;
            //360-a
            float[] nr = rotate2d(n, ra);
            velocity_a_r = rotate2d(velocity_a, ra);
            velocity_b_r = rotate2d(velocity_b, ra);
        }
        if (n[1] < 0.0){
            ra = alpha;
            //180-a
            float[] nr = rotate2d(n, ra);
            velocity_a_r = rotate2d(velocity_a, ra);
            velocity_b_r = rotate2d(velocity_b, ra);
        }

        float[] new_v_a_r = {velocity_b_r[0], velocity_a_r[1]};
        float[] new_v_b_r = {velocity_a_r[0], velocity_b_r[1]};

        float[] new_v_a = rotate2d(new_v_a_r, 360 - ra);
        float[] new_v_b = rotate2d(new_v_b_r, 360 - ra);
        
//        float oldVecALen = (float)Math.sqrt(this.objectA.getVelocity()[0]*this.objectA.getVelocity()[0]+this.objectA.getVelocity()[1]*this.objectA.getVelocity()[1]);
//        float oldVecBLen = (float)Math.sqrt(this.objectB.getVelocity()[0]*this.objectB.getVelocity()[0]+this.objectB.getVelocity()[1]*this.objectB.getVelocity()[1]);
//        float newVecALen = (float)Math.sqrt(new_v_a[0]*new_v_a[0]+new_v_a[1]*new_v_a[1]);
//        float newVecBLen = (float)Math.sqrt(new_v_b[0]*new_v_b[0]+new_v_b[1]*new_v_b[1]);
//        float massA = this.objectA.getMass();
//        float massB = this.objectB.getMass();
        float restA = this.objectA.getRestitution();
        float restB = this.objectB.getRestitution();
//        
//        float scalingFactorA = ((restA*massB*(oldVecBLen-oldVecALen)+massA*oldVecALen+massB*oldVecBLen)/massA+massB)/newVecALen;
//        float scalingFactorB = ((restB*massA*(oldVecALen-oldVecBLen)+massA*oldVecALen+massB*oldVecBLen)/massA+massB)/newVecBLen;

        
        new_v_a[0] = new_v_a[0] * restA;
        new_v_a[1] = new_v_a[1] * restA;
        
        new_v_b[0] = new_v_b[0] * restB;
        new_v_b[1] = new_v_b[1] * restB;

        this.objectA.setVelocity(new_v_a);
        this.objectB.setVelocity(new_v_b);
    }
    
    public float getAngle(float[] vector_1, float[] vector_2){
        float skalar = (vector_1[0]*vector_2[0]+vector_1[1]*vector_2[1]);
        float betrag_1 = (float) Math.sqrt(vector_1[0]*vector_1[0]+vector_1[1]*vector_1[1]);
        float betrag_2 = (float) Math.sqrt(vector_2[0]*vector_2[0]+vector_2[1]*vector_2[1]);
        float angle = (float) Math.acos(skalar/(betrag_1*betrag_2));

        return (float) Math.toDegrees(angle);
    }
    
    public float[] rotate2d(float[] vector, float rotation){
        rotation = (float)Math.toRadians(rotation);
        float rx = (vector[0] * (float)Math.cos(rotation)) - (vector[1] * (float)Math.sin(rotation));
        float ry = (vector[0] * (float)Math.sin(rotation)) + (vector[1] * (float)Math.cos(rotation));
        float[] newVector = {rx, ry};
        return newVector;
    }
}
