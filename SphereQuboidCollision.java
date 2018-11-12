/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulationtest;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Jan-Laptop
 */
public class SphereQuboidCollision {
    Sphere sphere;
    Quader quboid;
    String pointA;
    String pointB;
    float ra = 0.0f;
    
    public SphereQuboidCollision(Sphere sphere, Quader quboid, String pointA, String pointB){
        this.sphere = sphere;
        this.quboid = quboid;
        this.pointA = pointA;
        this.pointB = pointB;
    }
    
    public void collide(){
        if (this.pointA == this.pointB){
            //System.out.println("corner collision...");
            float[] vectorR = {this.sphere.getPositionX(), this.sphere.getPositionY()};
            vectorR[0] = vectorR[0]-this.quboid.getPositionX();
            vectorR[1] = vectorR[1]-this.quboid.getPositionY();
            vectorR = rotate2d(vectorR, 360 - this.quboid.getRotation());
            vectorR[0] = vectorR[0]+this.quboid.getPositionX();
            vectorR[1] = vectorR[1]+this.quboid.getPositionY();
            float[] n = {0.0f,0.0f};
            float[] xAxis = {1.0f,0.0f};
            float alpha = 0.0f;
            //float ra = 0.0f;
            if ( this.pointA == "a"){
                n[0] = vectorR[0]-this.quboid.getCornerPoints()[0][0];
                n[1] = vectorR[1]-this.quboid.getCornerPoints()[0][1];
                alpha = getAngle(n, xAxis);
                this.ra = alpha+90;
            } else if ( this.pointA == "b"){
                n[0] = vectorR[0]-this.quboid.getCornerPoints()[1][0];
                n[1] = vectorR[1]-this.quboid.getCornerPoints()[1][1];
                alpha = getAngle(n, xAxis);
                this.ra = alpha+90;
            } else if ( this.pointA == "c"){
                n[0] = vectorR[0]-this.quboid.getCornerPoints()[2][0];
                n[1] = vectorR[1]-this.quboid.getCornerPoints()[2][1];
                alpha = getAngle(n, xAxis);
                this.ra = 360-alpha+90;
            } else {
                n[0] = vectorR[0]-this.quboid.getCornerPoints()[3][0];
                n[1] = vectorR[1]-this.quboid.getCornerPoints()[3][1];
                alpha = getAngle(n, xAxis);
                this.ra = 360-alpha+90;
            }
            float[] velR = rotate2d(this.sphere.getVelocity(), 360 - this.quboid.getRotation());
            velR = rotate2d(velR, this.ra);
            if (velR[1] <= 0.0f){
                velR[0] = velR[0]*this.sphere.getRestitution();
                velR[1] = -velR[1]*this.sphere.getRestitution();
            }
            velR = rotate2d(velR, 360-this.ra);
            velR = rotate2d(velR, this.quboid.getRotation());
            float[] newVel = velR;
            this.sphere.setVelocity(newVel);
        } else {
            //collision at top
            if(this.pointA == "a" && this.pointB == "b"){
                float[] velR = rotate2d(this.sphere.getVelocity(), 360-this.quboid.getRotation());
                if (velR[1] >= 0.001f){
                    velR[0] = velR[0]*this.sphere.getRestitution();
                    velR[1] = -velR[1]*this.sphere.getRestitution();
                } else {
                    //velR[0] = velR[0]*this.sphere.getRestitution();
                    //velR[1] = -velR[1]*this.sphere.getRestitution();
                    velR[1] = 0.0f;
                }
                float[] newVel = rotate2d(velR, this.quboid.getRotation());                
                this.sphere.setVelocity(newVel);
            }
            //collision at bottom
            if(this.pointA == "c" && this.pointB == "d"){
                float[] velR = rotate2d(this.sphere.getVelocity(), 360-this.quboid.getRotation());
                if (velR[1] <= -0.001f){
                    velR[0] = velR[0]*this.sphere.getRestitution();
                    velR[1] = -velR[1]*this.sphere.getRestitution();
                } else {
//                    velR[0] = velR[0]*this.sphere.getRestitution();
//                    velR[1] = -velR[1]*this.sphere.getRestitution();
                    velR[1] = 0.0f;
                }
                float[] newVel = rotate2d(velR, this.quboid.getRotation());
                this.sphere.setVelocity(newVel);
            }
            //collision at right
            if(this.pointA == "b" && this.pointB == "c"){
                float[] velR = rotate2d(this.sphere.getVelocity(), 360-this.quboid.getRotation());
                if (velR[0] <= -0.001f){
                    velR[0] = -velR[0]*this.sphere.getRestitution();
                    velR[1] = velR[1]*this.sphere.getRestitution();
                } else {
//                    velR[0] = velR[0]*this.sphere.getRestitution();
//                    velR[1] = -velR[1]*this.sphere.getRestitution();
                    velR[0] = 0.0f;
                }
                float[] newVel = rotate2d(velR, this.quboid.getRotation());
                this.sphere.setVelocity(newVel);
            }
            //collision at left
            if(this.pointA == "a" && this.pointB == "d"){
                float[] velR = rotate2d(this.sphere.getVelocity(), 360-this.quboid.getRotation());
                if (velR[0] >= 0.001f){
                    velR[0] = -velR[0]*this.sphere.getRestitution();
                    velR[1] = velR[1]*this.sphere.getRestitution();
                } else {
//                    velR[0] = velR[0]*this.sphere.getRestitution();
//                    velR[1] = -velR[1]*this.sphere.getRestitution();
                    velR[0] = 0.0f;
                }
                float[] newVel = rotate2d(velR, this.quboid.getRotation());
                this.sphere.setVelocity(newVel);
            }
        }
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
