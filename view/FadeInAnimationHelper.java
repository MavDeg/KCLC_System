package kclc.view;

import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class FadeInAnimationHelper <Node>{
    private Node objToAnimate;
    private FadeTransition fadeIn = new FadeTransition();

    public FadeInAnimationHelper(Node objToAnimate) {
        this.objToAnimate = objToAnimate;
    }

    public void play(){
        fadeIn.setDuration(Duration.millis(750));
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(10);
        fadeIn.setNode((javafx.scene.Node) this.objToAnimate);
        fadeIn.play();
    }
}
