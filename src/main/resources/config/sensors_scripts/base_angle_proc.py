import sensors_base
from sensors_base import *
from rgups.railvideo.model.alarms import UiAlarm

sensors_base.host = host
print('Hello from base angle script!')
for k,v in conf.iteritems():
    print('%s -- %s' % (k,v))

diff = stats.getDeltaFor(data.sensorName, data.channel)
slog.info("Data diff %s" % diff)

#post_warning("Превышение допустимого угла отклонения канала %s сенсора %s " % (data.channel, data.sensorName))




