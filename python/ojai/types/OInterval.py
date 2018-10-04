from __future__ import division
from builtins import object
from past.utils import old_div
from ojai.error.UnsupportedConstructorException import UnsupportedConstructorException
from ojai.types import constants


class OInterval(object):
    """An immutable class which encapsulates a time interval."""

    __APPROX_DAYS_IN_YEAR = old_div(((365 * 4) + 1),4.0)

    __APPROX_DAYS_IN_MONTH = old_div(__APPROX_DAYS_IN_YEAR, 12)

    def __init__(self, milli_seconds=None, years=None, months=None, days=None,
                 seconds=None, iso8601DurationPattern=None):
        # if all([milli_seconds, years, months, days, seconds]):
        if years is not None and months is not None and days is not None and seconds is not None and milli_seconds is not None:
            self.__milli_seconds = milli_seconds
            self.__seconds = seconds
            self.__days = days
            self.__months = months
            self.__years = years
            # total_days = long(((years * self.__APPROX_DAYS_IN_YEAR) + (months * self.__APPROX_DAYS_IN_MONTH) + days))
            total_days = ((years * self.__APPROX_DAYS_IN_YEAR) + (months * self.__APPROX_DAYS_IN_MONTH) + days)
            # self.__time_duration = constants.MILLISECONDS_PER_DAY * total_days + seconds * 1000 + milli_seconds
            self.__time_duration = constants.MILLISECONDS_PER_DAY * total_days + seconds * 1000 + milli_seconds
        elif milli_seconds is not None and years is None and months is None and days is None and seconds is None:
            self.__time_duration = milli_seconds
            self.__milli_seconds = int(milli_seconds % 1000)
            self.__seconds = int(old_div((milli_seconds % constants.MILLISECONDS_PER_DAY), 1000))
            self.__days = int(old_div(milli_seconds, constants.MILLISECONDS_PER_DAY))
            self.__months = 0
            self.__years = 0
        elif iso8601DurationPattern is not None:
            # FIXME: parse the string as per ISO 8601 duration and time stamps format
            self.__init__(0, 0, 0, 0, 0)
        else:
            raise UnsupportedConstructorException("This params set is not supported for the OInterval init")

    @property
    def years(self):
        return self.__years

    @property
    def months(self):
        return self.__months

    @property
    def days(self):
        return self.__days

    @property
    def seconds(self):
        return self.__seconds

    @property
    def milli_seconds(self):
        return self.__milli_seconds

    @property
    def time_duration(self):
        return self.__time_duration

    def __hash__(self):
        __result = 31 * 1 * int(self.time_duration ^ (self.time_duration >> 32))
        return __result

    def __eq__(self, other):
        if self is other:
            return True
        if other is None:
            return False
        if not isinstance(self, type(other)):
            return False
        if self.time_duration != other.time_duration:
            return False
        return True
