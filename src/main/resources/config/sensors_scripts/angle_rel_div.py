import sys
import sensors_base
from datetime import datetime
from sensors_base import *
from rgups.railvideo.model.alarms import UiAlarm

TYPE = 'AT_ANGLE_DEV'

sensors_base.host = host
sensors_base.data = data
sensors_base.TYPE = TYPE

diff = stats.getDeltaFor(data.sensorName, data.channel)

if not diff:
    raise RuntimeError, "No statistics found for sensor: %s, channel: %s " % (data.sensorName, data.channel)

diff_val = diff.key

if (diff_val < conf['warn_threshold']):
    quit()

diff_min_date = diff.value.key
diff_max_date = diff.value.value

max_point = stats.getMaxPair(data.sensorName, data.channel)
min_point = stats.getMinPair(data.sensorName, data.channel)

if (diff_val >= conf['warn_threshold']):
    create_func = create_warning
if (diff_val >= conf['error_threshold']):
    create_func = create_error

time_fmt = '%Y-%m-%d %H:%M:%S'

alarm = create_func("Изменение угла канала %s" % data.channel,
        descr="Изменение угла канала %s датчика %s за время %sсек. на величину %s" % (
            data.channel, data.sensorName, abs((diff_max_date - diff_min_date)/1000), diff_val
        ), details="Минимальное значение %s зарегистрировано в %s. Максимальное значение %s, зарегистрировано в %s. " % (
            min_point.key, datetime.fromtimestamp(min_point.value/1000).strftime(time_fmt),
            max_point.key, datetime.fromtimestamp(max_point.value/1000).strftime(time_fmt)
    ))

alarm.value = diff_val

host.publishAlarm(alarm)
