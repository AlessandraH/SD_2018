#!/bin/bash
javac *.java
rmic RmiOddImpl
rmic RmiOddImpl1
rmiregistry &
