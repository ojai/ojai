from abc import ABCMeta, abstractmethod

from ojai.DocumentStream import DocumentStream
from future.utils import with_metaclass


class QueryResult(with_metaclass(ABCMeta, DocumentStream)):

    @abstractmethod
    def get_query_plan(self):
        """Returns a query plan that was used for this QueryResults"""
        raise NotImplementedError("This should have been implemented.")
