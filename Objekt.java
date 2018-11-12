/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulationtest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class Objekt implements Serializable{
    //ArrayList<Keyframe> Keyframes = new ArrayList<Keyframe>();
    float [] position = {0.0f, 0.0f};
    float [] positionOld = {0.0f, 0.0f};
    float [] startPosition = {0.0f,0.0f};
    float [] scale = {1.0f,1.0f};
    float rotation = 0.0f;
    float tempRotation = 0.0f;
    float startRotation;
    float mass;
    float radius = 0.0f;
    float dragCoefficient = 0.1f;
    float [] velocity = {0.0f, 0.0f};
    float [] velocityOld = {0.0f, 0.0f};
    float [] startVelocity = {0.0f,0.0f};
    float angularVelocity = 0.0f;
    float angularVelocityOld=  0.0f;
    float startAngularVelocity = 0.0f;
    String material = "Wood";
    String type;
    Boolean passive;
    float restitution = 0.7f;
    
    public void recalc(float deltaT, ArrayList<ForceField> forceFields, ArrayList<SphereSphereCollision> sphereSphereCollisions, ArrayList<SphereQuboidCollision> sphereQuboidCollisions){
        if(passive == false){
            float[] accelerations = {0.0f,0.0f};
            for (int i = 0; i < forceFields.size(); i++){
                if(this.position[0] >= forceFields.get(i).getCornerPoints()[0][0] && this.position[0] <= forceFields.get(i).getCornerPoints()[1][0]){
                    if(this.position[1] >= forceFields.get(i).getCornerPoints()[0][1] && this.position[1] <= forceFields.get(i).getCornerPoints()[3][1]){
                        accelerations[0] = accelerations[0]+forceFields.get(i).getAccelerationX()*this.mass;
                        accelerations[1] = accelerations[1]+forceFields.get(i).getAccelerationY()*this.mass;
                    }
                }
            }
            for(int i = 0; i < sphereQuboidCollisions.size(); i++){
                if (sphereQuboidCollisions.get(i).sphere == this){
                    if(sphereQuboidCollisions.get(i).pointA == "a" && sphereQuboidCollisions.get(i).pointB == "b"){
                        float[] accR = rotate2d(accelerations, 360-sphereQuboidCollisions.get(i).quboid.getRotation());
                        if (accR[1] >= 0.0f){
                            float[] dragAcc = {this.dragCoefficient*(-accR[1]), 0.0f};
                            float oldAccRx = accR[0];
                            accR[0] = accR[0]+dragAcc[0];
                            accR[1] = 0.0f;
                            if(oldAccRx >= 0.0f){
                                if(accR[0] < 0.0f){
                                    accR[0] = 0.0f;
                                }
                            }
                            if(oldAccRx < 0.0f){
                                if(accR[0] > 0.0f){
                                    accR[0] = 0.0f;
                                }
                            }
                            System.out.println("radius: " + this.radius);
                            this.angularVelocity = (float)(accR[0]/2*this.radius*Math.PI)*360;
                            System.out.println("angularVelocity: " + this.angularVelocity);
                        }
                        accR= rotate2d(accR, sphereQuboidCollisions.get(i).quboid.getRotation());
                        accelerations = accR;
                    }
                    if(sphereQuboidCollisions.get(i).pointA == "b" && sphereQuboidCollisions.get(i).pointB == "c"){
                        float[] accR = rotate2d(accelerations, 360-sphereQuboidCollisions.get(i).quboid.getRotation());
                        if (accR[0] <= 0.0f){
                            float[] dragAcc = {0.0f, this.dragCoefficient*(-accR[0])};
                            float oldAccRy = accR[1];
                            accR[0] = 0.0f;
                            accR[1] = accR[1];
                            if(oldAccRy >= 0.0f){
                                if(accR[1] < 0.0f){
                                    accR[1] = 0.0f;
                                }
                            }
                            if(oldAccRy < 0.0f){
                                if(accR[1] > 0.0f){
                                    accR[1] = 0.0f;
                                }
                            }
                        }
                        accR= rotate2d(accR, sphereQuboidCollisions.get(i).quboid.getRotation());
                        accelerations = accR;
                    }
                    if(sphereQuboidCollisions.get(i).pointA == "c" && sphereQuboidCollisions.get(i).pointB == "d"){
                        float[] accR = rotate2d(accelerations, 360-sphereQuboidCollisions.get(i).quboid.getRotation());
                        if (accR[1] <= 0.0f){
                            float[] dragAcc = {this.dragCoefficient*(-accR[1]), 0.0f};
                            float oldAccRx = accR[0];
                            accR[0] = accR[0]+dragAcc[0];
                            accR[1] = 0.0f;
                            if(oldAccRx >= 0.0f){
                                if(accR[0] < 0.0f){
                                    accR[0] = 0.0f;
                                }
                            }
                            if(oldAccRx < 0.0f){
                                if(accR[0] > 0.0f){
                                    accR[0] = 0.0f;
                                }
                            }
                        }
                        accR= rotate2d(accR, sphereQuboidCollisions.get(i).quboid.getRotation());
                        accelerations = accR;
                    }
                    if(sphereQuboidCollisions.get(i).pointA == "a" && sphereQuboidCollisions.get(i).pointB == "d"){
                        float[] accR = rotate2d(accelerations, 360-sphereQuboidCollisions.get(i).quboid.getRotation());
                        if (accR[0] >= 0.0f){
                            float[] dragAcc = {0.0f, this.dragCoefficient*(-accR[0])};
                            float oldAccRy = accR[1];
                            accR[0] = 0.0f;
                            accR[1] = accR[1];
                            if(oldAccRy >= 0.0f){
                                if(accR[1] < 0.0f){
                                    accR[1] = 0.0f;
                                }
                            }
                            if(oldAccRy < 0.0f){
                                if(accR[1] > 0.0f){
                                    accR[1] = 0.0f;
                                }
                            }
                        }
                        accR= rotate2d(accR, sphereQuboidCollisions.get(i).quboid.getRotation());
                        accelerations = accR;
                    }
                }
            }
            
            this.positionOld = this.position;
            this.velocityOld = this.velocity;

            this.velocity[0] = this.velocityOld[0]+accelerations[0]*deltaT;
            this.velocity[1] = this.velocityOld[1]+accelerations[1]*deltaT;

            this.position[0] = this.positionOld[0] + this.velocityOld[0] * deltaT + (accelerations[0]*deltaT*deltaT)/2;
            this.position[1] = this.positionOld[1] + this.velocityOld[1] * deltaT + (accelerations[1]*deltaT*deltaT)/2;

            this.angularVelocityOld = this.angularVelocity;
            this.rotation = this.rotation + deltaT*this.angularVelocity;
        }
    }

    public float [] getScale(){ 
        return scale;
    }
    public void setScale(float [] newScale){
        scale = newScale;
    }
    
    public String getType(){
        return this.type;
    }
    
    public void setVelocity(float[] velocity){
        this.velocity = velocity;
        this.velocityOld = this.velocity;
    }
    
    public void setVelocity(float velX, float velY){
        this.velocity = new float[2];
        this.velocity[0] = velX;
        this.velocity[1] = velY;
        this.velocityOld = this.velocity;
    }
    
    public void setAngularVelocity(float velocity){
        this.angularVelocity = velocity;
        this.angularVelocityOld = this.angularVelocity;
    }
    
    public void setRotation(float rot){
        if(rot > 360){
            rot = rot -360;
        }
        this.rotation = rot;
    }
    
    public void setTempRotation(float rot){
        this.tempRotation = rot;
    }
    
    public void setPosition(float[] position){
        this.position = position;
    }
    
    public void setPositionOld(float[] position){
        this.positionOld = position;
    }
    
    public void setRestitution(float rest){
        this.restitution = rest;
    }
    
    public float getRestitution(){
        return this.restitution;
    }
    
    public float[] getPosition(){
        return this.position;
    }
    public float getPositionX(){
        return this.position[0];
    }
    public float getPositionY(){
        return this.position[1];
    }
    public int getPositionX(int scale){
        return (int) (this.position[0]*scale);
    }
    public int getPositionY(int scale){
        return (int) (this.position[1]*scale);
    }
    public float[] getStartPosition(){
        return this.startPosition;
    }
    
    public float[] getVelocity(){
        return this.velocity;
    }
    
    public float[] getStartVelocity(){
        return this.startVelocity;
    }
    
    public float getRotation(){
        return this.rotation;
    }
    
    public float getTempRotation(){
        return this.tempRotation;
    }
    
    public float getMass(){
        return this.mass;
    }
    
    public String getMaterial(){
        return this.material;
    }
    
    public void setMaterial(String mat){
        this.material = mat;
        if (mat == "wood"){
            this.restitution = 0.603f;
            this.dragCoefficient = 0.5f;
        } else if (mat == " steel"){
            this.restitution = 0.597f;
            this.dragCoefficient = 0.8f;
        } else if (mat == "concrete"){
            this.restitution = 0.48f;
            this.dragCoefficient = 0.3f;
        }else if (mat == "rubber"){
            this.restitution = 0.828f;
            this.dragCoefficient = 0.4f;
        }
    }
    
    public void setPassive(boolean pas){
        this.passive = pas;
    }
    
    public void rescale(){
        System.out.println("rescaling");
    }
    
    public void setRadius(float rad){
        System.out.print("setRadius(rad)");
    }
    
    public float getRadius(){
        System.out.print("getRadius()");
        return 0.0f;
    }
    
    public void setAcceleration(float accX, float accY){
        System.out.println("setAcc()");
    }
    
    public float[] getAcceleration(){
        float[] acc = {0.0f,0.0f};
        return acc;
    }
    public float getAccelerationX(){
        return 0.0f;
    }
    public float getAccelerationY(){
        return 0.0f;
    }
    
    public float[] rotate2d(float[] vector, float rotation){
        rotation = (float)Math.toRadians(rotation);
        float rx = (vector[0] * (float)Math.cos(rotation)) - (vector[1] * (float)Math.sin(rotation));
        float ry = (vector[0] * (float)Math.sin(rotation)) + (vector[1] * (float)Math.cos(rotation));
        float[] newVector = {rx, ry};
        return newVector;
    }
    
}

