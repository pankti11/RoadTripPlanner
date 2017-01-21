import googlemaps
import requests
import json
import calendar
import time

radius = 5000
boolean = {True:'Yes',False:'No'}

def get_trip_route(Start,End):
    gmaps = googlemaps.Client(key='AIzaSyDOp92qApfbV_3_pzR1MC7PQKe7UosUpu4')
    directions_result = gmaps.directions(Start,
                                         End,
                                         mode="driving")
    return directions_result[0]

def get_places_of_interest(loc,rad,year,month,day,hour,minute):
    # gmaps = googlemaps.Client(key='AIzaSyDOp92qApfbV_3_pzR1MC7PQKe7UosUpu4')
    # interest = gmaps.places(loc, rad)
    tm = calendar.timegm(time.strptime('%s %d, %d @ %d:%d:00 UTC' %(month,day,year,hour,minute), '%b %d, %Y @ %H:%M:%S UTC'))
    interest = requests.get("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%f,%f&radius=%d&departure_time=%d&key=AIzaSyDOp92qApfbV_3_pzR1MC7PQKe7UosUpu4" %(loc['lat'],loc['lng'],rad,tm))
    a = interest.text
    j = json.loads(a)
    for i in j['results']:
        print '****',i['name'],'******'
        print 'Location: ',i['geometry']['location']
        if 'opening_hours' in i:
            print 'Open Now: ',boolean[i['opening_hours']['open_now']]
        if 'rating' in i:
            print 'Rating: ',i['rating']

def trip_details(result_dict):
    leg = result_dict['legs']
    route_dict = leg[0]
    steps = route_dict['steps']
    count = 0
    for step in steps:
        dist = step['distance']
        dist_value = dist['value']
        # print dist_value
        if dist_value <= radius:
            # get_places_of_interest(step['start_location'], dist_value/2)
            get_places_of_interest(step['end_location'], dist_value/2,2017,'Jan',21,8,35)
            # print dist_value
            # print step['start_location']
            # print step['end_location']
        else :
            start = step['start_location']
            end = step['end_location']
            div_factor = int(dist_value / radius) + 1
            long_diff= end['lng']-start['lng']
            lat_diff= end['lat']-start['lat']
            rad = dist_value/div_factor
            for i in xrange(1,div_factor):
                # print start['lng'] + i*long_diff/div_factor 
                # print start['lat'] + i*lat_diff/div_factor
                if i == div_factor-1:
                    get_places_of_interest(end, rad,2017,'Jan',21,8,35)
                else:
                    lng = start['lng'] + i*long_diff/div_factor
                    lat = start['lat'] + i*lat_diff/div_factor
                    loc = {u'lat':lat,u'lng':lng}
                    get_places_of_interest(step['end_location'], rad,2017,'Jan',21,8,35)

def show_list_of_hotels(step):
    loc = step['start_location']
    hotels = requests.get("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%f,%f&radius=10000&type=lodging&key=AIzaSyDOp92qApfbV_3_pzR1MC7PQKe7UosUpu4" %(loc['lat'],loc['lng']))
    a = hotels.text
    j = json.loads(a)
    for i in j['results']:
        print '****',i['name'],'******'
        print 'Location: ',i['geometry']['location']
        if 'opening_hours' in i:
            print 'Open Now: ',boolean[i['opening_hours']['open_now']]
        if 'rating' in i:
            print 'Rating: ',i['rating']
    #min and max price?


if __name__ == "__main__":
    route = get_trip_route("Toronto","Montreal")
    print 'Distance: ',route['legs'][0]['distance']['text'],'/',int(int(route['legs'][0]['distance']['text'].split()[0])*0.621371),' miles'
    print 'Duration: ',route['legs'][0]['duration']['text']
    hrs = route['legs'][0]['duration']['text'].split()[0].strip(' ')
    mid = int(route['legs'][0]['distance']['text'].split()[0])/2
    if int(hrs) >= 2:
        dist = 0
        for i in route['legs'][0]['steps']:
            dist += float(i['distance']['text'].split()[0])
            if dist >= mid:
                break
        show_list_of_hotels(i)
    trip_details(route)

