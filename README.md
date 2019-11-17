# Eye
Utility to process pictures


## Architectural tenets to address

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




## Notes

#### find dependencies, for removing dependencies
    mvn dependency:tree -Dverbose -Dincludes=log4j:log4j

#### list contents of jar, for finding dependencies to remove
    jar -tf target/bias.jar 

