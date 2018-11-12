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
//Klasse Polygon
    public class PolyShape extends Objekt {
       private float [][] points;

//Konstruktor
        public PolyShape()
        {

        }
        
//GETTER UND SETTER
    public float [][] getPoints()
    {
        return points;
    } 

    public void setPoints(float [][] p)
    {
        // ansonsten neuerRadius = 10
        points = p;
    }        
        
    }