class SegmentIterator:

    def __init__(self, root):
        self.__current = root

    @property
    def current(self):
        return self.__current

    def __iter__(self):
        return self

    def next(self):
        if self.__current is None:
            return StopIteration
        ret = self.__current
        self.__current = self.__current.child
        return ret

    def has_next(self):
        # TODO use try-except instead of has_next()!!!
        pass
