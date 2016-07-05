package com.broochem.sfcrime;

import quickml.data.AttributesMap;
import quickml.data.ClassifierInstance;
import quickml.data.PredictionMap;
import quickml.supervised.ensembles.randomForest.randomDecisionForest.RandomDecisionForest;
import quickml.supervised.ensembles.randomForest.randomDecisionForest.RandomDecisionForestBuilder;
import quickml.supervised.tree.attributeIgnoringStrategies.IgnoreAttributesWithConstantProbability;
import quickml.supervised.tree.decisionTree.DecisionTreeBuilder;
import quickml.supervised.tree.decisionTree.scorers.GRPenalizedGiniImpurityScorerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
//import java.util.HashSet;
import java.util.List;
//import java.util.Set;

public class SFCrimeMain {

        public static String header = "Id,ARSON,ASSAULT,BAD CHECKS,BRIBERY,BURGLARY,DISORDERLY CONDUCT,DRIVING UNDER THE INFLUENCE,DRUG/NARCOTIC,DRUNKENNESS,EMBEZZLEMENT,EXTORTION,FAMILY OFFENSES,FORGERY/COUNTERFEITING,FRAUD,GAMBLING,KIDNAPPING,LARCENY/THEFT,LIQUOR LAWS,LOITERING,MISSING PERSON,NON-CRIMINAL,OTHER OFFENSES,PORNOGRAPHY/OBSCENE MAT,PROSTITUTION,RECOVERED VEHICLE,ROBBERY,RUNAWAY,SECONDARY CODES,SEX OFFENSES FORCIBLE,SEX OFFENSES NON FORCIBLE,STOLEN PROPERTY,SUICIDE,SUSPICIOUS OCC,TREA,TRESPASS,VANDALISM,VEHICLE THEFT,WARRANTS,WEAPON LAWS";
       
        public static String[] labels = new String[]{          
                "ARSON","ASSAULT","BAD CHECKS","BRIBERY","BURGLARY","DISORDERLY CONDUCT","DRIVING UNDER THE INFLUENCE","DRUG/NARCOTIC","DRUNKENNESS","EMBEZZLEMENT","EXTORTION","FAMILY OFFENSES","FORGERY/COUNTERFEITING","FRAUD","GAMBLING","KIDNAPPING","LARCENY/THEFT","LIQUOR LAWS","LOITERING","MISSING PERSON","NON-CRIMINAL","OTHER OFFENSES","PORNOGRAPHY/OBSCENE MAT","PROSTITUTION","RECOVERED VEHICLE","ROBBERY","RUNAWAY","SECONDARY CODES","SEX OFFENSES FORCIBLE","SEX OFFENSES NON FORCIBLE","STOLEN PROPERTY","SUICIDE","SUSPICIOUS OCC","TREA","TRESPASS","VANDALISM","VEHICLE THEFT","WARRANTS","WEAPON LAWS"
        };
       
        public static void main( String args[]) throws Exception {

                List<ClassifierInstance> irisDataset = loadDataSet();
        //final RandomForest randomForest = new RandomForestBuilder().buildPredictiveModel(irisDataset);
        final RandomDecisionForest randomForest = new RandomDecisionForestBuilder<ClassifierInstance>(new DecisionTreeBuilder<ClassifierInstance>()
                .scorerFactory(new GRPenalizedGiniImpurityScorerFactory(1.0))
                .maxDepth(6)
                .minAttributeValueOccurences(0)
                .attributeIgnoringStrategy(new IgnoreAttributesWithConstantProbability(0.2)))
                .numTrees(100)
                .buildPredictiveModel(irisDataset);

        List<AttributesMap> testList = loadTestSet();
       
        int counter = 0;
       
        PrintWriter writer = new PrintWriter("/home/tom/development/data/sfcrime/output.txt", "UTF-8");
        writer.println(header);
       
        for (AttributesMap attMap : testList) {
                PredictionMap predictionMap = randomForest.predict( attMap);           
                String line = "";
                line += counter++;
                for ( String label : labels) {
                        line += "," + predictionMap.get( label);
                }
            writer.println(line);
        }
        writer.close();
        }
               
        public static List<AttributesMap> loadTestSet() throws IOException {
        final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/home/tom/development/data/sfcrime/test.csv")));
        final List<AttributesMap> instances = new ArrayList<>();

        String[] headings = new String[]{ "Id","Dates","DayOfWeek","PdDistrict","Address","X","Y"};

        //header skip
        br.readLine();
       
        String line = br.readLine();              
        while (line != null) {
            String[] splitLine = line.split(",");

            AttributesMap attributes = AttributesMap.newHashMap();
            for (int x = 0; x < splitLine.length; x++) {                
                switch ( x) {
                        case 5:
                        case 6:
                                                attributes.put(headings[x], Double.valueOf( splitLine[x]));
                                                break;
               
                        case 0:
                                //attributes.put( headings[x], Double.valueOf( splitLine[x]));
                               
                        case 1:
                        case 4:
                                                break;
                                                                               
                                        default:
                                                attributes.put(headings[x], splitLine[x]);
                                                break;
                                }
            }
            instances.add( attributes);
            line = br.readLine();
        }

        br.close();        
        return instances;
        }
       
   public static  List<ClassifierInstance> loadDataSet() throws IOException {
           
        final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/home/tom/development/data/sfcrime/trainsimple.csv")));
        final List<ClassifierInstance> instances = new ArrayList<>();

        String[] headings = new String[]{ "Dates", "Category", "DayOfWeek", "PdDistrict", "Address", "X", "Y"};

        //header skip
        br.readLine();
       
        String line = br.readLine();              
        while (line != null) {
            String[] splitLine = line.split(",");

            AttributesMap attributes = AttributesMap.newHashMap();
            for (int x = 0; x < splitLine.length; x++) {                
                switch ( x) {
                        case 5:
                        case 6:
                                                attributes.put(headings[x], Double.valueOf( splitLine[x]));
                                                break;
               
                        case 0:
                                //attributes.put( headings[x], Double.valueOf( splitLine[x]));
                               
                        case 1:
                        case 4:
                                                break;
                                                                               
                                        default:
                                                attributes.put(headings[x], splitLine[x]);
                                                break;
                                }
            }
            instances.add(new ClassifierInstance( attributes, splitLine[1]));            
            line = br.readLine();
        }

        br.close();        
        return instances;
    }
}