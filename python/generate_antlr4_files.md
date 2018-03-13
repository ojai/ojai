## Generate antlr4 files. ##

1. For generation the antlr4 files need to download jar, which you can find on the [official site](http://www.antlr.org/download.html).
 - as example `curl -O http://www.antlr.org/download/antlr-4.7.1-complete.jar`
2. Make sure that you are in the private-ojai root directory. As example in terminal ran `pwd` command and result must be like:
   ``path/to/project/private-ojai/``
3. Specify the path to your jar file and launch command like:
`java -Xmx500m -cp path/to/jar/antlr-4.7.1-complete.jar org.antlr.v4.Tool -Dlanguage=Python2 /path/to/gramar/file/FieldPath.g4`

4. All files will be generated in the directory with the `FieldPath.g4` file.
