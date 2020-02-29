package net.iceyleagons.frostedengineering.vegetation.genes.types.interfaces;

import net.iceyleagons.frostedengineering.vegetation.parts.Branch;
import net.iceyleagons.frostedengineering.vegetation.parts.Root;

public interface IRootGene {
	Root next(Branch trunk);

	int nextAmount();
}
