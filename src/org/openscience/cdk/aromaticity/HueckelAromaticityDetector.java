/*  $RCSfile$
 *  $Author$
 *  $Date$
 *  $Revision$
 *
 *  Copyright (C) 2002-2004  The Chemistry Development Kit (CDK) project
 *
 *  Contact: cdk-devel@lists.sourceforge.net
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2.1
 *  of the License, or (at your option) any later version.
 *  All we ask is that proper credit is given for our work, which includes
 *  - but is not limited to - adding the above copyright notice to the beginning
 *  of your source code files, and to any copyright notice that you may distribute
 *  with programs based on this work.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */
package org.openscience.cdk.aromaticity;

import org.openscience.cdk.*;
import org.openscience.cdk.ringsearch.*;
import java.util.Vector;
import java.io.*;

/**
 * The HuckelAromaticityDetector detects the aromaticity based on
 * the Hueckel 4n+2 pi-electrons Rule. This is done by one of the
 * detectAromaticity methods. They set the aromaticity flags of
 * appropriate Atoms, Bonds and Rings. After the detection, you
 * can use getFlag(CDKConstants.ISAROMATIC) on these ChemObjects.
 *
 * @cdkPackage standard
 *
 * @author     steinbeck, kaihartmann
 * @created    2001-09-04
 */
public class HueckelAromaticityDetector {
	static boolean debug = false;
	
	
	/**
	 * Retrieves the set of all rings and performs an aromaticity detection
	 * based on Hueckels 4n + 2 rule.
	 *
	 * @return  True if molecule is aromatic
	 */
	public static boolean detectAromaticity(AtomContainer ac) throws org.openscience.cdk.exception.NoSuchAtomException {
		return (detectAromaticity(ac, true));
	}
	
	
	/**
	 * Uses precomputed set of ALL rings and performs an aromaticity detection
	 * based on Hueckels 4n + 2 rule.
	 *
	 * @param   ringSet  set of ALL rings
	 * @return  True if molecule is aromatic
	 */
	public static boolean detectAromaticity(AtomContainer ac, RingSet ringSet) throws org.openscience.cdk.exception.NoSuchAtomException {
		return (detectAromaticity(ac, ringSet, true));
	}
	
	
	/**
	 * Retrieves the set of all rings and performs an aromaticity detection
	 * based on Hueckels 4n + 2 rule.
	 *
	 * @param   removeAromatictyFlags  Leaves ChemObjects that are already marked as aromatic as they are
	 * @return                         True if molecule is aromatic
	 */
	public static boolean detectAromaticity(AtomContainer ac, boolean removeAromatictyFlags) throws org.openscience.cdk.exception.NoSuchAtomException {
		if (debug) {
			System.out.println("Entered Aromaticity Detection");
		}
		if (debug) {
			System.out.println("Starting AllRingsFinder");
		}
		RingSet ringSet = new AllRingsFinder().findAllRings(ac);
		if (debug) {
			System.out.println("Finished AllRingsFinder");
		}
		if (ringSet.size() > 0) {
			return detectAromaticity(ac, ringSet, removeAromatictyFlags);
		}
		return false;
	}
	
	
	/**
	 * Uses precomputed set of ALL rings and performs an aromaticity detection
	 * based on Hueckels 4n + 2 rule.
	 *
	 * @param  ringSet                 set of ALL rings
	 * @param  removeAromaticityFlags  Leaves ChemObjects that are already marked as aromatic as they are
	 * @return                         True if molecule is aromatic
	 */
	public static boolean detectAromaticity(AtomContainer ac, RingSet ringSet, boolean removeAromaticityFlags) {
		boolean foundSomething = false;
		if (removeAromaticityFlags) {
			for (int f = 0; f < ac.getAtomCount(); f++) {
				ac.getAtomAt(f).setFlag(CDKConstants.ISAROMATIC, false);
			}
			for (int f = 0; f < ac.getElectronContainerCount(); f++) {
				ElectronContainer ec = ac.getElectronContainerAt(f);
				if (ec instanceof Bond) {
					ec.setFlag(CDKConstants.ISAROMATIC, false);
				}
			}
			for (int f = 0; f < ringSet.size(); f++) {
				((Ring) ringSet.get(f)).setFlag(CDKConstants.ISAROMATIC, false);
			}
		}
		
		Ring ring = null;
		ringSet.sort();
		for (int f = 0; f < ringSet.size(); f++) {
			ring = (Ring) ringSet.elementAt(f);
			if (debug) {
				System.out.println("Testing ring no " + f + " for aromaticity:");
			}
			if (AromaticityCalculator.isAromatic(ring, ac)) {
				ring.setFlag(CDKConstants.ISAROMATIC, true);
				
				for (int g = 0; g < ring.getAtomCount(); g++) {
					ring.getAtomAt(g).setFlag(CDKConstants.ISAROMATIC, true);
				}
				
				for (int g = 0; g < ring.getElectronContainerCount(); g++) {
					ElectronContainer ec = ring.getElectronContainerAt(g);
					if (ec instanceof Bond) {
						ec.setFlag(CDKConstants.ISAROMATIC, true);
					}
				}
				
				foundSomething = true;
				if (debug) {
					System.out.println("Ring no " + f + " is aromatic.");
				}
			} else {
				if (debug) {
					System.out.println("Ring no " + f + " is not aromatic.");
				}
			}
		}
		return foundSomething;
	}

