import os
import json
import urllib2
import time
import random

print('hi')

sample = {
    "start_time": "1500448779799",
    "end_time": "1500448783801",
    "sensors":[
        {
        "sensor_name": "angle_sensor_1",
        "sensor_type": "angle_sensor",
        "channels":
            [
                {
                    "name": "axis_X",
                    "min_value": "-176.760",
                    "max_value": "163.440",
                    "average_value": "10.728"
                },
                {
                    "name": "axis_Y",
                    "min_value": "-164.880",
                    "max_value": "146.880",
                    "average_value": "-8.676"
                },
                {
                    "name": "axis_Z",
                    "min_value": "-167.040",
                    "max_value": "176.760",
                    "average_value": "16.614"
                }
            ]
        }
    ]
}

sensor1 = {"sensor_name": "angle_sensor_1",
           "sensor_type": "angle_sensor",
           "channels":
               [
                   {
                       "name": "axis_X",
                       "min_value": 100,
                       "max_value": 106,
                       "ex_prob":0.05
                   },
                   {
                       "name": "axis_Y",
                       "min_value": 50,
                       "max_value": 55,
                       "ex_prob":0.05
                   },
                   {
                       "name": "axis_Z",
                       "min_value": 30,
                       "max_value": 40,
                       "ex_prob":0.05
                   }
               ]
           }

sensor2 = {"sensor_name": "angle_sensor_2",
           "sensor_type": "angle_sensor",
           "channels":
               [
                   {
                       "name": "axis_X",
                       "min_value": 200,
                       "max_value": 210,
                       "ex_prob":0.05
                   },
                   {
                       "name": "axis_Y",
                       "min_value": 150,
                       "max_value": 155,
                       "ex_prob":0.05
                   },
                   {
                       "name": "axis_Z",
                       "min_value": 130,
                       "max_value": 136,
                       "ex_prob":0.05
                   }
               ]
           }

sensor3 = {"sensor_name": "door_sensor_1",
           "sensor_type": "door_sensor",
           "channels":
               [
                   {
                       "name": "door_1",
                       "min_value": 0,
                       "max_value": 1,
                       "rnd_values":[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1]
                   }
               ]
           }

sensors = (sensor1, sensor2, sensor3)

prev_time = time.time()

def generate_sample():
    global prev_time
    sign = lambda x: (1, -1)[x < 0]
    ret = {
        "start_time":prev_time,
        "end_time":time.time() * 1000
    }
    prev_time = ret['end_time']
    sensors_data = []
    for s in sensors:
        sd = {}
        sd['sensor_name'] = s['sensor_name']
        sd['sensor_type'] = s['sensor_type']
        channels_data = []
        for ch in s['channels']:
            chd = {'name': ch['name']}
            if ch.get('rnd_values', None):
                min = ch['rnd_values'][0]
                max = ch['rnd_values'][random.randrange(0, len(ch['rnd_values']))]
                avg = (min + max) / 2
            else:
                ex_prob = ch.get('ex_prob', 0.1)
                min_cap = ch['min_value']
                max_cap = ch['max_value']
                min_cap = min_cap * (1+ex_prob*sign(min_cap))
                max_cap = max_cap * (1-ex_prob*sign(max_cap))
                avg = random.uniform(min_cap, max_cap)
                min = random.uniform(min_cap, avg)
                max = random.uniform(avg, max_cap)
            chd['average_value'] = avg
            chd['min_value'] = min
            chd['max_value'] = max
            channels_data.append(chd)
        sd['channels'] = channels_data
        sensors_data.append(sd)
    ret['sensors'] = sensors_data
    return ret









#
#print(response)
for i in range(0, 5):
    j_str = json.dumps(generate_sample())
    print(j_str)
    print('\n\n')
#    req = urllib2.Request('http://192.168.5.112:8765/sensors/stats')
    req = urllib2.Request('http://localhost:8765/sensors/stats')
    req.add_header('Content-Type', 'application/json')
    response = urllib2.urlopen(req, j_str)
    time.sleep(1)