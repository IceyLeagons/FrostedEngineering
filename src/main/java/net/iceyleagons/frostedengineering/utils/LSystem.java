package net.iceyleagons.frostedengineering.utils;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Wikipedia sure is useful.
 *
 * @author Gabe
 * @version 1.0
 */
public class LSystem {

    /**
     * The starting string
     */
    @Getter
    final String axiom;

    /**
     * The provided ruleset
     */
    @Getter
    final Ruleset ruleset;

    /**
     * Creates a new L-System instance.
     *
     * @param axiom   the starting point of the derivation.
     * @param ruleset the set of rules.
     */
    public LSystem(String axiom, Ruleset ruleset) {
        this.axiom = axiom;
        this.ruleset = ruleset;
    }

    /**
     * End result of a string produced by the provided rule set.
     *
     * @param steps the number of iterations.
     * @return the completed string.
     */
    public String derive(int steps) {
        String result = axiom;
        for (int i = 0; i < steps; i++) {
            StringBuilder tmp = new StringBuilder();
            for (int j = 0; j < result.length(); j++)
                tmp.append(ruleset.getByV("" + tmp.charAt(j)).getP());
            result = tmp.toString();
        }

        return result;
    }

    /**
     * Contains a set of rules. Pretty self-explanatory if I do say so myself.
     *
     * @author Gabe
     * @version 1.0
     */
    public static class Ruleset {
        private final ArrayList<Rule> ruleArrayList = new ArrayList<>();

        /**
         * <b>Contains a set of rules.</b>
         *
         * @param rules the initial rule set.
         */
        public Ruleset(Rule... rules) {
            addRules(rules);
        }

        /**
         * Adds a singular rule to the rule set.
         *
         * @param rule the rule to add to the rule set.
         */
        public void addRule(Rule rule) {
            ruleArrayList.add(rule);
        }

        /**
         * Adds multiple rules to the rule set.
         *
         * @param rules the rules to add to the rule set.
         */
        public void addRules(Rule... rules) {
            Arrays.asList(rules).forEach(this::addRule);
        }

        /**
         * Gets a rule from the array list by V.
         *
         * @param V the symbols.
         * @return the rule matching the given V.
         */
        public Rule getByV(String V) {
            return ruleArrayList.stream().parallel().filter(rule -> rule.getV().equals(V)).findFirst().get();
        }
    }

    /**
     * Holds L-System variables.
     *
     * @author Gabe
     * @version 1.0
     */
    public static class Rule {

        /**
         * Replaceable symbols.
         */
        @Getter
        private final String V;

        /**
         * Production rules
         */
        @Getter
        private final String P;

        /**
         * <b>According to Wikipedia, the free encyclopedia</b>
         *
         * @param V the set of symbols containing both irreplaceable and replaceable elements.
         * @param P the production rules.
         */
        public Rule(String V, String P) {
            this.V = V;
            this.P = P;
        }
    }
}
