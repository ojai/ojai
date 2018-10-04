from __future__ import division
from builtins import object
from past.utils import old_div
import datetime

import dateutil.parser

from ojai.error.UnsupportedConstructorException import UnsupportedConstructorException


class OTimestamp(object):
    __epoch = datetime.datetime.utcfromtimestamp(0)
    # TODO Is UTC_CHRONOLOGY must return current time in UTC time zone? and must be created only once with instance?
    # __UTC_CHRONOLOGY = datetime.datetime.utcfromtimestamp(0)
    __UTC_CHRONOLOGY = datetime.datetime(1970, 1, 1, 0, 0, 0, 000)

    """ Two types of OTimestamp init:
        First params set:
            year - the year
            month_of_year - the month of the year, from 1 to 12
            day_of_month - the day of the month, from 1 to 31
            hour_of_day - the hour of the day, from 0 to 23
            minute_of_hour - the minute of the hour, from 0 to 59
            second_of_minute - the second of the minute, from 0 to 59
            millis_of_second - the millisecond of the second, from 0 to 999
        Second:
            date - the Date to extract fields from"""

    def __init__(self, year=None, month_of_year=None, day_of_month=None, hour_of_day=None, minute_of_hour=None,
                 second_of_minute=None, millis_of_second=None, date=None, millis_since_epoch=None):
        # if all([year, month_of_year, day_of_month, hour_of_day,
        #         minute_of_hour, second_of_minute, millis_of_second]) is not None:
        if (year is not None and month_of_year is not None and day_of_month is not None and hour_of_day is not None
                and minute_of_hour is not None and second_of_minute is not None and millis_of_second is not None):
            self.__date_time = datetime.datetime(year=year, month=month_of_year, day=day_of_month,
                                                 hour=hour_of_day, minute=minute_of_hour, second=second_of_minute,
                                                 microsecond=millis_of_second * 1000)
            self.__millis_since_epoch = (self.__date_time - self.__epoch).total_seconds() * 1000.0
        elif millis_since_epoch is not None:
            self.__date_time = None
            self.__millis_since_epoch = millis_since_epoch
        elif date is not None:
            if not isinstance(type(date), type(datetime.datetime)):
                raise TypeError
            self.__date_time = date
            self.__millis_since_epoch = (self.__date_time.replace(tzinfo=None) - self.__epoch).total_seconds() * 1000.0
        else:
            raise UnsupportedConstructorException

    @property
    def millis_since_epoch(self):
        return self.__millis_since_epoch

    @property
    def date_time(self):
        return self.__date_time

    def get_year(self):
        return (self.__UTC_CHRONOLOGY + datetime.timedelta(milliseconds=self.millis_since_epoch)).year

    def get_month(self):
        return (self.__UTC_CHRONOLOGY + datetime.timedelta(milliseconds=self.millis_since_epoch)).month

    def get_day_of_month(self):
        return (self.__UTC_CHRONOLOGY + datetime.timedelta(milliseconds=self.millis_since_epoch)).day

    def get_hour(self):
        return (self.__UTC_CHRONOLOGY + datetime.timedelta(milliseconds=self.millis_since_epoch)).hour

    def get_minute(self):
        return (self.__UTC_CHRONOLOGY + datetime.timedelta(milliseconds=self.millis_since_epoch)).minute

    def get_second(self):
        return (self.__UTC_CHRONOLOGY + datetime.timedelta(milliseconds=self.millis_since_epoch)).second

    def get_millis(self):
        return old_div((self.__UTC_CHRONOLOGY + datetime.timedelta(milliseconds=self.millis_since_epoch)).microsecond, 1000)

    def __get_date_time(self):
        if self.__date_time is None:
            self.__date_time = self.__UTC_CHRONOLOGY + datetime.timedelta(milliseconds=self.millis_since_epoch)
            # from dateutil.relativedelta import relativedelta
            # self.__date_time = self.__UTC_CHRONOLOGY + relativedelta(microsecond=self.millis_since_epoch)
        return self.date_time

    # Create datetime object from millis since epoch
    def to_date(self):
        return datetime.datetime.fromtimestamp(old_div(self.millis_since_epoch, 1000.0))

    # Returns the ISO8601 format timestamp string in UTC.
    def to_utc_str_now(self):
        return self.__get_date_time().utcnow().isoformat()

    # Returns the ISO8601 format timestamp string in local time zone.
    def to_local_str_now(self):
        return self.__get_date_time().now().isoformat()

    def to_str(self, pattern):
        return self.__get_date_time().strftime(pattern)

    @staticmethod
    def parse(date_time_str):
        return OTimestamp(date=dateutil.parser.parse(date_time_str))

    def __str__(self):
        # return self.to_str('%Y-%m-%d %H:%M:%S')
        return self.to_str('%Y-%m-%dT%H:%M:%S.%fZ')

    def __cmp__(self, other):
        if type(other) is not self or type(other) is not type(datetime.datetime):
            raise TypeError
        return self.millis_since_epoch - other.millis_since_epoch

    def __hash__(self):
        return int(self.millis_since_epoch ^ (self.millis_since_epoch >> 32))

    def __eq__(self, other):
        if self is other:
            return True
        if other is None:
            return False
        if not isinstance(self, type(other)):
            return False
        if self.millis_since_epoch != other.millis_since_epoch:
            return False
        return True
