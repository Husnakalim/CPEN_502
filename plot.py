import matplotlib.pyplot as plt
import numpy as np

filename = "errortest.txt"

with open(filename) as textFile:
        lines = [line.split(",") for line in textFile]

"""
with open(filename) as textFile:
        data = [[float(value) for value in line.split(",")] for line in textFile]
"""

data = np.asarray(lines)
data = data[:, 0:-1]
data = data.astype(float)

epoch = np.arange(1, 730, 1)

print(data[1:, 8])

plt.plot(epoch, data[21:750, 8])
plt.show()
