class FieldSegmentIterator:
    def __init__(self, root):
        self.__current = root

    def __iter__(self):
        return self

    def next(self):
        if self.__current is None:
            raise StopIteration
        else:
            ret = self.__current
            self.__current = self.__current.child()
            return ret
