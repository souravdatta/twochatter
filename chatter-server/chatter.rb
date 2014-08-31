require 'sinatra'
require 'dm-core'
require 'dm-migrations'
require 'json'

configure :production do
  DataMapper.setup(:default, ENV['HEROKU_POSTGRESQL_AQUA_URL'])
end

class Message
  include DataMapper::Resource
  
  property :id, Serial
  property :to, String
  property :from, String
  property :time, String
  property :data, Text, lazy: false
  property :delivered, Boolean, required: true
end

DataMapper.finalize

class Chatter < Sinatra::Base
  before do
    headers 'Access-Control-Allow-Origin' => '*'
  end
  
  get '/api/ping/:uname' do
    msgs = Message.all(to: params[:uname], delivered: false)
    msgs.each do |m|
      m.update(delivered: true)
    end
    jmsgs = []
    msgs.each do |m|
      jmsgs << {from: m.from, to: m.to, time: m.time, data: m.data}
    end
    p jmsgs
    msgs.each {|m| m.destroy}
    JSON.generate(jmsgs)
  end
  
  get '/api/send/:from/:to/:time/:message' do
    m = Message.new(to: params[:to], from: params[:from], time: params[:time], data: params[:message],
                    delivered: false)
    m.save
    JSON.generate({done: true})
  end
end
