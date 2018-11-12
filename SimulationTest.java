/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulationtest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Jan-Laptop
 */
public class SimulationTest extends Application {
    
    int screenScaleFactor = 100;
    
    Thread simThread;
    
    boolean isSimulating = false;
    
    
    boolean objectJustSelected = false;
    
    ArrayList<ForceField> forceFields = new ArrayList<ForceField>();
    ArrayList<Rectangle> drawForceFields = new ArrayList<Rectangle>();
    
    ArrayList<Sphere> sceneSpheres = new ArrayList<Sphere>();
    ArrayList<Circle> drawCircles = new ArrayList<Circle>();
    
    ArrayList<Quader> sceneQuboids = new ArrayList<Quader>();
    ArrayList<Rectangle> drawQuboids = new ArrayList<Rectangle>();
    
    ArrayList<SphereSphereCollision> sphereSphereCollisions = new ArrayList<SphereSphereCollision>();
    ArrayList<SphereQuboidCollision> sphereQuboidCollisions = new ArrayList<SphereQuboidCollision>();
    
    Level level = new Level();
    
    Image targetImage;
    ImageView targetView = new ImageView();
    
    Objekt selectedSphere;
    //Quader selectedQuboid;
    
    Circle futureSphere = null;
    Rectangle futureQuboid = null;
    
    Parent root;
    Parent sphereProp;
    Parent quboidProp;
    Parent polygonProp;
    Parent forceProp;
    
    Stage winningStage;
    
