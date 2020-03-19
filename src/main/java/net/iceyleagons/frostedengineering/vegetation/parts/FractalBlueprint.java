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
package net.iceyleagons.frostedengineering.vegetation.parts;

import java.util.List;

public class FractalBlueprint {

	private Branch trunk;
	private List<Root> roots;
	private List<List<Branch>> branches;
	private List<List<LeafCluster>> leafClusters;

	public FractalBlueprint(Branch trunk, List<Root> roots, List<List<Branch>> branches,
			List<List<LeafCluster>> leafClusters) {
		this.trunk = trunk;
		this.roots = roots;
		this.branches = branches;
		this.leafClusters = leafClusters;
	}

	public Branch getTrunk() {
		return trunk;
	}

	public List<Root> getRoots() {
		return roots;
	}

	public List<List<Branch>> getBranches() {
		return branches;
	}

	public List<List<LeafCluster>> getLeafClusters() {
		return leafClusters;
	}

}
