PRIVAaaS - ARX-POC - EUBra-BISEA

PRIVAaaS is a set of libraries and tools that allow controlling and reducing data leakage in the context of Big Data processing and, consequently, protecting sensible information that is processed by data analytics algorithms.

Find the sources of PRIVAaaS big data privacy tool here. Find more about EUBra-BISEA at the project website http://www.eubra-bigsea.eu.

We add ARX to the library to performe K-anonymity model based on re-identification risk analyze

Proof of Concept
The project contained in arx-poc implements 3.5.1 version of ARX Anonimization tool library. It includes anonymizer, which implements a simple jar executable service. It does not require an external server.

How to use
You can include the library in your Java application and execute de jar file. The library usage requires three inputs:

1. an anonymization policy, in a .csv file, separeted by semicolon, e.g. (policy example);
2. the data to be anonymized, in one or more .csv files, separeted by semicolon, e.g. (data example).
3. the path, name and extension to data output.

Using command line, execute:

#java - jar run/arx-poc.jar "dataset.csv" "policy.csv" "anonimized_dataset.csv"

*Warning: In some cases you will need inform the absolut path to run.



