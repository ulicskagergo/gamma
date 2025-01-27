/********************************************************************************
 * Copyright (c) 2020-2021 Contributors to the Gamma project
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * SPDX-License-Identifier: EPL-1.0
 ********************************************************************************/
package hu.bme.mit.gamma.scenario.statechart.util;

import hu.bme.mit.gamma.scenario.model.LoopCombinedFragment;
import hu.bme.mit.gamma.statechart.interface_.Port;
import hu.bme.mit.gamma.util.GammaEcoreUtil;

public class ScenarioStatechartUtil {

	public static final ScenarioStatechartUtil INSTANCE = new ScenarioStatechartUtil();

	protected ScenarioStatechartUtil() {
	}
	
	protected final GammaEcoreUtil ecoreUtil = GammaEcoreUtil.INSTANCE;

	private final String reversed = "REVERSED";

	private final String coldViolation = "coldViolation";

	private final String hotViolation = "hotViolation";

	private final String Accepting = "AcceptingState";

	private final String initial = "Initial";
	
	private final String LoopVariable = "LoopIteratingVariable";

	private final String result = "result";
	
	private final String IteratingVariable = "IteratingVariable";

	public String getIteratingVariable() {
		return IteratingVariable;
	}

	public String getResult() {
		return result;
	}

	public boolean isTurnedOut(Port p) {
		return p.getName().endsWith(reversed);
	}

	public String getTurnedOutPortName(Port p) {
		if (isTurnedOut(p)) {
			return p.getName().substring(0, p.getName().length() - reversed.length());
		}
		return p.getName() + reversed;
	}

	public String getColdViolation() {
		return coldViolation;
	}

	public String getHotViolation() {
		return hotViolation;
	}

	public String getAccepting() {
		return Accepting;
	}

	public String getInitial() {
		return initial;
	}
	
	public int getLoopDepth(LoopCombinedFragment loop) {
		return ecoreUtil.getAllContainersOfType(loop, LoopCombinedFragment.class).size();
	}
	
	public String getLoopvariableNameForDepth(int depth) {
		return LoopVariable + depth;
	}

}
