
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