    @FXML
    private void saveLevel (ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();      
        File file = fileChooser.showSaveDialog(null);
        String path = file.getAbsolutePath();
        
        //listen in die level klasse schreiben...
        level.setForcefields(forceFields);
        level.setSceneSpheres(sceneSpheres);
        level.setSceneQuboids(sceneQuboids);
        
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path))) {
            out.writeObject(level);
            System.out.println("Serialization succeeded");
            System.out.println();
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Serialization failed");
            System.out.println();
        }
    }
    
    @FXML
    private void showLevelDialog (ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();      
        File file = fileChooser.showOpenDialog(null);
        String path = file.getAbsolutePath();
        
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))) {
            level = (Level) in.readObject();
            System.out.println("Serialization succeeded");
            System.out.println();
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Serialization failed");
            System.out.println();
        }
        
        isSimulating = false;
        
        forceFields = level.getForcefields();
        sceneSpheres = level.getSceneSpheres();
        sceneQuboids = level.getSceneQuboids();
        
        drawForceFields.clear();
        drawCircles.clear();
        drawQuboids.clear();
        
        objekte.getChildren().clear();
        selectedSphere = null;
        
        transformXhandle.setVisible(false);
        transformYhandle.setVisible(false);
        transformZhandle.setVisible(false);
        scaleXhandle.setVisible(false);
        scaleYhandle.setVisible(false);
        scaleZhandle.setVisible(false);
        rotateCenter.setVisible(false);
        rotateHandle.setVisible(false);
        
        for(int i = 0; i < forceFields.size(); i++){
            System.out.println(forceFields.get(i).toString());
            Rectangle field = new Rectangle(forceFields.get(i).getWidth(screenScaleFactor), forceFields.get(i).getHeight(screenScaleFactor), Color.web("#ff0000"));
            field.setStrokeWidth(3.0);
            field.setFill(Color.TRANSPARENT);
            field.setStroke(Color.RED);
            field.setX(forceFields.get(i).getPositionX(screenScaleFactor));
            field.setY(forceFields.get(i).getPositionY(screenScaleFactor));
            drawForceFields.add(field);
        }
        
        for(int i = 0; i < sceneSpheres.size(); i++){
            Circle circle = new Circle(sceneSpheres.get(sceneSpheres.size()-1).getRadius(screenScaleFactor), Color.web("#606060"));
            circle.setStrokeType(StrokeType.CENTERED);
            circle.setStrokeWidth(0.0);
            circle.setTranslateX(sceneSpheres.get(sceneSpheres.size()-1).getPositionX(screenScaleFactor));
            circle.setTranslateY(sceneSpheres.get(sceneSpheres.size()-1).getPositionY(screenScaleFactor));

            circle.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    int index = drawCircles.indexOf(event.getSource());
                    System.out.println(index + "th sphere selected");
                    selectedSphere = sceneSpheres.get(index);

                    propertiesPane.getChildren().clear();
                    propertiesPane.getChildren().add(sphereProp);
                    //cbMaterial.setItems(selectedSphere.getMaterialien());
                    txtSphereTransformX.setText(""+selectedSphere.getPositionX());
                    txtSphereTransformY.setText(""+selectedSphere.getPositionY());
                    objectJustSelected = true;
                }
            });

            drawCircles.add(circle);

            objekte.getChildren().add(drawCircles.get(drawCircles.size()-1));
            
            targetView.setX(level.getDrawTargetX(screenScaleFactor));
            targetView.setY(level.getDrawTargetY(screenScaleFactor));
            objekte.getChildren().add(targetView);

            System.out.println("sphere created");
        }
        
        for(int i = 0; i < sceneQuboids.size(); i++){
            Rectangle cuboid = new Rectangle(sceneQuboids.get(sceneQuboids.size()-1).getWidth(screenScaleFactor), sceneQuboids.get(sceneQuboids.size()-1).getHeight(screenScaleFactor), Color.web("#a0a0a0"));
            cuboid.setX(sceneQuboids.get(sceneQuboids.size()-1).getPositionX(screenScaleFactor) - sceneQuboids.get(sceneQuboids.size()-1).getWidth(screenScaleFactor)/2);
            cuboid.setY(sceneQuboids.get(sceneQuboids.size()-1).getPositionY(screenScaleFactor) - sceneQuboids.get(sceneQuboids.size()-1).getHeight(screenScaleFactor)/2);
            cuboid.setRotate((double)sceneQuboids.get(sceneQuboids.size()-1).getRotation());
            cuboid.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    int index = drawQuboids.indexOf(event.getSource());
                    System.out.println(index + "th quboid selected");
                    selectedSphere = sceneQuboids.get(index);
                    
                    propertiesPane.getChildren().clear();
                    //propertiesPane.getChildren().add(quboidProp);
                    objectJustSelected = true;
                }
            });
            drawQuboids.add(cuboid);
            objekte.getChildren().add(drawQuboids.get(drawQuboids.size()-1));
            System.out.println("quboid created");
        }
        
    }

    
    boolean transformMode = true;
    boolean scaleMode = false;
    boolean rotateMode = false;
    
    @FXML
    Button btnTransform;
    @FXML
    Button btnScale;
    @FXML
    Button btnRotate;
    
    @FXML
    private void handleTransformButton(ActionEvent event){
        System.out.println("transform mode...");
        transformMode = true;
        scaleMode = false;
        rotateMode = false;
        scaleXhandle.setVisible(false);
        scaleYhandle.setVisible(false);
        scaleZhandle.setVisible(false);
        rotateCenter.setVisible(false);
        rotateHandle.setVisible(false);
    }
    @FXML
    private void handleScaleButton(ActionEvent event){
        System.out.println("scale mode...");
        transformMode = false;
        scaleMode = true;
        rotateMode = false;
        transformXhandle.setVisible(false);
        transformYhandle.setVisible(false);
        transformZhandle.setVisible(false);
        rotateCenter.setVisible(false);
        rotateHandle.setVisible(false);
    }
    @FXML
    private void handleRotateButton(ActionEvent event){
        System.out.println("rotate mode...");
        transformMode = false;
        scaleMode = false;
        rotateMode = true;
        transformXhandle.setVisible(false);
        transformYhandle.setVisible(false);
        transformZhandle.setVisible(false);
        scaleXhandle.setVisible(false);
        scaleYhandle.setVisible(false);
        scaleZhandle.setVisible(false);
    }
    
    Image transformXimage;
    ImageView transformXhandle = new ImageView();
    Image transformYimage;
    ImageView transformYhandle = new ImageView();
    Rectangle transformZhandle = new Rectangle();
    
    Image scaleXimage;
    ImageView scaleXhandle = new ImageView();
    Image scaleYimage;
    ImageView scaleYhandle = new ImageView();
    Rectangle scaleZhandle = new Rectangle();
    
    Image rotateImage;
    ImageView rotateHandle = new ImageView();
    Circle rotateCenter = new Circle();
    
    Image woodTextureImage;
    Image steelTextureImage;
    Image concreteTextureImage;
    Image rubberTextureImage;
    
    @FXML
    Button btnSphereApply;
    @FXML
    Button btnQuboidApply;
    
    @FXML
    private void applySphereChanges(ActionEvent event){
        if(selectedSphere != null){
            float[] pos = {Float.parseFloat(txtSphereTransformX.getText()),Float.parseFloat(txtSphereTransformY.getText())};
            selectedSphere.setPosition(pos);
            float rad = Float.parseFloat(txtSphereRadius.getText());
            //selectedSphere.setRadius(rad);
            selectedSphere.setRotation(Float.parseFloat(txtSphereRotation.getText()));
            String mat = cbSphereMaterial.getValue();
            selectedSphere.setMaterial(mat);
            if(mat == "wood"){
                ImagePattern texture = new ImagePattern(woodTextureImage, 0,0,1,1,true);
                drawCircles.get(sceneSpheres.indexOf(selectedSphere)).setFill(texture);
            } else if(mat == "steel"){
                ImagePattern texture = new ImagePattern(steelTextureImage, 0,0,1,1,true);
                drawCircles.get(sceneSpheres.indexOf(selectedSphere)).setFill(texture);
            } else if(mat == "concrete"){
                ImagePattern texture = new ImagePattern(concreteTextureImage, 0,0,1,1,true);
                drawCircles.get(sceneSpheres.indexOf(selectedSphere)).setFill(texture);
            } else if(mat == "rubber"){
                ImagePattern texture = new ImagePattern(rubberTextureImage, 0,0,1,1,true);
                drawCircles.get(sceneSpheres.indexOf(selectedSphere)).setFill(texture);
            }
        }
    }
    
    @FXML
    private void applyQuboidChanges(ActionEvent event){
        if(selectedSphere != null){
            float[] pos = {Float.parseFloat(txtQuboidTransformX.getText()),Float.parseFloat(txtQuboidTransformY.getText())};
            selectedSphere.setPosition(pos);
            selectedSphere.setRotation(Float.parseFloat(txtQuboidRotation.getText()));
            String mat = cbQuboidMaterial.getValue();
            selectedSphere.setMaterial(mat);
            if(mat == "wood"){
                ImagePattern texture = new ImagePattern(woodTextureImage, 0,0,1,1,true);
                drawQuboids.get(sceneQuboids.indexOf(selectedSphere)).setFill(texture);
            } else if(mat == "steel"){
                ImagePattern texture = new ImagePattern(steelTextureImage, 0,0,1,1,true);
                drawQuboids.get(sceneQuboids.indexOf(selectedSphere)).setFill(texture);
            } else if(mat == "concrete"){
                ImagePattern texture = new ImagePattern(concreteTextureImage, 0,0,1,1,true);
                drawQuboids.get(sceneQuboids.indexOf(selectedSphere)).setFill(texture);
            } else if(mat == "rubber"){
                ImagePattern texture = new ImagePattern(rubberTextureImage, 0,0,1,1,true);
                drawQuboids.get(sceneQuboids.indexOf(selectedSphere)).setFill(texture);
            }
        }
    }
    
    @FXML
    private void applyForceChanges(ActionEvent event){
        if(selectedSphere != null){
            float[] pos = {Float.parseFloat(txtForceTransformX.getText()),Float.parseFloat(txtForceTransformY.getText())};
            selectedSphere.setPosition(pos);
            float[] acc = {Float.parseFloat(txtForceAccX.getText()),Float.parseFloat(txtForceAccY.getText())};
            selectedSphere.setAcceleration(acc[0], acc[1]);
        }
    }
    
    @FXML
    private void deleteSphere(ActionEvent event){
        int index = 0;
        index = sceneSpheres.indexOf(selectedSphere);
        objekte.getChildren().remove(drawCircles.get(index));
        sceneSpheres.remove(index);
        drawCircles.remove(index);
        transformXhandle.setVisible(false);
        transformYhandle.setVisible(false);
        transformZhandle.setVisible(false);
        scaleXhandle.setVisible(false);
        scaleYhandle.setVisible(false);
        scaleZhandle.setVisible(false);
        rotateCenter.setVisible(false);
        rotateHandle.setVisible(false);
        propertiesPane.getChildren().clear();
    }
    
    @FXML
    private void deleteQuboid(ActionEvent event){
        int index = 0;
        index = sceneQuboids.indexOf(selectedSphere);
        objekte.getChildren().remove(drawQuboids.get(index));
        sceneQuboids.remove(index);
        drawQuboids.remove(index);
        transformXhandle.setVisible(false);
        transformYhandle.setVisible(false);
        transformZhandle.setVisible(false);
        scaleXhandle.setVisible(false);
        scaleYhandle.setVisible(false);
        scaleZhandle.setVisible(false);
        rotateCenter.setVisible(false);
        rotateHandle.setVisible(false);
        propertiesPane.getChildren().clear();
    }
    
    @FXML
    ComboBox<String> cbSphereMaterial;
    /**COMBOBOX SPHEREMATERIAL**/
    ObservableList<String> Spherelist = FXCollections.observableArrayList("wood", "steel", "concrete", "rubber");
    
           @FXML
    ComboBox<String> cbQuboidMaterial;
    /**COMBOBOX QUBOIDMATERIAL**/
    ObservableList<String> Quboidlist = FXCollections.observableArrayList("wood", "steel", "concrete", "rubber");
    
    @FXML
    TextField txtSphereTransformX;
    @FXML
    TextField txtSphereTransformY;
    @FXML
    TextField txtSphereRadius;
    @FXML
    TextField txtSphereRotation;
    @FXML
    Label lblSphereMass;
    
    @FXML
    TextField txtQuboidTransformX;
    @FXML
    TextField txtQuboidTransformY;
    @FXML
    TextField txtQuboidScaleX;
    @FXML
    TextField txtQuboidScaleY;
    @FXML
    TextField txtQuboidRotation;
    
    @FXML
    TextField txtForceTransformX;
    @FXML
    TextField txtForceTransformY;
    @FXML
    TextField txtForceScaleX;
    @FXML
    TextField txtForceScaleY;
    @FXML
    TextField txtForceAccX;
    @FXML
    TextField txtForceAccY;
    
    @FXML
    FlowPane flowPaneLeft;
    
    @FXML
    FlowPane flowPaneTop;
    
    @FXML
    Pane simPane;
    
    @FXML
    Pane propertiesPane;
    
    @FXML
    private void playSimAction(ActionEvent event) {
        isSimulating = true;
        System.out.println("play simulation...");
    }
    @FXML
    private void pauseSimAction(ActionEvent event) {
        isSimulating = false;
//        for (int i = 0; i < sceneSpheres.size(); i++){
//            System.out.println("startPos: x: " + sceneSpheres.get(i).getStartPosition()[0] + " y: " + sceneSpheres.get(i).getStartPosition()[1]);
//            sceneSpheres.get(i).setPosition(sceneSpheres.get(i).getStartPosition());
//            //sceneSpheres.get(i).setPositionOld(sceneSpheres.get(i).getStartPosition());
//            sceneSpheres.get(i).setVelocity(sceneSpheres.get(i).getStartVelocity());
//            System.out.println("position reset...");
//        }
        System.out.println("pause simulation...");
    }
    
    @FXML
    Button btnSphere;
    
    @FXML
    Button btnQuboid;
    
    @FXML
    private void handleSphereDragDetected(MouseEvent event){
        System.out.println("Sphere is being dragged...");
        Dragboard db = btnSphere.startDragAndDrop(TransferMode.ANY);
        
        ClipboardContent content = new ClipboardContent();
        content.putString("newSphere");
        db.setContent(content);
        futureSphere = new Circle();
        futureSphere.setRadius(50);
        futureSphere.setFill(Color.rgb(0,0,0,0.5));
        objekte.getChildren().add(futureSphere);
        event.consume();
    }
    
    @FXML
    private void handleQuboidDragDetected(MouseEvent event){
        System.out.println("Quboid is being dragged...");
        Dragboard db = btnQuboid.startDragAndDrop(TransferMode.ANY);
                
        ClipboardContent content = new ClipboardContent();
        content.putString("newQuboid");
        db.setContent(content);
        event.consume();
    }
    
    @FXML
    private void handleGravityDragDetected(MouseEvent event){
        System.out.println("GravityField is being dragged...");
        Dragboard db = btnQuboid.startDragAndDrop(TransferMode.ANY);
                
        ClipboardContent content = new ClipboardContent();
        content.putString("newGravity");
        db.setContent(content);
        event.consume();
    }
    
    @FXML
    private void handleForceDragDetected(MouseEvent event){
        System.out.println("ForceField is being dragged...");
        Dragboard db = btnQuboid.startDragAndDrop(TransferMode.ANY);
                
        ClipboardContent content = new ClipboardContent();
        content.putString("newForce");
        db.setContent(content);
        event.consume();
    }
    
    @FXML
    private void handleSimDragOver(DragEvent event){
        if (event.getGestureSource() != simPane &&
            event.getDragboard().hasString()) {
            /* allow for both copying and moving, whatever user chooses */
            event.acceptTransferModes(TransferMode.ANY);
            if(event.getDragboard().getString().equals("newSphere")){
                int x = (int)(event.getSceneX()-flowPaneLeft.getPrefWidth());
                int y = (int)(event.getSceneY()-flowPaneTop.getPrefHeight());
                futureSphere.setTranslateX(x);
                futureSphere.setTranslateY(y);
            }
            if(selectedSphere != null){
                if(transformMode){
                    if(event.getDragboard().getString().substring(0, 10).equals("transformX")){
                        int offset = Integer.parseInt(event.getDragboard().getString().substring(10));
                        int x = (int)(event.getSceneX()-flowPaneLeft.getPrefWidth()-offset);
                        int y = (int)(selectedSphere.getPositionY(screenScaleFactor));
                        float[] pos = {(float)x/screenScaleFactor,(float)y/screenScaleFactor};
                        selectedSphere.setPosition(pos);
                    } 
                    if(event.getDragboard().getString().substring(0, 10).equals("transformY")){
                        int offset = Integer.parseInt(event.getDragboard().getString().substring(10));
                        int x = (int)(selectedSphere.getPositionX(screenScaleFactor));
                        int y = (int)(event.getSceneY()-flowPaneTop.getPrefHeight()-offset);
                        float[] pos = {(float)x/screenScaleFactor,(float)y/screenScaleFactor};
                        selectedSphere.setPosition(pos);
                    }
                    if(event.getDragboard().getString().substring(0, 10).equals("transformZ")){
                        int offsetX = Integer.parseInt(event.getDragboard().getString().substring(10, 14));
                        int offsetY = Integer.parseInt(event.getDragboard().getString().substring(14));
                        int x = (int)(event.getSceneX()-flowPaneLeft.getPrefWidth()-offsetX);
                        int y = (int)(event.getSceneY()-flowPaneTop.getPrefHeight()-offsetY);
                        float[] pos = {(float)x/screenScaleFactor,(float)y/screenScaleFactor};
                        selectedSphere.setPosition(pos);
                    }
                }
                if(scaleMode){
                    if(selectedSphere.getType().equals("sphere")){
                        if(event.getDragboard().getString().substring(0, 6).equals("scaleX") || event.getDragboard().getString().substring(0, 6).equals("scaleY")){
                            int offset = Integer.parseInt(event.getDragboard().getString().substring(6));
                            float[] distance = {(float)(event.getSceneX()-flowPaneLeft.getPrefWidth())-selectedSphere.getPositionX(screenScaleFactor), (float)(event.getSceneY()-flowPaneTop.getPrefHeight())-selectedSphere.getPositionY(screenScaleFactor)};
                            float distanceLen = (float)Math.sqrt(distance[0]*distance[0]+distance[1]*distance[1]);
                            float factor = (distanceLen/offset);
                            float[] scale = {Math.abs(factor), Math.abs(factor)};
                            selectedSphere.setScale(scale);
                            System.out.println("scale: x: " + selectedSphere.getScale()[0] + " y: " + selectedSphere.getScale()[1]);
                        }
                        if(event.getDragboard().getString().substring(0, 6).equals("scaleZ")){
                            int offsetX = Integer.parseInt(event.getDragboard().getString().substring(6, 10));
                            int offsetY = Integer.parseInt(event.getDragboard().getString().substring(10));
                            float[] distance = {(float)(event.getSceneX()-flowPaneLeft.getPrefWidth())-selectedSphere.getPositionX(screenScaleFactor), (float)(event.getSceneY()-flowPaneTop.getPrefHeight())-selectedSphere.getPositionY(screenScaleFactor)};
                            float distanceLen = (float)Math.sqrt(distance[0]*distance[0]+distance[1]*distance[1]);
                            float factor = 1.0f;
                            if(Math.abs(offsetX) > Math.abs(offsetY)){
                                factor = (distanceLen/Math.abs(offsetX));
                            } else {
                                factor = (distanceLen/Math.abs(offsetY));
                            }
                            float[] scale = {factor, factor};
                            selectedSphere.setScale(scale);
                            System.out.println("scale: x: " + selectedSphere.getScale()[0] + " y: " + selectedSphere.getScale()[1]);
                        }
                    }
                    if(selectedSphere.getType().equals("Quader") || selectedSphere.getType().equals("force")){
                        if(event.getDragboard().getString().substring(0, 6).equals("scaleX")){
                            int offset = Integer.parseInt(event.getDragboard().getString().substring(6));
                            float distanceLen = (float)(event.getSceneX()-flowPaneLeft.getPrefWidth())-selectedSphere.getPositionX(screenScaleFactor);
                            float factor = (distanceLen/offset);
                            float[] scale = {factor, selectedSphere.getScale()[1]};
                            selectedSphere.setScale(scale);
                            System.out.println("scale: x: " + selectedSphere.getScale()[0] + " y: " + selectedSphere.getScale()[1]);
                        }
                        if(event.getDragboard().getString().substring(0, 6).equals("scaleY")){
                            int offset = Integer.parseInt(event.getDragboard().getString().substring(6));
                            float distanceLen = (float)(event.getSceneY()-flowPaneLeft.getPrefWidth())-selectedSphere.getPositionY(screenScaleFactor);
                            float factor = (distanceLen/offset)*selectedSphere.getScale()[0];
                            float[] scale = {selectedSphere.getScale()[0], factor};
                            selectedSphere.setScale(scale);
                            System.out.println("scale: x: " + selectedSphere.getScale()[0] + " y: " + selectedSphere.getScale()[1]);
                        }
                    }
                }
                if(rotateMode){
                    if(selectedSphere.getType().equals("force") != true){
                        if(event.getDragboard().getString().substring(0, 6).equals("rotate")){
                            int offsetX = Integer.parseInt(event.getDragboard().getString().substring(6, 10));
                            int offsetY = Integer.parseInt(event.getDragboard().getString().substring(10));
                            float[] offVec = {(float)offsetX, (float)offsetY};
                            float offVecLen = (float)Math.sqrt(offVec[0]*offVec[0]+offVec[1]*offVec[1]);
                            float[] distVec = {(float)(event.getSceneX()-flowPaneLeft.getPrefWidth())-selectedSphere.getPositionX(screenScaleFactor), (float)(event.getSceneY()-flowPaneTop.getPrefHeight())-selectedSphere.getPositionY(screenScaleFactor)};
                            float distVecLen = (float)Math.sqrt(distVec[0]*distVec[0]+distVec[1]*distVec[1]);
                            float alpha = getAngle(offVec, distVec);
                            distVec[0] = distVec[0]/distVecLen*offVecLen;
                            distVec[1] = distVec[1]/distVecLen*offVecLen;
                            if(distVec[0]<offVec[0] && distVec[1]<(-offVec[1])){
                                selectedSphere.setTempRotation(-alpha);
                            } else {
                                selectedSphere.setTempRotation(alpha);
                            }
                        }
                    }
                }
            }
        }
        
        event.consume();
    }
    
    @FXML
    private void handleSimDragDropped(DragEvent event){
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasString()) {
            System.out.println(db.getString());
            if (db.getString() == "newSphere"){
                int x = (int)(event.getSceneX()-flowPaneLeft.getPrefWidth());
                int y = (int)(event.getSceneY()-flowPaneTop.getPrefHeight());
                createSphere(x, y);
                objekte.getChildren().remove(futureSphere);
            }
            if (db.getString() == "newQuboid"){
                int x = (int)(event.getSceneX()-flowPaneLeft.getPrefWidth());
                int y = (int)(event.getSceneY()-flowPaneTop.getPrefHeight());
                createQuboid(x, y);
            }
            if (db.getString() == "newGravity"){
                createGravityField();
            }
            if (db.getString() == "newForce"){
                int x = (int)(event.getSceneX()-flowPaneLeft.getPrefWidth());
                int y = (int)(event.getSceneY()-flowPaneTop.getPrefHeight());
                createForceField(x-100, y-75, 200, 150, 0f, 0f);
            }
            if(selectedSphere != null){
                if(db.getString().substring(0, 5).equals("scale")){
                    selectedSphere.rescale();
                }
                if(db.getString().substring(0, 6).equals("rotate")){
                    selectedSphere.setRotation(selectedSphere.getRotation()+selectedSphere.getTempRotation());
                    selectedSphere.setTempRotation(0.0f);
                }
            }
           success = true;
        }        
        event.setDropCompleted(success);
        event.consume();
    }
    
    @FXML
    ScrollPane scrollPane;
    
    Group simulationArea = new Group();
    Group szenerie = new Group();
    Group objekte = new Group();
    Group handles = new Group();
    
    @Override
    public void start(Stage primaryStage) {
        simThread = new Thread() {
            public void run() {
                long lastTimeStamp = System.currentTimeMillis();
                while(true){
                    objectJustSelected = false;
                    long timeStamp = System.currentTimeMillis();
                    float deltaT = ((float)(timeStamp - lastTimeStamp))/1000;
                    if (deltaT <= 0.005){
                        try {
                            Thread.sleep(5);
                            timeStamp = System.currentTimeMillis();
                            deltaT = ((float)(timeStamp - lastTimeStamp))/1000;
                        } catch (Exception e){
                        }
                    }
                    lastTimeStamp = timeStamp;
                    
//                    if(scrollPane != null){
//                        simPane.setTranslateX((scrollPane.getWidth()-simPane.getWidth())/2);
//                        simPane.setTranslateY((scrollPane.getHeight()-simPane.getHeight())/2);
//                    }

                    for (int i = 0; i < drawForceFields.size(); i++){
                        drawForceFields.get(i).setX(forceFields.get(i).getPositionX(screenScaleFactor));
                        drawForceFields.get(i).setY(forceFields.get(i).getPositionY(screenScaleFactor));
                        drawForceFields.get(i).setWidth(forceFields.get(i).getWidth(screenScaleFactor));
                        drawForceFields.get(i).setHeight(forceFields.get(i).getHeight(screenScaleFactor));
                    }
                    for (int i = 0; i < drawCircles.size(); i++){
                        drawCircles.get(i).setTranslateX(sceneSpheres.get(i).getPositionX(screenScaleFactor));
                        drawCircles.get(i).setTranslateY(sceneSpheres.get(i).getPositionY(screenScaleFactor));
                        drawCircles.get(i).setRadius(sceneSpheres.get(i).getRadius(screenScaleFactor));
                        drawCircles.get(i).setRotate(sceneSpheres.get(i).getRotation());
                    }
                    for (int i = 0; i < drawQuboids.size(); i++){
                        drawQuboids.get(i).setX(sceneQuboids.get(i).getPositionX(screenScaleFactor) - sceneQuboids.get(i).getWidth(screenScaleFactor)/2);
                        drawQuboids.get(i).setY(sceneQuboids.get(i).getPositionY(screenScaleFactor) - sceneQuboids.get(i).getHeight(screenScaleFactor)/2);
                        drawQuboids.get(i).setWidth(sceneQuboids.get(i).getWidth(screenScaleFactor));
                        drawQuboids.get(i).setHeight(sceneQuboids.get(i).getHeight(screenScaleFactor));
                        drawQuboids.get(i).setRotate((double)(sceneQuboids.get(i).getRotation()+sceneQuboids.get(i).getTempRotation()));
                    }
                    
                    if(selectedSphere != null){
                        if(transformMode){
                            transformXhandle.setVisible(true);
                            transformYhandle.setVisible(true);
                            transformZhandle.setVisible(true);
                            transformXhandle.setX(selectedSphere.getPositionX(screenScaleFactor)+10);
                            transformXhandle.setY(selectedSphere.getPositionY(screenScaleFactor)-20);
                            transformYhandle.setX(selectedSphere.getPositionX(screenScaleFactor)-20);
                            transformYhandle.setY(selectedSphere.getPositionY(screenScaleFactor)-110);
                            transformZhandle.setX(selectedSphere.getPositionX(screenScaleFactor)-10);
                            transformZhandle.setY(selectedSphere.getPositionY(screenScaleFactor)-10);
                        }
                        if(scaleMode){
                            scaleXhandle.setVisible(true);
                            scaleYhandle.setVisible(true);
                            scaleZhandle.setVisible(true);
                            scaleXhandle.setX(selectedSphere.getPositionX(screenScaleFactor)+10);
                            scaleXhandle.setY(selectedSphere.getPositionY(screenScaleFactor)-20);
                            scaleYhandle.setX(selectedSphere.getPositionX(screenScaleFactor)-20);
                            scaleYhandle.setY(selectedSphere.getPositionY(screenScaleFactor)-110);
                            scaleZhandle.setX(selectedSphere.getPositionX(screenScaleFactor)-10);
                            scaleZhandle.setY(selectedSphere.getPositionY(screenScaleFactor)-10);
                        }
                        if(rotateMode){
                            rotateCenter.setVisible(true);
                            rotateHandle.setVisible(true);
                            rotateCenter.setTranslateX(selectedSphere.getPositionX(screenScaleFactor));
                            rotateCenter.setTranslateY(selectedSphere.getPositionY(screenScaleFactor));
                            rotateHandle.setX(selectedSphere.getPositionX(screenScaleFactor)-33);
                            rotateHandle.setY(selectedSphere.getPositionY(screenScaleFactor)-67);
                        }
                    }

                    if (isSimulating == true){
                        //target collision
                        for (int i = 0; i < sceneSpheres.size(); i++) {
                            if(sceneSpheres.get(i).getPosition()[0] >= level.getTargetPosition()[0]-level.getWidth()/2 && sceneSpheres.get(i).getPosition()[0] <= level.getTargetPosition()[0]+level.getWidth()/2){
                                if(sceneSpheres.get(i).getPosition()[1] >= level.getTargetPosition()[1]-level.getHeight()/2 && sceneSpheres.get(i).getPosition()[1] <= level.getTargetPosition()[1]+level.getHeight()/2){
                                    level.setFinished(true);
                                    isSimulating = false;
                                    System.out.println("################################################");
                                    System.out.println("##############                     #############");
                                    System.out.println("##############   |-------------|   #############");
                                    System.out.println("##############   |  Gewonnen!  |   #############");
                                    System.out.println("##############   |-------------|   #############");
                                    System.out.println("##############                     #############");
                                    System.out.println("################################################");
                                    
                                    winningStage.show();
                                }
                            }
                        }
                        
                        
                        //sphere collision 
                        for (int i = 0; i < sceneSpheres.size(); i++) {
                            for (int j = i + 1; j < sceneSpheres.size(); j++) {
                                float[] distance_vector = {sceneSpheres.get(i).getPositionX() - sceneSpheres.get(j).getPositionX(), sceneSpheres.get(i).getPositionY() - sceneSpheres.get(j).getPositionY()};
                                float distance_length = (float)Math.sqrt((distance_vector[0]*distance_vector[0] + distance_vector[1]*distance_vector[1]));
                                if(distance_length < sceneSpheres.get(i).getRadius() + sceneSpheres.get(j).getRadius()) {          
                                    System.out.println("sphere collision...");
                                    sphereSphereCollisions.add(new SphereSphereCollision(sceneSpheres.get(i), sceneSpheres.get(j)));
                                }
                            }
                        }
                        for (int i = 0; i < sphereSphereCollisions.size(); i++){
                            sphereSphereCollisions.get(i).collide();
                        }
                        //sphereSphereCollisions.clear();
                        
                        //sphere-cuboid collision
                        for (int i = 0; i < sceneSpheres.size(); i++) {
                            for (int j = 0; j < sceneQuboids.size(); j++) {
                                float radius = sceneSpheres.get(i).getRadius();
                                float[] vectorR = {sceneSpheres.get(i).getPositionX() - sceneQuboids.get(j).getPositionX(), sceneSpheres.get(i).getPositionY() - sceneQuboids.get(j).getPositionY()};
                                vectorR = rotate2d(vectorR, 360 - sceneQuboids.get(j).getRotation());
                                vectorR[0] = vectorR[0] + sceneQuboids.get(j).getPositionX();
                                vectorR[1] = vectorR[1] + sceneQuboids.get(j).getPositionY();
                                
                                float[] a = {sceneQuboids.get(j).getCornerPoints()[0][0],sceneQuboids.get(j).getCornerPoints()[0][1]};
                                float[] b = {sceneQuboids.get(j).getCornerPoints()[1][0],sceneQuboids.get(j).getCornerPoints()[1][1]};
                                float[] c = {sceneQuboids.get(j).getCornerPoints()[2][0],sceneQuboids.get(j).getCornerPoints()[2][1]};
                                float[] d = {sceneQuboids.get(j).getCornerPoints()[3][0],sceneQuboids.get(j).getCornerPoints()[3][1]};
                                

                                if ((a[1] <= vectorR[1]) && (vectorR[1] <= d[1])) {
                                    if (((a[0] - radius) <= vectorR[0]) && (vectorR[0] <= a[0]-radius*0.9)) {
                                        //System.out.println("collision at D");
                                        sphereQuboidCollisions.add(new SphereQuboidCollision(sceneSpheres.get(i), sceneQuboids.get(j), "a", "d"));
                                    }
                                    if ((b[0]+radius*0.9 <= vectorR[0]) && (vectorR[0] <= (b[0] + radius))) {
                                        //System.out.println("collision at B");
                                        sphereQuboidCollisions.add(new SphereQuboidCollision(sceneSpheres.get(i), sceneQuboids.get(j), "b", "c"));
                                    }
                                } else if ((a[0] <= vectorR[0]) && (vectorR[0] <= b[0])) {
                                    if (((a[1] - radius) <= vectorR[1]) && (vectorR[1] <= (a[1]-radius*0.9))) {
                                        //System.out.println("collision at A");
                                        sphereQuboidCollisions.add(new SphereQuboidCollision(sceneSpheres.get(i), sceneQuboids.get(j), "a", "b"));
                                    }
                                    if ((d[1]+radius*0.9 <= vectorR[1]) && (vectorR[1] <= (d[1] + radius))) {
                                        //System.out.println("collision at C");
                                        sphereQuboidCollisions.add(new SphereQuboidCollision(sceneSpheres.get(i), sceneQuboids.get(j), "c", "d"));
                                    }
                                } else {
                                    float distance = (float) Math.sqrt((vectorR[0]-a[0])*(vectorR[0]-a[0]) + (vectorR[1]-a[1])*(vectorR[1]-a[1]));
                                    if (distance <= radius*0.9){
                                         //System.out.println("collision at a");
                                         sphereQuboidCollisions.add(new SphereQuboidCollision(sceneSpheres.get(i), sceneQuboids.get(j), "a", "a"));
                                    }
                                    distance = (float) Math.sqrt((vectorR[0]-b[0])*(vectorR[0]-b[0]) + (vectorR[1]-b[1])*(vectorR[1]-b[1]));
                                    if (distance <= radius*0.9){
                                          //System.out.println("collision at b");
                                          sphereQuboidCollisions.add(new SphereQuboidCollision(sceneSpheres.get(i), sceneQuboids.get(j), "b", "b"));
                                    }
                                    distance = (float) Math.sqrt((vectorR[0]-c[0])*(vectorR[0]-c[0]) + (vectorR[1]-c[1])*(vectorR[1]-c[1]));
                                    if (distance <= radius*0.9){
                                          //System.out.println("collision at c");
                                          sphereQuboidCollisions.add(new SphereQuboidCollision(sceneSpheres.get(i), sceneQuboids.get(j), "c", "c"));
                                    }
                                    distance = (float) Math.sqrt((vectorR[0]-d[0])*(vectorR[0]-d[0]) + (vectorR[1]-d[1])*(vectorR[1]-d[1]));
                                    if (distance <= radius*0.9){
                                         //System.out.println("collision at d");
                                         sphereQuboidCollisions.add(new SphereQuboidCollision(sceneSpheres.get(i), sceneQuboids.get(j), "d", "d"));
                                    }
                                }
                            }
                        }
                        for (int i = 0; i < sphereQuboidCollisions.size(); i++){
                            sphereQuboidCollisions.get(i).collide();
                        }
                        //sphereQuboidCollisions.clear();
                        
                        //recalculation of spheres
                        for (int i = 0; i < sceneSpheres.size(); i++){
                            sceneSpheres.get(i).recalc(deltaT, forceFields, sphereSphereCollisions, sphereQuboidCollisions);
                        }
                        //recalculation of Quboids
                        for (int i = 0; i < sceneQuboids.size(); i++){
                            sceneQuboids.get(i).recalc(deltaT, forceFields, sphereSphereCollisions, sphereQuboidCollisions);
                        }
                        sphereSphereCollisions.clear();
                        sphereQuboidCollisions.clear();
                        
                    }
                }
            }
        };
        simThread.start();
        
        
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    System.out.println("quit this whole thing...");
                    simThread.stop();
                    stop();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gui.fxml"));
        loader.setController(this);
        try{
            root = loader.load();
        } catch (Exception e){
            System.out.print(e);
        }
        
        loader = new FXMLLoader(getClass().getResource("sphereProp.fxml"));
        loader.setController(this);
        try{
            sphereProp = loader.load();
            System.out.println("sphereProp loaded...");
        } catch (Exception e){
            System.out.print(e);
        }
        
        loader = new FXMLLoader(getClass().getResource("quboidProp.fxml"));
        loader.setController(this);
        try{
            quboidProp = loader.load();
            System.out.println("quboidProp loaded...");
        } catch (Exception e){
            System.out.print(e);
        }
        
//        loader = new FXMLLoader(getClass().getResource("polygonProp.fxml"));
//        loader.setController(this);
//        try{
//            polygonProp = loader.load();
//        } catch (Exception e){
//            System.out.print(e);
//        }
        
        loader = new FXMLLoader(getClass().getResource("forceProp.fxml"));
        loader.setController(this);
        try{
            forceProp = loader.load();
            System.out.println("forceProp loaded...");
        } catch (Exception e){
            System.out.print(e);
        }
        
        //propertiesPane.getChildren().add(sphereProp);
        
        simPane.getChildren().add(simulationArea);
        
        simPane.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if(objectJustSelected == false){
                    selectedSphere = null;
                    transformXhandle.setVisible(false);
                    transformYhandle.setVisible(false);
                    transformZhandle.setVisible(false);
                    scaleXhandle.setVisible(false);
                    scaleYhandle.setVisible(false);
                    scaleZhandle.setVisible(false);
                    rotateCenter.setVisible(false);
                    rotateHandle.setVisible(false);
                    propertiesPane.getChildren().clear();
                    System.out.println("no sphere selected");
                }
            }
        });
        
        File file = new File("src/simulationtest/wood.jpg");
        String newUrl = createImagePath(file);
        woodTextureImage = new Image(newUrl);
        System.out.println("wood image loaded...");
        file = new File("src/simulationtest/steel.jpg");
        newUrl = createImagePath(file);
        steelTextureImage = new Image(newUrl);
        file = new File("src/simulationtest/concrete.jpg");
        newUrl = createImagePath(file);
        concreteTextureImage = new Image(newUrl);
        file = new File("src/simulationtest/rubber.jpg");
        newUrl = createImagePath(file);
        rubberTextureImage = new Image(newUrl);
        
        createGravityField();
        forceFields.get(0).setName("Gravity");
        
        createSphere(100,200);
        
        createQuboid(250, 400);
        createQuboid(800, 400);
        
        file = new File("src/simulationtest/handles/transformXhandle.png");
        newUrl = createImagePath(file);
        transformXimage = new Image(newUrl);
        transformXhandle.setImage(transformXimage);
        transformXhandle.setX(0);
        transformXhandle.setY(0);
        transformXhandle.setOnDragDetected(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                System.out.println("transformXhandle is being dragged...");
                Dragboard db = transformXhandle.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                int offset = (int)(event.getSceneX()-flowPaneLeft.getPrefWidth())-selectedSphere.getPositionX(screenScaleFactor);
                content.putString("transformX"+offset);
                db.setContent(content);
                event.consume();
            }
        });
        transformXhandle.setVisible(false);
        handles.getChildren().add(transformXhandle);
        
        file = new File("src/simulationtest/handles/transformYhandle.png");
        newUrl = createImagePath(file);
        transformYimage = new Image(newUrl);
        transformYhandle.setImage(transformYimage);
        transformYhandle.setX(0);
        transformYhandle.setY(0);
        transformYhandle.setOnDragDetected(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                System.out.println("transformYhandle is being dragged...");
                Dragboard db = transformYhandle.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                int offset = (int)(event.getSceneY()-flowPaneTop.getPrefHeight())-selectedSphere.getPositionY(screenScaleFactor);
                content.putString("transformY"+offset);
                db.setContent(content);
                event.consume();
            }
        });
        transformYhandle.setVisible(false);
        handles.getChildren().add(transformYhandle);
        
        transformZhandle.setWidth(20);
        transformZhandle.setHeight(20);
        transformZhandle.setFill(Color.web("#3239c9"));
        transformZhandle.setOnDragDetected(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                System.out.println("transformYhandle is being dragged...");
                Dragboard db = transformZhandle.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                int offsetX = (int)(event.getSceneX()-flowPaneLeft.getPrefWidth())-selectedSphere.getPositionX(screenScaleFactor);
                String offX = ""+Math.abs(offsetX);
                int len = offX.length();
                if(offsetX < 0){
                    for (int i = 0; i < 3-len; i++){
                        offX = "0" + offX;
                    }
                    offX = "-" + offX;
                } else {
                    for (int i = 0; i < 4-len; i++){
                        offX = "0" + offX;
                    }
                }
                int offsetY = (int)(event.getSceneY()-flowPaneTop.getPrefHeight())-selectedSphere.getPositionY(screenScaleFactor);
                String offY = ""+Math.abs(offsetY);
                len = offY.length();
                if(offsetY < 0){
                    for (int i = 0; i < 3-len; i++){
                        offY = "0" + offY;
                    }
                    offY = "-" + offY;
                } else {
                    for (int i = 0; i < 4-len; i++){
                        offY = "0" + offY;
                    }
                }
                content.putString("transformZ"+offX+offY);
                db.setContent(content);
                event.consume();
            }
        });
        transformZhandle.setVisible(false);
        handles.getChildren().add(transformZhandle);
        
        file = new File("src/simulationtest/handles/scaleXhandle.png");
        newUrl = createImagePath(file);
        scaleXimage = new Image(newUrl);
        scaleXhandle.setImage(scaleXimage);
        scaleXhandle.setX(0);
        scaleXhandle.setY(0);
        scaleXhandle.setOnDragDetected(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                System.out.println("scaleXhandle is being dragged...");
                Dragboard db = scaleXhandle.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                int offset = (int)(event.getSceneX()-flowPaneLeft.getPrefWidth())-selectedSphere.getPositionX(screenScaleFactor);
                content.putString("scaleX"+offset);
                db.setContent(content);
                event.consume();
            }
        });
        scaleXhandle.setVisible(false);
        handles.getChildren().add(scaleXhandle);
        
        file = new File("src/simulationtest/handles/scaleYhandle.png");
        newUrl = createImagePath(file);
        scaleYimage = new Image(newUrl);
        scaleYhandle.setImage(scaleYimage);
        scaleYhandle.setX(0);
        scaleYhandle.setY(0);
        scaleYhandle.setOnDragDetected(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                System.out.println("scaleYhandle is being dragged...");
                Dragboard db = scaleYhandle.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                int offset = (int)(event.getSceneY()-flowPaneTop.getPrefHeight())-selectedSphere.getPositionY(screenScaleFactor);
                content.putString("scaleY"+offset);
                db.setContent(content);
                event.consume();
            }
        });
        scaleYhandle.setVisible(false);
        handles.getChildren().add(scaleYhandle);
        
        scaleZhandle.setWidth(20);
        scaleZhandle.setHeight(20);
        scaleZhandle.setFill(Color.web("#3239c9"));
        scaleZhandle.setOnDragDetected(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                System.out.println("scaleZhandle is being dragged...");
                Dragboard db = scaleZhandle.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                int offsetX = (int)(event.getSceneX()-flowPaneLeft.getPrefWidth())-selectedSphere.getPositionX(screenScaleFactor);
                String offX = ""+Math.abs(offsetX);
                int len = offX.length();
                if(offsetX < 0){
                    for (int i = 0; i < 3-len; i++){
                        offX = "0" + offX;
                    }
                    offX = "-" + offX;
                } else {
                    for (int i = 0; i < 4-len; i++){
                        offX = "0" + offX;
                    }
                }
                int offsetY = (int)(event.getSceneY()-flowPaneTop.getPrefHeight())-selectedSphere.getPositionY(screenScaleFactor);
                String offY = ""+Math.abs(offsetY);
                len = offY.length();
                if(offsetY < 0){
                    for (int i = 0; i < 3-len; i++){
                        offY = "0" + offY;
                    }
                    offY = "-" + offY;
                } else {
                    for (int i = 0; i < 4-len; i++){
                        offY = "0" + offY;
                    }
                }
                content.putString("scaleZ"+offX+offY);
                db.setContent(content);
                event.consume();
            }
        });
        scaleZhandle.setVisible(false);
        handles.getChildren().add(scaleZhandle);
        
        file = new File("src/simulationtest/handles/rotateHandle.png");
        newUrl = createImagePath(file);
        rotateImage = new Image(newUrl);
        rotateHandle.setImage(rotateImage);
        rotateHandle.setX(0);
        rotateHandle.setY(0);
        rotateHandle.setOnDragDetected(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                System.out.println("scaleYhandle is being dragged...");
                Dragboard db = rotateHandle.startDragAndDrop(TransferMode.ANY);

                ClipboardContent content = new ClipboardContent();
                int offsetX = (int)(event.getSceneX()-flowPaneLeft.getPrefWidth())-selectedSphere.getPositionX(screenScaleFactor);
                String offX = ""+Math.abs(offsetX);
                int len = offX.length();
                if(offsetX < 0){
                    for (int i = 0; i < 3-len; i++){
                        offX = "0" + offX;
                    }
                    offX = "-" + offX;
                } else {
                    for (int i = 0; i < 4-len; i++){
                        offX = "0" + offX;
                    }
                }
                int offsetY = (int)(event.getSceneY()-flowPaneTop.getPrefHeight())-selectedSphere.getPositionY(screenScaleFactor);
                String offY = ""+Math.abs(offsetY);
                len = offY.length();
                if(offsetY < 0){
                    for (int i = 0; i < 3-len; i++){
                        offY = "0" + offY;
                    }
                    offY = "-" + offY;
                } else {
                    for (int i = 0; i < 4-len; i++){
                        offY = "0" + offY;
                    }
                }
                content.putString("rotate"+offX+offY);
                db.setContent(content);
                event.consume();
            }
        });
        rotateHandle.setVisible(false);
        handles.getChildren().add(rotateHandle);
        
        rotateCenter.setRadius(10);
        rotateCenter.setFill(Color.web("#3239c9"));
        rotateCenter.setVisible(false);
        handles.getChildren().add(rotateCenter);
        
        level.setTargetPosition(994-100, 614-21, screenScaleFactor);
        level.setWidth(200, screenScaleFactor);
        level.setHeight(40, screenScaleFactor);
        
        file = new File("src/simulationtest/target.png");
        newUrl = createImagePath(file);
        targetImage = new Image(newUrl);
        targetView.setImage(targetImage);
        targetView.setX(level.getDrawTargetX(screenScaleFactor));
        targetView.setY(level.getDrawTargetY(screenScaleFactor));
        objekte.getChildren().add(targetView);

        szenerie.getChildren().add(objekte);
        szenerie.getChildren().add(handles);
        simulationArea.getChildren().add(szenerie);
        
        winningStage = new Stage();
        StackPane winPane = new StackPane();
        Label winLabel = new Label("winning...");
        winPane.getChildren().add(winLabel);
        
        Scene winScene = new Scene(winPane, 200,200);
        winningStage.setTitle("winning!");
        winningStage.setScene(winScene);
        
        Scene scene = new Scene(root);
        scene.setOnKeyTyped(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                System.out.println(event.getCharacter());
            }
        });
        scene.getStylesheets().add("simulationtest/StyleSheet.css");
        
        primaryStage.setTitle("Simulation!");
        primaryStage.setScene(scene);
        primaryStage.show();
        
    }
    
    public void createGravityField(){
        ForceField ffield = new ForceField(0, 0, 994, 614, 0f, 981f, screenScaleFactor);
        forceFields.add(ffield);
        System.out.println(ffield.toString());
        Rectangle field = new Rectangle(ffield.getWidth(screenScaleFactor), ffield.getHeight(screenScaleFactor), Color.web("#ff0000"));
        field.setStrokeWidth(3.0);
        field.setFill(Color.TRANSPARENT);
        field.setStroke(Color.RED);
        field.setX(ffield.getPositionX(screenScaleFactor));
        field.setY(ffield.getPositionY(screenScaleFactor));
        drawForceFields.add(field);
        objekte.getChildren().add(drawForceFields.get(drawForceFields.size()-1));
    }
    
    public void createForceField(int x, int y, int width, int height, float accX, float accY){
        ForceField ffield = new ForceField((float)x, (float)y, (float)width, (float)height, accX, accY, screenScaleFactor);
        forceFields.add(ffield);
        System.out.println(ffield.toString());
        Rectangle field = new Rectangle(ffield.getWidth(screenScaleFactor), ffield.getHeight(screenScaleFactor), Color.web("#ff0000"));
        field.setStrokeWidth(3.0);
        field.setFill(Color.TRANSPARENT);
        field.setStroke(Color.RED);
        field.setX(ffield.getPositionX(screenScaleFactor));
        field.setY(ffield.getPositionY(screenScaleFactor));
        field.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                int index = drawForceFields.indexOf(event.getSource());
                System.out.println(index + "th sphere selected");
                selectedSphere = forceFields.get(index);

                propertiesPane.getChildren().clear();
                propertiesPane.getChildren().add(forceProp);
                txtForceTransformX.setText(""+selectedSphere.getPositionX());
                txtForceTransformY.setText(""+selectedSphere.getPositionY());
                txtForceAccX.setText(""+selectedSphere.getAccelerationX());
                txtForceAccY.setText(""+selectedSphere.getAccelerationY());
                objectJustSelected = true;
            }
        });
        drawForceFields.add(field);
        objekte.getChildren().add(drawForceFields.get(drawForceFields.size()-1));
    }
    
    public void createSphere(int x, int y){
        System.out.println("creating sphere...");
        float[] pos = {(float)x/screenScaleFactor, (float)y/screenScaleFactor};
        float[] vel = {0.0f, 0.0f};
        Sphere sphere = new Sphere(0.5f, pos);
        sphere.setVelocity(vel);
        sceneSpheres.add(sphere);
        selectedSphere = sphere;
        
        ImagePattern woodTexture = new ImagePattern(woodTextureImage, 0,0,1,1,true);
        
        Circle circle = new Circle(sceneSpheres.get(sceneSpheres.size()-1).getRadius(screenScaleFactor), Color.web("#606060"));
        circle.setFill(woodTexture);
        circle.setStrokeType(StrokeType.CENTERED);
        circle.setStrokeWidth(0.0);
        circle.setTranslateX(sceneSpheres.get(sceneSpheres.size()-1).getPositionX(screenScaleFactor));
        circle.setTranslateY(sceneSpheres.get(sceneSpheres.size()-1).getPositionY(screenScaleFactor));
        
        circle.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                int index = drawCircles.indexOf(event.getSource());
                System.out.println(index + "th sphere selected");
                selectedSphere = sceneSpheres.get(index);

                propertiesPane.getChildren().clear();
                propertiesPane.getChildren().add(sphereProp);
                //cbMaterial.setItems(selectedSphere.getMaterialien());
                txtSphereTransformX.setText(""+selectedSphere.getPositionX());
                txtSphereTransformY.setText(""+selectedSphere.getPositionY());
                txtSphereRadius.setText(""+selectedSphere.getRadius());
                txtSphereRotation.setText(""+selectedSphere.getRotation());
                cbSphereMaterial.setItems(Spherelist);
                cbSphereMaterial.getSelectionModel().select(selectedSphere.getMaterial());
                objectJustSelected = true;
            }
        });
        
        drawCircles.add(circle);
        
        objekte.getChildren().add(drawCircles.get(drawCircles.size()-1));
        
        System.out.println("sphere created");
    }
    
    public void createQuboid(int x, int y){
        System.out.println("creating quboid...");
        sceneQuboids.add(new Quader((float)x/screenScaleFactor, (float)y/screenScaleFactor, 1.0f, 1.0f, 0f));
        Rectangle cuboid = new Rectangle(sceneQuboids.get(sceneQuboids.size()-1).getWidth(screenScaleFactor), sceneQuboids.get(sceneQuboids.size()-1).getHeight(screenScaleFactor), Color.web("#a0a0a0"));
        cuboid.setX(sceneQuboids.get(sceneQuboids.size()-1).getPositionX(screenScaleFactor) - sceneQuboids.get(sceneQuboids.size()-1).getWidth(screenScaleFactor)/2);
        cuboid.setY(sceneQuboids.get(sceneQuboids.size()-1).getPositionY(screenScaleFactor) - sceneQuboids.get(sceneQuboids.size()-1).getHeight(screenScaleFactor)/2);
        cuboid.setRotate((double)sceneQuboids.get(sceneQuboids.size()-1).getRotation());
        
        ImagePattern woodTexture = new ImagePattern(woodTextureImage, 0,0,1,1,true);
        cuboid.setFill(woodTexture);
        
        cuboid.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                int index = drawQuboids.indexOf(event.getSource());
                System.out.println(index + "th quboid selected");
                selectedSphere = sceneQuboids.get(index);

                propertiesPane.getChildren().clear();
                propertiesPane.getChildren().add(quboidProp);
                txtQuboidTransformX.setText(""+selectedSphere.getPositionX());
                txtQuboidTransformY.setText(""+selectedSphere.getPositionY());
                txtQuboidRotation.setText(""+selectedSphere.getRotation());
                cbQuboidMaterial.setItems(Quboidlist);
                cbQuboidMaterial.getSelectionModel().select(selectedSphere.getMaterial());
                objectJustSelected = true;
            }
        });
        drawQuboids.add(cuboid);
        objekte.getChildren().add(drawQuboids.get(drawQuboids.size()-1));
        System.out.println("quboid created");
    }
    
    public float[] rotate2d(float[] vector, float rotation){
        rotation = (float)Math.toRadians(rotation);
        float rx = (vector[0] * (float)Math.cos(rotation)) - (vector[1] * (float)Math.sin(rotation));
        float ry = (vector[0] * (float)Math.sin(rotation)) + (vector[1] * (float)Math.cos(rotation));
        float[] newVector = {rx, ry};
        return newVector;
    }
    
    public float getAngle(float[] vector_1, float[] vector_2){
        float skalar = (vector_1[0]*vector_2[0]+vector_1[1]*vector_2[1]);
        float betrag_1 = (float) Math.sqrt(vector_1[0]*vector_1[0]+vector_1[1]*vector_1[1]);
        float betrag_2 = (float) Math.sqrt(vector_2[0]*vector_2[0]+vector_2[1]*vector_2[1]);
        float angle = (float) Math.acos(skalar/(betrag_1*betrag_2));

        return (float) Math.toDegrees(angle);
    }
    
    public String createImagePath(File file){
        String url = "file:" + file.getAbsolutePath();
        String newUrl = "";
        for (int i = 0; i < url.length(); i++){
            if (url.substring(i, i+1).equals("\\")){
                newUrl = newUrl + "/";
            } else {
                newUrl = newUrl + url.substring(i, i+1);
            }
        }
        return newUrl;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
