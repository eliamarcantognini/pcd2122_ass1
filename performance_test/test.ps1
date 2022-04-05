# nB = nBodies
# nS = nSteps
# mode {0 = scalable, 1 = fullthread}
#                   nB  nS  mode
java -jar .\app.jar 100 1000 0
java -jar .\app.jar 100 10000 0
java -jar .\app.jar 100 50000 0

java -jar .\app.jar 1000 1000 0
java -jar .\app.jar 1000 10000 0
java -jar .\app.jar 1000 50000 0

java -jar .\app.jar 5000 1000 0
java -jar .\app.jar 5000 10000 0
java -jar .\app.jar 5000 50000 0

java -jar .\app.jar 100 1000 1
java -jar .\app.jar 100 10000 1
java -jar .\app.jar 100 50000 1

java -jar .\app.jar 1000 1000 1
java -jar .\app.jar 1000 10000 1
java -jar .\app.jar 1000 50000 1

java -jar .\app.jar 5000 1000 1
java -jar .\app.jar 5000 10000 1
java -jar .\app.jar 5000 50000 1