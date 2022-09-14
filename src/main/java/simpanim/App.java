package simpanim;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import simpanim.transformations.point.duration.RandomStart;
import simpanim.transformations.point.location.Middle;
import simpanim.transformations.point.property.RandomAngle;
import simpanim.transformations.point.wallcollision.*;
import simpanim.transformations.population.InitialPopulationTransformation;

import java.util.*;
import java.util.List;


public class App extends Application{
    private static final double resolution = 1000;
    private final List<Point> points = new ArrayList<>();

    private final Group root = new Group();

    private SubScene animation;
    Group nodesGroup;
    PixelGameField pixelGameAnimation;

    ComboBox<WallCollisionStrategy> wallCollisionStrategy;

    TextField starAmount;

    TextField originFrames;
    TextField speed;

    ColorPicker colorPicker;

    @Override
    public void start(final Stage primaryStage) {
        Scene mainScene = new Scene(root, 1450, 1024, Color.BLACK);

        Button startButton = new Button("start animation");
        startButton.setOnAction(actionEvent -> {
            if(animation != null){
                root.getChildren().removeAll(nodesGroup);
                root.getChildren().remove(animation);
                pixelGameAnimation.stop();
                points.clear();
            }
            nodesGroup = new Group();

            for (int i=0; i<Integer.parseInt(starAmount.getText()); i++) {
                Point point = new Point();
                Circle circle = new Circle(1, Color.WHITE);
                circle.setFill(colorPicker.getValue());
                point.setCircle(circle);
                point.setSize(1);
                points.add(point);
                nodesGroup.getChildren().add(circle);
            }

            animation = new SubScene(nodesGroup, resolution, resolution, true, SceneAntialiasing.BALANCED);
            animation.setTranslateX(10);
            animation.setTranslateY(10);
            //test
            root.getChildren().add(animation);
            pixelGameAnimation = new PixelGameField(points, nodesGroup, resolution, Double.parseDouble(speed.getText()));
            pixelGameAnimation.setStrategy(wallCollisionStrategy.getValue());
            InitialPopulationTransformation initialPopulationTransformation = new InitialPopulationTransformation(Integer.parseInt(originFrames.getText()), resolution);
            initialPopulationTransformation.setStartModifier(new RandomStart());
            initialPopulationTransformation.setAngleModifier(new RandomAngle());
            initialPopulationTransformation.setLocationModifier(new Middle());
            pixelGameAnimation.initializeGameField(initialPopulationTransformation);
            pixelGameAnimation.start();
        });



        Button endButton = new Button("end animation");
        endButton.setOnAction(actionEvent -> {
            if(animation != null){
                root.getChildren().removeAll(nodesGroup);
                root.getChildren().remove(animation);
                points.clear();
                pixelGameAnimation.stop();
            }
        });

        wallCollisionStrategy = new ComboBox<>();
        wallCollisionStrategy.setButtonCell(new WallCollisionStrategyListCell());
        wallCollisionStrategy.getItems().addAll(
                        new LoopOnWallCollision(),
                        new PerfectWallCollision(),
                        new SquareWallCollision(),
                        new TeleportOnWallCollision()
                );
        wallCollisionStrategy.getSelectionModel().selectFirst();
        wallCollisionStrategy.setPrefWidth(200);
        Label label = new Label("Wall collision type: ");
        label.setTextFill(Color.WHITE);

        starAmount = new TextField();
        starAmount.setPrefWidth(200);
        Label label2 = new Label("Amount of points: ");
        label2.setTextFill(Color.WHITE);


        originFrames = new TextField();
        originFrames.setPrefWidth(200);
        Label label3 = new Label("Origin frames (point density): ");
        label3.setTextFill(Color.WHITE);

        speed = new TextField();
        speed.setPrefWidth(200);
        speed.setTooltip(new Tooltip("A point in a single frame passes through distance of pixels defined as speed" +
                "Inputting negative numbers will reverse the direction of the movement."));

        Label label4 = new Label("Point speed: ");
        label4.setTextFill(Color.WHITE);

        colorPicker = new ColorPicker(Color.WHITE);
        Label label5 = new Label("Point color: ");
        label5.setTextFill(Color.WHITE);


        GridPane gridPane = new GridPane();
        gridPane.setVgap(4);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(2, 2, 2, 2));

        gridPane.add(startButton, 0, 0);
        gridPane.add(endButton, 1, 0);

        gridPane.add(label, 0, 1);
        gridPane.add(wallCollisionStrategy, 1, 1);

        gridPane.add(label2, 0, 2);
        gridPane.add(starAmount, 1, 2);
        starAmount.setText("5000");

        gridPane.add(label3, 0, 3);
        gridPane.add(originFrames, 1, 3);
        originFrames.setText("50");

        gridPane.add(label4, 0, 4);
        gridPane.add(speed, 1, 4);
        speed.setText("1");

        gridPane.add(label5, 0, 5);
        gridPane.add(colorPicker, 1, 5);

        gridPane.setTranslateX(1025);
        gridPane.setTranslateY(10);

        root.getChildren().add(gridPane);

        primaryStage.setScene(mainScene);
        primaryStage.show();

    }


    static class WallCollisionStrategyListCell extends ListCell<WallCollisionStrategy> {
        @Override
        protected void updateItem(WallCollisionStrategy strategy, boolean empty){
            super.updateItem(strategy, empty);
            if (empty) {
                setText(null);
            } else {
                setText(strategy.getClass().getSimpleName());
            }
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
