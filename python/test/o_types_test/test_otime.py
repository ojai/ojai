from __future__ import unicode_literals

from ojai.o_types.OTime import OTime

try:
    import unittest2 as unittest
except ImportError:
    import unittest


class OTimeTest(unittest.TestCase):

    def test_days_from_epoch(self):
        epoch = 8587555
        o_time = OTime(timestamp=epoch)
        self.assertEqual(o_time.get_hour(), 12)
        self.assertEqual(o_time.get_minute(), 25)
        self.assertEqual(o_time.get_second(), 55)
        self.assertEqual(o_time.get_millis(), 0)
        parse_o_time = OTime.parse(time_str="12:25:55")
        self.assertEqual(parse_o_time.get_hour(), o_time.get_hour())
        self.assertEqual(parse_o_time.get_minute(), o_time.get_minute())
        self.assertEqual(parse_o_time.get_second(), o_time.get_second())
        self.assertTrue(o_time.__eq__(parse_o_time))
        self.assertEqual(o_time.to_str("%H:%M:%S"), "12:25:55")
        self.assertEqual(o_time.time_to_str(), "12:25:55")

    def test_o_time_from_time(self):
        o_time = OTime(hour_of_day=12, minutes=25, seconds=55)
        self.assertEqual(o_time.get_hour(), 12)
        self.assertEqual(o_time.get_minute(), 25)
        self.assertEqual(o_time.get_second(), 55)
        self.assertEqual(o_time.get_millis(), 0)
        parse_o_time = OTime.parse(time_str="12:25:55")
        self.assertEqual(parse_o_time.get_hour(), o_time.get_hour())
        self.assertEqual(parse_o_time.get_minute(), o_time.get_minute())
        self.assertEqual(parse_o_time.get_second(), o_time.get_second())
        self.assertTrue(o_time.__eq__(parse_o_time))
        self.assertEqual(o_time.to_str("%H:%M:%S"), "12:25:55")
        self.assertEqual(o_time.time_to_str(), "12:25:55")

    def test_o_time_from_date(self):
        import datetime
        o_time = OTime(date=datetime.datetime(year=1970, month=1, day=1, hour=12, minute=25, second=55))
        self.assertEqual(o_time.get_hour(), 12)
        self.assertEqual(o_time.get_minute(), 25)
        self.assertEqual(o_time.get_second(), 55)
        self.assertEqual(o_time.get_millis(), 0)
        parse_o_time = OTime.parse(time_str="12:25:55")
        self.assertEqual(parse_o_time.get_hour(), o_time.get_hour())
        self.assertEqual(parse_o_time.get_minute(), o_time.get_minute())
        self.assertEqual(parse_o_time.get_second(), o_time.get_second())
        self.assertTrue(o_time.__eq__(parse_o_time))
        self.assertEqual(o_time.to_str("%H:%M:%S"), "12:25:55")
        self.assertEqual(o_time.time_to_str(), "12:25:55")


if __name__ == '__main__':
    suite = unittest.TestLoader().loadTestsFromTestCase(OTimeTest)
    unittest.TextTestRunner(verbosity=2).run(suite)
