from builtins import object
import datetime
import dateutil.parser

from ojai.error.UnsupportedConstructorException import UnsupportedConstructorException


class ODate(object):

    __EPOCH_DATE = datetime.datetime(1970, 1, 1)
    __START_OF_DAY = datetime.time(hour=0, minute=0, second=0)

    def __init__(self, year=None, month=None, day_of_month=None, epoch=None, date=None, days_since_epoch=None):
        if all([year, month, day_of_month]) and epoch is None and date is None:
            self.__date = datetime.datetime(year, month, day_of_month)
            self.__days_since_epoch = (self.__date - self.__EPOCH_DATE).days
        elif date is not None:
            if isinstance(date, ODate):
                self.__date = datetime.datetime(date.get_year(), date.get_month(), date.get_day_of_month())
                self.__days_since_epoch = (self.__date - self.__EPOCH_DATE).days
            else:
                if type(date) is not datetime.datetime:
                    raise TypeError("date should be datetime.date type or comparable to it")
                self.__date = date
                self.__days_since_epoch = (self.__date - self.__EPOCH_DATE).days
        elif epoch is not None:
            self.__date = datetime.datetime.fromtimestamp(epoch)
            self.__days_since_epoch = (self.__date - self.__EPOCH_DATE).days
        elif days_since_epoch is not None:
            self.__date = None
            self.__days_since_epoch = days_since_epoch
        else:
            raise UnsupportedConstructorException("Bad params set")

    @property
    def epoch_date(self):
        return self.__EPOCH_DATE

    @property
    def start_of_day(self):
        return self.__START_OF_DAY

    @property
    def days_since_epoch(self):
        return self.__days_since_epoch

    @staticmethod
    def from_days_since_epoch(days_since_epoch):
        return ODate(days_since_epoch=days_since_epoch)

    @staticmethod
    def parse(date_str):
        return ODate(date=dateutil.parser.parse(date_str))

    def __get_date(self):
        if self.__date is None:
            # self.__date = self.__EPOCH_DATE + datetime.datetime.fromtimestamp(self.__days_since_epoch)
            self.__date = self.__EPOCH_DATE + datetime.timedelta(self.__days_since_epoch)
        return self.__date

    # Return the years of this datetime
    def get_year(self):
        return self.__get_date().year

    # Return the month of this datetime
    def get_month(self):
        return self.__get_date().month

    # Return the day of month of this datetime
    def get_day_of_month(self):
        return self.__get_date().day

    def to_days_since_epoch(self):
        return self.__days_since_epoch

    def to_date_str(self):
        return self.to_string("%Y-%m-%d")

    def to_string(self, pattern):
        return self.__get_date().strftime(pattern)

    # return date with time set to 0
    def to_date(self):
        return datetime.datetime.combine(self.__get_date().date(), self.start_of_day)

    def __str__(self):
        return self.to_date_str()

    def __cmp__(self, other):
        if not isinstance(self, type(other)):
            raise TypeError
        return self.days_since_epoch - other.days_since_epoch

    def __hash__(self):
        return self.__days_since_epoch

    def __eq__(self, other):
        if self is other:
            return True
        if other is None:
            return False
        if not isinstance(self, type(other)):
            return False
        if self.days_since_epoch != other.days_since_epoch:
            return False
        return True
