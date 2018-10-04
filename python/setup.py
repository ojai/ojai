from setuptools import setup, find_packages
from codecs import open
from os import path

here = path.abspath(path.dirname(__file__))
setup(name='ojai_python_api',
      version='1.1',
      description='OJAI Python APIs',
      url='https://github.com/ojai/ojai/tree/master/python',
      author='MapR, Inc.',
      keywords='ojai python api',
      packages=find_packages(exclude=['docs*', 'test*', 'documentations*', 'store']),
      python_requires='>=2.7',
      install_requires=['aenum>=2.0.10', 'python-dateutil>=2.6.1', 'future']
      )


# Get the long description from the README file
with open(path.join(here, 'README.md'), encoding='utf-8') as f:
    long_description = f.read()
