
package simulationtest;

import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Level implements Serializable{
    ArrayList<ForceField> forceFields = new ArrayList<ForceField>();
    ArrayList<Sphere> sceneSpheres = new ArrayList<Sphere>();
    ArrayList<Quader> sceneQuboids = new ArrayList<Quader>();
    
    float[] targetPosition = {0.0f,0.0f};
    float targetWidth = 0.0f;
    float targetHeight = 0.0f;
    
    boolean finished = false;

    public Level(){
    }
    
    public void setTargetPosition(float[] pos){
        this.targetPosition = pos;
    }
    
    public void setTargetPosition(float posX, float posY){
        this.targetPosition[0] = posX;
        this.targetPosition[1] = posY;
    }
    
    public void setTargetPosition(float posX, float posY, int scale){
        this.targetPosition[0] = posX/scale;
        this.targetPosition[1] = posY/scale;
    }
    
    public void setWidth(float width){
        this.targetWidth = width;
    }
    
    public void setWidth(float width, int scale){
        this.targetWidth = width/scale;
    }
    
    public void setHeight(float height){
        this.targetHeight = height;
    }
    
    public void setHeight(float height, int scale){
        this.targetHeight = height/scale;
    }
    
    public void setFinished(boolean fin){
        this.finished = fin;
    }
    
    public void setForcefields(ArrayList<ForceField> forceFields){
        this.forceFields = forceFields;        
    }
    
    public void setSceneSpheres(ArrayList<Sphere> sceneSpheres){
        this.sceneSpheres = sceneSpheres;
    }
    
    public void setSceneQuboids(ArrayList<Quader> sceneQuboids){
        this.sceneQuboids = sceneQuboids;
    }
    
    
    public ArrayList<ForceField> getForcefields(){
        return this.forceFields;
    }
    
    public ArrayList<Sphere> getSceneSpheres(){
        return this.sceneSpheres;
    }
    
    public ArrayList<Quader> getSceneQuboids(){
        return this.sceneQuboids;
    }
    
    public float[] getTargetPosition(){
        return this.targetPosition;
    }
    
    public float setTargetPositionX(int scale){
        return this.targetPosition[0]*scale;
    }
    
    public float setTargetPositionY(int scale){
        return this.targetPosition[1]*scale;
    }
    
    public float getWidth(){
        return this.targetWidth;
    }
    
    public float getWidth(int scale){
        return this.targetWidth/scale;
    }
    
    public float getHeight(){
        return this.targetHeight;
    }
    
    public float getHeight(int scale){
        return this.targetHeight/scale;
    }
    
    public boolean getFinished(){
        return this.finished;
    }
    
    public int getDrawTargetX(int scale){
        return (int)(this.targetPosition[0]*scale - 100);
    }
    
    public int getDrawTargetY(int scale){
        return (int)(this.targetPosition[1]*scale-(200-21));
    }
}
