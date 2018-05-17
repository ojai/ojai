from __future__ import unicode_literals

from ojai.types.OTimestamp import OTimestamp

try:
    import unittest2 as unittest
except ImportError:
    import unittest


class OTimestampTest(unittest.TestCase):

    def test_days_from_epoch(self):
        o_timestamp = OTimestamp(year=1970, month_of_year=4, day_of_month=10, hour_of_day=12,
                                 minute_of_hour=25, second_of_minute=55, millis_of_second=600)
        o_timestamp_clone = OTimestamp(year=1970, month_of_year=4, day_of_month=10, hour_of_day=12,
                                       minute_of_hour=25, second_of_minute=55, millis_of_second=600)

        self.assertEqual(o_timestamp.get_year(), 1970)
        self.assertEqual(o_timestamp.get_month(), 4)
        self.assertEqual(o_timestamp.get_day_of_month(), 10)
        self.assertEqual(o_timestamp.get_hour(), 12)
        self.assertEqual(o_timestamp.get_minute(), 25)
        self.assertEqual(o_timestamp.get_second(), 55)
        self.assertEqual(o_timestamp.get_millis(), 600)
        self.assertTrue(o_timestamp.__eq__(o_timestamp_clone))

    def test_o_timestamp_from_millis_epoch(self):
        o_timestamp = OTimestamp(millis_since_epoch=8598355000)
        self.assertEqual(o_timestamp.get_year(), 1970)
        self.assertEqual(o_timestamp.get_month(), 4)
        self.assertEqual(o_timestamp.get_day_of_month(), 10)
        self.assertEqual(o_timestamp.get_hour(), 12)
        self.assertEqual(o_timestamp.get_minute(), 25)
        self.assertEqual(o_timestamp.get_second(), 55)

    def test_o_timestamp_from_date(self):
        import datetime
        date = datetime.datetime(year=1970, month=4, day=10, hour=12, minute=25, second=55)
        o_timestamp = OTimestamp(date=date)
        self.assertEqual(o_timestamp.get_year(), 1970)
        self.assertEqual(o_timestamp.get_month(), 4)
        self.assertEqual(o_timestamp.get_day_of_month(), 10)
        self.assertEqual(o_timestamp.get_hour(), 12)
        self.assertEqual(o_timestamp.get_minute(), 25)
        self.assertEqual(o_timestamp.get_second(), 55)

    def test_parse_timestamp_to_o_timestamp(self):
        o_timestamp = OTimestamp.parse("April 10 1970 12:25:55")
        self.assertEqual(o_timestamp.get_year(), 1970)
        self.assertEqual(o_timestamp.get_month(), 4)
        self.assertEqual(o_timestamp.get_day_of_month(), 10)
        self.assertEqual(o_timestamp.get_hour(), 12)
        self.assertEqual(o_timestamp.get_minute(), 25)
        self.assertEqual(o_timestamp.get_second(), 55)


if __name__ == '__main__':
    suite = unittest.TestLoader().loadTestsFromTestCase(OTimestampTest)
    unittest.TextTestRunner(verbosity=2).run(suite)
