package alekhina_eliseeva.ssa.controller;

public class RatingLine {
    private final String NAME;
    private final int SCORE;

    RatingLine(String name, int score) {
        NAME = name;
        SCORE = score;
    }

    String getName() {
        return NAME;
    }

    int getScore() {
        return SCORE;
    }
}
