/*******************************************************************************
 * Copyright (C) IceyLeagons(https://iceyleagons.net/) 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package old.vegetationold.parts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import org.bukkit.util.Vector;

import fastnoise.MathUtils;
import old.vegetationold.genes.types.IGenome;
import old.vegetationold.genes.types.interfaces.IAngleGene;
import old.vegetationold.genes.types.interfaces.IClumpGene;
import old.vegetationold.genes.types.interfaces.IDecayGene;
import old.vegetationold.genes.types.interfaces.ILeafGene;
import old.vegetationold.genes.types.interfaces.IRootGene;
import old.vegetationold.genes.types.interfaces.ISplitGene;
import old.vegetationold.genes.types.interfaces.ITrunkGene;

public class FractalGrowthPattern {

    private ISplitGene splitGene;
    private IClumpGene clumpGene;
    private IAngleGene angleGene;
    private IDecayGene decayGene;
    private ITrunkGene trunkGene;
    private ILeafGene leafGene;
    private IRootGene rootGene;
    @SuppressWarnings("unused")
    private int iterations = 0;

    public FractalGrowthPattern(IGenome genome) {
        this.splitGene = genome.getSplit();
        this.clumpGene = genome.getClump();
        this.angleGene = genome.getAngle();
        this.decayGene = genome.getDecay();
        this.trunkGene = genome.getTrunk();
        this.leafGene = genome.getLeaf();
        this.rootGene = genome.getRoot();
    }

    public CompletableFuture<FractalBlueprint> generateBlueprint() {
        return CompletableFuture.supplyAsync(new Supplier<FractalBlueprint>() {

            @Override
            public FractalBlueprint get() {
                Branch trunk;
                List<Root> roots = new ArrayList<>();
                List<List<Branch>> branches = new ArrayList<>();
                List<List<LeafCluster>> leafClusters = new ArrayList<>();

                trunk = trunkGene.next();
                LeafCluster trunkLeafCluster = getCluster(trunk);
                List<LeafCluster> phaseLeafCluster = new ArrayList<>();
                if (trunkLeafCluster != null) {
                    phaseLeafCluster.add(trunkLeafCluster);
                }
                leafClusters.add(phaseLeafCluster);
                List<Branch> prevLayer = Collections.singletonList(trunk);
                branches.add(null);
                for (int i = 0; i < 6; i++) {
                    prevLayer = iterate(prevLayer);
                    branches.add(prevLayer);
                    List<LeafCluster> phaseLeafClusters = new ArrayList<>();
                    for (Branch branch : prevLayer) {
                        LeafCluster branchLeafCluster = getCluster(branch);
                        if (branchLeafCluster != null) {
                            phaseLeafClusters.add(branchLeafCluster);
                        }
                    }
                    leafClusters.add(phaseLeafClusters);
                }
                for (int i = 0; i < rootGene.nextAmount(); i++) {
                    roots.add(rootGene.next(trunk));
                }

                return new FractalBlueprint(trunk, roots, branches, leafClusters);
            }

        });
    }

    public List<Branch> iterate(List<Branch> branchLayer) {
        List<Branch> outerLayer = new ArrayList<>();
        for (Branch branch : branchLayer) {
            outerLayer.addAll(splitBranch(branch, splitGene.next()));
        }
        iterations++;
        return outerLayer;
    }

    private List<Branch> splitBranch(Branch branch, int numBranches) {
        List<Branch> childBranches = new ArrayList<>();
        Vector randomPerp = MathUtils.getRandomPerpVector(branch.getDirection());
        double totalRadians = Math.PI * 2 * (1 - clumpGene.next());
        for (int i = 0; i < numBranches; i++) {
            double angle = angleGene.next();
            Vector starting = MathUtils.getRotatedVector(randomPerp, branch.getDirection(),
                    i * totalRadians / numBranches);
            Vector axis = MathUtils.getRotatedVector(starting, branch.getDirection(), totalRadians / numBranches);
            double decay = decayGene.next(angle, angleGene.getBounds());
            Branch child = branch.split(axis, angle, decay);
            if (child != null) {
                childBranches.add(child);
            }
        }
        return childBranches;
    }

    private LeafCluster getCluster(Branch branch) {
        if (branch.getRadius() <= leafGene.getMaxBranchRadius()) {
            return new LeafCluster(branch.getEnd(), branch.getRadius() * leafGene.getRadiusMultiplier());
        } else {
            return null;
        }
    }

}
