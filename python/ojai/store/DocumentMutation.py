from abc import ABCMeta, abstractmethod
from future.utils import with_metaclass


class DocumentMutation(with_metaclass(ABCMeta, object)):
    """
    The DocumentMutation abstract class defines the APIs to perform mutation
    of a Document already stored in a DocumentStore.
    """

    @abstractmethod
    def empty(self):
        """Empties this DocumentMutation object."""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def set(self, field_path, value):
        """Sets the field at the given FieldPath to given value
        :param field_path: path of the field that needs to be updated. Type may be FieldPath, str.
        :param value: the new value to set at the path. Type may be None, Value, bool, byte, int, long, float, str,
         ODate, OTime, OTimestamp, OInterval, bytearray, list, dictionary, Document.
        :return self for chained invocation"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def set_or_replace(self, field_path, value):
        """Sets or replaces the field at the given FieldPath to Value
        :param field_path: FieldPath in the document that needs to be updated. Type may be FieldPath, str.
        :param value: the new value to set or replace at the given path. Type may be None, Value, bool, byte, int, long,
        float, str, ODate, OTime, OTimestamp, OInterval, bytearray, list, dictionary, Document.
        :return self for chained invocation"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def append(self, field_path, value, offset=None, length=None):
        """Appends the given value to an existing value at the given path.
        :param field_path: the FieldPath to apply this append operation. Type may be FieldPath, str.
        :param value: the value to append. Type may be bytearray, list, str.
        :param length: length in byte array
        :param offset: offset in byte array
        :return self for chained invocation"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def merge(self, field_path, value):
        """Merges the existing MAP at the given FieldPath with the specified Document.
        :param field_path: FieldPath to apply this merge operation. Type may be FieldPath, str.
        :param value: the value to be merged. Type may be Document, dictionary.
        :return self for chained invocation"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def increment(self, field_path, inc):
        """Atomically increment the existing value at given the FieldPath by the given value.
        :param field_path: FieldPath to apply this increment operation. Type may be FieldPath, str.
        :param inc: increment to apply to a field - can be positive or negative. Type may be byte, int, long,
        float.
        :return self for chained invocation"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def decrement(self, field_path, dec):
        """Atomically decrements the given field (in dot separated notation) of the given row id.
        If the field path specified for the decrement operation doesn't
        exist in the document in document store, then this operation will create a new element at the
        given path. This new element will be of same type as the value specified
        in the parameter.

        This operation will fail if the type of any intermediate path elements specified
        in the append field path doesn't match the type of the corresponding field in the
        document stored on the document store. For example, an operation on field "a.b.c" will fail
        if, on the document store, document a itself is an array or integer.

        If the field doesn't exist in
        the document store then it will be created with the type of given decremental value.
        A decrement operation can be applied on any of the numeric types
        of a field, such as byte, int, long or float.
        The operation will fail if the decrement is applied to a field
        that is of a non-numeric type.

        The decrement operation won't change the type of the existing value stored in
        the given field for the row. The resultant value of the field will be
        truncated based on the original type of the field.

        For example, field 'score' is of type int and contains 60. The decrement
        '5.675', a double, is applied. The resultant value of the field
        will be 54 (54.325 will be truncated to 54).

        :param field_path: field name in dot separated notation. Type may be FieldType, str.
        :param dec: decrement to apply to a field - can be positive or negative. Type may be byte, int, float.
        :return self for chained invocation."""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def delete(self, field_path):
        """Deletes the field at the given path.
        If the field does not exist, the mutation operation will silently succeed.
        :param field_path: he FieldPath to delete
        :return self for chained invocation"""
        raise NotImplementedError("Should have implemented this")

