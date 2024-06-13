import socket

# ARQUIVO PYTHON SOCKET DE CLIENTE PARA TESTES!!!

def main():
    # Criando um objeto socket
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    
    # Definindo o host e a porta
    host = '127.0.0.1'
    port = 12346
    
    # Conectando-se ao servidor
    client_socket.connect((host, port))
    
    # Enviando uma mensagem para o servidor
    message = '3 binario8.bin 2\n2 idade 27 nacionalidade "GERMANY"\n1 nomeClube "JUVENTUS"'
    #message = '1 /home/victor/Downloads/arquivos/dado1.csv binarioteste.bin'
    client_socket.send(message.encode())
    
    # Recebendo a resposta do servidor
    response = client_socket.recv(1024).decode()
    print("Resposta do servidor:", response)
    
    # Fechando a conexão com o servidor
    client_socket.close()

if __name__ == "__main__":
    main()