from __future__ import unicode_literals

from ojai.fields.impl.Segment import IndexSegment, SegmentType, NameSegment

try:
    import unittest2 as unittest
except ImportError:
    import unittest


class SegmentTest(unittest.TestCase):

    def test_index_segment(self):
        index_segment0 = IndexSegment(0)
        index_segment1 = IndexSegment(1, child=index_segment0)
        index_segment2 = IndexSegment(2, child=index_segment1)
        self.assertTrue(index_segment0.segment_eq(index_segment0))
        self.assertFalse(index_segment0.segment_eq(index_segment1))
        self.assertTrue(index_segment1.is_array())
        self.assertTrue(index_segment2.is_array())
        builder = index_segment2.as_path_str(quote=True)
        self.assertEqual(builder, '[2][1][0]')
        self.assertTrue(index_segment2.has_index())
        self.assertNotEqual(index_segment2.get_segment(), index_segment1.get_segment())
        self.assertTrue(index_segment0.is_leaf())
        self.assertFalse(index_segment1.is_leaf())
        self.assertFalse(index_segment2.is_leaf())
        self.assertEqual(index_segment2.segment_type, SegmentType.ARRAY)
        self.assertEqual(index_segment2.segment_cmp(index_segment1), 1)
        self.assertEqual(index_segment2.segment_cmp(index_segment0), 2)

    def test_name_segment(self):
        name_segment0 = NameSegment(child=None, name="zero", quoted=True)
        name_segment1 = NameSegment(child=name_segment0, name="one")
        name_segment2 = NameSegment(child=name_segment1, name="two", quoted=True)

        self.assertTrue(name_segment0.is_named())
        self.assertEqual(name_segment0.segment_type, SegmentType.LEAF)
        self.assertEqual(name_segment1.segment_type, SegmentType.DICTIONARY)

        self.assertFalse(name_segment1.is_array())
        self.assertTrue(name_segment2.is_dictionary())
        self.assertEqual(name_segment1.get_segment(), name_segment1)
        self.assertNotEqual(name_segment1.get_segment(), name_segment2)
        self.assertNotEqual(name_segment1.get_segment(), name_segment0)

        self.assertFalse(name_segment1.quoted)
        self.assertTrue(name_segment2.quoted)

        builder = name_segment2.as_path_str(quote=False)
        self.assertEqual(builder, "\"two\".one.\"zero\"")


if __name__ == '__main__':
    suite = unittest.TestLoader().loadTestsFromTestCase(SegmentTest)
    unittest.TextTestRunner(verbosity=2).run(suite)
