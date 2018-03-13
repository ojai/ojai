from ojai.fields.FieldPath import FieldPath
from ojai.fields.impl.Segment import NameSegment


class FieldPathConstant(object):

    empty = FieldPath(NameSegment(name="", quoted=False, child=None))
