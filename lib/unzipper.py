from sys import path
from pathlib import Path
path.append(str(Path(__file__).parent.resolve().parent.resolve()) + "/lib/patoolib")  
from patoolib import extract_archive

from os import listdir
from os.path import join


archive_path = str(Path(__file__).parent.resolve().parent.resolve()) + "/homeworks/test_files.zip"
output_path = str(Path(__file__).parent.resolve().parent.resolve()) + "/homeworks/outputs"

try:
    folder1 = extract_archive(archive_path, outdir=output_path)
    for filename in listdir(folder1):
        full_path = join(folder1, filename)
except(Exception):
    print("\nZip file cannot found or file is broken!. BROKEN_FILE_EXCEPTION")