	/*
	 *  public static boolean isAromatic(AtomContainer ac, Ring ring)
	 *  {
	 *  return AromaticityCalculator.isAromatic(ring, ac);
	 *  }
	 *  *	public static boolean isAromatic(AtomContainer ac, Ring ring)
	 *  {
	 *  int piElectronCount = 0;
	 *  int freeElectronPairCount = 0;
	 *  Atom atom = null;
	 *  Bond bond = null;
	 *  int aromaCounter = 0;
	 *  if (debug) System.out.println("isAromatic() -> ring.size(): " + ring.getAtomCount());
	 *  for (int g = 0; g < ring.getAtomCount(); g++)
	 *  {
	 *  atom = ring.getAtomAt(g);
	 *  if ("O-N-S-P".indexOf(atom.getSymbol()) > -1)
	 *  {
	 *  freeElectronPairCount += 1;
	 *  }
	 *  if (atom.getFlag(CDKConstants.ISAROMATIC))
	 *  {
	 *  aromaCounter ++;
	 *  }
	 *  }
	 *  for (int g = 0; g < ring.getElectronContainerCount(); g++) {
	 *  ElectronContainer ec = ring.getElectronContainerAt(g);
	 *  if (ec instanceof Bond) {
	 *  bond = (Bond)ec;
	 *  if (bond.getOrder() > 1) {
	 *  piElectronCount += 2*(bond.getOrder()-1);
	 *  }
	 *  }
	 *  }
	 *  for (int f = 0; f < ((ring.getAtomCount() - 2)/4) + 2; f ++)
	 *  {
	 *  if (debug) System.out.println("isAromatic() -> freeElectronPairCount: " + freeElectronPairCount);
	 *  if (debug) System.out.println("isAromatic() -> piElectronCount: " + piElectronCount);
	 *  if (debug) System.out.println("isAromatic() -> f: " + f);
	 *  if (debug) System.out.println("isAromatic() -> (4 * f) + 2: " + ((4 * f) + 2));
	 *  if (debug) System.out.println("isAromatic() -> ring.size(): " + ring.getAtomCount());
	 *  if (debug) System.out.println("isAromatic() -> aromaCounter: " + aromaCounter);
	 *  if (aromaCounter == ring.getAtomCount()) return true;
	 *  else if ((piElectronCount == ring.getAtomCount())&&((4 * f) + 2 == piElectronCount)) return true;
	 *  else if ((4 * f) + 2 == piElectronCount + (freeElectronPairCount * 2) && ring.getAtomCount() < piElectronCount + (freeElectronPairCount * 2)) return true;
	 *  }
	 *  return false;
	 *  }
	 */
}

