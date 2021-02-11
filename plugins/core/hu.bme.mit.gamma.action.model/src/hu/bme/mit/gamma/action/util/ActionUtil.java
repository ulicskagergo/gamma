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
package hu.bme.mit.gamma.action.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import hu.bme.mit.gamma.action.model.Action;
import hu.bme.mit.gamma.action.model.ActionModelFactory;
import hu.bme.mit.gamma.action.model.AssignmentStatement;
import hu.bme.mit.gamma.action.model.Block;
import hu.bme.mit.gamma.action.model.VariableDeclarationStatement;
import hu.bme.mit.gamma.expression.model.AccessExpression;
import hu.bme.mit.gamma.expression.model.Declaration;
import hu.bme.mit.gamma.expression.model.DirectReferenceExpression;
import hu.bme.mit.gamma.expression.model.Expression;
import hu.bme.mit.gamma.expression.model.ReferenceExpression;
import hu.bme.mit.gamma.expression.model.VariableDeclaration;
import hu.bme.mit.gamma.expression.util.ExpressionUtil;

public class ActionUtil extends ExpressionUtil {
	// Singleton
	public static final ActionUtil INSTANCE = new ActionUtil();
	protected ActionUtil() {}
	//
	
	protected ActionModelFactory actionFactory = ActionModelFactory.eINSTANCE;
	
	public Action extend(Action originalAction, Action newAction) {
		if (originalAction == null) {
			return newAction;
		}
		else if (newAction == null) {
			return originalAction;
		}
		else if (originalAction instanceof Block) {
			Block block = (Block) originalAction;
			block.getActions().add(newAction);
			return block;
		}
		else {
			Block block = actionFactory.createBlock();
			block.getActions().add(originalAction);
			block.getActions().add(newAction);
			return block;
		}
	}
	
	public Action extend(Action originalAction, Collection<? extends Action> newActions) {
		Action extensibleAction = originalAction;
		for (Action newAction : newActions) {
			extensibleAction = extend(extensibleAction, newAction);
		}
		return extensibleAction;
	}
	
	public List<AssignmentStatement> getAssignments(VariableDeclaration variable,
			Collection<AssignmentStatement> assignments) {
		List<AssignmentStatement> assignmentsOfVariable = new ArrayList<>();
		for (AssignmentStatement assignment : assignments) {
			ReferenceExpression lhs = assignment.getLhs();
			if (lhs instanceof DirectReferenceExpression) {
				DirectReferenceExpression reference = (DirectReferenceExpression) lhs;
				Declaration declaration = reference.getDeclaration();
				if (declaration == variable) {
					assignmentsOfVariable.add(assignment);
				}
			}
			else if (lhs instanceof AccessExpression) {
				//TODO handle access expressions
			}
		}
		return assignmentsOfVariable;
	}
	
	public List<VariableDeclarationStatement> getVariableDeclarationStatements(Block block) {
		EList<Action> subactions = block.getActions();
		List<VariableDeclarationStatement> variableDeclarationStatements =
				new ArrayList<VariableDeclarationStatement>();
		for (Action subaction : subactions) {
			if (subaction instanceof VariableDeclarationStatement) {
				VariableDeclarationStatement statement =
						(VariableDeclarationStatement) subaction;
				variableDeclarationStatements.add(statement);
			}
		}
		return variableDeclarationStatements;
	}
	
	public List<VariableDeclaration> getVariableDeclarations(Block block) {
		List<VariableDeclarationStatement> variableDeclarationStatements =
				getVariableDeclarationStatements(block);
		List<VariableDeclaration> variableDeclarations = new ArrayList<VariableDeclaration>();
		for (VariableDeclarationStatement variableDeclarationStatement :
				variableDeclarationStatements) {
			variableDeclarations.add(variableDeclarationStatement.getVariableDeclaration());
		}
		return variableDeclarations;
	}
	
	public List<VariableDeclarationStatement> getPrecedingVariableDeclarationStatements(
			Block block, Action action) {
		EList<Action> subactions = block.getActions();
		int index = subactions.indexOf(action);
		List<VariableDeclarationStatement> localVariableDeclarations =
				new ArrayList<VariableDeclarationStatement>();
		for (int i = 0; i < index; ++i) {
			EObject subaction = subactions.get(i);
			if (subaction instanceof VariableDeclarationStatement) {
				VariableDeclarationStatement statement =
						(VariableDeclarationStatement) subaction;
				localVariableDeclarations.add(statement);
			}
		}
		return localVariableDeclarations;
	}
	
	public List<VariableDeclaration> getPrecedingVariableDeclarations(
			Block block, Action action) {
		List<VariableDeclarationStatement> precedingVariableDeclarationStatements =
				getPrecedingVariableDeclarationStatements(block, action);
		List<VariableDeclaration> localVariableDeclarations =
				new ArrayList<VariableDeclaration>();
		for (VariableDeclarationStatement precedingVariableDeclarationStatement :
				precedingVariableDeclarationStatements) {
			localVariableDeclarations.add(
					precedingVariableDeclarationStatement.getVariableDeclaration());
		}
		return localVariableDeclarations;
	}
	
	public AssignmentStatement createAssignment(VariableDeclaration variable,
			Expression expression) {
		AssignmentStatement assignmentStatement = actionFactory.createAssignmentStatement();
		DirectReferenceExpression reference = factory.createDirectReferenceExpression();
		reference.setDeclaration(variable);
		assignmentStatement.setLhs(reference);
		assignmentStatement.setRhs(expression);
		return assignmentStatement;
	}
	
}