# coding: utf-8
import googlemaps
import requests
import json
import calendar
import time
import xml.etree.ElementTree as ET
names = []
total_places = 0


boolean = {True:'Yes',False:'No'}

def get_trip_route(Start,End):
    processd = "https://maps.googleapis.com/maps/api/directions/json?origin=" + Start + "&destination=" + End + "&key=AIzaSyCCYpsbmrXpqhZix7y6FOcx2t_ITjj-GW0"
    json_data = requests.get(processd)
    directions_result = json_data.text
    j = json.loads(directions_result)
    return j["routes"][0]

def get_places_of_interest(loc,rad,year,month,day,hour,minute,count_inst):
    # gmaps = googlemaps.Client(key='AIzaSyDOp92qApfbV_3_pzR1MC7PQKe7UosUpu4')
    # interest = gmaps.places(loc, rad)
    global names
    tm = calendar.timegm(time.strptime('%s %d, %d @ %d:%d:00 UTC' %(month,day,year,hour,minute), '%b %d, %Y @ %H:%M:%S UTC'))
    # link = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + str(loc['lat']) + "," + str(loc['lng']) + "&key=AIzaSyAJ22GaiU62PwA8LL2G7PMxhQH3HN0VkMk"
    # print link
    # location_data = requests.get(link)
    # location_data_json = json.loads(location_data.text)
    # results = location_data_json['results'][0]

    #
    processd = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + str(loc['lat']) + "," + str(loc['lng']) + "&type=point_of_feature" + "&radius=10000&rankby=prominence&key=AIzaSyC8pwTKL113tv5z334Tah6mlWPY-JLVq4g"
    location_data = requests.get(processd)
    a = location_data.text
    j = json.loads(a)
    list_temp = []
    count = 0
    global total_places  
    #print j['results']
    for i in j['results']:
        if i['name'] not in names:
            names.append(i['name'])
            if 'rating' in i and float(i['rating']) > 4.0 :
                if 'photos' in i:

                    #if 'opening_hours' in i:
                    #    print 'Open Now: ',boolean[i['opening_hours']['open_now']]

                    #print '****',i['name'],'******'
                    #print 'Location: ',i['geometry']['location']
                    #print 'Rating: ',i['rating']
                    data = {}
                    total_places = total_places + 1
                    count = count + 1
                    data['name'] = i['name']
                    data['longitude'] = i['geometry']['location']['lng']
                    data['lattitude'] = i['geometry']['location']['lat']
                    data['rating'] = i['rating']
                    image_ref = i['photos'][0]['photo_reference']
                    image_url = "https://maps.googleapis.com/maps/api/place/photo?photoreference="+ image_ref +"&sensor=false&maxheight=100&maxwidth=200&key=AIzaSyC8pwTKL113tv5z334Tah6mlWPY-JLVq4g"
                    data['image-url'] = image_url

                    if data not in list_temp:
                        list_temp.append(data)
                    else:
                        "Already Present"

            if count == 5:
                break
    return list_temp


    # for lst_loc in results['address_components']:
    #     if "administrative_area_level_2" in lst_loc['types']: 
    #         query_term = lst_loc['short_name']
    #         query_term = query_term.replace(" ","+")
    #         link2 = "https://maps.googleapis.com/maps/api/place/textsearch/xml?query=point+of+interest+" + query_term + "&rankby=prominence&key=AIzaSyCJJSZAOG0y1LTz0o5Omvtf8-iOw9t4tMQ"
    #         interst_xml = requests.get(link2)
    #         print link2
    #         text_interest = interst_xml.text.encode("utf8")
    #         xml_string = str(text_interest)

    #         file1 = open("xml-file" + str(count_inst) +".xml","a")
    #         file1.write(xml_string)
    #         root = ET.fromstring(xml_string)

    #         print root.tag
    #         count = 0

    #         for child in root.iter("result"):
    #             rate_tag = child.find("rating")
    #             if rate_tag != None:
    #                 rate = rate_tag.text
    #                 name = child.find("name").text
    #                 print name
    #                 print rate
    #                 count = count + 1
    #             if count == 5:
    #                 break



