call set-env.bat

set NAME=dp_2.10-0.1-SNAPSHOT-one-jar

rem call scala target\scala-2.10\%NAME%.jar experiment.form.MainForm
java -jar target\scala-2.10\%NAME%.jar
pause