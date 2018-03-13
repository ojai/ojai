from aenum import enum


class Segment:

    def __init__(self, child=None):
        self.__child = child
        if child is None:
            self.segment_type = SegmentType.LEAF
        else:
            self.segment_type = SegmentType.ARRAY if child.is_indexed() else SegmentType.DICTIONARY

    def is_dictionary(self):
        """:return true if the current segment_type is SegmentType.DICTIONARY"""
        return self.segment_type == SegmentType.DICTIONARY

    def is_array(self):
        """:return true if the current segment_type is SegmentType.ARRAY"""
        return self.segment_type == SegmentType.ARRAY

    def is_leaf(self):
        """:return true if the current segment_type is SegmentType.LEAF"""
        return self.segment_type == SegmentType.LEAF

    def is_indexed(self):
        """:return true if the current segment_type is identified by an index"""
        return False

    def is_named(self):
        """:return true if the current segment is identified by a name"""
        return False

    def __clone_with_new_child(self, segment):
        # TODO I think, that this method is not required for first release
        pass

    def segment_cmp(self, segment):
        pass

    def write_segment(self, builder, quote):
        raise NotImplementedError

    def segment_eq(self, segment):
        raise NotImplementedError

    @property
    def child(self):
        return self.__child

    def is_last_path(self):
        return self.child is None

    def as_path_str(self, quote=None):
        if quote is not None and not isinstance(quote, bool):
            raise TypeError
        builder = ""
        segment = self
        builder = segment.write_segment(builder=builder, quote=quote)
        segment = segment.child
        while segment is not None:
            if segment.is_named():
                builder += '.'
            builder = segment.write_segment(builder=builder, quote=quote)
            segment = segment.child
        return builder


class IndexSegment(Segment):

    def __init__(self, index, child=None):
        # Segment.__init__(self)
        Segment.__init__(self, child)
        """
        :param child: child of segment
        :param index: index of segment in array. Type may be int.
        """
        # Segment.__init__(self, child)
        if index is not None and index < -1:
            raise AttributeError
        else:
            self.__index = index

    @property
    def index(self):
        return self.__index

    def has_index(self):
        return self.index != -1 and self.index is not None

    def is_indexed(self):
        return True

    def get_segment(self):
        return self

    def segment_hash(self):
        return self.index

    def segment_cmp(self, segment):
        if isinstance(segment, IndexSegment):
            return self.index - segment.index
        else:
            return 1 if segment is None else -1

    def segment_eq(self, segment):
        if self is segment:
            return True
        elif segment is None:
            return False
        elif isinstance(segment, IndexSegment):
            return segment.index == self.index
        else:
            return False

    def write_segment(self, builder, quote):
        if self.index >= 0:
            builder += '[' + str(self.index) + ']'
        else:
            builder += '[]'
        return builder

    def as_json_str(self):
        return str(self.index)


class NameSegment(Segment):

    def __init__(self, name, quoted=None, child=None):
        # TODO implement quoted check
        Segment.__init__(self, child)
        self.__name = name
        self.__quoted = quoted

    @property
    def name(self):
        return self.__name

    @property
    def quoted(self):
        return self.__quoted

    def is_named(self):
        return True

    def segment_cmp(self, segment):
        if isinstance(segment, NameSegment):
            return 1 if self.name == segment.name else -1
        return 1

    def get_segment(self):
        return self

    def segment_hash(self):
        # TODO check it later. I think we don't need hash
        return 0 if self.name is None else bytearray(self.name)

    def segment_eq(self, segment):
        if segment is self:
            return True
        elif segment is None:
            return False
        elif self.name is None:
            # TODO why if both segment name is None - segment equals
            return segment.name is None
        else:
            return str(segment.name).lower() == str(self.name).lower()

    def __eq__(self, other):
        return self.segment_eq(other)

    def __escape(self, builder, quote):
        # switcher = {
        #     '"': '\\',
        #     '`': '\\',
        #     '\\': '\\',
        #     '\b': '\\b',
        #     '\f': '\\f',
        #     '\n': '\\n',
        #     '\r': '\\r',
        #     '\t': '\\t',
        #     '.': lambda: if not quote and not self.quoted else '',
        #     '['
        # }
        for char in self.name:
            if any(x == char for x in ['"', '`', '\\']):
                builder += '\\' + char
            elif any(x == char for x in ['\b', '\f', '\n', '\r', '\t']):
                builder += '\\' + char
            elif any(x == char for x in ['.', '[', ']']) and not quote and not self.quoted:
                builder += '\\'
            else:
                # TODO how we add simple characters
                builder += char
        # TODO
        return builder

    @staticmethod
    def un_escape(raw_str):
        is_escape = False
        builder = ''
        # for ch in raw_str:
        for index in range(len(raw_str)):
            ch = raw_str[index]
            if is_escape:
                if ch == 'u':
                    if index + 4 >= len(raw_str):
                        raise TypeError
                    builder += "TODO"
                    index += 4
                # elif ch in any(['"', '`', '\\', '/', '.', '[', ']']):
                elif any(x == ch for x in ['"', '`', '\\', '/', '.', '[', ']']):
                    builder += ch
                elif ch == 'b':
                    builder += '\b'
                elif ch == 'f':
                    builder += '\f'
                elif ch == 'n':
                    builder += '\n'
                elif ch == 'r':
                    builder += '\r'
                elif ch == 't':
                    builder += '\t'
                else:
                    raise AttributeError
                is_escape = False
            elif ch == '\\':
                is_escape = True
            else:
                builder += ch
        return builder

    @staticmethod
    def un_quote(raw_str):
        if raw_str is None or len(raw_str) < 2 or "\"`".find(raw_str[0]) \
                == -1 or "\"`".find(raw_str[len(raw_str) - 1]) == -1:
            raise AttributeError
        b = raw_str[1:len(raw_str)-1]
        return NameSegment.un_escape(b)

    def write_segment(self, builder, quote):
        if quote or self.quoted:
            builder += '"'
        builder = self.__escape(builder, quote)
        if quote or self.quoted:
            builder += '"'

        return builder

    def as_json_str(self):
        builder = '\"'
        builder = self.write_segment(builder, False)
        builder += '"'
        return builder


class SegmentType(enum):
    DICTIONARY = 1,
    ARRAY = 2,
    LEAF = 3
