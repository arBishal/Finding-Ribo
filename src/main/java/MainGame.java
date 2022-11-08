import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.audio.AudioPlayer;
import com.almasb.fxgl.audio.Sound;
import com.almasb.fxgl.core.View;
import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.EntityView;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.ui.FXGLTextFlow;
import com.sun.tools.javac.Main;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.TextListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static javafx.application.Platform.exit;


public class MainGame extends GameApplication {
    private Entity splash, background, playButton, exitButton, effect, slide, nextButton;
    private Entity lockScreen[] = new Entity[4];

    private Entity block1[] = new Entity[16];
    private int block1Array[] = new int[16];

    private int level = 0, levelThree = 0, levelInsideLevel = 0;

    private final int PLAY_BUTTON_HEIGHT = 50;
    private final int PLAY_BUTTON_WIDTH = 168;
    private final int EXIT_BUTTON_HEIGHT = 50;
    private final int EXIT_BUTTON_WIDTH = 121;
    private int remaining = 0;
    private int temp;
    private double blockDimension;
    private double timeOfMasterTimer;

    private Boolean playBoolean = false, exitBoolean = false, selected = false;
    private Point2D cursorPointInUI, centerOfBlock;


    protected void setLevel(int level){

        this.level = level;


        if(level == 1){

            background.setView(FXGL.getAssetLoader().loadTexture("pb (1).png"));
            background.setScaleX(0.9377);
            background.setScaleY(0.9387);

            levelInsideLevel = 0;

            int temp, random;
            double x, y, shiftedX, shiftedY;

            temp = 0;

            y = 50;
            blockDimension = 153.6;

            for(int i=0; i<4; i++){
                x = 130;
                for(int j=0; j<4; j++){

                    random = random(0,3);
                    block1Array[temp] = random;

                    if(random == 1){
                        shiftedX = x + blockDimension;
                        shiftedY = y;
                        remaining++;
                    }
                    else if(random == 2){
                        shiftedX = x + blockDimension;
                        shiftedY = y + blockDimension;
                        remaining++;
                    }
                    else if(random == 3){
                        shiftedX = x;
                        shiftedY = y + blockDimension;
                        remaining++;
                    }
                    else{
                        shiftedX = x;
                        shiftedY = y;
                    }


                    block1[temp] = FXGL.entityBuilder()
                            .at(shiftedX, shiftedY)
                            .scale(0.8,0.8)
                            .rotate(90*random)
                            .buildAndAttach();
                    x = x + blockDimension;
                    temp++;
                }
                y = y + blockDimension;
            }

            if(playButton.isActive()) {
                playButton.removeFromWorld();
            }
            if(exitButton.isActive()) {
                exitButton.removeFromWorld();
            }

            String tempString;

            for(int i=0; i<16; i++){

                tempString ="1 (";
                tempString = tempString + (i+1);
                tempString = tempString + ").jpg";

                block1[i].setView(FXGL.getAssetLoader().loadTexture(tempString));
            }


        }

        else if(level == 2){

            background.setView(FXGL.getAssetLoader().loadTexture("pb (2).png"));
            levelInsideLevel = 0;

            this.level = -2;

            System.out.println("setlevel 3 te aise");


            for(int i=0; i<16; i++){
                block1Array[i] = -1;
            }

            for(int i=1; i<=8; ){
                temp = random(0,15);
                if(block1Array[temp] == -1){

                    if(i<0){
                        block1Array[temp] = -i;
                        i = -i + 1;
                    }
                    else{
                        block1Array[temp] = i;
                        i = -i;
                    }
                }
            }


            System.out.println("3");

            String tempString;

            for(int i=0; i<16; i++){

                tempString ="2 (";
                tempString = tempString + (block1Array[i]);
                tempString = tempString + ").png";

                block1[i].setView(FXGL.getAssetLoader().loadTexture(tempString));

            }

            timeOfMasterTimer = getMasterTimer().getNow();

            getMasterTimer().runOnceAfter(() -> {
                for(int i=0; i<16; i++){
                    System.out.println("5 sec");
                    block1[i].setView(FXGL.getAssetLoader().loadTexture("temp.png"));
                }
                this.level = 2;
            }, Duration.seconds(5));
        }

        else if(level == 3){

            background.setView(FXGL.getAssetLoader().loadTexture("pb (3).png"));

            levelInsideLevel = 0;

            String tempString;

            for(int i=0; i<16; i++){
                block1Array[i] = -1;
            }

            for(int i=0; i<16; ){

                temp = random(0,15);

                if(block1Array[temp] == -1){
                    block1Array[temp] = i;
                    i++;
                }
            }

            for(int i=0; i<16; i++){

                tempString ="3 (";
                tempString = tempString + (block1Array[i]+1);
                tempString = tempString + ").jpg";

                block1[i].setView(FXGL.getAssetLoader().loadTexture(tempString));
            }
            FXGL.getInput().mockButtonPress(MouseButton.PRIMARY);

        }

        else if(level == 4){

            String tempString;
            background.setView(FXGL.getAssetLoader().loadTexture("pb (4).png"));
            for(int i=0; i<4; i++){
                tempString = "lock_screen (";
                tempString = tempString + (i+1);
                tempString = tempString + ").png";
                lockScreen[i] = FXGL.entityBuilder()
                        .at(150+250*i, 540)
                        .view(tempString)
                        .buildAndAttach();
            }
        }

        else if(level == 5){

            background.setView(FXGL.getAssetLoader().loadTexture("pb (5).png"));

            levelInsideLevel = 0;

            for(int i=0; i<16; i++){
                block1Array[i] = -1;
            }

            remaining = random(0,15);
            block1Array[remaining] = 15;

            System.out.println("loop");

            for(int i=0; i<15; ){
                temp = random(0,15);
                if(block1Array[temp] == -1){
                    block1Array[temp] = i;
                    i++;
                }
            }

            System.out.println("second loop");

            String tempString;

            for(int i=0; i<16; i++){
                tempString = "5 (";
                tempString = tempString + (block1Array[i]+1);
                tempString = tempString + ").png";
                block1[i].setView(FXGL.getAssetLoader().loadTexture(tempString));
            }

            levelThree = 0;

            for(int i=0; i<15; i++){
                if(block1Array[i] == i){
                    levelThree++;
                }
            }

            System.out.println("done");

        }

    }


