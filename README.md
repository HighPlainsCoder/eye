# Primes
Pointless program to generate primes in a distributed, cooperating, multiprocess system.



https://primes.utm.edu/lists/small/10000.txt

http://compoasso.free.fr/primelistweb/page/prime/liste_online_en.php




### Architectural tenets to address

* Build in anticipation of failure.
  * TODO: dag will record successes, and adjust params to include all data since last success.
* Support bursting (and scaling down).
  * TODO: 
* Make components easily testable. Well-defined test data sets and environments.
  * Done.  I Tests have constant sample data, and also custom data for custom tests.
* Define success, failures and errors (is your app healthy?) [i.e. have external monitors]
  * We are having a dagwatch alarming if this fails enough.
* Easily debug and optimize technical problems in aggregate.
* Robust configuration management. BODE (Build Once Deploy Everywhere) - Support multiple environments (dev, qa, prod) with ability to redeploy
  * Done. All possible config values are alterable in the execution (TODO:
  almost all? check for exceptions)
* Contract driven development. Well-defined interfaces. Enables good ownership.
  * The program was written with a view to the known contract and interfaces.
  Some items are somewhat loose, so tuning knobs are in place. 
* Monitor and profile latency at exchange.
  * NA 
* Any given data artifact has a single source of truth.
  * Not a good one.  The Aerospike dataset is the source of truth, 
  but Aerospike isnt easily scannable, especially this one



## Testing

We set up a test environment using `docker-compose`

```bash
docker-compose up -d
```

Once this all comes up, the following command creates the sourcedata.ad_activity table and populates it with 
some initial data from `./hive/data`
```bash
docker exec -it bias_hive-server_1 sh -c /staging/setup_hive_tables.sh
```

The query `SELECT count(1) from sourcedata.ad_activity;` should return 306 rows.

To get a hive prompt, run this command: 

```bash
docker exec -it bias_hive-server_1 hive
```

Then, to shut it all down
```bash
docker-compose down
```


## notes

#### find dependencies, for removing dependencies
    mvn dependency:tree -Dverbose -Dincludes=log4j:log4j

#### list contents of jar, for finding dependencies to remove
    jar -tf target/bias.jar 

