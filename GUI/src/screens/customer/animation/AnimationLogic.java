package screens.customer.animation;

import com.sun.deploy.util.BlackList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import screens.MainPage;

import java.util.Objects;

public class AnimationLogic {
    private final static Image WALK_1 = new Image(Objects.requireNonNull(MainPage.class.getResource("/screens/resources/animation/1.png")).toString());
    private final static Image WALK_2 = new Image(Objects.requireNonNull(MainPage.class.getResource("/screens/resources/animation/2.png")).toString());
    private final static Image WALK_3 = new Image(Objects.requireNonNull(MainPage.class.getResource("/screens/resources/animation/3.png")).toString());
    private final static Image WALK_4 = new Image(Objects.requireNonNull(MainPage.class.getResource("/screens/resources/animation/4.png")).toString());
    private final static Image WALK_5 = new Image(Objects.requireNonNull(MainPage.class.getResource("/screens/resources/animation/5.png")).toString());
    private final static Image WALK_6 = new Image(Objects.requireNonNull(MainPage.class.getResource("/screens/resources/animation/6.png")).toString());
    private final static Image WALK_7 = new Image(Objects.requireNonNull(MainPage.class.getResource("/screens/resources/animation/7.png")).toString());
    private final static Image WALK_8 = new Image(Objects.requireNonNull(MainPage.class.getResource("/screens/resources/animation/8.png")).toString());
    private final static Image WALK_9 = new Image(Objects.requireNonNull(MainPage.class.getResource("/screens/resources/animation/9.png")).toString());
    private final static Image WALK_10 = new Image(Objects.requireNonNull(MainPage.class.getResource("/screens/resources/animation/10.png")).toString());
    private final static Image WALK_11 = new Image(Objects.requireNonNull(MainPage.class.getResource("/screens/resources/animation/11.png")).toString());
    private final static Image WALK_12 = new Image(Objects.requireNonNull(MainPage.class.getResource("/screens/resources/animation/12.png")).toString());
    private final static Image WALK_13 = new Image(Objects.requireNonNull(MainPage.class.getResource("/screens/resources/animation/13.png")).toString());

    private Timeline walkTimeline;
    private ImageView animationImage;
    private BooleanProperty animationProperty;

    public AnimationLogic(ImageView animationImage) {
        this.animationImage = animationImage;
        animationProperty = new SimpleBooleanProperty(false);
    }

    private void setWalkAnimation() {
        walkTimeline = new Timeline();
        walkTimeline.setCycleCount(Timeline.INDEFINITE);

        walkTimeline.getKeyFrames().add(new KeyFrame(
                Duration.millis(100), (event) -> animationImage.setImage(WALK_1)));

        walkTimeline.getKeyFrames().add(new KeyFrame(
                Duration.millis(200), (event) -> animationImage.setImage(WALK_2)));

        walkTimeline.getKeyFrames().add(new KeyFrame(
                Duration.millis(300), (event) -> animationImage.setImage(WALK_3)));

        walkTimeline.getKeyFrames().add(new KeyFrame(
                Duration.millis(400), (event) -> animationImage.setImage(WALK_4)));

        walkTimeline.getKeyFrames().add(new KeyFrame(
                Duration.millis(500), (event) -> animationImage.setImage(WALK_5)));

        walkTimeline.getKeyFrames().add(new KeyFrame(
                Duration.millis(600), (event) -> animationImage.setImage(WALK_6)));

        walkTimeline.getKeyFrames().add(new KeyFrame(
                Duration.millis(700), (event) -> animationImage.setImage(WALK_7)));

        walkTimeline.getKeyFrames().add(new KeyFrame(
                Duration.millis(800), (event) -> animationImage.setImage(WALK_8)));

        walkTimeline.getKeyFrames().add(new KeyFrame(
                Duration.millis(900), (event) -> animationImage.setImage(WALK_9)));

        walkTimeline.getKeyFrames().add(new KeyFrame(
                Duration.millis(1000), (event) -> animationImage.setImage(WALK_10)));

        walkTimeline.getKeyFrames().add(new KeyFrame(
                Duration.millis(1100), (event) -> animationImage.setImage(WALK_11)));

        walkTimeline.getKeyFrames().add(new KeyFrame(
                Duration.millis(1200), (event) -> animationImage.setImage(WALK_12)));

        walkTimeline.getKeyFrames().add(new KeyFrame(
                Duration.millis(1300), (event) -> animationImage.setImage(WALK_13)));

    }

    public void animationOn() {
        walkTimeline.play();
        animationProperty.set(true);
        animationImage.setVisible(true);
    }

    public void animationOff() {
        walkTimeline.stop();
        animationProperty.set(false);
        animationImage.setVisible(false);
    }

    public boolean isAnimationProperty() {
        return animationProperty.get();
    }

    public BooleanProperty animationPropertyProperty() {
        return animationProperty;
    }
}
