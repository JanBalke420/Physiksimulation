/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulationtest;

import java.io.Serializable;

/**
 *
 * @author M.Kaur
 */
public class ForceField extends Objekt{
    float width;
    float height;
    float[] position =  new float[2];
    float[] acceleration = new float[2];
    private float[][] cornerPoints = new float[4][2];
    String name = "none";



    public ForceField()
    {
    }
    
    public ForceField(float posX, float posY, float width, float height, float accX, float accY)
    {
        this.position[0] = posX;
        this.position[1] = posY;
        
        this.width = width;
        this.height = height;
        
        this.acceleration[0] = accX;
        this.acceleration[1] = accY;
        
        this.type = "force";
        
        recalcPoints();
    }
    
    public ForceField(float posX, float posY, float width, float height, float accX, float accY, int scale)
    {
        this.position[0] = posX/scale;
        this.position[1] = posY/scale;
        
        this.width = width/scale;
        this.height = height/scale;
        
        this.acceleration[0] = accX/scale;
        this.acceleration[1] = accY/scale;
        
        this.type = "force";
        
        recalcPoints();
    }
    
    public void recalcPoints(){
        float[] a = {this.position[0],this.position[1]};
        float[] b = {this.position[0]+this.width,this.position[1]};
        float[] c = {this.position[0]+this.width,this.position[1]+this.height};
        float[] d = {this.position[0],this.position[1]+this.height};
        
        this.cornerPoints[0] = a;
        this.cornerPoints[1] = b;
        this.cornerPoints[2] = c;
        this.cornerPoints[3] = d;
    }
    
    public String toString(){
        String str = "Forcefield: Name: " + this.name + ", position: " + this.position[0] + "," + this.position[1] + ", width: " + this.width + ", height: " + this.height + ", accelearation: " + this.acceleration[0] + "," + this.acceleration[1];
        return str;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return this.name;
    }

    public void setWitdh(float width){
        this.width = width;
        recalcPoints();
    }
    
    public void setHeight(float height){
        this.height = height;
        recalcPoints();
    }
    
    public void setPosition(float[] pos){
        this.position[0] = pos[0];
        this.position[1] = pos[1];
        recalcPoints();
    }
    
    public void setPosition(float posX, float posY){
        this.position[0] = posX;
        this.position[1] = posY;
        recalcPoints();
    }
    
    public void setAcceleration(float accX, float accY){
        this.acceleration[0] = accX;
        this.acceleration[1] = accY;
    }
    
    public float getWidth(){
        return this.width*this.scale[0];
    }
    public int getWidth(int scale){
        return (int) (this.width * this.scale[0] * scale);
    }
    
    public float getHeight(){
        return this.height * this.scale[1];
    }
    public int getHeight(int scale){
        return (int) (this.height * this.scale[1] * scale);
    }
    
    public void rescale(){
        this.width = this.width * this.scale[0];
        this.height = this.height * this.scale[1];
        this.scale[0] = 1.0f;
        this.scale[1] = 1.0f;
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
    
    public float[] getAcceleration(){
        return this.acceleration;
    }
    public float getAccelerationX(){
        return this.acceleration[0];
    }
    public float getAccelerationY(){
        return this.acceleration[1];
    }
    
    public void move(){
        this.position[0] = this.position[0] + 1;
        this.position[1] = this.position[1] + 1;
    }
    
    public float[][] getCornerPoints(){
        recalcPoints();
        return this.cornerPoints;
    }

}
