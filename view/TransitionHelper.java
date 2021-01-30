package kclc.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;

public class TransitionHelper {

    // Constant
    public static final int WINDOWFADE_DELAY = 50;   // in milliseconds
    public static final int BUTTONPRESS_DELAY = 15;   // in milliseconds
    public static final int WINDOWFADE_DELAY_MAXCOUNT = 10;
    public static final int BUTTONPRESS_DELAY_MAXCOUNT = 10;

    // Global config
    public static int windowFadeCounter = 10;
    public static int buttonPressAnimationCounter = 10;


    /** Hack for fade out animation during closing of an stage.
     * The timeline will run in background for specified count. Each count will lessen the opacity of the current window.
     * When iot reaches 0, it will completely hide the window by calling the window.hide() method
     * */
    public static void windowFadeOutTransition(Window window){
        Timeline fiveSecondsWonder = new Timeline(
                new KeyFrame(Duration.millis(WINDOWFADE_DELAY),
                        new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {
                                windowFadeCounter -= 1;   // maybe neeed to reverse and use minus instead of add
                                window.setOpacity((double)windowFadeCounter * 0.1);
                                //System.out.println("this is called every 5 seconds on UI thread");
                                //System.out.println(windowFadeCounter % 10);
                                if(windowFadeCounter == 0){
                                    windowFadeCounter = WINDOWFADE_DELAY_MAXCOUNT;
                                    window.hide();
                                }
                            }

                        }));
        fiveSecondsWonder.setCycleCount(10);
        fiveSecondsWonder.play();
    }

    /** This is a delay to make sure that button press animation will have sufficient time to perform
     * before a transition to other stage occur.
     * */
    public static void buttonPresstoOtherWindowTransition(String fxmlPath){
        Timeline fiveSecondsWonder = new Timeline(
                new KeyFrame(Duration.millis(BUTTONPRESS_DELAY),
                        new EventHandler<ActionEvent>() {

                            @Override
                            public void handle(ActionEvent event) {
                                buttonPressAnimationCounter -= 1;
                                if(buttonPressAnimationCounter == 0){

                                    FXMLLoader loader = new FXMLLoader();
                                    loader.setLocation(getClass().getResource(fxmlPath));
                                    try {
                                        loader.load();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Parent root = loader.getRoot();
                                    Stage stage = new Stage();
                                    stage.initModality(Modality.APPLICATION_MODAL);  // the main window should not be able to press unless this is closed
                                    stage.setTitle("KCLC School Management System");
                                    stage.setResizable(false);
                                    stage.setScene(new Scene((root)));
                                    // stage.showAndWait();
                                    stage.show();

                                    buttonPressAnimationCounter = BUTTONPRESS_DELAY_MAXCOUNT;  // reset back the counter
                                }
                            }
                        }));
        fiveSecondsWonder.setCycleCount(10);
        fiveSecondsWonder.play();
    }
}
