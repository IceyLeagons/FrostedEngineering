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
package old.vegetationold.genes.types;

import java.util.HashMap;

import old.vegetationold.Genes;
import old.vegetationold.genes.types.interfaces.IAngleGene;
import old.vegetationold.genes.types.interfaces.IClumpGene;
import old.vegetationold.genes.types.interfaces.IDecayGene;
import old.vegetationold.genes.types.interfaces.ILeafGene;
import old.vegetationold.genes.types.interfaces.IRootGene;
import old.vegetationold.genes.types.interfaces.ISplitGene;
import old.vegetationold.genes.types.interfaces.ITrunkGene;

public interface IGenome {

    IClumpGene getClump();

    ISplitGene getSplit();

    IAngleGene getAngle();

    IDecayGene getDecay();

    ITrunkGene getTrunk();

    ILeafGene getLeaf();

    IRootGene getRoot();

    HashMap<Double, Genes> getGenes();

}
