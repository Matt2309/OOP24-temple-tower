package it.unibo.templetower.model;

/**
 * Using strategy, is possible to extend this class, creating different action by only ovveride the {@code interact} method
 */
public interface RoomBehavior {
    void interact(Player player);
}
