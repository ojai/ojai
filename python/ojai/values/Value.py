from abc import ABCMeta, abstractmethod
from aenum import Enum


class Value:
    __metaclass__ = ABCMeta

    @abstractmethod
    def get_type(self):
        """:return the Type of this ValueType."""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_byte(self):
        """":return the value as a byte."""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_int(self):
        """:return the value as a int."""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_long(self):
        """:return the value as a long."""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_float(self):
        """:return the value as a float."""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_decimal(self):
        """:return the value as a decimal."""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_boolean(self):
        """:return the value as a bool"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_string(self):
        """:return the value as a str"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_timestamp(self):
        """:return the value as a OTimestamp"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_timestamp_as_long(self):
        """:return a long value representing the number of milliseconds since epoch."""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_date(self):
        """:return the value as a ODate"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_date_as_int(self):
        """:return a int representing the number of DAYS since Unix epoch."""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_time(self):
        """:return the value as a OTime"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_time_as_long(self):
        """:return a long representing the number of milliseconds since midnight."""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_interval(self):
        """:return the value as a OInterval"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_interval_as_long(self):
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_dictionary(self):
        """:return the value as a dict"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_list(self):
        """:return the value as a list"""
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_obj(self):
        raise NotImplementedError("Should have implemented this")

    @abstractmethod
    def get_binary(self):
        """:return the value as a bytearray"""
        raise NotImplementedError("Should have implemented this")


class ValueType(Enum):
    # A non-existing value of unknown type and quantity.
    NULL = 1

    # A boolean value.
    BOOLEAN = 2

    # Character sequence.
    STRING = 3

    # Bytes represent as string
    BYTE = 4

    # Integer
    INT = 5

    # Long
    LONG = 6

    # Floating point number.
    FLOAT = 7

    # Arbitrary precision, fixed point decimal value.
    DECIMAL = 8

    # Integer representing the number of DAYS since Unix epoch.
    DATE = 9

    # Integer representing time of the day in milliseconds.
    TIME = 10

    # Integer representing the number of milliseconds since epoch.
    TIMESTAMP = 11

    # A value representing a period of time between two instants.l
    INTERVAL = 12

    # Uninterpreted sequence of bytes.
    BINARY = 13

    # Mapping of str and Value
    DICTIONARY = 14

    # A list of Value
    ARRAY = 15

    @staticmethod
    def __check_value(value_type):
        """Represent ValueTypes to dictionary for util methods.
        :param value_type: ValueType enum value.
        :return value of value_type"""
        value_dictionary = {ValueType.NULL: 1, ValueType.BOOLEAN: 2, ValueType.STRING: 3, ValueType.BYTE: 4,
                            ValueType.INT: 5,
                            ValueType.LONG: 6, ValueType.FLOAT: 7, ValueType.DECIMAL: 8, ValueType.DATE: 9,
                            ValueType.TIME: 10, ValueType.TIMESTAMP: 11, ValueType.INTERVAL: 12,
                            ValueType.BINARY: 13, ValueType.DICTIONARY: 14, ValueType.ARRAY: 15}
        return value_dictionary[value_type]

    @staticmethod
    def is_scalar(value):
        """Check that given value is scalar.
        :param value: ValueType type.
        :return True if a value of given ValueType is DICTIONARY or ARRAY"""
        return ValueType.__check_value(value) != ValueType.__check_value(ValueType.DICTIONARY) \
               and ValueType.__check_value(value) != ValueType.__check_value(ValueType.ARRAY)

    @staticmethod
    def is_numeric(value):
        """Check that given value is numeric.
        :param value: ValueType type.
        :return True if a value of given ValueType between BYTE and DECIMAL."""
        return ValueType.__check_value(ValueType.BYTE) <= ValueType.__check_value(value) \
               <= ValueType.__check_value(ValueType.DECIMAL)
