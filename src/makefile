
all : 
	javac -deprecation -d ./build/classes ./BCK/ANN/*.java
	javac -deprecation -d ./build/classes -cp ./build/classes/:./build/classes/GUI ./BCK/GUI/*.java

run :
	java -DBCK.home=./build/classes/BCK/GUI/ -cp ./build/classes/ BCK.GUI.bck

doc :
	javadoc -d ./BCK/doc -author -version BCK.GUI BCK.ANN
	cp ./BCK/doc/*.html ./public_html/BCK/javadoc/
	cp ./BCK/ANN/*.java /users/ca4/tdoris.ca4/public_html/BCK/code/ANN/
	cp ./BCK/GUI/*.java /users/ca4/tdoris.ca4/public_html/BCK/code/GUI/



