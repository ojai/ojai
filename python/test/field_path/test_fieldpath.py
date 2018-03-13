from __future__ import unicode_literals

from ojai.fields.FieldPath import FieldPath
from ojai.fields.impl.FieldPathConstant import FieldPathConstant

try:
    import unittest2 as unittest
except ImportError:
    import unittest


class FieldPathTest(unittest.TestCase):

    def test_empty_field_path(self):
        root = FieldPathConstant.empty.root
        self.assertIsNotNone(root)
        self.assertTrue(root.is_last_path)
        self.assertTrue(root.is_leaf())
        self.assertEqual("", root.as_path_str())

    def test_path_with_underscore(self):
        fp = FieldPath.parse_from("test_path")
        self.assertTrue(fp.get_root_segment().is_leaf())
        self.assertEqual("\"test_path\"", fp.as_path_str(True))
        self.assertEqual("test_path", fp.as_path_str(False))
        self.assertEqual("test_path", fp.as_path_str())
        self.assertEqual("\"test_path\"", fp.as_json_str())

    def test_path_with_hyphen(self):
        fp = FieldPath.parse_from("test-path")
        self.assertTrue(fp.get_root_segment().is_leaf())
        self.assertEqual("\"test-path\"", fp.as_path_str(True))
        self.assertEqual("test-path", fp.as_path_str(False))
        self.assertEqual("test-path", fp.as_path_str())
        self.assertEqual("\"test-path\"", fp.as_json_str())

    def test_path_with_space(self):
        fp = FieldPath.parse_from("test path.with space")
        self.assertFalse(fp.get_root_segment().is_leaf())
        self.assertTrue(fp.get_root_segment().child.is_leaf())

        self.assertEqual('\"test path\".\"with space\"', fp.as_path_str(True))
        self.assertEqual('test path.with space', fp.as_path_str(False))
        self.assertEqual('test path.with space', fp.as_path_str())
        self.assertEqual('\"test path.with space\"', fp.as_json_str())

        fp = FieldPath.parse_from("a[ ]")
        self.assertEqual('\"a\"[]', fp.as_path_str(True))
        self.assertEqual('a[]', fp.as_path_str(False))
        self.assertEqual('\"a[]\"', fp.as_json_str())

    def test_simple_path_single_segment(self):
        fp = FieldPath.parse_from("a")
        self.assertTrue(fp.get_root_segment().is_leaf())
        self.assertEqual('\"a\"', fp.as_path_str(True))
        self.assertEqual('a', fp.as_path_str(False))
        self.assertEqual('a', fp.as_path_str())
        self.assertEqual('\"a\"', fp.as_json_str())

    def test_quoted_path(self):
        fp = FieldPath.parse_from('\"test path.with space\"')
        self.assertTrue(fp.get_root_segment().is_last_path())
        self.assertEqual('\"test path.with space\"', fp.as_path_str(True))
        self.assertEqual('\"test path.with space\"', fp.as_path_str(False))
        self.assertEqual('\"test path.with space\"', fp.as_path_str())

    def test_quoted_escaped_path(self):
        fp = FieldPath.parse_from('\"the\\\"quick.brown\\\\fox\"')
        self.assertTrue(fp.get_root_segment().is_last_path())
        self.assertEqual('\"the\\\"quick.brown\\\\fox\"', fp.as_path_str(True))
        self.assertEqual('\"the\\\"quick.brown\\\\fox\"', fp.as_path_str(False))
        self.assertEqual('\"the\\\"quick.brown\\\\fox\"', fp.as_path_str())
        self.assertEqual("\"\\\"the\\\\\"quick.brown\\\\fox\\\"\"", fp.as_json_str())

    def test_simple_path_double_segment(self):
        fp = FieldPath.parse_from('a.\"b\"')
        self.assertTrue(fp.get_root_segment().is_dictionary())
        self.assertTrue(fp.get_root_segment().child.is_leaf())
        self.assertEqual('\"a\".\"b\"', fp.as_path_str(True))
        self.assertEqual('a.\"b\"', fp.as_path_str(False))
        self.assertEqual('a.\"b\"', fp.as_path_str())
        self.assertEqual('\"a.\\\"b\\\"\"', fp.as_json_str())

    def test_simple_path_with_arrays(self):
        fp = FieldPath.parse_from('a.b[3].c')
        self.assertTrue(fp.get_root_segment().is_dictionary())
        self.assertTrue(fp.get_root_segment().child.is_array())
        self.assertTrue(fp.get_root_segment().child.child.is_indexed())
        self.assertTrue(fp.get_root_segment().child.child.is_dictionary())
        self.assertTrue(fp.get_root_segment().child.child.child.is_leaf())

        self.assertEqual('\"a\".\"b\"[3].\"c\"', fp.as_path_str(True))
        self.assertEqual('a.b[3].c', fp.as_path_str())

    def test_simple_path_with_numeric_name_segment(self):
        fp = FieldPath.parse_from('1.23.4a')
        self.assertTrue(fp.get_root_segment().is_dictionary())
        self.assertTrue(fp.get_root_segment().child.is_dictionary())
        self.assertTrue(fp.get_root_segment().child.child.is_named())
        self.assertTrue(fp.get_root_segment().child.child.is_leaf())

        self.assertEqual('\"1\".\"23\".\"4a\"', fp.as_path_str(True))
        self.assertEqual('1.23.4a', fp.as_path_str())
        self.assertEqual('\"1.23.4a\"', fp.as_json_str())

    def test_simple_path_with_arrays_empty_index(self):
        fp = FieldPath.parse_from('a.b[].c')
        self.assertTrue(fp.get_root_segment().is_dictionary())
        self.assertTrue(fp.get_root_segment().child.is_array())
        self.assertTrue(fp.get_root_segment().child.child.is_indexed())
        self.assertTrue(fp.get_root_segment().child.child.is_dictionary())
        self.assertTrue(fp.get_root_segment().child.child.child.is_leaf())
        self.assertEqual('\"a\".\"b\"[].\"c\"', fp.as_path_str(True))
        self.assertEqual('a.b[].c', fp.as_path_str())
        self.assertEqual('"a.b[].c"', fp.as_json_str())

    def test_escaped_path_single_segment(self):
        fp = FieldPath.parse_from('\"a\"')
        self.assertTrue(fp.get_root_segment().is_leaf())
        self.assertEqual('\"a\"', fp.as_path_str(True))
        self.assertEqual('\"a\"', fp.as_path_str())
        self.assertEqual('\"\\\"a\\\"\"', fp.as_json_str())

    def test_escaped_path_double_segment(self):
        fp = FieldPath.parse_from('\"a.b\"')
        self.assertTrue(fp.get_root_segment().is_leaf())
        self.assertEqual('\"a.b\"', fp.as_path_str(True))
        self.assertEqual('\"a.b\"', fp.as_path_str())
        self.assertEqual('\"\\\"a.b\\\"\"', fp.as_json_str())

    def test_escaped_path_with_arrays(self):
        fp = FieldPath.parse_from('a.\"b[3].c\"')
        self.assertTrue(fp.get_root_segment().is_dictionary())
        self.assertTrue(fp.get_root_segment().child.is_leaf())
        self.assertEqual('\"a\".\"b[3].c\"', fp.as_path_str(True))
        self.assertEqual('a.\"b[3].c\"', fp.as_path_str())
        self.assertEqual('\"a.\\\"b[3].c\\\"\"', fp.as_json_str())

    def test_escaped_path_with_arrays_empty_index(self):
        fp = FieldPath.parse_from('\"a.b\"[].c')
        self.assertTrue(fp.get_root_segment().is_array())
        self.assertTrue(fp.get_root_segment().child.is_indexed())
        self.assertTrue(fp.get_root_segment().child.is_dictionary())
        self.assertTrue(fp.get_root_segment().child.child.is_leaf())
        self.assertEqual('\"a.b\"[].\"c\"', fp.as_path_str(True))
        self.assertEqual('\"a.b\"[].c', fp.as_path_str())
        self.assertEqual('\"\\\"a.b\\\"[].c\"', fp.as_json_str())

    def test_canonical_form(self):
        fp1 = FieldPath.parse_from("a.b.\"c\"[4]")
        fp2 = FieldPath.parse_from("a.\"b\".c[4]")
        self.assertEqual(fp1, fp2)
