import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
from sklearn.linear_model import LinearRegression
from sklearn.preprocessing import PolynomialFeatures

data = pd.read_csv("data.csv", sep='')
data = data.rename(columns={'MODE':'mode', 'nBodies':'bodies', 'nSteps':'steps', 'Time (s)': 'res'})
data = data.astype({'mode':'category', 'bodies':'int64', 'steps':'int64', 'res':'float64'})

seq = data[0:9]
seq1 = seq[0:3]
seq10 = seq[3:6]
seq50 = seq[6:9]
seq50.res = seq50.res / 60
fullt = data[9:18]
fullt1 = fullt[0:3]
fullt10 = fullt[3:6]
fullt50 = fullt[6:9]
fullt50.res = fullt50.res / 60
scal = data[18:27]
scal1 = scal[0:3]
scal10 = scal[3:6]
scal50 = scal[6:9]
scal50.res = scal50.res / 60

seq1.plot(kind='line', x='steps', y='res', ax=plt.gca())
fullt1.plot(kind='line', x='steps', y='res', ax=plt.gca())
scal1.plot(kind='line', x='steps', y='res', ax=plt.gca())
plt.grid(visible=True, linestyle='-', linewidth=0.5)
plt.xlabel('Num Iterations')
plt.ylabel('Time in seconds')
plt.legend(['Sequential', 'Full thread', 'Scalable'])
plt.title('100 bodies')
plt.savefig('100bodies.png', dpi=600)

seq10.plot(kind='line', x='steps', y='res', ax=plt.gca())
fullt10.plot(kind='line', x='steps', y='res', ax=plt.gca())
scal10.plot(kind='line', x='steps', y='res', ax=plt.gca())
plt.grid(visible=True, linestyle='-', linewidth=0.5)
plt.xlabel('Num Iterations')
plt.ylabel('Time in seconds')
plt.legend(['Sequential', 'Full thread', 'Scalable'])
plt.title('1000 bodies')
plt.savefig('1000bodies.png', dpi=600)

seq50.plot(kind='line', x='steps', y='res', ax=plt.gca())
fullt50.plot(kind='line', x='steps', y='res', ax=plt.gca())
scal50.plot(kind='line', x='steps', y='res', ax=plt.gca())
plt.grid(visible=True, linestyle='-', linewidth=0.5)
plt.xlabel('Num Iterations')
plt.ylabel('Time in minutes')
plt.legend(['Sequential', 'Full thread', 'Scalable'])
plt.title('5000 bodies')
plt.savefig('5000bodies.png', dpi=600)

seq_1 = seq[0:9:3]
seq_2 = seq[1:9:3]
seq_3 = seq[2:9:3]
fullt_1 = fullt[0:9:3]
fullt_2 = fullt[1:9:3]
fullt_3 = fullt[2:9:3]
scal_1 = scal[0:9:3]
scal_2 = scal[1:9:3]
scal_3 = scal[2:9:3]

seq_1.plot(kind='line', x='bodies', y='res', ax=plt.gca())
fullt_1.plot(kind='line', x='bodies', y='res', ax=plt.gca())
scal_1.plot(kind='line', x='bodies', y='res', ax=plt.gca())
plt.grid(visible=True, linestyle='-', linewidth=0.5)
plt.xlabel('Num Bodies')
plt.ylabel('Time in minutes')
plt.legend(['Sequential', 'Full thread', 'Scalable'])
plt.title('1000 iterations')
plt.savefig('1000iterations.png', dpi=600)

seq_2.plot(kind='line', x='bodies', y='res', ax=plt.gca())
fullt_2.plot(kind='line', x='bodies', y='res', ax=plt.gca())
scal_2.plot(kind='line', x='bodies', y='res', ax=plt.gca())
plt.grid(visible=True, linestyle='-', linewidth=0.5)
plt.xlabel('Num Bodies')
plt.ylabel('Time in minutes')
plt.legend(['Sequential', 'Full thread', 'Scalable'])
plt.title('10000 iterations')
plt.savefig('10000iterations.png', dpi=600)

seq_3.plot(kind='line', x='bodies', y='res', ax=plt.gca())
fullt_3.plot(kind='line', x='bodies', y='res', ax=plt.gca())
scal_3.plot(kind='line', x='bodies', y='res', ax=plt.gca())
plt.grid(visible=True, linestyle='-', linewidth=0.5)
plt.xlabel('Num Bodies')
plt.ylabel('Time in minutes')
plt.legend(['Sequential', 'Full thread', 'Scalable'])
plt.title('50000 iterations')
plt.savefig('50000iterations.png', dpi=600)

seq = pd.read_csv("test_seq_res.txt", sep=' ', header=None)
seq.columns = ['bodies', 'res']
seq.astype({'bodies':'int64', 'res':'float64'})
seq.res = seq.res / 100

seq.plot(kind='line', x='bodies', y='res', ax=plt.gca())
plt.xlabel('Num Bodies')
plt.ylabel('Time in seconds')
plt.legend(['Sequential'])
plt.title('Sequential times x bodies')
plt.savefig('SeqBodies.png', dpi=600)

x = seq.bodies.values.reshape(-1, 1)
y = seq.res.values.reshape(-1, 1)

lr = LinearRegression()
lr.fit(x, y)
lr_pr = lr.predict(x)
plt.plot(x, pr)
plt.show()

pr = PolynomialFeatures(degree=3)
x_p = pr.fit_transform(x)
lr = LinearRegression()
lr.fit(x_p, y)
pr = lr.predict(x_p)

seq.plot(kind='line', x='bodies', y='res', ax=plt.gca())
plt.plot(x, pr)
plt.xlabel('Num Bodies')
plt.ylabel('Time in seconds')
plt.legend(['Data', 'Poly Regression'])
plt.title('Sequential trend')
plt.savefig('SeqRegr.png', dpi=600)