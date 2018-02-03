from abc import ABCMeta, abstractmethod


class ConnectionManager:
    __metaclass__ = ABCMeta

    @abstractmethod
    def get_connection(self, url, options=None):
        """Returns a connection to MapR-DB.
        :param url: concrete URL to server.
        :param options: specific settings."""
        raise NotImplementedError("Should have implemented this")
