package br.unicamp.ft.arx.service;
import org.deidentifier.arx.*;
import org.deidentifier.arx.aggregates.HierarchyBuilderRedactionBased;
import org.deidentifier.arx.aggregates.HierarchyBuilderRedactionBased.Order;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.util.StringTokenizer;
import org.deidentifier.arx.AttributeType.MicroAggregationFunction;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.Arrays;
import org.deidentifier.arx.criteria.KAnonymity;
import org.deidentifier.arx.ARXAnonymizer;
import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXLattice.ARXNode;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.AttributeType.Hierarchy;
import org.deidentifier.arx.Data;

public class Anonymizer extends ServiceArx {
    private static int Contline;
    public static void main(String[] args) throws IOException {
        
    long StartTime = System.currentTimeMillis();
    String Dataset = args[0];
    String Policy = args [1];
    String saveIn = args[2];
    int K = Integer.parseInt(args[3]);  
        
     LineNumberReader lineCounter = new LineNumberReader(new InputStreamReader(new FileInputStream(Dataset)));
           String nextLine =null;
            try {
		while ((nextLine = lineCounter.readLine()) != null) {if (nextLine == null) break;}
                setContline (lineCounter.getLineNumber());
                } catch (IOException done) {
		}         
             if (getContline() <= K){
             System.out.println("It is not possible to implement the risk requested. You Need specify new threshold or add more records to the dataset"); 
             System.exit(0); }
            
             if (getContline() > K) {                       			
        
        DataSource source = DataSource.createCSVSource(Dataset, Charset.forName("UTF-8"), ';', true);
       
            try {
            FileReader reader = new FileReader(Policy); // Localização do Arquivo
            BufferedReader leitor = new BufferedReader(reader);
            StringTokenizer st = null;
            String line = null;
            String field; // Armazena campo de numero
            String classdata;// Armazena campo de matricula
            while ((line = leitor.readLine()) != null) {
                //UTILIZA DELIMITADOR ; PARA DIVIDIR OS CAMPOS
                st = new StringTokenizer(line, ";");
                String dados = null;
                while (st.hasMoreTokens()) {
                    // Field Label
                    dados = st.nextToken();
                    field = dados;
                    // Field Data Classification
                    dados = st.nextToken();
                    classdata = dados;
                    source.addColumn(field, DataType.STRING);
                    
                }
            }
                //    leitor.close();
               //     reader.close();
        } catch (
                IOException | NumberFormatException e)

        {
        }

        Data data = Data.create(source);
        

        try

        {
            FileReader reader1 = new FileReader(Policy); // Localização do Arquivo
            BufferedReader leitor1 = new BufferedReader(reader1);
            StringTokenizer st1 = null;
            String linha1 = null;
            String field1; // Armazena campo de numero
            String classdata1;// Armazena campo de matricula
            while ((linha1 = leitor1.readLine()) != null) {
                //UTILIZA DELIMITADOR ; PARA DIVIDIR OS CAMPOS
                st1 = new StringTokenizer(linha1, ";");
                String dados1 = null;
                             
                while (st1.hasMoreTokens()) {
                                        
                    // Field Label
                    dados1 = st1.nextToken();
                    field1 = dados1;
                    // if (field1.equals("Rmax")){                	 
                   // Rmax = Double.parseDouble(st1.nextToken());
                 
                     // } else 
                     {
                                     
                    // Field Data Classification
                    dados1 = st1.nextToken();
                    classdata1 = dados1;
                    //load Hierarchy Builder
                     //HierarchyBuilderRedactionBased<?> supressing1 = HierarchyBuilderRedactionBased.create(Order.LEFT_TO_RIGHT,
                             //   Order.LEFT_TO_RIGHT,Character.MIN_VALUE,Character.MAX_VALUE);
                    HierarchyBuilderRedactionBased<?> supressing1 = HierarchyBuilderRedactionBased.create(Order.LEFT_TO_RIGHT,
                            Order.LEFT_TO_RIGHT,' ','*');
                    
                    HierarchyBuilderRedactionBased<?> supressing2 = HierarchyBuilderRedactionBased.create(Order.RIGHT_TO_LEFT,
                            Order.RIGHT_TO_LEFT,' ','*');
                  
                    if (classdata1.equals("1")) {
                        data.getDefinition().setAttributeType(field1, AttributeType.INSENSITIVE_ATTRIBUTE);
                    }
                    if (classdata1.equals("2")) {
                        data.getDefinition().setAttributeType(field1, AttributeType.IDENTIFYING_ATTRIBUTE);
                    }
                    if (classdata1.equals("3")) {
                        data.getDefinition().setAttributeType(field1, AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
                        data.getDefinition().setMicroAggregationFunction(field1, MicroAggregationFunction.createGeneralization());

                    }
                    if (classdata1.equals("4")) {
                        data.getDefinition().setAttributeType(field1, AttributeType.IDENTIFYING_ATTRIBUTE);
                    }
                    if (classdata1.equals("SR")) {
                       data.getDefinition().setAttributeType(field1, AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
                        data.getDefinition().setAttributeType(field1, supressing1);
                    }
                    if (classdata1.equals("SL")) {
                       data.getDefinition().setAttributeType(field1, AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
                        data.getDefinition().setAttributeType(field1, supressing2);
                    }
                    if (classdata1.equals("DT")) {
                        data.getDefinition().setAttributeType(field1, Hierarchy.create("arx-poc"+File.separator +"hierarchy"+ File.separator +"birthdate.csv", StandardCharsets.UTF_8, ';'));     
                    }
                    if (classdata1.equals("AG")) {
                             data.getDefinition().setAttributeType(field1, Hierarchy.create("arx-poc"+File.separator +"hierarchy"+ File.separator +"age.csv", StandardCharsets.UTF_8, ';'));                             
                   }
                     if (classdata1.equals("CT")) {
                             data.getDefinition().setAttributeType(field1, Hierarchy.create("arx-poc"+File.separator +"hierarchy"+ File.separator +"custom.csv", StandardCharsets.UTF_8, ';'));                          
                   }
            }}
    
        }
            // leitor1.close();
               //     reader1.close(); 
        } catch (
                IOException | NumberFormatException e)

        {
        }
        ARXPopulationModel populationmodel = ARXPopulationModel.create(data.getHandle().getNumRows(), 0.01d);


        // Create an instance of the anonymizer
        ARXAnonymizer anonymizer = new ARXAnonymizer();
        ARXConfiguration config = ARXConfiguration.create();
        config.addPrivacyModel(new KAnonymity(K));
        config.setMaxOutliers(0d);
        
        ARXResult result = anonymizer.anonymize(data, config);
        ARXNode node = result.getGlobalOptimum();
        
       // setRR1(result.getOutput().getRiskEstimator(populationmodel).getSampleBasedReidentificationRisk().getEstimatedProsecutorRisk());
// Perform risk analysis
        System.out.println("- Output data");
       
//print(result.getOutput());
        System.out.println("\n- Mixed risks");
        System.out.println("  * Prosecutor re-identification risk: " + result.getOutput().getRiskEstimator(populationmodel).getSampleBasedReidentificationRisk().getEstimatedProsecutorRisk());
        System.out.println("  * Journalist re-identification risk: " + result.getOutput().getRiskEstimator(populationmodel).getSampleBasedReidentificationRisk().getEstimatedJournalistRisk());
        System.out.println("  * Marketer re-identification risk: " + result.getOutput().getRiskEstimator(populationmodel).getSampleBasedReidentificationRisk().getEstimatedMarketerRisk());
        System.out.println("  * K anonimity implementado: " + K);
        System.out.println(" - Information loss: " + result.getGlobalOptimum().getLowestScore() + " / " + result.getGlobalOptimum().getHighestScore());
        System.out.println(" - Statistics");
        System.out.println(result.getOutput(result.getGlobalOptimum(), false).getStatistics().getEquivalenceClassStatistics());
        System.out.println("Data: " + data.getHandle().getView().getNumRows() + " records with " + data.getDefinition().getQuasiIdentifyingAttributes().size() + " quasi-identifiers");
        System.out.println(" - Policies available: " + result.getLattice().getSize());
        System.out.println(" - Solution: " + Arrays.toString(node.getTransformation()));
        System.out.println("   * Optimal: " + result.getLattice().isComplete());
        System.out.println("   * Time needed: " + result.getTime() + "[ms]");
        System.out.print(" - Writing data...");
        result.getOutput(false).save(saveIn, ';');
        System.out.println("Done!");
        long StopTime = System.currentTimeMillis();
        System.out.println("*** Total Execution Time: "+(StopTime - StartTime)+"[ms]");
        }}

    public static int getContline() {
        return Contline;
    }

    /**
     * @param aContline the contline to set
     */
    public static void setContline(int aContline) {
        Contline = aContline;
    }
}