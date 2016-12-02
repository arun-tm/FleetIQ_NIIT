/* UnderlinedAtom.java
 * =========================================================================
 * This file is originally part of the JMathTeX Library - http://jmathtex.sourceforge.net
 * 
 * Copyright (C) 2004-2007 Universiteit Gent
 * Copyright (C) 2009 DENIZET Calixte
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 * 
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program (see
 * the META-INF directory in the source jar). This license can also be
 * found on the GNU website at http://www.gnu.org/licenses/gpl.html.
 * 
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 * 
 */

package com.teramatrix.demo.JLatex.core;

/**
 * An atom representing another atom with a line under it.
 */
class UnderlinedAtom extends Atom {

	// the base to be underlined
	private final Atom base;

	public UnderlinedAtom(Atom f) {
		base = f;
		type = TeXConstants.TYPE_ORDINARY; // for spacing rules
	}

	public Box createBox(TeXEnvironment env) {
		float drt = env.getTeXFont().getDefaultRuleThickness(env.getStyle());

		// create formula box in same style
		Box b = (base == null ? new StrutBox(0, 0, 0, 0) : base.createBox(env));

		// create vertical box
		VerticalBox vBox = new VerticalBox();
		vBox.add(b);
		vBox.add(new StrutBox(0, 3 * drt, 0, 0));
		vBox.add(new HorizontalRule(drt, b.getWidth(), 0));

		// baseline vertical box = baseline box b
		// there's also an invisible strut of height drt under the rule
		vBox.setDepth(b.getDepth() + 5 * drt);
		vBox.setHeight(b.getHeight());

		return vBox;
	}
}