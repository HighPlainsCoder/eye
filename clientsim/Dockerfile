FROM library/ruby:2.6.1-slim

RUN apt-get update && apt-get install -y gcc make

EXPOSE  4567

WORKDIR /opt/ias
RUN gem install bundler json sinatra
ADD ias_sim.rb .
CMD exec ruby ias_sim.rb -o 0.0.0.0
