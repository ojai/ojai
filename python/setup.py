from setuptools import setup, find_packages
from codecs import open
from os import path

here = path.abspath(path.dirname(__file__))

setup(name='ojai_python_api',
      version='0.1',
      description='OJAI Python APIs',
      url='https://github.com/ojai/ojai/tree/master/python',
      author='MapR inc.',
      authoer_email='dshylov@mapr.com',
      keywords='ojai python client mapr',
      packages=find_packages(exclude=['contrib', 'docs', 'tests*', 'document', 'values', 'store']),
      python_requires='>=2.7'
      )

# Get the long description from the README file
with open(path.join(here, 'README.md'), encoding='utf-8') as f:
    long_description = f.read()
