package com.lunch.location.services.parser.classifier.input;

import java.util.ArrayList;

import org.javatuples.Pair;

import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public abstract class ToRandomTreeInput {
	
	public abstract Instance convert(String menuRootElement, Instances dataset, ArrayList<Attribute> attributes);
	
	public Instance convert(String menuRootElement) {
		Pair<Instances, ArrayList<Attribute>> instancesAndAttributes = getInstances("Rel", 1);
		Instance result = convert(menuRootElement, instancesAndAttributes.getValue0(), instancesAndAttributes.getValue1());
		result.setDataset(instancesAndAttributes.getValue0());
		return result;
	}

	
	public Pair<Instances, ArrayList<Attribute>> getInstances(String name, int noInstances) {
		ArrayList<Attribute> attributes = getAttributes();
		Instances result = new Instances(name, attributes, noInstances);
		result.setClassIndex(attributes.size() - 1);
		return new Pair<Instances, ArrayList<Attribute>>(result, attributes);
	}
	
	protected abstract ArrayList<Attribute> getAttributes();

}
