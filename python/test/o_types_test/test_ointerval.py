from __future__ import unicode_literals

from ojai.types.OInterval import OInterval

try:
    import unittest2 as unittest
except ImportError:
    import unittest


class OIntervalTest(unittest.TestCase):

    def test_instance_millis(self):
        # check than 2 instance with the same constructor are equals
        instance1 = OInterval(milli_seconds=86754099)
        instance2 = OInterval(milli_seconds=86754099)
        self.assertTrue(instance1.__eq__(instance2))

        self.assertEqual(instance1.time_duration, long(86754099))
        from ojai.types.constants import MILLISECONDS_PER_DAY
        self.assertEqual(instance1.seconds, int((86754099 % MILLISECONDS_PER_DAY) / 1000))
        self.assertEqual(instance1.days, int(86754099 / MILLISECONDS_PER_DAY))
        self.assertEqual(instance1.years, 0)
        self.assertEqual(instance1.months, 0)

    def test_instance_full(self):
        instance = OInterval(milli_seconds=999, years=3, months=4, days=6, seconds=59)

        self.assertEqual(instance.milli_seconds, 999)
        self.assertEqual(instance.years, 3)
        self.assertEqual(instance.months, 4)
        self.assertEqual(instance.days, 6)
        self.assertEqual(instance.seconds, 59)

    # Test check that __hash__ works correct
    def test_instance_hash(self):
        instance1 = OInterval(milli_seconds=86754099)
        instance2 = OInterval(milli_seconds=86754099)
        instance3 = OInterval(milli_seconds=86754098)

        self.assertEqual(instance1.__hash__(), instance2.__hash__())
        self.assertFalse(instance1.__hash__() == instance3.__hash__())


if __name__ == '__main__':
    suite = unittest.TestLoader().loadTestsFromTestCase(OIntervalTest)
    unittest.TextTestRunner(verbosity=2).run(suite)
