# PRIVAaaS - EUBra-BISEA
[PRIVAaaS](.) is a set of libraries and tools that allow controlling and reducing data leakage in the context of Big Data processing and, consequently, protecting sensible information that is processed by data analytics algorithms.

Find the sources of **PRIVAaaS** big data privacy tool [here][repos]. Find more about [EUBra-BISEA][bigsea] at the project website http://www.eubra-bigsea.eu.

We will add ARX to the library soon.

## Proof of Concept
The `maven` project contained in [priva-poc](development/priva-poc) implements a first version of the libraries. It includes [LightPrivacyService][lps], which implements a simple restful service. It does not require an external server.


## How to use
You can include the library in your Java application or through its REST API.
The library usage requires two inputs:

* an anonymization **policy**, in a `.json` file, e.g. ([policy example][policy]);
* the **data** to be anonymized, in one or more `.json` files, e.g. ([data example][data]).

### How to use the REST API
To setup the REST server, you should run the project with the main class [LightPrivacyService][lps]. To run the project you can either use `maven` or the [Docker][docker] solution below.

Then, you should use the HTTP method post to the url: http://localhost:4567/endpoint/.
Tools can help you with that. You can use the [postman](https://www.getpostman.com/) or the [cURL](https://curl.haxx.se) (in your shell) to try it.


### Run the Project in a Docker container
To avoid the need for you to install `java`, `maven`, etc. we created a [Docker][docker] container that makes the life much easier.
You only need to install [Docker][docker], and after that the process is fully automated. In practice you need to run the files [Docker-build.sh](development/priva-poc/Docker-build.sh) and [Docker-run.sh](development/priva-poc/Docker-run.sh).

[Docker-build.sh](development/priva-poc/Docker-build.sh) creates the image based on the included [Dockerfile](development/priva-poc/Dockerfile) and the first time may take a while, due to the need to download the required dependencies.
[Docker-run.sh](development/priva-poc/Docker-run.sh) starts the container with the [LightPrivacyService][lps].


#### Demo java client
Examples are provided with this code. The project in [PRIVAaaS-client](development/PRIVAaaS-client) portrays how to use the web service provided.

#### Using cURL

Other rest clients can be used. For instance `cURL`, with a command like this.

> curl -X POST -v http://localhost:4567/endpoint/  -d @"../../priva-poc/input/example-mock-data/mock_data.policy.json"  -d @"../../priva-poc/input/example-mock-data/mock_data.json"

#### Html Form

Another usage example is provided with an html form in:

* http://localhost:4567/priva/examples/paas-post-form.html



[repos]: https://github.com/eubr-bigsea/PRIVAaaS
[bigsea]: http://www.eubra-bigsea.eu
[policy]: development/priva-poc/src/main/resources/public/priva/examples/example.policy.json
[data]: development/priva-poc/src/main/resources/public/priva/examples/example.data.json
[lps]: development/priva-poc/src/main/java/br/unicamp/ft/priva/aas/LightPrivacyService.java
[docker]: https://www.docker.com/
