from abc import ABCMeta, abstractmethod


class Query:

    """OJAI interface which lets users build an OJAI Query that can be executed
    on an OJAI DocumentStore."""

    __metaclass__ = ABCMeta

    @abstractmethod
    def set_option(self, option_name, value=None):
        """Sets a named query option. A query option can be used to provide hints to query execution engine.
        However, under stable conditions, a query option can not alter the result of the query.
        :param option_name: specific query options.
        :param value: object value.
        :return Query"""
        raise NotImplementedError("This should have been implemented.")

    @abstractmethod
    def set_options(self, options):
        """Sets multiple query options for this Query.
        :param options: specific query options. Could be either a Document, dictionary or a JSON string"""
        raise NotImplementedError("This should have been implemented.")

    @abstractmethod
    def set_timeout(self, timeout_in_millis):
        """Sets a duration after which the query will fails with QueryTimeoutError"""
        raise NotImplementedError("This should have been implemented.")

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
