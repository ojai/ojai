from abc import ABCMeta, abstractmethod


class Document:
    """Abstract class of OJAI Document, which describe all supported functional"""

    __metaclass__ = ABCMeta

    @abstractmethod
    def set_id(self, _id):
        """Sets the the "_id" field of this Document to the specified Value.
            :param _id: The value of _id field. Type may be str, Value, bytearray"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_id(self):
        """Method returns _id field of the Document"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def size(self):
        """:return the number of top level entries in the document"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def empty(self):
        """ Removed all of the entries from the document"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def set(self, field_path, value):
        """Sets the value of the specified fieldPath in this Document to the
        specified String.
        :param field_path: the FieldPath to set. Type may be str and FieldPath.
        :param value: the value. Type may be bool, byte, long, float, OTime, ODate, OTimestamp, OInterval,
        bytearray, dictionary, Document, Value, list, None.
        :return document itself"""

        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def delete(self, field_path):
        """Deletes the value at the specified FieldPath if it exists.
        :param field_path: the FieldPath to delete from the document. Type may be FieldPath, str.
        :return document itself"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_str(self, field_path):
        """Returns the value at the specified fieldPath.
        :param field_path: the path to get from the document. Type may be FieldPath, str.
        :return value at the specified field_path as str"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_boolean(self, field_path):
        """Returns the value at the specified fieldPath.
                :param field_path: the path to get from the document. Type may be FieldPath, str.
                :return value at the specified field_path as bool"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_int(self, field_path):
        """Returns the value at the specified fieldPath.
                :param field_path: the path to get from the document. Type may be FieldPath, str.
                :return value at the specified field_path as int"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_long(self, field_path):
        """Returns the value at the specified fieldPath.
                :param field_path: the path to get from the document. Type may be FieldPath, str.
                :return value at the specified field_path as long"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_float(self, field_path):
        """Returns the value at the specified fieldPath.
                :param field_path: the path to get from the document. Type may be FieldPath, str.
                :return value at the specified field_path as float"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_time(self, field_path):
        """Returns the value at the specified fieldPath.
                :param field_path: the path to get from the document. Type may be FieldPath, str.
                :return value at the specified field_path as OTime"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_date(self, field_path):
        """Returns the value at the specified fieldPath.
                :param field_path: the path to get from the document. Type may be FieldPath, str.
                :return value at the specified field_path as ODate"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_timestamp(self, field_path):
        """Returns the value at the specified fieldPath.
                :param field_path: the path to get from the document. Type may be FieldPath, str.
                :return value at the specified field_path as OTimestamp"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_binary(self, field_path):
        """Returns the value at the specified fieldPath.
                :param field_path: the path to get from the document. Type may be FieldPath, str.
                :return value at the specified field_path as bytearray"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_interval(self, field_path):
        """Returns the value at the specified fieldPath.
                :param field_path: the path to get from the document. Type may be FieldPath, str.
                :return value at the specified field_path as OInterval"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_dictionary(self, field_path):
        """Returns the value at the specified fieldPath.
                :param field_path: the path to get from the document. Type may be FieldPath, str.
                :return value at the specified field_path as dictionary"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_list(self, field_path):
        """Returns the value at the specified fieldPath.
                :param field_path: the path to get from the document. Type may be FieldPath, str.
                :return value at the specified field_path as list"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def as_dictionary(self):
        """Representing the Document into dictionary
        :return a new dictionary representing the Document"""
        raise NotImplementedError("Should have implemented this")
