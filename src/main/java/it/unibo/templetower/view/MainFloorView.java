package it.unibo.templetower.view;

import java.util.HashMap;
import java.util.Map;

import it.unibo.templetower.controller.GameController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Screen;

/** 
 * This scene represents a floor of the game, where the player can move between rooms.
 */
public class MainFloorView {
    private Pane dPane;
    private Circle outer;
    private Circle inner;

    ToggleButton left;
    ToggleButton right;
    ToggleButton enter;
    HBox buttons;

    private static final double HEIGHT = Screen.getPrimary().getVisualBounds().getHeight();
    private static final double WIDTH = Screen.getPrimary().getVisualBounds().getWidth();

    private static final double OUTER_RADIUS = HEIGHT / 3;
    private static final double INNER_RADIUS = OUTER_RADIUS*0.5;

    private int nRooms;

    private final Map<Integer, Arc> sectorMap = new HashMap<>();

    public Scene createScene(SceneManager manager, GameController controller) {
        //Background
        BorderPane root = new BorderPane();
        dPane = new Pane();
        root.setCenter(dPane);
        root.setId("circle-room-back");

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        
        //Inner and outer circles for create the rooms container
        this.nRooms = controller.getRooms().size();
        outer = createCircle("outer-circle-rooms", OUTER_RADIUS);
        inner = createCircle("inner-circle-rooms", INNER_RADIUS);
        dPane.getChildren().addAll(outer, inner);
        
        //Listener for keeping the scene responsive to screen size changes
        scene.widthProperty().addListener((obs, oldVal, newVal) -> adaptScene(scene, controller));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> adaptScene(scene, controller));
        
        //Control buttons
        createButtons(controller);

        return scene;
    }

    private void createButtons(GameController controller) {
        left = new ToggleButton("<");
        right = new ToggleButton(">");
        enter = new ToggleButton("ENTRA");
        
        buttons = new HBox(left, enter, right);
        buttons.getStyleClass().add("buttons");
        buttons.setAlignment(Pos.BOTTOM_CENTER);
        buttons.setPrefWidth(100);

        left.setMinWidth(buttons.getPrefWidth());
        right.setMinWidth(buttons.getPrefWidth());
        enter.setMinWidth(buttons.getPrefWidth());
        
        //When the left button is clicked, the player moves to the previous room
        left.setOnMouseClicked(e -> handleRoomChange(controller, -1));

        //When the right button is clicked, the player moves to the next room
        right.setOnMouseClicked(e -> handleRoomChange(controller, 1));

        //When the enter button is clicked, the player moves to the first room
        enter.setOnMouseClicked(e -> handleFloorEnter(controller));
        
        dPane.getChildren().add(buttons);
    }

    //when the room changes, the sector is highlighted
    private void handleRoomChange(GameController controller, int direction) {
        controller.changeRoom(direction);
        highlightSector(controller.getPlayerActualRoom());
    }

    private void handleFloorEnter(GameController controller) {
        controller.enterFirstRoom();
        highlightSector(controller.getPlayerActualRoom());
    }

    private Circle createCircle(String id, double radius) {
        Circle circle = new Circle(radius);
        circle.setId(id);
        return circle;
    }

    //Adapts the scene to the screen size
    private void adaptScene(Scene scene, GameController controller) {
        double centerX = scene.getWidth() / 2;
        double centerY = scene.getHeight() / 2.5;

        updateCirclePositionAndRadius(outer, centerX, centerY, Math.min(scene.getWidth(), scene.getHeight()) / 3);
        updateCirclePositionAndRadius(inner, centerX, centerY, Math.min(scene.getWidth(), scene.getHeight()) / 5);

        double roomRadius = (outer.getRadius() + inner.getRadius()) / 2;
        dPane.getChildren().removeIf(node -> node instanceof Arc || node instanceof Text || node instanceof Line || node instanceof HBox);

        buttons.setLayoutX(centerX - ((buttons.getPrefWidth() * 3) / 2));
        buttons.setLayoutY(scene.getHeight() / 1.1);
        
        dPane.getChildren().add(buttons);
        sectorMap.clear();
        controller.getRooms().forEach(room -> {
            createRoomAndSector(room.getId(), centerX, centerY, roomRadius);
        });
        inner.toFront();
    }

    private void updateCirclePositionAndRadius(Circle circle, double centerX, double centerY, double radius) {
        circle.setCenterX(centerX);
        circle.setCenterY(centerY);
        circle.setRadius(radius);
    }

    private void createRoomAndSector(int roomIndex, double centerX, double centerY, double roomRadius) {
        int ANGLE_COMPENSATION = 35;
        double angle = 2 * Math.PI / nRooms * roomIndex;
        double x = centerX + roomRadius * Math.cos(angle) - ANGLE_COMPENSATION;
        double y = centerY + roomRadius * Math.sin(angle) - ANGLE_COMPENSATION;

        // Room label
        dPane.getChildren().add(createRoomLabel(x, y, roomIndex));

        Arc sector = createSector(centerX, centerY, outer.getRadius(), roomIndex);

        //add the sector to the map for highlighting
        sectorMap.put(roomIndex, sector);
        dPane.getChildren().add(sector);

        dPane.getChildren().add(createDivisionLine(centerX, centerY, angle));
    }

    private void highlightSector(int roomIndex) {
        // Reset all previous highlights
        sectorMap.values().forEach(sector -> sector.setFill(null));
        
        // Highlight the selected sector
        Arc selectedSector = sectorMap.get(roomIndex);
        if (selectedSector != null) {
            selectedSector.setFill(Color.YELLOW);
        }
    }

    private Text createRoomLabel(double x, double y, int roomIndex) {
        Text label = new Text(x + 10, y + 25, "R" + (roomIndex +1));
        label.setFill(Color.WHITE);
        return label;
    }

    private Arc createSector(double centerX, double centerY, double outerRadius, int roomIndex) {
        double ANGLE_COMPENSATION = 26.5;
        double startAngle = (nRooms - roomIndex - 1) * (360.0 / nRooms);
        startAngle = startAngle + ANGLE_COMPENSATION;
        double sectorLength = 360.0 / nRooms;

        Arc sector = new Arc(centerX, centerY, outerRadius, outerRadius, startAngle, sectorLength);
        sector.getStyleClass().add("sector");
        sector.setType(ArcType.ROUND);
        return sector;
    }

    private Line createDivisionLine(double centerX, double centerY, double angle) {
        angle = angle-90;
        double startX = centerX + inner.getRadius() * Math.cos(angle + Math.PI / 2); // Cerchio interno
        double startY = centerY + inner.getRadius() * Math.sin(angle + Math.PI / 2);
        double endX = centerX + outer.getRadius() * Math.cos(angle + Math.PI / 2);   // Cerchio esterno
        double endY = centerY + outer.getRadius() * Math.sin(angle + Math.PI / 2);

        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(Color.WHITE);
        return line;
    }
}