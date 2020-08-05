package net.iceyleagons.frostedengineering.utils;

import fastnoise.MathUtils.Vector3;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import net.iceyleagons.frostedengineering.vegetation.Branch;
import net.iceyleagons.frostedengineering.vegetation.Leaf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;

import static fastnoise.MathUtils.*;

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
     * Processes V with the provided options.
     *
     * @param V       derived l-system scheme.
     * @param options options for generation.
     * @return instructions for branches and leaves.
     */
    public CompletableFuture<Instructions> process(String V, Options options) {
        return CompletableFuture.supplyAsync(() -> {
            Instructions instructions = new Instructions();
            Branch mainBranch = null;
            Vector3 position = new Vector3(0.f, 0.f, 0.f);
            Rotation rotation = new Rotation(0.f, 0.f, 0.f);

            LinkedList<PositionData> memory = new LinkedList<>();
            for (char v : V.toCharArray()) {
                switch (v) {
                    case 'B':
                        Vector3 oldPos = position;
                        position = Vector3.mul(position, Vector3.add(rotation.toVector(), new Vector3((float) Math.random() * options.angleVariation,
                                (float) Math.random() * options.angleVariation, (float) Math.random() * options.angleVariation)));
                        Branch branch = new Branch(mainBranch, rotation.realVector(), oldPos);
                        instructions.getBranches().add(branch);
                        mainBranch = branch;
                        break;
                    case 'L':
                        instructions.getLeaves().add(new Leaf(Vector3.mul(position, Vector3.add(rotation.toVector(), new Vector3((float) Math.random() * options.angleVariation,
                                (float) Math.random() * options.angleVariation, (float) Math.random() * options.angleVariation)))));
                        break;
                    case '<':
                        rotation.addRotation(-options.twist, RotationAxis.Y);
                        break;
                    case '>':
                        rotation.addRotation(options.twist, RotationAxis.Y);
                        break;
                    case '+':
                        rotation.addRotation(options.vTilt, RotationAxis.Z);
                        break;
                    case '-':
                        rotation.addRotation(-options.vTilt, RotationAxis.Z);
                        break;
                    case '\\':
                        rotation.addRotation(-options.uTilt, RotationAxis.X);
                        break;
                    case '/':
                        rotation.addRotation(options.uTilt, RotationAxis.X);
                        break;
                    case '[':
                        memory.add(new PositionData(mainBranch, rotation, position));
                        break;
                    case ']':
                        PositionData data = memory.removeLast();
                        mainBranch = data.parent;
                        position = data.position;
                        rotation = data.rotation;
                        break;
                    default:
                        break;
                }
            }

            return instructions;
        });
    }

    /**
     * Basic data class containing data about the position.
     */
    @Data
    @AllArgsConstructor
    static
    class PositionData {
        /**
         * Parent of the branch.
         */
        Branch parent;
        /**
         * Rotation of the branch.
         */
        Rotation rotation;
        /**
         * Position of the branch.
         */
        Vector3 position;
    }

    /**
     * Basic data class containing options for the l-system.
     */
    @Data
    @AllArgsConstructor
    public static class Options {
        /**
         * The rotation value in the \/ (local x-axis)
         */
        float uTilt;
        /**
         * The rotation value in the +- (local z-axis)
         */
        float vTilt;
        /**
         * The rotation value in the <> (local y-axis)
         */
        float twist;
        /**
         * The value for randomness in the angle of a branch
         */
        float angleVariation;
        /**
         * The value for randomness in the length of a branch
         */
        float lengthVariation;
    }

    /**
     * Basic data class containing information about the l-system tree.
     */
    public static class Instructions {
        /**
         * The list of branches.
         */
        @Getter
        ArrayList<Branch> branches = new ArrayList<>();
        /**
         * The list of leaves.
         */
        @Getter
        ArrayList<Leaf> leaves = new ArrayList<>();
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
            return ruleArrayList.stream().parallel().filter(rule -> rule.getV().equals(V)).findFirst().orElse(null);
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
