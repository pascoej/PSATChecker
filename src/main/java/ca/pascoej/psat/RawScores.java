package ca.pascoej.psat;

/**
 * Created by john on 12/5/14.
 */
public class RawScores {
    static class Subscore {
        int correct,incorrect,omitted;

        public Subscore(int correct, int incorrect, int omitted) {
            this.correct = correct;
            this.incorrect = incorrect;
            this.omitted = omitted;
        }

        public int getCorrect() {
            return correct;
        }

        public int getIncorrect() {
            return incorrect;
        }

        public int getOmitted() {
            return omitted;
        }

        @Override
        public String toString() {
            return "Subscore{" +
                    "omitted=" + omitted +
                    ", incorrect=" + incorrect +
                    ", correct=" + correct +
                    '}';
        }
    }

    private Subscore math,writing,reading;

    public RawScores(Subscore math, Subscore writing, Subscore reading) {
        this.math = math;
        this.writing = writing;
        this.reading = reading;
    }

    public Subscore getMath() {
        return math;
    }

    public Subscore getWriting() {
        return writing;
    }

    public Subscore getReading() {
        return reading;
    }
    @Override
    public String toString() {
        return "RawScores{" +
                "reading=" + reading +
                ", writing=" + writing +
                ", math=" + math +
                '}';
    }
}
