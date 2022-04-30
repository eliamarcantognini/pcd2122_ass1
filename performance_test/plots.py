import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
from sklearn.linear_model import LinearRegression
from sklearn.preprocessing import PolynomialFeatures

data = pd.read_csv("data.csv", sep=';')
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
asyn = data[27:36]
asyn1 = asyn[0:3]
asyn10 = asyn[3:6]
asyn50 = asyn[6:9]
asyn50.res = asyn50.res / 60

seq1.plot(kind='line', x='steps', y='res', ax=plt.gca())
fullt1.plot(kind='line', x='steps', y='res', ax=plt.gca())
scal1.plot(kind='line', x='steps', y='res', ax=plt.gca())
asyn1.plot(kind='line', x='steps', y='res', ax=plt.gca())
plt.grid(visible=True, linestyle='-', linewidth=0.5)
plt.xlabel('Num Iterations')
plt.ylabel('Time in seconds')
plt.legend(['Sequential', 'Full thread', 'Scalable', 'Async'])
plt.title('100 bodies')
plt.savefig('100bodies.png', dpi=600);

seq10.plot(kind='line', x='steps', y='res', ax=plt.gca())
fullt10.plot(kind='line', x='steps', y='res', ax=plt.gca())
scal10.plot(kind='line', x='steps', y='res', ax=plt.gca())
asyn10.plot(kind='line', x='steps', y='res', ax=plt.gca())
plt.grid(visible=True, linestyle='-', linewidth=0.5)
plt.xlabel('Num Iterations')
plt.ylabel('Time in seconds')
plt.legend(['Sequential', 'Full thread', 'Scalable', 'Async'])
plt.title('1000 bodies')
plt.savefig('1000bodies.png', dpi=600);

seq50.plot(kind='line', x='steps', y='res', ax=plt.gca())
fullt50.plot(kind='line', x='steps', y='res', ax=plt.gca())
scal50.plot(kind='line', x='steps', y='res', ax=plt.gca())
asyn50.plot(kind='line', x='steps', y='res', ax=plt.gca())
plt.grid(visible=True, linestyle='-', linewidth=0.5)
plt.xlabel('Num Iterations')
plt.ylabel('Time in minutes')
plt.legend(['Sequential', 'Full thread', 'Scalable', 'Async'])
plt.title('5000 bodies')
plt.savefig('5000bodies.png', dpi=600);

seq_1 = seq[0:9:3]
seq_2 = seq[1:9:3]
seq_3 = seq[2:9:3]
fullt_1 = fullt[0:9:3]
fullt_2 = fullt[1:9:3]
fullt_3 = fullt[2:9:3]
scal_1 = scal[0:9:3]
scal_2 = scal[1:9:3]
scal_3 = scal[2:9:3]
asyn_1 = asyn[0:9:3]
asyn_2 = asyn[1:9:3]
asyn_3 = asyn[2:9:3]

seq_1.plot(kind='line', x='bodies', y='res', ax=plt.gca())
fullt_1.plot(kind='line', x='bodies', y='res', ax=plt.gca())
scal_1.plot(kind='line', x='bodies', y='res', ax=plt.gca())
asyn_1.plot(kind='line', x='bodies', y='res', ax=plt.gca())
plt.grid(visible=True, linestyle='-', linewidth=0.5)
plt.xlabel('Num Bodies')
plt.ylabel('Time in minutes')
plt.legend(['Sequential', 'Full thread', 'Scalable', 'Async'])
plt.title('1000 iterations')
plt.savefig('1000iterations.png', dpi=600);

seq_2.plot(kind='line', x='bodies', y='res', ax=plt.gca())
fullt_2.plot(kind='line', x='bodies', y='res', ax=plt.gca())
scal_2.plot(kind='line', x='bodies', y='res', ax=plt.gca())
asyn_2.plot(kind='line', x='bodies', y='res', ax=plt.gca())
plt.grid(visible=True, linestyle='-', linewidth=0.5)
plt.xlabel('Num Bodies')
plt.ylabel('Time in minutes')
plt.legend(['Sequential', 'Full thread', 'Scalable', 'Async'])
plt.title('10000 iterations')
plt.savefig('10000iterations.png', dpi=600);

seq_3.plot(kind='line', x='bodies', y='res', ax=plt.gca())
fullt_3.plot(kind='line', x='bodies', y='res', ax=plt.gca())
scal_3.plot(kind='line', x='bodies', y='res', ax=plt.gca())
asyn_3.plot(kind='line', x='bodies', y='res', ax=plt.gca())
plt.grid(visible=True, linestyle='-', linewidth=0.5)
plt.xlabel('Num Bodies')
plt.ylabel('Time in minutes')
plt.legend(['Sequential', 'Full thread', 'Scalable', 'Async'])
plt.title('50000 iterations')
plt.savefig('50000iterations.png', dpi=600);