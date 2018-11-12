/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package madphysics;

/**
 *
 * @author M.Kaur
 */
public class Keyframe {
    float [][] position;
    float [][] scale;
    float time;
    float rotation;
    
//Konstruktor
    public Keyframe()
    {
        
    }
    
//GETTER UND SETTER
    public float [][] getPosition()
    {
        return position;
    }
    
    public void setPosition(float [][] pos)
    {
        position = pos;
    }
}
