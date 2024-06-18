import subprocess
import socket
import threading
import os
import glob
import platform

# Function to compile the C program
def compile_c_program(source_pattern, output_file):
    source_files = glob.glob(source_pattern)
    if not source_files:
        print("No source files found")
        return

    source_files_str = " ".join(source_files)
    compile_command = f"gcc {source_files_str} -o {output_file}"

    if platform.system() == 'Windows':
        # Convert Windows paths to Unix-like paths for Cygwin
        source_files_unix = [source_file.replace("\\", "/") for source_file in source_files]
        source_files_str = " ".join(source_files_unix)
        output_file_unix = output_file.replace("\\", "/")
        compile_command = f"gcc {source_files_str} -o {output_file_unix}"
        cygwin_bash_path = "C:/cygwin64/bin/bash.exe"
        cygwin_bin_dir = "C:/cygwin64/bin"
        env = os.environ.copy()
        env["PATH"] = f"{cygwin_bin_dir};{env['PATH']}"

        print(f"Compile command: {compile_command}")

        # Run the compile command with Cygwin's bash
        process = subprocess.run([cygwin_bash_path, '-c', compile_command], capture_output=True, text=True, env=env)
    else:
        print(f"Compile command: {compile_command}")
        # Run the compile command directly on Linux
        process = subprocess.run(compile_command, shell=True, capture_output=True, text=True)

    if process.returncode != 0:
        print("Compilation failed")
        print(process.stderr)
    else:
        print("Compilation successful")

# Function to run the compiled C program
def run_c_program(executable, input_string):
    run_command = f"./{executable}"

    if platform.system() == 'Windows':
        executable_unix = executable.replace("\\", "/")
        run_command = f"./{executable_unix}"
        cygwin_bash_path = "C:/cygwin64/bin/bash.exe"
        cygwin_bin_dir = "C:/cygwin64/bin"
        env = os.environ.copy()
        env["PATH"] = f"{cygwin_bin_dir};{env['PATH']}"

        print(f"Windows Detected - Run command: {run_command}")

        # Run the program command with Cygwin's bash
        process = subprocess.Popen([cygwin_bash_path, '-c', run_command], stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True, env=env)
    else:
        print(f"Linux Detected - Run command: {run_command}")
        # Run the program command directly on Linux
        process = subprocess.Popen(run_command, stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True, shell=True)

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
            client_socket.sendall(output.encode())  # Ensure output is encoded before sending
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
