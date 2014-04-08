
set CUR_PRJ="%cd%"
cd ..\..\..\scripts
call env.bat
cd /d %CUR_PRJ%


rem call scala target\scala-2.10\rsa_2.10-0.1-SNAPSHOT.jar experiment.form.MainForm
java -jar target\scala-2.10\rsa_2.10-0.1-SNAPSHOT-one-jar.jar
pause