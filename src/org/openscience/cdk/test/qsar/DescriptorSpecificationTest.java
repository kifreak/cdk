/* $Revision: 7691 $ $Author: egonw $ $Date: 2007-01-11 12:47:48 +0100 (Thu, 11 Jan 2007) $
 * 
 * Copyright (C) 2007  Egon Willighagen <egonw@users.sf.net>
 * 
 * Contact: cdk-devel@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA. 
 */
package org.openscience.cdk.test.qsar;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.openscience.cdk.qsar.DescriptorSpecification;
import org.openscience.cdk.test.CDKTestCase;

/**
 * @cdk.module test-standard
 */
public class DescriptorSpecificationTest extends CDKTestCase {
    
    public DescriptorSpecificationTest(String name) {
        super(name);
    }
    
	public static Test suite() {
		return new TestSuite(DescriptorSpecificationTest.class);
	}
	
	private final static String DESC_REF = "bla"; 
	private final static String DESC_IMPL_TITLE = "bla2"; 
	private final static String DESC_IMPL_VENDOR = "bla3"; 
	private final static String DESC_IMPL_ID = "bla4"; 
	
	public void testDescriptorSpecification_String_String_String_String() {
		DescriptorSpecification spec = new DescriptorSpecification(
            DESC_REF, DESC_IMPL_TITLE, DESC_IMPL_ID, DESC_IMPL_VENDOR
		);
		assertNotNull(spec);
	}

	public void testGetImplementationVendor() {
		DescriptorSpecification spec = new DescriptorSpecification(
            DESC_REF, DESC_IMPL_TITLE, DESC_IMPL_ID, DESC_IMPL_VENDOR
		);
		assertEquals(DESC_IMPL_VENDOR, spec.getImplementationVendor());
	}

	public void testGetSpecificationReference() {
		DescriptorSpecification spec = new DescriptorSpecification(
            DESC_REF, DESC_IMPL_TITLE, DESC_IMPL_ID, DESC_IMPL_VENDOR
		);
		assertEquals(DESC_REF, spec.getSpecificationReference());
	}

	public void testGetImplementationIdentifier() {
		DescriptorSpecification spec = new DescriptorSpecification(
            DESC_REF, DESC_IMPL_TITLE, DESC_IMPL_ID, DESC_IMPL_VENDOR
		);
		assertEquals(DESC_IMPL_ID, spec.getImplementationIdentifier());
	}

	public void testGetImplementationTitle() {
		DescriptorSpecification spec = new DescriptorSpecification(
            DESC_REF, DESC_IMPL_TITLE, DESC_IMPL_ID, DESC_IMPL_VENDOR
		);
		assertEquals(DESC_IMPL_TITLE, spec.getImplementationTitle());
	}

}


