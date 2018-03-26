from __future__ import unicode_literals

from ojai.o_types.ODate import ODate

try:
    import unittest2 as unittest
except ImportError:
    import unittest


class ODateTest(unittest.TestCase):

    def test_days_from_epoch(self):
        days = 99
        o_date = ODate(days_since_epoch=days)
        o_date_clone = ODate(days_since_epoch=days)
        self.assertTrue(o_date.__eq__(o_date_clone))
        self.assertEqual(o_date.__cmp__(o_date_clone), 0)
        self.assertEqual(o_date.__hash__(), o_date_clone.__hash__())
        self.assertEqual(o_date.to_date_str(), "1970-04-10")
        self.assertEqual(o_date.get_year(), 1970)
        self.assertEqual(o_date.get_month(), 4)
        self.assertEqual(o_date.get_day_of_month(), 10)
        self.assertEqual(o_date.to_date().time().hour, 0)
        self.assertEqual(o_date.to_date().time().minute, 0)
        self.assertEqual(o_date.to_date().time().second, 0)

    def test_o_date_parse(self):
        o_date = ODate.parse("1970-04-10")
        self.assertEqual(o_date.days_since_epoch, 99)
        self.assertEqual(o_date.get_year(), 1970)
        self.assertEqual(o_date.get_month(), 4)
        self.assertEqual(o_date.get_day_of_month(), 10)
        self.assertEqual(o_date.to_date().time().hour, 0)
        self.assertEqual(o_date.to_date().time().minute, 0)
        self.assertEqual(o_date.to_date().time().second, 0)

    def test_concrete_date(self):
        o_date = ODate(year=1970, month=4, day_of_month=10)
        self.assertEqual(o_date.days_since_epoch, 99)
        self.assertEqual(o_date.get_year(), 1970)
        self.assertEqual(o_date.get_month(), 4)
        self.assertEqual(o_date.get_day_of_month(), 10)
        self.assertEqual(o_date.to_date().time().hour, 0)
        self.assertEqual(o_date.to_date().time().minute, 0)
        self.assertEqual(o_date.to_date().time().second, 0)

    def test_o_date_epoch(self):
        epoch = 8546400
        o_date = ODate(epoch=epoch)
        days = 99
        o_date_clone = ODate(days_since_epoch=days)
        self.assertTrue(o_date.__eq__(o_date_clone))
        self.assertEqual(o_date.__cmp__(o_date_clone), 0)
        self.assertEqual(o_date.__hash__(), o_date_clone.__hash__())
        self.assertEqual(o_date.get_year(), o_date_clone.get_year())
        self.assertEqual(o_date.get_month(), o_date_clone.get_month())
        self.assertEqual(o_date.get_day_of_month(), o_date_clone.get_day_of_month())

    def test_o_date_datetime(self):
        import datetime
        date = datetime.datetime(year=1970, month=4, day=10, hour=5, minute=15, second=55)
        o_date = ODate(date=date)
        self.assertEqual(o_date.days_since_epoch, 99)
        self.assertEqual(o_date.get_year(), 1970)
        self.assertEqual(o_date.get_month(), 4)
        self.assertEqual(o_date.get_day_of_month(), 10)
        self.assertEqual(o_date.to_date().time().hour, 0)
        self.assertEqual(o_date.to_date().time().minute, 0)
        self.assertEqual(o_date.to_date().time().second, 0)


if __name__ == '__main__':
    suite = unittest.TestLoader().loadTestsFromTestCase(ODateTest)
    unittest.TextTestRunner(verbosity=2).run(suite)