    @Override
    protected void initSettings(GameSettings settings){

        System.out.println("initSettings");
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Finding Ribo");
        settings.setCloseConfirmation(true);
        settings.setVersion("1.0");
        settings.setFullScreenAllowed(true);
    }


    protected void initGame() {

        splash = FXGL.entityBuilder()
                .at(0, 0)
                .scale(1.67,1.67)
                .view("splash.gif")
                .buildAndAttach();



        getMasterTimer().runOnceAfter(() -> {

            splash.removeFromWorld();

            background = FXGL.entityBuilder()
                    .at(0, 0)
                    .view("home.gif")
                    .scale(1.78, 1.6)
                    .buildAndAttach();

            playButton = FXGL.entityBuilder()
                    .at(556, 285)
                    .view("play_button.png")
                    .buildAndAttach();

            exitButton = FXGL.entityBuilder()
                    .at(580, 385)
                    .view("exit_button.png")
                    .buildAndAttach();

            slide = FXGL.entityBuilder()
                    .scale(0.9377,0.9387)
                    .buildAndAttach();

            nextButton = FXGL.entityBuilder()
                    .at(1146,650)
                    .buildAndAttach();



        },Duration.millis(20000));

    }

    protected void initPhysics(){
        System.out.println("initPhysics");
    }



    protected void onUpdate(double tpf){

    }



