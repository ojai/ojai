from abc import ABCMeta, abstractmethod

from ojai.DocumentStream import DocumentStream


class QueryResult(DocumentStream):

    __metaclass__ = ABCMeta

    @abstractmethod
    def get_query_plan(self):
        """Returns a query plan that was used for this QueryResults"""
        raise NotImplementedError("This should have been implemented.")
