LIB_HOME=""
for f in /app/servmina/lib/*.jar
do
  LIB_HOME=$LIB_HOME:$f
done
#echo "$CLASSPATH:$LIB_HOME"
java -classpath "$CLASSPATH:$LIB_HOME" cn.pubinfo.main.server.Main
