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
package hu.bme.mit.gamma.ui.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import hu.bme.mit.gamma.util.FileUtil;
import hu.bme.mit.gamma.util.GammaEcoreUtil;

public class TaskExecutionTimeMeasurer implements TaskHook {
	
	private final int iterationCount;
	private long startTime;
	
	private boolean isFirst = true;
	private final List<Double> elapsedTimes = new ArrayList<Double>();
	
	private final Calculator<Double> calculator;
	private final String fileName;
	private File targetFile;
	
	private final GammaEcoreUtil ecoreUtil = GammaEcoreUtil.INSTANCE;
	private final FileUtil fileUtil = FileUtil.INSTANCE;
	
	protected Logger logger = Logger.getLogger("GammaLogger");
	
	public TaskExecutionTimeMeasurer(int iterationCount,
			Calculator<Double> calculator, String fileName) {
		this.iterationCount = iterationCount + 1; // Due to Java JIT, we do not count the first one
		this.calculator = calculator;
		this.fileName = fileName;
	}
	
	public void startTaskProcess(Object object) {
		// Setting target file
		if (object instanceof EObject) {
			EObject eObject = (EObject) object;
			Resource resource = eObject.eResource();
			File siblingFile = ecoreUtil.getFile(resource);
			String parentUri = siblingFile.getParent();
			this.targetFile = new File(parentUri + File.separator + fileName);
		}
		
		isFirst = true;
		elapsedTimes.clear();
		logger.log(Level.INFO, "Starting measuerement");
	}
	
	public int getIterationCount() {
		return iterationCount;
	}
	
	public void startIteration() {
		logger.log(Level.INFO, "Starting iteration " + elapsedTimes.size() + 1);
		startTime = System.nanoTime();
	}
	
	public void endIteration() {
		long endTime = System.nanoTime();
		double elapsedTime = (endTime - startTime) / 1000000.0; // In ms
		if (isFirst) {
			isFirst = false;
			logger.log(Level.INFO, "First (not counted) iteration has been finished");
		}
		else {
			elapsedTimes.add(elapsedTime);
			logger.log(Level.INFO, "Finished iteration " + elapsedTimes.size());
		}
	}
	
	public void endTaskProcess() {
		StringBuilder builder = new StringBuilder();
		for (Double value : elapsedTimes) {
			builder.append(value + System.lineSeparator());
		}
		double median = calculator.calculate(elapsedTimes);
		builder.append("Median: " + median);
		
		fileUtil.saveString(targetFile, builder.toString());
		
		logger.log(Level.INFO, "Finished measuerement");
	}

}