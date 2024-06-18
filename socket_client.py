import socket

def main():
    # Creating a socket object
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    
    # Defining the host and port
    host = '127.0.0.1'
    port = 12345
    
    try:
        # Connecting to the server
        client_socket.connect((host, port))
        
        # Sending a message to the server
        message = '2 binario3.bin \n'  # Ensure to add a newline character if needed
        client_socket.sendall(message.encode())
        
        # Receiving the response from the server
        response_bytes = client_socket.recv(1024)
        response_str = response_bytes.decode()
        print("Server response:", response_str)

    except Exception as e:
        print(f"Error: {e}")
    finally:
        # Closing the connection with the server
        client_socket.close()

if __name__ == "__main__":
    main()
