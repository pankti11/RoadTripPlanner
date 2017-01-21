import googlemaps
radius = 5000
gmaps = googlemaps.Client(key='AIzaSyDOp92qApfbV_3_pzR1MC7PQKe7UosUpu4')
directions_result = gmaps.directions("Toronto",
                                     "Montreal",
                                     mode="driving")
result_dict = directions_result[0]
leg = result_dict['legs']
route_dict = leg[0]
steps = route_dict['steps']
count = 0
for step in steps:
    dist = step['distance']
    dist_value = dist['value']
    print dist_value
    if dist_value <= radius:
        print dist_value
        print step['start_location']
    else :
        start = step['start_location']
        end = step['end_location']
        div_factor = int(dist_value / radius) + 1
        long_diff= end['lng']-start['lng']
        lat_diff= end['lat']-start['lat']
        for i in range(0,div_factor):
            print "Long"
            print start['lng'] + i*long_diff/div_factor
            print "Lat"
            print start['lat'] + i*lat_diff/div_factor