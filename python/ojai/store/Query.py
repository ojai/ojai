from abc import ABCMeta, abstractmethod
from future.utils import with_metaclass


class Query(with_metaclass(ABCMeta, object)):

    """OJAI interface which lets users build an OJAI Query that can be executed
    on an OJAI DocumentStore."""

    @abstractmethod
    def select(self, field_paths):
        """Adds the list of field paths to the list of projected fields.
        If not specified, the entire Document will be returned.
        Multiple invocation will append new fields to the list.
        :param field_paths: path to the selected fields.
        :return self for chained invocation."""
        raise NotImplementedError("This should have been implemented.")

    @abstractmethod
    def where(self, condition):
        """Sets the filtering condition for the query.
        :param condition: query condition, represent as str or QueryCondition object.
        :return self for chained invocation."""
        raise NotImplementedError("This should have been implemented.")

    @abstractmethod
    def order_by(self, field_paths, order=None):
        """Sets the sort ordering of the returned Documents to the ascending order of specified field paths.
        :param field_paths: specified field paths. Type may be str or FieldPath.
        :param order: order.
        :return self for chained invocation."""
        raise NotImplementedError("This should have been implemented.")

    @abstractmethod
    def offset(self, offset):
        """Zero (0) based index which specifies number of Documents to skip before
        returning any result. Negative values are not permitted.
        Multiple invocation will overwrite the previous value.
        :param offset: long or int value.
        :return self for chained invocation."""
        raise NotImplementedError("This should have been implemented.")

    @abstractmethod
    def limit(self, limit):
        """Restricts the maximum number of documents returned from this query
        to the specified value. Negative values are not permitted.
        :param limit: maximum number of returned documents. Long or int type.
        :return self for chained invocation."""
        raise NotImplementedError("This should have been implemented.")

    @abstractmethod
    def build(self):
        """Builds this Query object and make it immutable."""
        raise NotImplementedError("This should have been implemented.")
