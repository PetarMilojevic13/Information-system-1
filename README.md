# Information-system-1
Simple university project, that is consisted of 3 subsystems that communicate mutually using Java Message Service.
Every subsystem has its own database and communication queue, that is used for sending and receiving message
from other subsystems and from central server.
Central server sends and receives messages from subsystems via JMS and receives and sends from client application via REST services.
Client application UI does not have specific GUI, so everything is done using terminal.
