package com.lunch.location.services.parser.train;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.javatuples.Pair;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.lunch.location.services.parser.MenuCandidate;
import com.lunch.location.services.parser.MenuPosTagger;
import com.lunch.location.services.parser.classifier.input.MenuRootElementToRandomTreeInput;
import com.lunch.location.services.parser.nlp.FoodListService;
import com.lunch.location.services.parser.nlp.WordListSimilarityCalculator;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffSaver;

public class MenuRootElementClassifierTrainer {

	public static void main(String[] args) throws Exception {
		String dataPath = "/Users/kromes/Desktop/trainData.txt";
		File file = new File(dataPath);

		ObjectMapper mapper = new ObjectMapper();
		List<String> lines = Files.readAllLines(file.toPath());
//
		MenuPosTagger tagger = new MenuPosTagger("/Users/kromes/Desktop/german-fast.tagger");
		
		MenuRootElementClassifierTrainer trainer = new MenuRootElementClassifierTrainer();
		ClassLoader classLoader = trainer.getClass().getClassLoader();
		
		File essenTrinken = new File(classLoader.getResource("foods/essenTrinken.txt").getFile());
		File gemuese = new File(classLoader.getResource("foods/gemuese.txt").getFile());
		File getraenke = new File(classLoader.getResource("foods/getraenke.txt").getFile());
		List<Path> foodPathes = Lists.newArrayList(essenTrinken.toPath(), gemuese.toPath(), getraenke.toPath());
		
		WordListSimilarityCalculator similarityCalculator = new WordListSimilarityCalculator();
		
		FoodListService foodlistService = new FoodListService(foodPathes, similarityCalculator);
		
		MenuRootElementToRandomTreeInput toRandomTreeInput = new MenuRootElementToRandomTreeInput(tagger, foodlistService);
		Pair<Instances, ArrayList<Attribute>> instancesAndAttributes = toRandomTreeInput.getInstances("menuData", lines.size() / 2);

		Instances instances = instancesAndAttributes.getValue0();
		ArrayList<Attribute> attributes = instancesAndAttributes.getValue1();
		for (int i = 0; i < lines.size(); i++) {
			if (i % 2 == 0) {
				MenuCandidate data = mapper.readValue(lines.get(i), MenuCandidate.class);
				String classification = lines.get(i + 1).trim();
				Instance instance = toRandomTreeInput.convert(data.getRootElement(), instances, attributes);
				instance.setValue(attributes.get(attributes.size() - 1), classification);
				instances.add(instance);
			}
		}

		ArffSaver saver = new ArffSaver();
		saver.setInstances(instances);
		saver.setFile(new File("/Users/kromes/Desktop/trainData.arff"));
		saver.writeBatch();

		instances.randomize(new java.util.Random(System.currentTimeMillis()));
		int trainSize = (int) Math.round(instances.numInstances() * 0.8);
		int testSize = instances.numInstances() - trainSize;
		Instances train = new Instances(instances, 0, trainSize);
		Instances test = new Instances(instances, trainSize, testSize);
		
		RandomForest forest=new RandomForest();
		
		forest.setNumIterations(2000);
		
		
		/** */
		forest.buildClassifier(train);
		/**
		 * train the alogorithm with the training data and evaluate the
		 * algorithm with testing data
		 */
		Evaluation eval = new Evaluation(train);
		eval.evaluateModel(forest, test);
		
		
		/** Print the algorithm summary */
		System.out.println("** Decision Tress Evaluation with Datasets **");
		System.out.println(eval.toSummaryString());
		System.out.print(" the expression for the input data as per alogorithm is ");
		System.out.println(forest);
		System.out.println(eval.toMatrixString());
		System.out.println(eval.toClassDetailsString());
		
		SerializationHelper.write("/Users/kromes/Desktop/randomForest.model", forest);
		RandomForest cls = (RandomForest) SerializationHelper.read("/Users/kromes/Desktop/randomForest.model");
		
		String toClassify = "\"Dein Salat, Deine Bowl\" Für den Grundpreis von 5,90€ erhalten Sie Eine Basis Ihrer Wahl inklusive Zutaten Ihrer Wahl im Wert von 2,00€ + Dressing Ihrer Wahl + Brot. Danach können Sie Ihren Salat / Ihre Bowl zusätzlich durch kalte oder warme Zutaten erweitern. Stellen Sie sich Ihr Wunschessen in 3 Schritten zusammen. Schritt 1: Basis wählen Salatmix (5,90€) - Romana Salat (5,90€) - Babyspinat (6,80€) Reis (5,90€) - Quinoa (5,90€) Schritt 2: Zutaten wählen (2,00€ inklusive, weitere Zutaten gegen Aufpreis) 0,5€ 1,00€ 1,50€ Die einzelnen Zutaten pro Preiskategorie sind in unserer Salatbar vor Ort in Hamburg ersichtlich. Sonstige Toppings Huhn (warm) / Ziegenkäse / Falafel 2,50€ Roastbeef / Lachs 2,90€ Croutons, Pinien-, Sonnenblumenkerne, Walnüsse, Sesam, Chia-Samen / jeweils 0,50€ Schritt 3: Dressing wählen Green Lovers (6 Kräuter, Zitronensaft, Olivenöl) Balsamico (Die Sicherheitsvariante) Caesar‘s (Der Klassiker) Dill-Joghurt (Joghurt, Saure Sahne, Dill, Zitronensaft) French (Mayonnaise, Petersilie, Zitronen- und Orangensaft) Rucola-Senf (passt fast immer, auch gut zum Couscous) Soja-Sesam (Sesamöl, Soyasauce, Ingwer, Zitronengras) Orange-Honig Dressing NEU Ranch Dressing NEU Wechselndes Spezial-Dressing (lass Dich überraschen) Alle Dressings wurden nach eigenen Hausrezepten entwickelt und werden von uns selber hergestellt.";
		double start = System.currentTimeMillis();
		Instance instanceToClassizy = toRandomTreeInput.convert(toClassify);
		double end = System.currentTimeMillis();
		System.out.println("took: " + (end - start));
		double[] result = cls.distributionForInstance(instanceToClassizy);
		Arrays.stream(result).forEach(System.out::println);

	}

}
