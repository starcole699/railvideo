import sys
import sensors_base
from datetime import datetime
from sensors_base import *
from rgups.railvideo.model.alarms import UiAlarm

sensors_types = {
    'security_sensor':('AT_SECURITY_VIOLATION', 'Открытие двери шкафа.'),
    'optical_sensor':('AT_LINE_DISCONNECT', 'Обрыв оптической линии.'),
}

alarm_value = min(data.minValue, data.maxValue)

if alarm_value > 0:
    quit()

ctx = {
    'data' : data,
    'TYPE' : sensors_types[data.sensorType][0],
    'host' : host
}

#sensors_base.host = host
#sensors_base.data = data
#sensors_base.TYPE = sensors_types[data.sensorType][0]
header = sensors_types[data.sensorType][1]

time_fmt = '%Y-%m-%d %H:%M:%S'

alarm = create_error(
    ctx,
    header,
    descr="Канал %s, датчик %s, время %s" % (
            data.channel, data.sensorName, datetime.fromtimestamp(data.endTime/1000).strftime(time_fmt)
            ),
    details="Значе датчиков: мин=%s, макс=%s, среднее=%s. Временной интервал: с %s по %s." % (
            data.minValue, data.maxValue, data.averageValue,
            datetime.fromtimestamp(data.startTime/1000).strftime(time_fmt),
            datetime.fromtimestamp(data.endTime/1000).strftime(time_fmt)
            )
    )

alarm.value = alarm_value

host.publishAlarm(alarm)
