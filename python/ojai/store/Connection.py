from abc import ABCMeta, abstractmethod
from future.utils import with_metaclass


class Connection(with_metaclass(ABCMeta, object)):
    """The Connection class defines the APIs to perform actions with storage.
    In client you may use static method:
    ConnectionFactory.get_connection(connection_str, options=None)"""

    @abstractmethod
    def create_store(self, store_path):
        """Creates a store into database and returns a DocumentStore instance, otherwise raise error
        :param store_path: store path
        :raises StoreError
        :return DocumentStore"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def is_store_exists(self, store_path):
        """Check is given store path exists in database.
        :param store_path: store path
        :raises StoreError
        :return boolean"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def delete_store(self, store_path):
        """Delete a store from database and returns boolean, otherwise raise error
        :param store_path: store path
        :raises StoreError
        :return boolean"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_store(self, store_path):
        """Returns a handle to an OJAI DocumentStore specified by the given name or path.
        :param store_path: name or path of an OJAI data source table."""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def new_document(self, json_string=None, dictionary=None):
        """Creates and returns a new, empty instance of an OJAI Document.
        :param json_string: string representation of Document.
        :param dictionary: python dict representation of Document.
        :return Document"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def new_mutation(self):
        """Creates and returns a new DocumentMutation object.
        :return DocumentMutation"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def new_condition(self):
        """Creates and returns a new QueryCondition object.
        :return QueryCondition"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def new_query(self, query_json=None):
        """Creates and returns empty or decoded from query_json new Query object.
        :param query_json: QUERY json, represents as string.
        :return: Query"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def close(self):
        """Close connection with server."""
        raise NotImplementedError("Should have implemented this")
