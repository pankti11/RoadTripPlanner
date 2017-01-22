import socket
from location-coordinate import main()

HOST, PORT = '', 3000

listen_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
listen_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
listen_socket.bind((HOST, PORT))
listen_socket.listen(1)
print ('Serving HTTP on port %s ...' % PORT)
while True:
    client_connection, client_address = listen_socket.accept()
    request = client_connection.recv(1024)
    index = request.find("{")
    location = request[index:]
    a = main(location)
    # print (request)


    http_response = a
    client_connection.sendall(http_response)
    client_connection.close()


