import socket
import ctypes
import os
import subprocess
import glob
import tempfile

# Diretório onde estão os arquivos .c e .h
src_dir = 'ArquivosC'

# Lista de todos os arquivos .c e .h
c_files = glob.glob(os.path.join(src_dir, '*.c'))
h_files = glob.glob(os.path.join(src_dir, '*.h'))

# Verifica se a lista de arquivos não está vazia
if not c_files:
    raise Exception("Nenhum arquivo .c encontrado no diretório especificado")
if not h_files:
    raise Exception("Nenhum arquivo .h encontrado no diretório especificado")

# Nome do arquivo de saída
output_file = 'funcoesC'

if os.name == 'posix':  # Se for Linux
    funcoesC = ctypes.CDLL(f'./{output_file}.so')
elif os.name == 'nt':   # Se for Windows
    funcoesC = ctypes.CDLL(f'./{output_file}.dll')
else:
    raise Exception("Sistema operacional não suportado")

def main():
    # Criando um objeto socket
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    
    # Definindo o host e a porta
    host = '127.0.0.1'
    port = 12346
    
    # Ligando o socket ao host e porta
    server_socket.bind((host, port))
    
    # Definindo o número máximo de conexões em fila
    server_socket.listen(5)
    
    print("Servidor socket iniciado. Aguardando conexões...")
    
    while True:
        # Aceitando conexões
        client_socket, addr = server_socket.accept()
        print(f"Conexão estabelecida com {addr}")
        
        # Recebendo dados do cliente
        data = client_socket.recv(1024).decode()
        
        if not data:
            break
        
        partes_instrucao = data.split(" ")

        # Codigo da instrucao
        codigo = partes_instrucao[0]
        if (codigo == '1'):
            # funcionalidade 1: create_table

            # Lê strings de entrada: nomes dos arquivos csv e bin
            nome_arq_csv = partes_instrucao[1]
            nome_arq_bin = partes_instrucao[2]

            # Converter strings para bytes, necessário para passagem de parâmetro
            nome_arq_csv_bytes = ctypes.c_char_p(nome_arq_csv.encode('utf-8'))
            nome_arq_bin_bytes = ctypes.c_char_p(nome_arq_bin.encode('utf-8'))

            # Definindo função create_table da biblioteca funcoesC
            funcoesC.create_table.argtypes = [ctypes.c_char_p, ctypes.c_char_p]
            funcoesC.create_table.restype = None

            # Chamada da função
            funcoesC.create_table(nome_arq_csv_bytes, nome_arq_bin_bytes)

            # A reposta para o cliente é o nome do arquivo binário gerado
            response = nome_arq_bin
            client_socket.send(response.encode())


        elif (codigo == '2'):
            # funcionalidade 2: select_from

            # Lê string de entrada: nome do arquivo de dados
            nome_arq_dados = partes_instrucao[1]

            # Converte string para bytes, necessário para passagem de parâmetro
            nome_arq_dados_bytes = ctypes.c_char_p(nome_arq_dados.encode('utf-8'))

            # Cria um arquivo temporário para escrever a saída
            temp_file_out = tempfile.NamedTemporaryFile(delete=False)
            temp_file_out.close()
            temp_file_out_path = temp_file_out.name.encode('utf-8')

            # Definição dos argumentos e retorno da função
            funcoesC.select_from.argtypes = [ctypes.c_char_p, ctypes.c_char_p]
            funcoesC.select_from.restype = None

            # Chamada da função
            funcoesC.select_from(nome_arq_dados_bytes, temp_file_out_path)

            # Lê o conteúdo do arquivo de saída temporário
            with open(temp_file_out.name, 'r', encoding='utf-8') as f:
                response = f.read()

            # Envia resposta ao cliente
            client_socket.send(response.encode())

            # Exclui o arquivo temporário
            os.unlink(temp_file_out.name)

        elif (codigo == '3'):
            # funcionalidade 3: select_from_where

            # Lê nome do arquivo
            nome_arq_bin = partes_instrucao[1]
            nome_arq_bin_bytes = ctypes.c_char_p(nome_arq_bin.encode('utf-8'))

            # Lê número de buscas
            num_buscas = partes_instrucao[2].split('\n')[0]
            num_buscas_bytes = ctypes.c_int(int(num_buscas))

            # Concatena o restante dos argumentos
            restante_args = ' '.join(partes_instrucao[2:])

            # Cria um arquivo temporário e escreve os argumentos restantes nele
            temp_file_in = tempfile.NamedTemporaryFile(delete=False)
            temp_file_in.write(restante_args.encode('utf-8'))
            temp_file_in.close()
            temp_file_in_path = temp_file_in.name.encode('utf-8')

            # Cria um arquivo temporário para escrever a saída
            temp_file_out = tempfile.NamedTemporaryFile(delete=False)
            temp_file_out.close()
            temp_file_out_path = temp_file_out.name.encode('utf-8')

            # Definição dos argumentos e retorno da função
            funcoesC.select_from_where.argtypes = [ctypes.c_char_p, ctypes.c_int, ctypes.c_char_p, ctypes.c_char_p]
            funcoesC.select_from_where.restype = None

            # Chamada da função
            funcoesC.select_from_where(nome_arq_bin_bytes, num_buscas_bytes, temp_file_in_path, temp_file_out_path)

            # Lê o conteúdo do arquivo de saída temporário
            with open(temp_file_out.name, 'r', encoding='utf-8') as f:
                response = f.read()

            # Envia resposta ao cliente
            client_socket.send(response.encode())

            # Exclui os arquivos temporários
            os.unlink(temp_file_in.name)
            os.unlink(temp_file_out.name)

        elif (codigo == '4'):
            # funcionalidade 4: create_index
            nome_arq_dados = partes_instrucao[1]
            nome_arq_indice = partes_instrucao[2]

        elif (codigo == '5'):
            # funcionalidade 5: delete_from_where
            num_remocoes = partes_instrucao[1]
            nome_arq_dados = partes_instrucao[2]
            nome_arq_indice = partes_instrucao[3]

        elif (codigo == '6'):
            # funcionalidade 6: insert_into
            num_insercoes = partes_instrucao[1]
            nome_arq_dados = partes_instrucao[2]
            nome_arq_indice = partes_instrucao[3]

        elif (codigo == '7'):
            # funcionalidade 7: update
            nome_arq_dados = partes_instrucao[1]
            nome_arq_indice = partes_instrucao[2]
        
        # Fechando a conexão com o cliente
        client_socket.close()


if __name__ == "__main__":
    main()
