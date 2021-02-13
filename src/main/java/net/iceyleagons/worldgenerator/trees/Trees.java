package net.iceyleagons.worldgenerator.trees;

import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChainFactory;
import lombok.*;
import net.iceyleagons.worldgenerator.WorldGenerator;
import net.iceyleagons.worldgenerator.biome.BiomeManager;
import net.iceyleagons.worldgenerator.trees.data.Branch;
import net.iceyleagons.worldgenerator.trees.data.Cluster;
import net.iceyleagons.worldgenerator.trees.data.Leaf;
import net.iceyleagons.worldgenerator.trees.data.Root;
import net.iceyleagons.worldgenerator.utils.FunctionIterator;
import net.iceyleagons.worldgenerator.utils.BData;
import net.iceyleagons.worldgenerator.utils.SegmentIterator;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.util.Vector;

import java.util.*;

import static net.iceyleagons.worldgenerator.trees.genes.RootGene.getRandomPerpVector;

@EqualsAndHashCode
@RequiredArgsConstructor(staticName = "create")
public class Trees {
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @Data
    public static class TreeConstructor {
        final Cluster cluster;

        final Branch root;
        final List<Root> roots;
        final List<List<Branch>> branches;
        final List<List<Leaf>> leaves;

        @Getter
        @RequiredArgsConstructor(staticName = "create")
        public static class Builder {
            @NonNull
            private final Cluster cluster;

            private int iterations = 0;
            private int totalIterations = 6;

            public Builder setIterations(int n) {
                this.totalIterations = n;
                return this;
            }

            public TreeConstructor build() {
                List<Root> roots = new ArrayList<>();
                List<List<Branch>> branches = new ArrayList<>();
                List<List<Leaf>> leaves = new ArrayList<>();

                Branch trunk = cluster.getTrunk().next();
                Leaf trunkLeafCluster = getLeaf(trunk);
                List<Leaf> phaseLeafCluster = new ArrayList<>();
                if (trunkLeafCluster != null)
                    phaseLeafCluster.add(trunkLeafCluster);

                leaves.add(phaseLeafCluster);
                List<Branch> prevLayer = Collections.singletonList(trunk);
                branches.add(null);

                for (int i = 0; i < totalIterations; i++) {
                    prevLayer = iterate(prevLayer);
                    branches.add(prevLayer);
                    List<Leaf> phaseLeafClusters = new ArrayList<>();
                    for (Branch branch : prevLayer) {
                        Leaf branchLeafCluster = getLeaf(branch);
                        if (branchLeafCluster != null)
                            phaseLeafClusters.add(branchLeafCluster);
                    }

                    leaves.add(phaseLeafClusters);
                }

                for (int i = 0; i < cluster.getRoot().nextAmount(); i++)
                    roots.add(cluster.getRoot().next(trunk));

                return new TreeConstructor(cluster, trunk, roots, branches, leaves);
            }

            private List<Branch> iterate(List<Branch> branchLayer) {
                List<Branch> outerLayer = new ArrayList<>();

                for (Branch branch : branchLayer)
                    outerLayer.addAll(splitBranch(branch, cluster.getSplit().next()));

                iterations++;
                return outerLayer;
            }

            /**
             * Splits a branch into multiple branch segments.
             *
             * @param branch the branch to split
             * @param n      the amount of branches it needs to be split into.
             * @return a list with the branch split.
             */
            private List<Branch> splitBranch(Branch branch, int n) {
                List<Branch> childBranches = new ArrayList<>();
                Vector perpetualVector = getRandomPerpVector(branch.getDirection());
                double totalRadians = Math.PI * 2 * (1 - cluster.getClump().next());

                for (int i = 0; i < n; i++) {
                    double angle = cluster.getAngle().next();
                    Vector starting = perpetualVector.clone().rotateAroundAxis(branch.getDirection(), i * totalRadians / (double) n);
                    Vector axis = starting.clone().rotateAroundAxis(branch.getDirection(), i * totalRadians / (double) n);
                    double decay = cluster.getDecay().next(angle, cluster.getAngle().getRange());
                    Branch child = branch.split(axis, angle, decay);

                    if (child != null)
                        childBranches.add(child);
                }

                return childBranches;
            }

