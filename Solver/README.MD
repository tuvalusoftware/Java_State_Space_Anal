**1. How to deploy to server:**
Make sure to config env variables in .bashrc
>export GUROBI_HOME="/home/nam/gurobi810/linux64"
>export PATH="${PATH}:${GUROBI_HOME}/bin"
>export LD_LIBRARY_PATH="${GUROBI_HOME}/lib"

In root directory of Solver project
>gradle build bootJar -x test
>java -jar build/libs/Solver-1.2.jar
