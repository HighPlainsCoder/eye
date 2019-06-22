require 'sinatra'
require 'json'


get '/db2/client/12113/absit' do
  url=params['adsafe_url']

  case url
  when /.*\.null/
    nil
  when /.*\.error/
    404
  else
    content_type :json
    {
        :action => 'passed',
        :url => url,
        :bsc => {
            :adt => 875,
            :alc => 901,
            :dlm => 1000,
            :drg => 875,
            :hat => 1000,
            :off => 1000,
            :sam => 1000
        }
    }.to_json
  end
end

get '/db2/client/12113/absuit' do
  content_type :json
  {
      :action => 'passed',
      :bsc => {
          :adt => 875,
          :alc => 903,
          :dlm => 1000,
          :drg => 875,
          :hat => 1000,
          :off => 1000,
          :sam => 1000
      },
      :iab1 => [
          :iab_food
      ],
      :iab2 => [],
      :ttl => '2014-03-21T16:21-0400'
  }.to_json
end


get '*' do
  404
end
