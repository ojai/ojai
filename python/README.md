Repository for python client OJAI support for MapR-DB.

Steps to execute:
1. Now it builds only for python 2.7 or higher. Check your python version
```python --version```
2. Check that pip installed:
```pip -V```
3. Next, install the following libraries, if not already installed.
```pip install twine wheel setuptools```
If some issue occurs with setuptools, use command:
```sudo -H pip install setuptools --upgrade```
4. Make sure that you are in the project root directory:
```ls -l setup.py```
5. Run command:
```python setup.py bdist_wheel```
6. Upon completion, the following directories will be created:
- build
- dist
- ojai_python_appi.egg-info
If you want to add created package locally in your project, copy dist/ojai_python_api-* to your project 
and in virtual env execute:
```pip install ojai_python_api-0.1-py2-none-any.whl```



Done.

Now you able to use the public API.

Example:
```
from ojai.document.Document import Document
class JsonDocument(Document):
```