    protected void initInput(){
        Input input = FXGL.getInput();

        System.out.println("initInput");


        input.addAction(new UserAction("Mouse Click"){

            protected void onActionBegin(){

                cursorPointInUI = input.getMousePositionWorld();

                //System.out.println("aise 1");

                if(splash.isActive()) {

                }

                else if(level == 0) {
                    if(levelInsideLevel == 0) {
                        if (cursorPointInUI.getX() < (playButton.getX() + PLAY_BUTTON_WIDTH)
                                && cursorPointInUI.getX() > (playButton.getX())
                                && cursorPointInUI.getY() < (playButton.getY() + PLAY_BUTTON_HEIGHT)
                                && cursorPointInUI.getY() > (playButton.getY())) {
                            slide.setView(FXGL.getAssetLoader().loadTexture("1.png"));
                            nextButton.setView(FXGL.getAssetLoader().loadTexture("next.png"));
                            levelInsideLevel = 1;

                        } else if (cursorPointInUI.getX() < (exitButton.getX() + EXIT_BUTTON_WIDTH)
                                && cursorPointInUI.getX() > (exitButton.getX())
                                && cursorPointInUI.getY() < (exitButton.getY() + EXIT_BUTTON_HEIGHT)
                                && cursorPointInUI.getY() > (exitButton.getY())) {
                            exit();
                        }
                    }
                    else if(levelInsideLevel == 1
                            && cursorPointInUI.getX() < (nextButton.getX() + 110)
                            && cursorPointInUI.getX() > (nextButton.getX())
                            && cursorPointInUI.getY() < (nextButton.getY() + 46)
                            && cursorPointInUI.getY() > (nextButton.getY())){
                        levelInsideLevel = 0;
                        slide.setView(FXGL.getAssetLoader().loadTexture("null.png"));
                        nextButton.setView(FXGL.getAssetLoader().loadTexture("null.png"));
                        setLevel(1);
                    }
                }

                else if(level == 1){

                    if(levelInsideLevel == 0) {
                        int tempX, tempY;

                        if (cursorPointInUI.getX() > 130
                                && cursorPointInUI.getX() < 744.4
                                && cursorPointInUI.getY() > 50
                                && cursorPointInUI.getY() < 664.4) {

                            tempX = (int) ((cursorPointInUI.getX() - 130) / 153.6);
                            tempY = (int) ((cursorPointInUI.getY() - 50) / 153.6);

                            temp = tempX + 4 * tempY;
                        } else {
                            temp = -1;
                        }

                        System.out.println(temp);

                        if (temp != -1) {

                            block1[temp].rotateBy(90);


                            if (block1Array[temp] == 0) {
                                block1[temp].translateX(+blockDimension);
                                remaining++;
                            } else if (block1Array[temp] == 1) {
                                block1[temp].translateY(blockDimension);
                            } else if (block1Array[temp] == 2) {
                                block1[temp].translateX(-blockDimension);
                            } else {
                                block1[temp].translateY(-blockDimension);
                                remaining--;
                            }

                            block1Array[temp] = (block1Array[temp] + 1) % 4;


                            if (remaining == 0) {

                                for(int i=0; i<16; i++){
                                    block1[i].setView(FXGL.getAssetLoader().loadTexture("null.png"));
                                }

                                slide.setView(FXGL.getAssetLoader().loadTexture("2.1.png"));
                                nextButton.setView(FXGL.getAssetLoader().loadTexture("next.png"));
                                levelInsideLevel = 1;

                            }


                        }
                    }
                    else if(levelInsideLevel == 1
                            && cursorPointInUI.getX() < (nextButton.getX() + 110)
                            && cursorPointInUI.getX() > (nextButton.getX())
                            && cursorPointInUI.getY() < (nextButton.getY() + 46)
                            && cursorPointInUI.getY() > (nextButton.getY())){
                        slide.setView(FXGL.getAssetLoader().loadTexture("2.2.png"));
                        nextButton.setView(FXGL.getAssetLoader().loadTexture("ok.png"));
                        nextButton.setPosition(585,560);
                        levelInsideLevel = 2;
                    }
                    else if(levelInsideLevel == 2
                            && cursorPointInUI.getX() < (nextButton.getX() + 110)
                            && cursorPointInUI.getX() > (nextButton.getX())
                            && cursorPointInUI.getY() < (nextButton.getY() + 46)
                            && cursorPointInUI.getY() > (nextButton.getY())){
                        slide.setView(FXGL.getAssetLoader().loadTexture("null.png"));
                        nextButton.setView(FXGL.getAssetLoader().loadTexture("null.png"));
                        nextButton.setPosition(1146,650);
                        levelInsideLevel = 0;
                        setLevel(2);
                    }
                }
                else if(level == 2){

                    if(levelInsideLevel == 0) {
                        int tempX, tempY;

                        if (cursorPointInUI.getX() > 130
                                && cursorPointInUI.getX() < 744.4
                                && cursorPointInUI.getY() > 50
                                && cursorPointInUI.getY() < 664.4) {


                            tempX = (int) ((cursorPointInUI.getX() - 130) / 153.6);
                            tempY = (int) ((cursorPointInUI.getY() - 50) / 153.6);

                            temp = tempX + 4 * tempY;
                            System.out.println("Clicked");
                        } else {
                            temp = -1;
                            System.out.println("Not Clicked");
                        }

                        if (temp != -1) {

                            if (block1Array[temp] != -1) {
                                if (!selected) {
                                    remaining = temp;
                                    String tempString;
                                    tempString = "2 (";
                                    tempString = tempString + (block1Array[temp]);
                                    tempString = tempString + ").png";
                                    block1[temp].setView(FXGL.getAssetLoader().loadTexture(tempString));
                                    selected = true;
                                    System.out.println("prothomta");
                                } else {
                                    String tempString;
                                    tempString = "2 (";
                                    tempString = tempString + (block1Array[temp]);
                                    tempString = tempString + ").png";
                                    block1[temp].setView(FXGL.getAssetLoader().loadTexture(tempString));
                                    if (block1Array[temp] == block1Array[remaining]) {
                                        block1Array[temp] = -1;
                                        block1Array[remaining] = -1;
                                        levelThree++;
                                        System.out.println("milse :)");
                                        if (levelThree > 7) {
                                            System.out.println("four e jaa...");
                                            levelInsideLevel = 1;

                                            for(int i=0; i<16; i++){
                                                block1[i].setView(FXGL.getAssetLoader().loadTexture("null.png"));
                                            }

                                            slide.setView(FXGL.getAssetLoader().loadTexture("3.png"));
                                            nextButton.setView(FXGL.getAssetLoader().loadTexture("next.png"));
                                        }
                                    } else {

                                        level = -3;

                                        getMasterTimer().runOnceAfter(() -> {
                                            block1[temp].setView(FXGL.getAssetLoader().loadTexture("temp.png"));
                                            block1[remaining].setView(FXGL.getAssetLoader().loadTexture("temp.png"));
                                            level = 2;
                                        }, Duration.seconds(1.5));
                                    }
                                    selected = false;
                                }
                            }

                        }
                    }
                    else if(levelInsideLevel == 1
                            && cursorPointInUI.getX() < (nextButton.getX() + 110)
                            && cursorPointInUI.getX() > (nextButton.getX())
                            && cursorPointInUI.getY() < (nextButton.getY() + 46)
                            && cursorPointInUI.getY() > (nextButton.getY())){
                        slide.setView(FXGL.getAssetLoader().loadTexture("null.png"));
                        nextButton.setView(FXGL.getAssetLoader().loadTexture("null.png"));
                        levelInsideLevel = 0;
                        setLevel(3);
                    }

                }

                else if(level == 3) {

                    if(levelInsideLevel == 0) {
                        int tempX, tempY;

                        if (cursorPointInUI.getX() > 130
                                && cursorPointInUI.getX() < 744.4
                                && cursorPointInUI.getY() > 50
                                && cursorPointInUI.getY() < 664.4) {

                            // System.out.println("aise 3");

                            tempX = (int) ((cursorPointInUI.getX() - 130) / 153.6);
                            tempY = (int) ((cursorPointInUI.getY() - 50) / 153.6);

                            temp = tempX + 4 * tempY;
                        } else {
                            temp = -1;
                        }

                        System.out.println(temp);

                        if (temp != -1) {

                            //System.out.println("aise 4");

                            if (!selected) {

                                // System.out.println("aise 5_1");

                                remaining = temp;

                                effect = FXGL.entityBuilder()
                                        .at(block1[temp].getPosition())
                                        .scale(0.8, 0.8)
                                        .view("effect_level2.png")
                                        .buildAndAttach();

                                // System.out.println("aise");
                                selected = true;

                            } else {

                                //System.out.println("aise 5_2");

                                effect.removeFromWorld();
                                View tempView = block1[temp].getView();
                                block1[temp].setView(block1[remaining].getView());
                                block1[remaining].setView(tempView);

                                //System.out.println(block1Array[remaining]);
                                //System.out.println(block1Array[temp]);

                                int i;

                                i = block1Array[temp];
                                block1Array[temp] = block1Array[remaining];
                                block1Array[remaining] = i;


                                //block1Array[temp] = block1Array[temp]^block1Array[remaining];
                                //block1Array[remaining] = block1Array[temp]^block1Array[remaining];
                                //block1Array[temp] = block1Array[remaining]^block1Array[temp];

                                //System.out.println(block1Array[remaining]);
                                //System.out.println(block1Array[temp]);

                                selected = false;

                                for (i = 0; i < 16; i++) {
                                    System.out.println("hey");
                                    System.out.println(block1Array[i]);
                                    System.out.println("hmmn");
                                    if (block1Array[i] != i) {
                                        break;
                                    }
                                }

                                System.out.println((i + 1) + " no. e bemil");

                                if (i == 16) {

                                    for(int j=0; j<16; j++){
                                        block1[j].setView(FXGL.getAssetLoader().loadTexture("null.png"));
                                    }

                                    slide.setView(FXGL.getAssetLoader().loadTexture("4.1.png"));
                                    nextButton.setView(FXGL.getAssetLoader().loadTexture("next.png"));
                                    levelInsideLevel = 1;
                                }

                            }

                        }
                    }
                    else if(levelInsideLevel == 1
                            && cursorPointInUI.getX() < (nextButton.getX() + 110)
                            && cursorPointInUI.getX() > (nextButton.getX())
                            && cursorPointInUI.getY() < (nextButton.getY() + 46)
                            && cursorPointInUI.getY() > (nextButton.getY())){
                        slide.setView(FXGL.getAssetLoader().loadTexture("4.2.png"));
                        levelInsideLevel = 2;
                    }
                    else if(levelInsideLevel == 2
                            && cursorPointInUI.getX() < (nextButton.getX() + 110)
                            && cursorPointInUI.getX() > (nextButton.getX())
                            && cursorPointInUI.getY() < (nextButton.getY() + 46)
                            && cursorPointInUI.getY() > (nextButton.getY())){
                        slide.setView(FXGL.getAssetLoader().loadTexture("4.3.png"));
                        levelInsideLevel = 3;
                    }
                    else if(levelInsideLevel == 3
                            && cursorPointInUI.getX() < (nextButton.getX() + 110)
                            && cursorPointInUI.getX() > (nextButton.getX())
                            && cursorPointInUI.getY() < (nextButton.getY() + 46)
                            && cursorPointInUI.getY() > (nextButton.getY())){
                        slide.setView(FXGL.getAssetLoader().loadTexture("4.4.png"));
                        levelInsideLevel = 4;
                    }
                    else if(levelInsideLevel == 4
                            && cursorPointInUI.getX() < (nextButton.getX() + 110)
                            && cursorPointInUI.getX() > (nextButton.getX())
                            && cursorPointInUI.getY() < (nextButton.getY() + 46)
                            && cursorPointInUI.getY() > (nextButton.getY())){
                        slide.setView(FXGL.getAssetLoader().loadTexture("null.png"));
                        nextButton.setView(FXGL.getAssetLoader().loadTexture("null.png"));
                        levelInsideLevel = 0;
                        setLevel(4);
                    }

                }
                else if(level == 4){

                    if(levelInsideLevel == 0
                            && cursorPointInUI.getX() < (lockScreen[1].getX() + 200)
                            && cursorPointInUI.getX() > (lockScreen[1].getX())
                            && cursorPointInUI.getY() < (lockScreen[1].getY() + 55)
                            && cursorPointInUI.getY() > (lockScreen[1].getY())){
                        for(int j=0; j<4; j++){
                            lockScreen[j].removeFromWorld();
                        }
                        slide.setView(FXGL.getAssetLoader().loadTexture("5.1.png"));
                        nextButton.setView(FXGL.getAssetLoader().loadTexture("next.png"));
                        levelInsideLevel = 1;
                    }
                    else if(levelInsideLevel == 1){
                        slide.setView(FXGL.getAssetLoader().loadTexture("5.2.png"));
                        levelInsideLevel = 2;
                    }
                    else if(levelInsideLevel == 2){
                        slide.setView(FXGL.getAssetLoader().loadTexture("null.png"));
                        nextButton.setView(FXGL.getAssetLoader().loadTexture("null.png"));
                        levelInsideLevel = 0;
                        setLevel(5);
                    }
                }

                else if(level == 5){

                    int tempX, tempY;

                    if(cursorPointInUI.getX() > 130
                            && cursorPointInUI.getX() < 744.4
                            && cursorPointInUI.getY() > 50
                            && cursorPointInUI.getY() < 664.4) {


                        tempX = (int)((cursorPointInUI.getX() - 130)/153.6);
                        tempY = (int)((cursorPointInUI.getY() - 50)/153.6);

                        temp = tempX + 4*tempY;
                    }
                    else{
                        temp = -1;
                    }

                    if(temp != -1){
                        View tempView;
                        int i;
                        if(remaining == temp-1){
                            if((temp-1)/4 == temp/4){
                                tempView = block1[remaining].getView();
                                block1[remaining].setView(block1[temp].getView());
                                block1[temp].setView(tempView);
                                i = block1Array[remaining];
                                block1Array[remaining] = block1Array[temp];

                                if(temp == block1Array[temp] && temp != i){
                                    levelThree--;
                                }
                                else if(temp != block1Array[temp] && temp == i){
                                    levelThree++;
                                }

                                block1Array[temp] = i;
                                remaining = temp;

                                if(levelThree == 15){

                                    for(int j=0; j<16; j++){
                                        block1[j].setView(FXGL.getAssetLoader().loadTexture("null.png"));
                                    }

                                    System.out.println("level five ");
                                }

                            }
                        }
                        else if(remaining == temp+1){
                            if((temp+1)/4 == temp/4){
                                tempView = block1[remaining].getView();
                                block1[remaining].setView(block1[temp].getView());
                                block1[temp].setView(tempView);
                                i = block1Array[remaining];
                                block1Array[remaining] = block1Array[temp];

                                if(temp == block1Array[temp] && temp != i){
                                    levelThree--;
                                }
                                else if(temp != block1Array[temp] && temp == i){
                                    levelThree++;
                                }

                                block1Array[temp] = i;
                                remaining = temp;

                                if(levelThree == 15){

                                    for(int j=0; j<16; j++){
                                        block1[j].setView(FXGL.getAssetLoader().loadTexture("null.png"));
                                    }

                                    System.out.println("level five ");
                                }

                            }
                        }
                        else if(remaining == temp-4){
                            tempView = block1[remaining].getView();
                            block1[remaining].setView(block1[temp].getView());
                            block1[temp].setView(tempView);
                            i = block1Array[remaining];
                            block1Array[remaining] = block1Array[temp];

                            if(temp == block1Array[temp] && temp != i){
                                levelThree--;
                            }
                            else if(temp != block1Array[temp] && temp == i){
                                levelThree++;
                            }

                            block1Array[temp] = i;
                            remaining = temp;

                            if(levelThree == 15){

                                for(int j=0; j<16; j++){
                                    block1[j].setView(FXGL.getAssetLoader().loadTexture("null.png"));
                                }

                                System.out.println("level five ");
                            }

                        }
                        else if(remaining == temp+4){
                            tempView = block1[remaining].getView();
                            block1[remaining].setView(block1[temp].getView());
                            block1[temp].setView(tempView);
                            i = block1Array[remaining];
                            block1Array[remaining] = block1Array[temp];

                            if(temp == block1Array[temp] && temp != i){
                                levelThree--;
                            }
                            else if(temp != block1Array[temp] && temp == i){
                                levelThree++;
                            }

                            block1Array[temp] = i;
                            remaining = temp;

                            if(levelThree == 15){

                                for(int j=0; j<16; j++){
                                    block1[j].setView(FXGL.getAssetLoader().loadTexture("null.png"));
                                }

                                System.out.println("level five ");
                            }
                        }
                    }

                }
            }


        }, MouseButton.PRIMARY);


    }



    public static void main(String[] args) {
        System.out.println("main1");
        launch(args);
        System.out.println("main2");
    }

}
