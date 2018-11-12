package simulationtest;

import java.util.ArrayList;

public class Sphere extends Objekt{
    private float radius;

    public Sphere (float radius, float[] position){
        this.type = "sphere";
        this.radius = Math.abs(radius);
        this.position = position;
        this.positionOld = position;
        this.startPosition = position;
        this.mass = (float) (Math.PI*0.75*this.radius*this.radius*this.radius);
        this.mass = this.mass *10;
        this.passive = false;
        this.restitution = 0.603f;
        this.dragCoefficient = 0.5f;
    }
    
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
                            //System.out.println("radius: " + this.radius);
                            //this.angularVelocity = (float)(accR[0]/(2*this.radius*Math.PI))*360;
                            //System.out.println("angularVelocity: " + this.angularVelocity);
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
                            //this.angularVelocity = (float)(accR[1]/(2*this.radius*Math.PI))*360;
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
                            //this.angularVelocity = (float)(accR[0]/(2*this.radius*Math.PI))*360;
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
                            //this.angularVelocity = (float)(accR[1]/(2*this.radius*Math.PI))*360;
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

    public float getRadius()
    {
        return this.radius*this.scale[0];
    }
    
    public float getRadius(int screenScale)
    {
        return this.radius*this.scale[0]*screenScale;
    } 

    public void setRadius(float neuerRadius)
    {
        this.radius = Math.abs(radius);
    }

    public void setScale(float[] scale){
        this.scale = scale;
        this.mass = (float) (Math.PI*0.75*this.radius*this.scale[0]*this.radius*this.scale[0]*this.radius*this.scale[0]);
        if (this.material == "wood"){
            this.mass = this.mass *500;
        } else if (this.material == "steel"){
            this.mass = this.mass *7850;
        } else if (this.material == "steel"){
            this.mass = this.mass *2400;
        } else if (this.material == "steel"){
            this.mass = this.mass *910;
        }
    }
    
    public void rescale(){
        this.radius = this.radius*this.scale[0];
        this.scale[0] = 1.0f;
        this.scale[1] = 1.0f;
    }
}
