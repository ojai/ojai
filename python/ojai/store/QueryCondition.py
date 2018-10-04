from builtins import object
from abc import ABCMeta, abstractmethod

from aenum import Enum
from future.utils import with_metaclass


class QueryCondition(with_metaclass(ABCMeta, object)):
    """The public API class contains comprehensive operations for the queries."""

    @abstractmethod
    def is_empty(self):
        """:return True if this condition is empty."""
        raise NotImplementedError("This should have been implemented.")

    @abstractmethod
    def is_built(self):
        """:return True if this condition is built."""
        raise NotImplementedError("This should have been implemented.")

    @abstractmethod
    def and_(self):
        """Begins a new AND compound condition block.
        :return self for chaining."""
        raise NotImplementedError("This should have been implemented.")

    @abstractmethod
    def or_(self):
        """Begins a new OR compound condition block.
        :return self for chaining."""
        raise NotImplementedError("This should have been implemented.")

    @abstractmethod
    def element_and(self, field_path):
        """Begins a new element-wise AND compound condition block.
        :param field_path: the array expression for the fields' container
        :return self for chaining"""
        raise NotImplementedError("This should have been implemented.")

    @abstractmethod
    def close(self):
        """Closes a compound condition block."""
        raise NotImplementedError("This should have been implemented.")

    @abstractmethod
    def build(self):
        """Closes all nested compound condition blocks.
        :return self"""
        raise NotImplementedError("This should have been implemented.")

    @abstractmethod
    def condition_(self, condition_to_add):
        """Appends the specified condition to the current condition block.
        :param condition_to_add: specific condition of QueryCondition type
        :return self for chained invocation"""
        raise NotImplementedError("This should have been implemented.")

    @abstractmethod
    def exists_(self, field_path):
        """Adds a condition that tests for existence of the specified.
        :param field_path: the path to test. Type may be str, FieldPath.
        :return self for chained invocation."""
        raise NotImplementedError("This should have been implemented.")

    @abstractmethod
    def not_exists_(self, field_path):
        """Adds a condition that tests for non-existence of the specified FieldPath.
        :param field_path: the path to test.Type may be str, FieldPath.
        :return self for chained invocation."""
        raise NotImplementedError("This should have been implemented.")

    @abstractmethod
    def in_(self, field_path, list_of_value):
        """ Adds a condition that tests if the Value at the specified
        FieldPath is equal to at least one of the values in the
        specified list.
        :param field_path: the path to test. Type may be str, FieldPath.
        :param list_of_value: the list of values to test against.
        :return self for chained invocation."""
        raise NotImplementedError("This should have been implemented.")

    @abstractmethod
    def not_in_(self, field_path, list_of_value):
        """ Adds a condition that tests if the Value at the specified
        FieldPath is not equal to any of the values in the
        specified list.
        :param field_path: the path to test. Type may be str, FieldPath.
        :param list_of_value: the list of values to test against.
        :return self for chained invocation."""
        raise NotImplementedError("This should have been implemented.")

    @abstractmethod
    def type_of_(self, field_path, value_type):
        """Adds a condition that tests if the Value at the specified
        FieldPath is of the specified ValueType.
        :param field_path: the path to test. Type may be str, FieldPath.
        :param value_type: ValueType.
        :return self for chained invocation."""
        raise NotImplementedError("This should have been implemented.")

    @abstractmethod
    def not_type_of_(self, field_path, value_type):
        """Adds a condition that tests if the Value at the specified
        FieldPath is not of the specified ValueType.
        :param field_path: the path to test. Type may be str, FieldPath.
        :param value_type: ValueType.
        :return self for chained invocation."""
        raise NotImplementedError("This should have been implemented.")

    @abstractmethod
    def matches_(self, field_path, regex):
        """Adds a condition that tests if the Value at the specified
        FieldPath is a String and matches the specified regular
        expression.
        :param field_path: the path to test. Type may be str, FieldPath.
        :param regex: the reference regular expression.
        :return self for chained invocation."""
        raise NotImplementedError("This should have been implemented.")

    @abstractmethod
    def not_matches_(self, field_path, regex):
        """Adds a condition that tests if the Value at the specified
        FieldPath is a String and does not match the specified regular
        expression.
        :param field_path: the path to test. Type may be str, FieldPath.
        :param regex: the reference regular expression.
        :return self for chained invocation."""
        raise NotImplementedError("This should have been implemented.")

    @abstractmethod
    def like_(self, field_path, like_expression, escape_char=None):
        """Adds a condition that tests if the Value at the specified
        FieldPath is a String and matches the specified SQL LIKE
        expression optionally escaped with the specified escape character.
        :param field_path: the path to test. Type may be str, FieldPath.
        :param like_expression: he reference LIKE pattern.
        :param escape_char: the escape character in the LIKE pattern.
        :return self for chained invocation."""
        raise NotImplementedError("This should have been implemented.")

    @abstractmethod
    def not_like_(self, field_path, like_expression, escape_char=None):
        """Adds a condition that tests if the Value at the specified
        FieldPath is a String and does not match the specified SQL LIKE
        expression optionally escaped with the specified escape character.
        :param field_path: the path to test. Type may be str, FieldPath.
        :param like_expression: he reference LIKE pattern.
        :param escape_char: the escape character in the LIKE pattern.
        :return self for chained invocation."""
        raise NotImplementedError("This should have been implemented.")

    @abstractmethod
    def is_(self, field_path, op, value):
        """Adds a condition that tests if the Value at the specified
        FieldPath satisfies the given Op against the specified value.
        :param field_path: the path to test. Type may be str, FieldPath.
        :param op: the QueryConditionOp to apply.
        :param value: the reference to Value.
        :return self for chained invocation."""
        raise NotImplementedError("This should have been implemented.")

    @abstractmethod
    def equals_(self, field_path, value):
        """Adds a condition that tests if the Value at the specified
        FieldPath equals the specified value. Two values are considered equal if and only if they contain the same
        key-value pair in the same order.
        :param field_path: the path to test. Type may be str, FieldPath.
        :param value: the reference to Value.
        :return self for chained invocation."""
        raise NotImplementedError("This should have been implemented.")

    @abstractmethod
    def not_equals_(self, field_path, value):
        """Adds a condition that tests if the Value at the specified
        FieldPath does not equal the specified value. Two values are considered equal if and only if they contain the same
        key-value pair in the same order.
        :param field_path: the path to test. Type may be str, FieldPath.
        :param value: the reference to Value.
        :return self for chained invocation."""
        raise NotImplementedError("This should have been implemented.")

    @abstractmethod
    def size_of_(self, field_path, op, size):
        """Adds a condition that tests if the size of the Value at the
        specified FieldPath satisfies the given QueryConditionOp and the size.
        The value must be one of the following types: STRING,
        BINARY, DICTIONARY or ARRAY.
        :param field_path: the path to test. Type may be str, FieldPath.
        :param op: the QueryConditionOp to apply.
        :param size: the reference size of Value.
        :return self for chained invocation."""
        raise NotImplementedError("This should have been implemented.")


