from abc import ABCMeta, abstractmethod


class DocumentStream:
    """ A stream of documents."""

    __metaclass__ = ABCMeta

    @abstractmethod
    def iterator(self):
        """Returns an iterator over a set of Document"""
        raise NotImplementedError("Should have implemented this")