def trip_details(result_dict):
    leg = result_dict['legs']
    route_dict = leg[0]
    steps = route_dict['steps']
    count = 0
    radius = 50000
    count_inst = 0
    selected_route = {}
    for step in steps:
        dist = step['distance']
        dist_value = dist['value']
        #print dist_value
        count_inst = count_inst + 1
        

        if dist_value <= radius:
            # get_places_of_interest(step['start_location'], dist_value/2)
            location_tuple = str(step['end_location']['lng']) + "," + str(step['end_location']['lat'])
            selected_route[location_tuple] = get_places_of_interest(step['end_location'], radius,2017,'Jan',21,8,35,count_inst)


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
                #print start['lng'] + i*long_diff/div_factor 
                # print start['lat'] + i*lat_diff/div_factor
                if i == div_factor-1:
                    #print end
                    location_tuple = str(end['lng']) + "," + str(end['lat'])
                    selected_route[location_tuple] = get_places_of_interest(end, radius,2017,'Jan',21,8,35,count_inst)
                else:
                    lng = start['lng'] + i*long_diff/div_factor
                    lat = start['lat'] + i*lat_diff/div_factor
                    #print start['lng'] + i*long_diff/div_factor
                    #print start['lat'] + i*lat_diff/div_factor
                    loc = {u'lat':lat,u'lng':lng}
                    data = {}
                    data['lng'] = float(lng)
                    data['lat'] = float(lat)
                    location_tuple = str(lng) + "," + str(lat)
                    selected_route[location_tuple] = get_places_of_interest(data, radius,2017,'Jan',21,8,35,count_inst)

    return selected_route

# def show_list_of_hotels(step):
#     loc = step['start_location']
#     hotels = requests.get("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%f,%f&radius=10000&type=lodging&key=AIzaSyCiCEgUs7_24JUyszrUtuK3on0doPDe6qQ" %(loc['lat'],loc['lng']))
#     a = hotels.text
#     j = json.loads(a)
#     for i in j['results']:
#         print '****',i['name'],'******'
#         print 'Location: ',i['geometry']['location']
#         if 'opening_hours' in i:
#             print 'Open Now: ',boolean[i['opening_hours']['open_now']]
#         if 'rating' in i:
#             print 'Rating: ',i['rating']
    #min and max price?

def main(loc):

    loc_json = json.loads(loc)
    #print loc_json
    start = str(loc_json['slat']) + ", " + str(loc_json['slng'])
    end = str(loc_json['lat']) + ", " + str(loc_json['lng'])
    #print type(start)
    # start = "42.3597607, -71.0597812"
    # end = "40.781979, -73.971714"
    # print type(start)

    route = get_trip_route(start,end)
    #print 'Distance: ',route['legs'][0]['distance']['text']
    #print 'Duration: ',route['legs'][0]['duration']['text']
    # hrs = route['legs'][0]['duration']['text'].split()[0].strip(' ')
    # mid = int(route['legs'][0]['distance']['text'].split()[0])/2
    
    # if int(hrs) >= 2:
    #     dist = 0
    #     for i in route['legs'][0]['steps']:
    #         dist += float(i['distance']['text'].split()[0])
    #         if dist >= mid:
    #             break
        #show_list_of_hotels(i)

    # print "CURRENT LOCATION:" + str(route['legs'][0]['end_location']['lat']) + str(route['legs'][0]['end_location']['lng'])
    #print 
    print trip_details(route)
    return trip_details(route)
    #print total_places


if __name__ == "__main__":
    # main('{"lat":40.781979,"lon":-73.971714,"slat":42.3597607,"slng":-71.0597812}')

    main('{"lat":42.75874019999999,"lng":-84.3988806,"slat":42.7281816,"slng":-84.4818591}')
    