            /**
             * Returns a leaf if possible to create one at that position.
             *
             * @param branch the branch the leaves are branching off of
             * @return null if impossible, a leaf object otherwise.
             */
            private Leaf getLeaf(Branch branch) {
                if (branch.getRadius() <= cluster.getLeaf().getMaxBranchRadius())
                    return new Leaf(branch.getEnd(), branch.getRadius() * cluster.getLeaf().getRadiusMultiplier());

                return null;
            }

        }
    }

    @NonNull
    private final Location location;
    @NonNull
    private final TreeConstructor constructor;

    private final Map<Location, BlockData> blockMap = new HashMap<>();
    private static final TaskChainFactory taskChainFactory = BukkitTaskChainFactory.create(WorldGenerator.MAIN.getPlugin());

    private void grow() {
        int phase = 0;
        boolean finished = false;
        while (!finished) {
            finished = true;

            if (phase == 0) {
                finished = false;
                buildBranch(constructor.getRoot());
            }

            if (phase == 1) {
                finished = false;
                for (Root root : constructor.getRoots())
                    buildRoot(root);
            }

            if (constructor.getBranches().size() > phase) {
                finished = false;

                List<Branch> branches = constructor.getBranches().get(phase);
                if (branches != null)
                    for (Branch branch : branches)
                        buildBranch(branch);
            }

            if (constructor.getLeaves().size() > phase) {
                finished = false;

                List<Leaf> leaves = constructor.getLeaves().get(phase);
                if (leaves != null)
                    for (Leaf leafCluster : leaves)
                        buildLeaves(leafCluster);
            }

            phase++;
        }

    }

    private void buildBranch(Branch branch) {
        int thickness = branch.getThickness() == Branch.Thickness.THICK ? 1 : 0;
        for (Location block : new SegmentIterator(location.getWorld(), branch.getBegin().add(location.toVector()), branch.getEnd().add(location.toVector()), branch.getRadius()))
            placeBranch(thickness, block);
    }

    private void placeBranch(int thickness, Location location) {
        BData[] mats = thickness == 1 ? constructor.getCluster().getThickMaterial() : constructor.getCluster().getThinMaterial();

        if (mats.length == 1)
            blockMap.put(location, mats[0].getBlockData());
        else
            blockMap.put(location, mats[BiomeManager.random.nextInt(mats.length)].getBlockData());
    }

    private void buildRoot(Root root) {
        for (Location location : new FunctionIterator(location.getWorld(), root.getOrigin().add(location.toVector()), root.getPlane(), root.getLength(), root.getRadius(), root.getFunction(), root.getFunction().getA(), root.getFunction().getB()))
            placeRoot(location);
    }

    private void placeRoot(Location location) {
        BData[] mats = constructor.getCluster().getRootMaterial();

        if (mats.length == 1)
            blockMap.put(location, mats[0].getBlockData());
        else
            blockMap.put(location, mats[BiomeManager.random.nextInt(mats.length)].getBlockData());
    }

    private void buildLeaves(Leaf leaf) {
        double radius = leaf.getRadius();
        Vector center = leaf.getCenter().add(location.toVector());

        for (int x = (int) radius * -1; x < radius + 0.5; x++)
            for (int y = (int) (radius * -0.5); y < radius + 0.5; y++)
                for (int z = (int) radius * -1; z < radius + 0.5; z++) {
                    Vector current = center.clone().add(new Vector(x, y, z));
                    if (center.distance(current) <= radius + 0.5)
                        attemptLeaf(current);
                }
    }

    private void attemptLeaf(Vector current) {
        Location location = current.toLocation(this.location.getWorld());
        BData[] mats = constructor.getCluster().getLeafMaterial();

        if (mats.length == 1)
            blockMap.put(location, mats[0].getBlockData());
        else
            blockMap.put(location, mats[BiomeManager.random.nextInt(mats.length)].getBlockData());
    }


    private void paste() {
        blockMap.forEach((loc, data) -> loc.getBlock().setBlockData(data, false));
    }

    public void place() {
        taskChainFactory.newChain()
                .async(this::grow)
                .sync(this::paste)
                .execute();
    }

}
