
all : 
	javac -deprecation -d /users/ca4/tdoris.ca4/BCK/CurrentDevelopment/classes /users/ca4/tdoris.ca4/BCK/src/BCK/ANN/*.java
	javac -deprecation -d /users/ca4/tdoris.ca4/BCK/classes /users/ca4/tdoris.ca4/BCK/src/BCK/GUI/*.java

run :
	java -DBCK.home=/users/ca4/tdoris.ca4/BCK/classes/BCK/GUI/ -mx100m -oss1m -ss1m -ms10m BCK.GUI.bck

doc :
	rm -fr /users/ca4/tdoris.ca4/public_html/BCK/javadoc/*.html
	rm -f /users/ca4/tdoris.ca4/BCK/doc/*.html
	javadoc -d /users/ca4/tdoris.ca4/BCK/doc -author -version BCK.GUI BCK.ANN
	cp /users/ca4/tdoris.ca4/BCK/doc/*.html /users/ca4/tdoris.ca4/public_html/BCK/javadoc/
	rm -fr /users/ca4/tdoris.ca4/public_html/BCK/code/ANN/*
	rm -fr /users/ca4/tdoris.ca4/public_html/BCK/code/BCK/*
	cp /users/ca4/tdoris.ca4/BCK/src/BCK/ANN/*.java /users/ca4/tdoris.ca4/public_html/BCK/code/ANN/
	cp /users/ca4/tdoris.ca4/BCK/src/BCK/GUI/*.java /users/ca4/tdoris.ca4/public_html/BCK/code/GUI/



