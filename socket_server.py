import subprocess
import socket
import threading
import glob

# Function to compile the C program
def compile_c_program(source_pattern, output_file):
    source_files = glob.glob(source_pattern)
    if not source_files:
        print("No source files found")
        return
    compile_command = ["gcc"] + source_files + ["-o", output_file]
    result = subprocess.run(compile_command, capture_output=True, text=True)
    if result.returncode != 0:
        print("Compilation failed")
        print(result.stderr)
    else:
        print("Compilation successful")

# Function to run the compiled C program
def run_c_program(executable, input_string):
    process = subprocess.Popen([f"./{executable}"], stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
    stdout, stderr = process.communicate(input=input_string)
    if process.returncode != 0:
        print("Program execution failed")
        print(stderr)
    return stdout

# Function to handle each client connection
def handle_client(client_socket, executable):
    try:
        input_string = client_socket.recv(1024).decode()
        if input_string:
            output = run_c_program(executable, input_string)
            client_socket.send(output.encode())
    except Exception as e:
        print(f"Error handling client: {e}")
    finally:
        client_socket.close()

# Function to start the socket server
def start_server(host, port, executable):
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind((host, port))
    server_socket.listen(5)
    print(f"Server started on {host}:{port}")
    
    while True:
        client_socket, addr = server_socket.accept()
        print(f"Connection from {addr}")
        client_handler = threading.Thread(target=handle_client, args=(client_socket, executable))
        client_handler.start()

# Main function
def main():
    source_pattern = "ArquivosC/*.c"  # Pattern for your C source files
    executable = "program"  # Name for the compiled executable
    host = "localhost"
    port = 12345
    
    compile_c_program(source_pattern, executable)
    
    start_server(host, port, executable)

if __name__ == "__main__":
    main()
