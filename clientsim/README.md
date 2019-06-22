# IAS Simulator

This folder builds a docker container implementing a very simple simulation of the IAS service for testing.

The simulator is built on [Sinatra](http://sinatrarb.com/). We don't use very many of Sinatra's features, all you'll probably need is found in the Routes section of the [README](http://sinatrarb.com/intro.html)

To edit the simulator, look at the file ias_sim.rb.

## Building & Running


```bash
docker build -t ias_sim ias_sim/
docker run -p 4567:4567 -d ias_sim

...

docker container ls
docker container stop <container id>
```

Likely this docker will be called from a docker-compose file for system testing. 
