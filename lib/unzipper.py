import patoolib
from os import listdir
from os.path import join
import os
import pathlib

archive_path = r""
output_path = r""

archive_path = str(pathlib.Path(__file__).parent.resolve().parent.resolve()) + "/homeworks/test_files.zip"
output_path = str(pathlib.Path(__file__).parent.resolve().parent.resolve()) + "/homeworks/outputs"

folder1 = patoolib.extract_archive(archive_path, outdir=output_path)
for filename in listdir(folder1):
    full_path = join(folder1, filename)
    for file in listdir(full_path):
        print(file)