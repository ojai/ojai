from ojai.fields.impl.FieldSegmentIterator import FieldSegmentIterator
from antlr4 import *


class FieldPath:
    """Class for representing a field path."""

    def __init__(self, root):
        self.__root = root
        # self.__empty = FieldPathConstant.empty

    # @staticmethod
    # def parse_from(field_path):
    #     """
    #     Parse and create FieldPath object from string path representation.
    #     :param field_path: represented as string
    #     :return: FieldPath obj
    #     """
    #     if not isinstance(field_path, str) or field_path is None:
    #         raise TypeError
    #     elif len(field_path) == 0:
    #         return FieldPath(NameSegment(name="", quoted=False, child=None))
    #
    #     lexer = FieldPathLexer(field_path)
    #     token_stream = CommonTokenStream(lexer)
    #     parser = FieldPathParser(token_stream)
    #
    #     fp = parser.parse()

    @property
    def root(self):
        return self.__root

    def iterator(self):
        return FieldSegmentIterator(root=self.__root)

    def __eq__(self, other):
        if self is other:
            return True
        if not isinstance(self, type(other)):
            return False
        if self.root is None:
            return other.root() is None
        return self.root.__eq__(other.root)

    def __hash__(self):
        pass

    def __str__(self):
        return self.as_path_str()

    def __cmp__(self, other):
        return self.__root.segment_cmp(other.get_root_segment())

    def as_path_str(self, quote=None):
        return self.__root.as_path_str(quote=quote)

    def get_root_segment(self):
        return self.__root

    def clone_with_new_parent(self, parent_segment):
        pass

    def clone_with_new_child(self, child_segment, index):
        pass

    def clone_after_ancestor(self, ancestor):
        pass

    def as_json_str(self):
        return '\"{0}\"'.format(self.as_path_str().replace('\"', '\\\"'))

    @staticmethod
    def parse_from(field_path):
        """
        Parse and create FieldPath object from string path representation.
        :param field_path: represented as string
        :return: FieldPath obj
        """
        from ojai.fields.impl.FieldPathConstant import FieldPathConstant
        if not isinstance(field_path, str) and not isinstance(field_path, unicode) or field_path is None:
            raise TypeError
        elif len(str(field_path)) == 0:
            # return FieldPath(NameSegment(name="", quoted=False, child=None))
            """:return Empty instance of NameSegment"""
            return FieldPathConstant.empty
        stream = InputStream(field_path)
        from ojai.antrl4.FieldPathPythonLexer import FieldPathPythonLexer
        lexer = FieldPathPythonLexer(stream)
        token_stream = CommonTokenStream(lexer)
        from ojai.antrl4.FieldPathPythonParser import FieldPathPythonParser
        parser = FieldPathPythonParser(token_stream)

        fp = parser.parse().fp

        return FieldPathConstant.empty if FieldPathConstant.empty.__eq__(fp) else fp
