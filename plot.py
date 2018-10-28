import matplotlib.pyplot as plt
import numpy as np

filename = "errortest.txt"

with open(filename) as textFile:
        lines = [line.split(",") for line in textFile]

print(lines[2])
