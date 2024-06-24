import subprocess
import socket
import threading
import os
import glob
import platform

# Função para compilar o programa C
def compile_c_program(source_pattern, output_file):
    # Obtém todos os arquivos fonte que correspondem ao padrão fornecido
    source_files = glob.glob(source_pattern)
    if not source_files:
        print("No source files found")
        return

    # Formata os nomes dos arquivos fonte e o comando de compilação
    source_files_str = " ".join(source_files)
    compile_command = f"gcc {source_files_str} -o {output_file}"

    # Verifica o sistema operacional para ajustar o comando de compilação se necessário
    if platform.system() == 'Windows':
        # Converte caminhos do Windows para um estilo Unix para uso com Cygwin
        source_files_unix = [source_file.replace("\\", "/") for source_file in source_files]
        source_files_str = " ".join(source_files_unix)
        output_file_unix = output_file.replace("\\", "/")
        compile_command = f"gcc {source_files_str} -o {output_file_unix}"
        cygwin_bash_path = "C:/cygwin64/bin/bash.exe"
        cygwin_bin_dir = "C:/cygwin64/bin"
        env = os.environ.copy()
        env["PATH"] = f"{cygwin_bin_dir};{env['PATH']}"

        print(f"Compile command: {compile_command}")

        # Executa o comando de compilação usando o bash do Cygwin
        process = subprocess.run([cygwin_bash_path, '-c', compile_command], capture_output=True, text=True, env=env)
    else:
        print(f"Compile command: {compile_command}")
        # Executa o comando de compilação diretamente no Linux
        process = subprocess.run(compile_command, shell=True, capture_output=True, text=True)

    # Verifica se a compilação foi bem-sucedida
    if process.returncode != 0:
        print("Compilation failed")
        print(process.stderr)
    else:
        print("Compilation successful")

# Função para executar o programa C compilado
def run_c_program(executable, input_string):
    # Define o comando para executar o programa compilado
    run_command = f"./{executable}"

    # Verifica o sistema operacional para ajustar o comando de execução se necessário
    if platform.system() == 'Windows':
        executable_unix = executable.replace("\\", "/")
        run_command = f"./{executable_unix}"
        cygwin_bash_path = "C:/cygwin64/bin/bash.exe"
        cygwin_bin_dir = "C:/cygwin64/bin"
        env = os.environ.copy()
        env["PATH"] = f"{cygwin_bin_dir};{env['PATH']}"

        print(f"Windows Detected - Run command: {run_command}")

        # Executa o programa usando o bash do Cygwin
        process = subprocess.Popen([cygwin_bash_path, '-c', run_command], stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True, env=env)
    else:
        print(f"Linux Detected - Run command: {run_command}")
        # Executa o programa diretamente no Linux
        process = subprocess.Popen(run_command, stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True, shell=True)

    # Envia a entrada para o programa e captura a saída padrão e de erro
    stdout, stderr = process.communicate(input=input_string)

    # Verifica se a execução do programa foi bem-sucedida
    if process.returncode != 0:
        print("Program execution failed")
        print(stderr)

    return stdout

# Função para lidar com cada conexão de cliente
def handle_client(client_socket, executable):
    while True:
        try:
            # Recebe dados do cliente
            input_string = client_socket.recv(1024).decode()
            if input_string:
                # Executa o programa C com os dados recebidos como entrada
                output = run_c_program(executable, input_string)
                print(input_string)
                # Envia a saída de volta para o cliente
                client_socket.sendall(output.encode())  # Garante que a saída seja codificada antes do envio
        except Exception as e:
            print(f"Error handling client: {e}")
            client_socket.close()
            return

# Função para iniciar o servidor de socket
def start_server(host, port, executable):
    # Cria um socket do servidor
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind((host, port))
    server_socket.listen(5)
    print(f"Server started on {host}:{port}")

    # Aceita a conexão do cliente
    client_socket, addr = server_socket.accept()
    print(f"Connection from {addr}")

    # Cria uma thread para lidar com o cliente
    client_handler = threading.Thread(target=handle_client, args=(client_socket, executable))
    client_handler.start()

# Função principal
def main():
    source_pattern = "ArquivosC/*.c"  # Padrão para seus arquivos fonte C
    executable = "program"  # Nome para o executável compilado
    host = "localhost"
    port = 12345

    # Compila o programa C
    compile_c_program(source_pattern, executable)

    # Inicia o servidor socket
    start_server(host, port, executable)

if __name__ == "__main__":
    main()
