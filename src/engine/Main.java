package engine;

public class Main {

    public static void main(String[] args) {
        Game game = new Game();
        try {
            game.init();
            game.run();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            game.destroy();
        }
    }
}
