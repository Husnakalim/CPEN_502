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


epoch = np.arange(1, 4340, 1)

epoch = np.arange(1, range, 1)

"""
plt.figure(1)
plt.title("Number of epochs versus error")
plt.plot(epoch, data[start_epoch : start_epoch + range - 1, net_nr])
#plt.(label)
plt.show(1)




for i in range(0,9):
    plt.plot(epoch, data[11:4350, i])

#plt.figure(2)
for i in range(10):
        epoch_l = data[0, i]
        epoch = np.arange(1, epoch_l, 1)
        plt.plot(epoch, data[:, i])


plt.show()
"""

plt.title(r'Plot 3: Total error versus number of epochs for binary XOR with $\alpha = 0.9$')

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

plt.savefig('TotErrVsEpoch_binaryMomentum.pdf')
plt.show()

