import sys

if len(sys.argv) > 1:
    filename = sys.argv[1]  # Get the filename from command-line arguments
    try:
        with open(filename, 'r') as file:
            with open(filename[:filename.find('.c')]+".rs", 'w') as destination_file:
                arrays = []
                for line in file:
                    new_line = line.replace('static const unsigned char', 'const')
                    if new_line.find('[') != -1 and new_line.find(']') != -1:
                        size = new_line[new_line.find('[')+1:new_line.find(']')]
                        new_line = new_line[:new_line.find('[')] +": [u8;" + size + "]" + new_line[new_line.find(']')+1:]
                    if new_line.find('const ') != -1:
                        arrays.append(new_line[new_line.find('const ') + 6:new_line.find(': [')])
                    new_line = new_line.replace('{', '[').replace('}', ']')
                    destination_file.write(new_line)
                new_line = "pub const MESSAGES: [&[u8]; " + str(len(arrays)) + "]= ["
                for index, item in enumerate(arrays):
                    if index == len(arrays) - 1:
                        new_line += ("&" + item + "];")
                    else:
                        new_line += ("&" + item + ", ")
                    if index % 5 == 0:
                        destination_file.write(new_line)
                        new_line = ""

    except FileNotFoundError:
        print(f"Error: File '{filename}' not found.")
    except Exception as e:
        print(f"An error occurred: {e}")
else:
    print("Usage: python your_script.py <filename>")