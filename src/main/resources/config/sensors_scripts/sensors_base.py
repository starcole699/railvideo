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


def post_warning(ctx, header, descr='', details=''):
    new_warning = ctx['host'].newWarning(header.decode('utf-8'));
    if descr:
        new_warning.descr = descr.decode('utf-8')

    if details:
        new_warning.details = details.decode('utf-8')

    ctx['host'].publishAlarm(new_warning)

def create_warning(ctx, header, descr='', details=''):
    return create_message(ctx, header, DbAlarm.AL_WARNING, descr=descr, details=details)


def create_error(ctx, header, descr='', details=''):
    return create_message(ctx, header, DbAlarm.AL_ERROR, descr=descr, details=details)


def create_message(ctx, header, level, descr='', details=''):
    global slog_g
    #global host
    #global s_name_g
    #global s_type_g
    #global s_chan_g
    #global TYPE
    new_warning = ctx['host'].createAlarm(level, ctx['TYPE'], header.decode('utf-8'))
    if descr:
        new_warning.descr = descr.decode('utf-8')

    if details:
        new_warning.details = details.decode('utf-8')

    new_warning.setUidFrom("SN=" + ctx['data'].sensorName, "CH=" + ctx['data'].channel)
    return new_warning

def send_alarm(alarm, host):
    host.publishAlarm(alarm)