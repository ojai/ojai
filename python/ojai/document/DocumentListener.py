from abc import ABCMeta, abstractmethod


class DocumentListener:
    __metaclass__ = ABCMeta

    def __init__(self):
        pass

    @abstractmethod
    def document_arrived(self, doc):
        """Called when a Document from the DocumentStream is available for consumption.
        :param doc: the available Document
        :return the implementation should return false to stop listening for more documents at which point
         the stream is closed """
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def failed(self, exception):
        """Called when an error occurs while retrieving a Document.
        The ill be closed and no new document will be returned.
        :param exception: the exception that describes the failure"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def eos(self):
        """Called when the end of the document stream is reached.
         The stream is already closed at this point."""
        raise NotImplementedError("Should have implemented this")
