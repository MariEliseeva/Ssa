package alekhina_eliseeva.ssa.controller;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class RatingLine {
    public Integer SCORE;
    public String NAME;

    RatingLine() {
    }

    RatingLine(String name, Integer score) {
        NAME = name;
        SCORE = score;
    }

    public String getName() {
        return NAME;
    }

    public int getScore() {
        return SCORE;
    }
}
