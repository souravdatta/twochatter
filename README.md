A simple two person chat app.

The server side (chatter-server) uses sinatra based Ruby that can be deplyed on Heroku.
Ruby dependencies can be installed by running `bundle install`.

The client (PiChatter) is written in Java swing as a desktop app that can be minimized to system tray and all that. The minimum JRE version is 7. The app is Netbeans based and it depends on 
  1. Apache Commons Codec (http://commons.apache.org/proper/commons-codec/)
  2. Restlet Rest client (http://restlet.com/)
  3. JSON Simple (https://code.google.com/p/json-simple/)