class QueryConditionOp(Enum):
    """Enum which represents query condition operations, which describe logical operations such as: LESS,
     LESS_OR_EQUAL, EQUAL, NOT_EQUAL, GREATER_OR_EQUAL, GREATER."""
    # The Value at the specified path is less than the reference value.
    # Reference value type: All scalars ValueType, such as: NULL, BOOLEAN, STRING, BYTE,
    # SHORT, INT, LONG, FLOAT, DOUBLE, DATE, TIME, TIMESTAMP, INTERVAL, BINARY.
    LESS = "$lt"

    # The Value at the specified path is less than or equal to the reference value.
    # Reference value type: All scalars ValueType, such as: NULL, BOOLEAN, STRING, BYTE,
    # SHORT, INT, LONG, FLOAT, DOUBLE, DATE, TIME, TIMESTAMP, INTERVAL, BINARY.
    LESS_OR_EQUAL = "$le"

    # The Value at the specified path is equal to the reference value.
    # Reference value type: All ValueType.
    EQUAL = "$eq"

    # The Value at the specified path is not equal to the reference value.
    # Reference value type: All ValueType.
    NOT_EQUAL = "$ne"

    # The Value at the specified path is greater than or equal to the reference value.
    # Reference value type: All scalars ValueType, such as: NULL, BOOLEAN, STRING, BYTE,
    # SHORT, INT, LONG, FLOAT, DOUBLE, DATE, TIME, TIMESTAMP, INTERVAL, BINARY.
    GREATER_OR_EQUAL = "$ge"

    # The Value at the specified path is greater than the reference value.
    # Reference value type: All scalars ValueType, such as: NULL, BOOLEAN, STRING, BYTE,
    # SHORT, INT, LONG, FLOAT, DOUBLE, DATE, TIME, TIMESTAMP, INTERVAL, BINARY.
    GREATER = "$gt"
