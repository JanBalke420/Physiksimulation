/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulationtest;

/**
 *
 * @author M.Kaur
 */
//Klasse Quader  
    public class Quader extends Objekt {
        private float width;
        private float height;
        private float[][] cornerPoints = new float[4][2];

//Konstruktor
    public Quader(float posX, float posY, float width, float height){
        this.type = "Quader";
        this.position[0] = posX;
        this.position[1] = posY;
        this.positionOld = position;
        
        this.width = width;
        this.height = height;
        
        this.passive = true;
        
        recalcPoints();
    }
    
    public Quader(float posX, float posY, float width, float height, float rotation){
        this.type = "Quader";
        this.position[0] = posX;
        this.position[1] = posY;
        this.positionOld = position;
        
        this.width = width;
        this.height = height;
        
        this.rotation = rotation;
        
        this.passive = true;
        
        recalcPoints();
    }
    
    public Quader(float posX, float posY, float width, float height, int screenScale){
        this.type = "Quader";
        this.position[0] = posX/screenScale;
        this.position[1] = posY/screenScale;
        this.positionOld = position;
        
        this.width = width/screenScale;
        this.height = height/screenScale;
        
        this.passive = true;
        
        recalcPoints();
    }
    
    public void recalcPoints(){
        float[] a = {this.position[0]-(this.width*this.scale[0])/2,this.position[1]-(this.height*this.scale[1])/2};
        float[] b = {this.position[0]+(this.width*this.scale[0])/2,this.position[1]-(this.height*this.scale[1])/2};
        float[] c = {this.position[0]+(this.width*this.scale[0])/2,this.position[1]+(this.height*this.scale[1])/2};
        float[] d = {this.position[0]-(this.width*this.scale[0])/2,this.position[1]+(this.height*this.scale[1])/2};
        
        this.cornerPoints[0] = a;
        this.cornerPoints[1] = b;
        this.cornerPoints[2] = c;
        this.cornerPoints[3] = d;
    }
    
    public float[][] getScreenPoints(int screenScale){
        float[][] screenPoints = new float[4][2];
        
        screenPoints[0][0] = this.cornerPoints[0][0]*screenScale;
        screenPoints[0][1] = this.cornerPoints[0][1]*screenScale;
        screenPoints[1][0] = this.cornerPoints[1][0]*screenScale;
        screenPoints[1][1] = this.cornerPoints[1][1]*screenScale;
        screenPoints[2][0] = this.cornerPoints[2][0]*screenScale;
        screenPoints[2][1] = this.cornerPoints[2][1]*screenScale;
        screenPoints[3][0] = this.cornerPoints[3][0]*screenScale;
        screenPoints[3][1] = this.cornerPoints[3][1]*screenScale;
        
        return screenPoints;
    }
    
    public float[] rotate2d(float[] vector, float rotation){
        rotation = (float)Math.toRadians(rotation);
        float rx = (vector[0] * (float)Math.cos(rotation)) - (vector[1] * (float)Math.sin(rotation));
        float ry = (vector[0] * (float)Math.sin(rotation)) + (vector[1] * (float)Math.cos(rotation));
        float[] newVector = {rx, ry};
        return newVector;
    }

//GETTER UND SETTER
    public float getWidth(){
        return this.width*this.scale[0];
    }
    public int getWidth(int scale){
        return (int) (this.width*this.scale[0] * scale);
    }
    
    public float getHeight(){
        return this.height*this.scale[1];
    }
    public int getHeight(int scale){
        return (int) (this.height*this.scale[1] * scale);
    }
    
    public void setWidth(float newWidth)
    {
        //
        width = newWidth;
    }
    
    public void setHeight(float newHeight)
    {
        //
        height = newHeight;
    }
    
    public float[][] getCornerPoints(){
        recalcPoints();
        return this.cornerPoints;
    }
    
    public void setScale(float[] scale){
        this.scale = scale;
        recalcPoints();
    }
    
    public void rescale(){
        this.width = this.width * this.scale[0];
        this.height = this.height * this.scale[1];
        this.scale[0] = 1.0f;
        this.scale[1] = 1.0f;
    }
}