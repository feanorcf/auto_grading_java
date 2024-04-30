from sys import path, argv
from pathlib import Path

path.append(str(Path(__file__).parent.resolve().parent.resolve()) + "/lib/patoolib")
from patoolib import extract_archive, create_archive

from os import listdir, chdir, getcwd
from os.path import join

root_path = str(Path(__file__).parent.resolve().parent.resolve())
archive_path = root_path + "/homeworks/test_files.zip"
output_path = root_path + "/homeworks/outputs"

def extract():
    try:
        folder1 = extract_archive(archive_path, outdir=output_path)
        for filename in listdir(folder1):
            full_path = join(folder1, filename)
    except(Exception):
        print("\nZip file cannot found or file is broken!. BROKEN_FILE_EXCEPTION")


def compress():
    try:
        files = listdir(output_path)  # TUPLE of files to be repacked
        cwd = getcwd()
        chdir(output_path)
        create_archive(root_path + "/homeworks/results.zip", files,)  # Archive can be zip, RAR, 7z...
        chdir(cwd)
    except(Exception):
        print("\nZip file already exists!. BROKEN_FILE_EXCEPTION")


if argv[1] == "0":
    extract()
elif argv[1] == "1":
    compress()