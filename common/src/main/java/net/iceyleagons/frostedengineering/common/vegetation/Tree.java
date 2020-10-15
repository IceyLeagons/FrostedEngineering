/*
 *  Copyright (C) IceyLeagons(https://iceyleagons.net/)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package net.iceyleagons.frostedengineering.common.vegetation;

import fastnoise.MathUtils;
import fastnoise.MathUtils.Vector3;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.utils.LSystem;
import net.iceyleagons.frostedengineering.utils.LSystem.Options;
import net.iceyleagons.frostedengineering.utils.LSystem.Rule;
import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Fractal (and soon L-System) tree generator.
 *
 * @author Gabe
 * @version 1.0
 */
public class Tree {
    /**
     * Type definitions:
     * <p>
     * <b> - Type 0: Fractal</b>
     * <p>
     * <b> - Type 1: L-System</b>
     * <p>
     * <b>L-SYSTEM IS EXPERIMENTAL!</b>
     */
    int type;
    ArrayList<Vector3> branchBlocks = new ArrayList<>();
    HashMap<Vector3, Material> replaceMap = new HashMap<>();

    // L-System specific stuff
    String result, axiom;
    LSystem lSystem;
    int iterations;

    // Fractal specific stuff
    ArrayList<Branch> branches = new ArrayList<>();
    ArrayList<Leaf> leaves = new ArrayList<>();
    int leafWidth, leafHeight, leafDensity, branchMinLength, branchMaxLength, stemLength;

    /**
     * Initiates a new Fractal tree.
     *
     * @param leafWidth       the width of the leaves.
     * @param leafHeight      the height of the leaves.
     * @param leafDensity     the density of the leaves.
     * @param branchMinLength the minimum length of branches.
     * @param branchMaxLength the maximum length of branches.
     * @param stemLength      the length of the trees stem.
     */
    public Tree(int leafWidth, int leafHeight, int leafDensity, int branchMinLength, int branchMaxLength, int stemLength) {
        type = 0;
        this.leafDensity = leafDensity;
        this.leafHeight = leafHeight;
        this.leafWidth = leafWidth;
        this.branchMinLength = branchMinLength;
        this.branchMaxLength = branchMaxLength;
        this.stemLength = stemLength;
    }

    /**
     * Initiates a new L-System tree.
     *
     * @param axiom      the starting symbols.
     * @param iterations the number of l-system iterations to go thru.
     */
    public Tree(String axiom, int iterations) {
        type = 1;
        this.result = this.axiom = axiom;
        this.iterations = iterations;
        this.lSystem = new LSystem(axiom, new LSystem.Ruleset(new Rule("B", "BB-[-<B+>B+>B]+[+>L-<B-<B]"),
                new Rule("L", "BB-[-<B+>B+>B]+[+>L-<B-<B]")));
    }

    /*
     * Tree placement below \/
     */

