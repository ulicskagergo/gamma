/********************************************************************************
 * Copyright (c) 2018-2020 Contributors to the Gamma project
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * SPDX-License-Identifier: EPL-1.0
 ********************************************************************************/
package hu.bme.mit.gamma.action.language.formatting;

import org.eclipse.xtext.formatting.impl.FormattingConfig;
import org.eclipse.xtext.service.AbstractElementFinder.AbstractGrammarElementFinder;

import hu.bme.mit.gamma.action.language.services.ActionLanguageGrammarAccess;
import hu.bme.mit.gamma.expression.language.formatting.ExpressionLanguageFormatterUtil;

public class ActionLanguageFormatterUtil {
	
	private final ExpressionLanguageFormatterUtil expressionLanguageFormatterUtil =
			new ExpressionLanguageFormatterUtil();
	
	public void format(FormattingConfig c, AbstractGrammarElementFinder f) {
		expressionLanguageFormatterUtil.format(c, f);
	}

	public void formatExpressions(FormattingConfig c, ActionLanguageGrammarAccess f) {
		expressionLanguageFormatterUtil.formatExpressions(c, f.getExpressionLanguageGrammarAccess());
		setSquareBrackets(c, f);
	}
	
	protected void setSquareBrackets(FormattingConfig c, ActionLanguageGrammarAccess f) {
		c.setNoSpace().before(f.getAssignableAccessExpressionAccess().getLeftSquareBracketKeyword_1_0_1());
		c.setNoSpace().around(f.getAssignableAccessExpressionAccess().getIndexAssignment_1_0_2());
	}

}