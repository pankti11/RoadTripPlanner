import socket
from run import main

HOST, PORT = '', 3000

listen_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
listen_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
listen_socket.bind((HOST, PORT))
listen_socket.listen(1)
print ('Serving HTTP on port %s ...' % PORT)
while True:
    client_connection, client_address = listen_socket.accept()
    request = client_connection.recv(1024)
    #print (type(request))
    txt = str(request,'utf-8')
    print (txt)
    index = txt.find("{")
    location = txt[index:]
    a = main(location)
    # print (request)
    s=json.dumps(a)
    https_response=json.loads(s)
    client_connection.sendall(http_response)
    client_connection.close()


