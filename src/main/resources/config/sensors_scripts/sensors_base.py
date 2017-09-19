from rgups.railvideo.model.alarms import DbAlarm as DbAlarm

base_val = 10

"""
data is FlatSensorData:
     Long startTime;
     Long endTime;
     String sensorName;
     String sensorType;
     String channel;
     Double minValue;
     Double maxValue;
     Double averageValue;
"""


def post_warning(header, descr='', details=''):
    global host
    new_warning = host.newWarning(header.decode('utf-8'));
    if descr:
        new_warning.descr = descr.decode('utf-8')

    if details:
        new_warning.details = details.decode('utf-8')

    host.publishAlarm(new_warning)

def create_warning(header, descr='', details=''):
    return create_message(header, DbAlarm.AL_WARNING, descr=descr, details=details)


def create_error(header, descr='', details=''):
    return create_message(header, DbAlarm.AL_ERROR, descr=descr, details=details)


def create_message(header, level, descr='', details=''):
    global host
    global data
    global TYPE
    new_warning = host.createAlarm(level, TYPE, header.decode('utf-8'));
    if descr:
        new_warning.descr = descr.decode('utf-8')

    if details:
        new_warning.details = details.decode('utf-8')

    new_warning.setUidFrom("SN=" + data.sensorName, "CH=" + data.channel)
    return new_warning

def send_alarm(alarm):
    global host
    host.publishAlarm(alarm)