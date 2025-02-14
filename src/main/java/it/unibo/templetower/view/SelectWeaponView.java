package it.unibo.templetower.view;

import it.unibo.templetower.controller.GameController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * {@inheritDoc}.
 */
public class SelectWeaponView {
    private static final int VBOX_SPACING = 20;

    /**
     * Creates and returns the change weapon scene with all necessary UI elements.
     * 
     * @param manager the scene manager to handle scene transitions
     * @param controller
     * @return the created change weapon scene
     */
    public StackPane createScene(final SceneManager manager, final GameController controller) {
        final StackPane root = new StackPane();
        final VBox vbox = new VBox(VBOX_SPACING); // VBox with 20px spacing
        vbox.setAlignment(Pos.CENTER);

        final Label titleLabel = new Label("Select Weapon to USE");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: black;");

        vbox.getChildren().add(titleLabel);

        if (controller.getPlayerWeapons().size() == 1) {
            final Button weapon1 = new Button(controller.getPlayerWeapons().get(0).name() + " - Damage: "
                    + controller.getPlayerWeapons().get(0).attack().getY());
            weapon1.setOnAction(e -> {
                controller.changeWeaponIndex(0);
            });
            weapon1.setStyle("-fx-font-size: 20px; -fx-padding: 15px 30px;");
            vbox.getChildren().add(weapon1);
        }

        if (controller.getPlayerWeapons().size() > 1) {
            final Button weapon2 = new Button(controller.getPlayerWeapons().get(1).name() + " - Damage: "
                    + controller.getPlayerWeapons().get(1).attack().getY());
            weapon2.setOnAction(e -> {
                controller.changeWeaponIndex(1);
            });
            weapon2.setStyle("-fx-font-size: 20px; -fx-padding: 15px 30px;");
            vbox.getChildren().add(weapon2);
        }

        if (controller.getPlayerWeapons().size() > 2) {
            final Button weapon3 = new Button(controller.getPlayerWeapons().get(2).name() + " - Damage: "
                    + controller.getPlayerWeapons().get(2).attack().getY());
            weapon3.setOnAction(e -> {
                controller.changeWeaponIndex(2);
            });
            weapon3.setStyle("-fx-font-size: 20px; -fx-padding: 15px 30px;");
            vbox.getChildren().add(weapon3);
        }

        // Creiamo il nuovo bottone "Back"
        final Button backButton = new Button("Back");
        backButton.setStyle("-fx-font-size: 20px; -fx-padding: 10px 20px;");

        // Configura l'azione del bottone per tornare alla scena precedente
        backButton.setOnAction(e -> manager.switchTo("combat_view")); // Assicurati di sostituire "previous_scene" con
                                                                      // il nome della scena precedente

        // Aggiungiamo il bottone al VBox
        vbox.getChildren().add(backButton);

        root.getChildren().add(vbox);

        return root;
    }
}