    /**
     * Places down the new tree.
     *
     * @param block      the starting point.
     * @param parameters the tree parameters.
     */
    public void place(Block block, TreeParameters parameters) {
        switch (type) {
            case 1:
                final long start = System.currentTimeMillis();
                createTree().thenAccept(ignored -> {
                    render(block, parameters);
                    leaves(block, parameters);
                    roots(block, parameters);

                    new BukkitRunnable() {
                        @Override
                        public void run() {

                            replaceMap.forEach((vector3, material) -> {
                                Block blokk = block.getWorld().getBlockAt((int) vector3.x, (int) vector3.y, (int) vector3.z);
                                if (blokk.getType() != parameters.branchMaterial && blokk.getType() != parameters.rootMaterial)
                                    blokk.setType(material);

                                if (blokk.getState().getBlockData() instanceof Leaves) {
                                    Leaves leaves = (Leaves) blokk.getState().getBlockData();
                                    leaves.setPersistent(true);
                                    blokk.setBlockData(leaves);
                                }
                            });
                            System.out.println("Took " + (System.currentTimeMillis() - start) + " ms' to finish.");
                        }
                    }.runTask(Main.MAIN);
                });
                break;
            default:
            case 0:
                final long start2 = System.currentTimeMillis();
                createTree().thenAccept(ignore -> grow(parameters).thenAccept(ignored -> {
                    leaves.clear();
                    render(block, parameters);
                    leaves(block, parameters);
                    roots(block, parameters);
                    new BukkitRunnable() {
                        @Override
                        public void run() {

                            replaceMap.forEach((vector3, material) -> {
                                Block blokk = block.getWorld().getBlockAt((int) vector3.x, (int) vector3.y, (int) vector3.z);
                                if (blokk.getType() != parameters.branchMaterial && blokk.getType() != parameters.rootMaterial)
                                    blokk.setType(material);

                                if (blokk.getState().getBlockData() instanceof Leaves) {
                                    Leaves leaves = (Leaves) blokk.getState().getBlockData();
                                    leaves.setPersistent(true);
                                    blokk.setBlockData(leaves);
                                } else if (parameters.logRotate && blokk.getBlockData() instanceof Orientable) {
                                    Orientable orientable = (Orientable) blokk.getBlockData();

                                    switch (ThreadLocalRandom.current().nextInt(3) + 1) {
                                        default:
                                        case 1:
                                            orientable.setAxis(Axis.X);
                                            break;
                                        case 2:
                                            orientable.setAxis(Axis.Y);
                                            break;
                                        case 3:
                                            orientable.setAxis(Axis.Z);
                                            break;
                                    }

                                    blokk.setBlockData(orientable);
                                }
                            });
                            System.out.println("Took " + (System.currentTimeMillis() - start2) + " ms' to finish.");
                        }
                    }.runTask(Main.MAIN);
                }));
                break;
        }
    }

    /**
     * Creates the branches and leaves for the tree with the specified parameters.
     *
     * @param block      the main block.
     * @param parameters the parameters.
     */
    void render(Block block, TreeParameters parameters) {
        branches.forEach(branch -> {
            if (branch.getParent() != null) {
                ArrayList<Vector3> lineSegments = MathUtils.brasenhamLine(branch.getPosition(), branch.getParent().getPosition());

                for (Vector3 lineSegment : lineSegments) {
                    Vector3 linePos = new Vector3((int) lineSegment.x + block.getX(),
                            (int) lineSegment.y + block.getY() + 1, // idk why add one, but let's just do it.
                            (int) lineSegment.z + block.getZ());

                    replaceMap.put(linePos, parameters.branchMaterial);
                    branchBlocks.add(linePos);
                }
            }
        });
    }

    /**
     * Places down the rendered leaves.
     *
     * @param block      the main block.
     * @param parameters the parameters.
     */
    void leaves(Block block, TreeParameters parameters) {
        branchBlocks.forEach(branchBlock -> {
            if (branchBlock.getY() > block.getY() + this.stemLength / 5 * 3) {
                for (int i = 0; i < parameters.leafPerBranch; i++) {
                    int x = (int) (branchBlock.getX() + (int) (Math.random() * 3) - 1),
                            y = (int) (branchBlock.getY() + (int) (Math.random() * 3) - 1),
                            z = (int) (branchBlock.getZ() + (int) (Math.random() * 3) - 1);

                    replaceMap.put(new Vector3(x, y, z), parameters.leafMaterial);
                }
            }
        });
    }

