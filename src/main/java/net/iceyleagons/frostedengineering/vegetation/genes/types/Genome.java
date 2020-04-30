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
package net.iceyleagons.frostedengineering.vegetation.genes.types;

import java.util.HashMap;

import net.iceyleagons.frostedengineering.vegetation.Genes;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.IAngleGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.IClumpGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.IDecayGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.ILeafGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.IRootGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.ISplitGene;
import net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces.ITrunkGene;

public class Genome implements IGenome {

    private IClumpGene clumpGene;
    private ISplitGene splitGene;
    private IAngleGene angleGene;
    private IDecayGene decayGene;
    private ITrunkGene trunkGene;
    private ILeafGene leafGene;
    private IRootGene rootGene;
    private HashMap<Double, Genes> genes;

    public Genome(IClumpGene clumpGene, ISplitGene splitGene, IAngleGene angleGene, IDecayGene decayGene,
                  ITrunkGene trunkGene, ILeafGene leafGene, IRootGene rootGene, HashMap<Double, Genes> ancestors) {
        this.clumpGene = clumpGene;
        this.splitGene = splitGene;
        this.angleGene = angleGene;
        this.decayGene = decayGene;
        this.trunkGene = trunkGene;
        this.leafGene = leafGene;
        this.rootGene = rootGene;
        this.genes = ancestors;
    }

    public Genome(IClumpGene clumpGene, ISplitGene splitGene, IAngleGene angleGene, IDecayGene decayGene,
                  ITrunkGene trunkGene, ILeafGene leafGene, IRootGene rootGene) {
        this.clumpGene = clumpGene;
        this.splitGene = splitGene;
        this.angleGene = angleGene;
        this.decayGene = decayGene;
        this.trunkGene = trunkGene;
        this.leafGene = leafGene;
        this.rootGene = rootGene;
    }

    public Genome(HashMap<Double, Genes> ancestors) {
        this.genes = ancestors;
    }

    @Override
    public IClumpGene getClump() {
        return clumpGene;
    }

    @Override
    public ISplitGene getSplit() {
        return splitGene;
    }

    @Override
    public IAngleGene getAngle() {
        return angleGene;
    }

    @Override
    public IDecayGene getDecay() {
        return decayGene;
    }

    @Override
    public ITrunkGene getTrunk() {
        return trunkGene;
    }

    @Override
    public ILeafGene getLeaf() {
        return leafGene;
    }

    @Override
    public IRootGene getRoot() {
        return rootGene;
    }

    @Override
    public HashMap<Double, Genes> getGenes() {
        return genes;
    }

}
