## Generate documentation with Doxygen. ##

In order to generate documentation, you need to do few simple steps.

1. Make sure that you are in the private-ojai python root directory. As example in terminal ran `pwd` command and result must be like:
``path/to/project/private-ojai/python``
2. Check that Doxygen is installed: `doxygen --version`. Otherwise install Doxygen in you machine:
    1) as example on ubuntu it just run `sudo apt-get install doxygen`. Foy other OSes you can find instruction in the [official page](http://www.stack.nl/~dimitri/doxygen/).
3. Check that Doxyfile is present. `ls Doxyfile`
4. Launch the command `doxygen Doxyfile`.
5. You can find all generated files in the ***documentation*** directory.
