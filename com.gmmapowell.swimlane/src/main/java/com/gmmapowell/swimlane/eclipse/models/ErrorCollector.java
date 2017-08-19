package com.gmmapowell.swimlane.eclipse.models;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.gmmapowell.swimlane.eclipse.interfaces.ErrorAccumulator;
import com.gmmapowell.swimlane.eclipse.interfaces.ErrorMessageListener;

public class ErrorCollector implements ErrorAccumulator {
	// TODO: should this be by group?
	private final Set<String> errors = new TreeSet<>();
	private final Set<ErrorMessageListener> errorListeners = new HashSet<>();

	@Override
	public void addErrorMessageListener(ErrorMessageListener eml) {
		eml.clear();
		errorListeners.add(eml);
		if (eml != null) {
			for (String s : errors)
				eml.error(s);
		}
	}

	@Override
	public void error(String msg) {
		errors.add(msg);
		for (ErrorMessageListener eml : errorListeners)
			eml.error(msg);
	}


}
