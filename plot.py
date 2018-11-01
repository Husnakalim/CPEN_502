import matplotlib.pyplot as plt
import numpy as np

filename = "errortest.txt"
net_nr = 8
range = 70
start_epoch = 10

with open(filename) as textFile:
        lines = [line.split(",") for line in textFile]

data = np.asarray(lines)
data = data[:, 0:-1]
data = data.astype(float)

epoch = np.arange(1, range, 1)

plt.title('Total error versus number of epochs')
i = 0
while i < 10:
        epoch_l = data[0, i]
        epoch_l = epoch_l.astype(int)
        epoch = np.arange(1, epoch_l, 1)
        plt.plot(epoch, data[1:epoch_l, i], label = 'Trial' + str(i + 1))

        i += 1

plt.ylabel('Total Error')
plt.xlabel('Number of Epochs')
plt.legend()
plt.savefig('TotErrVsEpoch_bipolar.pdf')
plt.show()
