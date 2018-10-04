from __future__ import division
from builtins import object
from past.utils import old_div
import datetime
import dateutil.parser

from ojai.error.UnsupportedConstructorException import UnsupportedConstructorException


class OTime(object):

    __EPOCH_DATE = datetime.datetime(1970, 1, 1)

    def __init__(self, timestamp=None, hour_of_day=None, minutes=None,
                 seconds=None, ms=None, date=None, millis_of_day=None):
        if timestamp is not None:
            self.__time = datetime.datetime.fromtimestamp(timestamp).time()
            self.__millis_of_day = (self.__time.hour * 60 * 60 + self.__time.second)\
                                   * 1000 + old_div(self.__time.microsecond, 1000.0)
        # elif all([hour_of_day, minutes, seconds]) is not None:
        elif hour_of_day is not None and minutes is not None and seconds is not None:
            if ms is None:
                self.__time = datetime.time(hour=hour_of_day, minute=minutes, second=seconds, microsecond=0)
            else:
                self.__time = datetime.time(hour=hour_of_day, minute=minutes, second=seconds, microsecond=ms)
            self.__millis_of_day = (self.__time.hour * 60 * 60 + self.__time.second)\
                                   * 1000 + old_div(self.__time.microsecond, 1000.0)
        elif date is not None:
            if not isinstance(type(date), type(datetime.datetime)):
                raise TypeError
            self.__time = date.time()
            self.__millis_of_day = (self.__time.hour * 60 * 60 + self.__time.second) \
                                   * 1000 + old_div(self.__time.microsecond, 1000.0)
        elif millis_of_day is not None:
            self.__time = None
            self.__millis_of_day = millis_of_day
        else:
            raise UnsupportedConstructorException

    @property
    def epoch_date(self):
        return self.__EPOCH_DATE

    @property
    def time(self):
        return self.__time

    @property
    def millis_of_day(self):
        return self.__millis_of_day

    @staticmethod
    def parse(time_str):
        return OTime(date=dateutil.parser.parse("1970-01-01 " + time_str))

    @staticmethod
    def from_millis_of_day(millis_of_day):
        return OTime(millis_of_day=millis_of_day)

    def __get_time(self):
        if self.time is None:
            self.__time = datetime.datetime.fromtimestamp(self.millis_of_day).time()
        return self.__time

    def get_hour(self):
        return self.__get_time().hour

    def get_minute(self):
        return self.__get_time().minute

    def get_second(self):
        return self.__get_time().second

    def get_millis(self):
        return old_div(self.__get_time().microsecond, 1000)

    def to_date(self):
        return datetime.datetime.combine(self.__EPOCH_DATE.date(), self.__time)

    def to_time_in_millis(self):
        return self.__millis_of_day

    def time_to_str(self):
        if self.millis_of_day % 1000 == 0:
            return self.to_str("%H:%M:%S")
        else:
            return self.to_str("%H:%M:%S:%f")

    def to_str(self, pattern):
        return self.__get_time().strftime(pattern)

    def __str__(self):
        return self.time_to_str()

    def __hash__(self):
        return int(self.millis_of_day)

    def __cmp__(self, other):
        if type(other) is not self:
            raise TypeError
        return self.millis_of_day - other.millis_of_day

    def __eq__(self, other):
        if self is other:
            return True
        if other is None:
            return False
        if not isinstance(self, type(other)):
            return False
        if self.millis_of_day != other.millis_of_day:
            return False
        return True