    /**
     * Places down the roots.
     *
     * @param block      the main block.
     * @param parameters the parameters.
     */
    void roots(Block block, TreeParameters parameters) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < parameters.roots; i++) {
            int x = (int) (Math.random() * 3) - 1, y = random.nextInt(3) + 1, z = (int) (Math.random() * 3) - 1;

            ArrayList<Vector3> lineSegments = MathUtils.brasenhamLine(new Vector3((float) x, (float) y, (float) z), new Vector3(0.f, 0.f, 0.f));
            lineSegments.forEach(lineSegment -> {
                Vector3 linePos = new Vector3((int) lineSegment.x + block.getX(),
                        (int) lineSegment.y + block.getY() + 1,
                        (int) lineSegment.z + block.getZ());

                replaceMap.put(linePos, parameters.rootMaterial);
            });
        }
    }

    /*
     * Tree Parameters below \/
     */

    /**
     * Contains parameters to generate parameters.
     */
    @Data
    @AllArgsConstructor
    public static class TreeParameters {
        Material branchMaterial, leafMaterial, rootMaterial;
        int width, height, growIterations, leafPerBranch, roots;
        boolean logRotate;
    }

    /*
     * Tree structure generation below \/
     */

    /**
     * This function generates the tree structure.
     *
     * @return ignore.
     */
    CompletableFuture<Boolean> createTree() {
        return CompletableFuture.supplyAsync(() -> {
            switch (type) {
                case 1:
                    lSystem.process(lSystem.derive(iterations),
                            new Options(0.f, 22.f, 67.f, 0.2f, 0.f)).thenAccept(instructions -> {
                        branches.addAll(instructions.getBranches());
                        leaves.addAll(instructions.getLeaves());
                    });
                    break;
                default:
                case 0:
                    ThreadLocalRandom rng = ThreadLocalRandom.current();
                    for (int i = 0; i < leafDensity; ++i)
                        leaves.add(new Leaf(new Vector3(rng.nextInt(this.leafWidth) - Math.round((float) (this.leafWidth / 2)),
                                rng.nextInt(this.leafHeight - this.stemLength) + this.stemLength,
                                rng.nextInt(this.leafWidth) - Math.round((float) (this.leafWidth / 2)))));

                    // No parent -> root
                    Branch root = new Branch(null, new Vector3(0.f, 1.f, 0.f), new Vector3(0.f, -1.f, 0.f));
                    branches.add(root);

                    Branch trunk;
                    for (Branch current = new Branch(root); !isClose(current); current = trunk) {
                        trunk = new Branch(current);
                        branches.add(trunk);
                    }
                    break;
            }

            return true;
        });
    }

    /**
     * Checks whether or not the specified branch is close enough to the leaves.
     *
     * @param branch the branch to check.
     * @return whether or not it's close enough.
     */
    boolean isClose(Branch branch) {
        for (Leaf leaf : leaves)
            if (Vector3.distance(branch.getPosition(), leaf.getPosition()) < branchMaxLength)
                return true;

        return false;
    }

    /**
     * Starts calculating the trees growth.
     *
     * @param parameters the parameters.
     * @return ignore result.
     */
    CompletableFuture<Boolean> grow(TreeParameters parameters) {
        return CompletableFuture.supplyAsync(() -> {
            for (int k = 0; k < parameters.growIterations; k++) {
                for (Leaf l : leaves) {
                    Branch closestBranch = null;
                    Vector3 closestDir = null;
                    double record = 100000.0;
                    for (Branch b : branches) {
                        Vector3 dir = Vector3.sub(l.getPosition(), b.getPosition());
                        double d = Vector3.length(dir);
                        if (d < branchMinLength) {
                            l.reached = true;
                            closestBranch = null;
                            break;
                        }

                        if (d <= branchMaxLength)
                            if (closestBranch == null || d < record) {
                                closestBranch = b;
                                closestDir = dir;
                                record = d;
                            }
                    }

                    if (closestBranch != null) {
                        closestDir = Vector3.normalize(closestDir);
                        Vector3 v = Vector3.add(closestBranch.getDirection(), closestDir);
                        closestBranch = new Branch(closestBranch.getParent(), v, closestBranch.getPosition());
                        closestBranch.setDirection(Vector3.add(closestDir, closestBranch.getDirection()));
                        Branch branch = closestBranch;
                        branch.count++;
                        branches.add(closestBranch);
                    }
                }

                for (int i = leaves.size() - 1; i >= 0; i--)
                    if (leaves.get(i).reached)
                        leaves.remove(i);

                Branch b2;
                for (int i = branches.size() - 1; i >= 0; i--)
                    if ((b2 = branches.get(i)).count > 0) {
                        b2.setDirection(Vector3.normalize(Vector3.div(b2.getDirection(), b2.count)));
                        branches.add(new Branch(b2));
                        b2.reset();
                    }
            }
            return true;
        });
    }
}
